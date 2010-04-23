package net.wazari.service.engine;


import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import net.wazari.dao.AlbumFacadeLocal;
import net.wazari.dao.PhotoFacadeLocal;
import net.wazari.dao.UtilisateurFacadeLocal;
import net.wazari.dao.entity.Album;
import net.wazari.dao.entity.Photo;

import net.wazari.service.AlbumUtilLocal;
import net.wazari.service.AlbumLocal;
import net.wazari.service.WebPageLocal;
import net.wazari.service.exchange.ViewSessionAlbum;
import net.wazari.service.exchange.ViewSession.Action;
import net.wazari.service.exchange.ViewSession.Special;
import net.wazari.service.exception.WebAlbumsServiceException;
import net.wazari.service.exchange.ViewSession.Box;
import net.wazari.service.exchange.ViewSession.EditMode;
import net.wazari.service.exchange.ViewSession.Mode;
import net.wazari.service.exchange.ViewSession.Type;

import net.wazari.util.system.FilesFinder;
import net.wazari.util.XmlBuilder;

@Stateless
public class AlbumBean implements AlbumLocal {

    private static final long serialVersionUID = 1L;
    private static final int TOP = 5;

    @EJB
    AlbumFacadeLocal albumDAO;
    @EJB
    AlbumUtilLocal albumUtil;
    @EJB
    private UtilisateurFacadeLocal userDAO;
    @EJB
    private PhotoFacadeLocal photoDAO;
    @EJB
    private WebPageLocal webPageService;
    private FilesFinder finder;

    public XmlBuilder treatAlbmEDIT(ViewSessionAlbum vSession,
            XmlBuilder submit)
            throws WebAlbumsServiceException {

        XmlBuilder output = new XmlBuilder("albm_edit");

        if (submit != null) {
            output.add(submit);
        }

        Integer albumId = vSession.getId();
        Integer page = vSession.getPage();
        Integer count = vSession.getCount();
        page = (page == null ? 0 : page);

        Album enrAlbum = albumDAO.find(albumId);

        if (enrAlbum == null) {
            output.cancel();
            output.addException("Impossible de trouver l'album (" + albumId + ")");
            return output.validate();
        }

        output.add("picture", enrAlbum.getPicture());
        output.add("name", enrAlbum.getNom());
        output.add("count", count);
        output.add("id", enrAlbum.getId());
        output.add("description", enrAlbum.getDescription());
        output.add("date", enrAlbum.getDate());

        output.add(webPageService.displayListLB(Mode.TAG_USED, vSession, null,
                Box.MULTIPLE));
        output.add(webPageService.displayListLB(Mode.TAG_NUSED, vSession, null,
                Box.MULTIPLE));
        output.add(webPageService.displayListLB(Mode.TAG_NEVER, vSession, null,
                Box.MULTIPLE));
        output.add(webPageService.displayListDroit(enrAlbum.getDroit().getId(), null));

        output.validate();
        return output.validate();
    }

    public XmlBuilder displayAlbum(List<Album> albums,
            XmlBuilder output,
            ViewSessionAlbum vSession,
            XmlBuilder submit,
            XmlBuilder thisPage) throws WebAlbumsServiceException {

        EditMode inEditionMode = vSession.getEditionMode();
        Integer albumId = vSession.getId();
        Integer page = vSession.getPage();
        Integer countAlbm = vSession.getCountAlbm();
        page = (page == null ? 0 : page);

        Integer[] bornes =
                webPageService.calculBornes(Type.ALBUM, page, countAlbm, albums.size());
        int max = Math.min(bornes[0] + WebPageBean.TAILLE_ALBUM, albums.size()) ;
        albums.subList(bornes[0], max) ;

        int count = bornes[0];

        for(Album enrAlbum : albums) {
            XmlBuilder album = new XmlBuilder("album");

            if (enrAlbum.getId() == albumId) {
                album.add(submit);
                submit = null;
            }

            //album.add(StringUtil.xmlDate(enrAlbum.getDate(), oldDate));

            album.add("id", enrAlbum.getId());
            album.add("count", count);
            album.add("title", enrAlbum.getNom());

            XmlBuilder details = new XmlBuilder("details");

            Photo enrPhoto = photoDAO.find(enrAlbum.getPicture());
            if (enrPhoto != null) {
                details.add("photoID", enrPhoto.getId());
                details.add("miniWidth", enrPhoto.getWidth());
                details.add("miniHeight", enrPhoto.getHeight());
            }
            
            details.add("description", enrAlbum.getDescription());

            //tags de l'album
            details.add(webPageService.displayListIBT(Mode.TAG_USED, vSession, enrAlbum.getId(),
                    Box.NONE, Type.ALBUM));
            //utilisateur ayant le droit à l'album
            //ou a l'une des photos qu'il contient
            if (vSession.isSessionManager() && !vSession.getConfiguration().isReadOnly()) {
                if (inEditionMode != EditMode.VISITE) {
                    details.add(enrAlbum.getDroit().getNom());
                    details.add("userInside", userDAO.loadUserInside(enrAlbum.getId()));
                }
            }
            album.add(details);

            count++;

            output.add(album);
        }
        if (submit != null) {
            output.add(submit);
        }

        output.add(webPageService.xmlPage(thisPage, bornes));

        return output.validate();
    }

    public XmlBuilder treatALBM(ViewSessionAlbum vSession)
            throws WebAlbumsServiceException {

        XmlBuilder output = new XmlBuilder("albums");
        XmlBuilder submit = null;

        Special special = vSession.getSpecial();
        if (special == Special.TOP5) {
            XmlBuilder top5 = new XmlBuilder("top5");

            List<Album> albums = albumDAO.queryAlbums(vSession, true, true, TOP);
            int i = 0;
            for (Album enrAlbum : albums) {
                XmlBuilder album = new XmlBuilder("album");
                album.add("id", enrAlbum.getId());
                album.add("count", i);
                album.add("nom", enrAlbum.getNom());
                if (enrAlbum.getPicture() != null) {
                    album.add("photo", enrAlbum.getPicture());
                }
                top5.add(album);
            }
            output.add(top5);
            return output.validate();
        }

        Action action = vSession.getAction();
        if (action == Action.SUBMIT && vSession.isSessionManager() && !vSession.getConfiguration().isReadOnly()) {
            submit = treatAlbmSUBMIT(vSession);
        }

        if (action == Action.EDIT && vSession.isSessionManager() && !vSession.getConfiguration().isReadOnly()) {
            output = treatAlbmEDIT(vSession, submit);

        } else {
            //sinon afficher la liste des albums de ce theme
            output.add(treatAlbmDISPLAY(vSession, submit));
        }

        return output.validate();
    }

    public XmlBuilder treatAlbmSUBMIT(ViewSessionAlbum vSession)
            throws WebAlbumsServiceException {

        XmlBuilder output = new XmlBuilder(null);
        Integer albumID = vSession.getId();


        Album enrAlbum = albumDAO.loadIfAllowed(vSession, albumID);
        if (enrAlbum == null) {
            return null;
        }

        Boolean supprParam = vSession.getSuppr();
        if (supprParam) {
            XmlBuilder suppr = new XmlBuilder("suppr_msg");
            if (finder.deleteAlbum(enrAlbum, suppr, vSession.getConfiguration())) {
                output.add(suppr);
                output.add("message", "Album correctement  supprimé !");
            } else {
                output.addException(suppr);
                output.addException("Exception", "an error occured ...");
            }
            return output.validate();
        }

        Integer user = vSession.getUserAllowed();
        String desc = vSession.getDescr();
        String nom = vSession.getNom();
        String date = vSession.getDate();
        Integer[] tags = vSession.getTags();
        Boolean force = vSession.getForce();

        albumUtil.updateDroit(enrAlbum, new Integer("0" + user));
        albumUtil.setTagsToPhoto(enrAlbum, tags, force);
        enrAlbum.setNom(nom);
        enrAlbum.setDescription(desc);
        albumDAO.setDateStr(enrAlbum, date);
        albumDAO.edit(enrAlbum);

        output.add("message", "Album (" + enrAlbum.getId() + ") correctement mise à jour !");

        return output.validate();
    }

    public XmlBuilder treatAlbmDISPLAY(ViewSessionAlbum vSession,
            XmlBuilder submit) throws WebAlbumsServiceException {

        XmlBuilder output = new XmlBuilder(null);
        XmlBuilder thisPage = new XmlBuilder("name", "Albums");

        List<Album> albums = albumDAO.queryAlbums(vSession, true, true, null);
        displayAlbum(albums, output, vSession, submit, thisPage);

        return output.validate();
    }
}
