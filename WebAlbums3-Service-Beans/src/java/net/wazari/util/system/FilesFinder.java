package net.wazari.util.system;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.Normalizer;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Stack;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import net.wazari.common.plugins.Importer.Capability;
import net.wazari.common.util.StringUtil;
import net.wazari.dao.*;
import net.wazari.dao.entity.*;
import net.wazari.service.exchange.Configuration;
import net.wazari.service.exchange.ViewSession;
import net.wazari.util.system.ImageResizer.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class FilesFinder {
    private static final String SEP = File.separator;
    private static final int DEFAULT_USER = 3;
    
    private static final Logger log = LoggerFactory.getLogger(FilesFinder.class.getCanonicalName());
    
    @EJB private CarnetFacadeLocal carnetDAO;
    @EJB private ThemeFacadeLocal themeDAO;
    @EJB private AlbumFacadeLocal albumDAO;
    @EJB private UtilisateurFacadeLocal userDAO;
    @EJB private PhotoFacadeLocal photoDAO;
    @EJB private TagPhotoFacadeLocal tagPhotoDAO;
    @EJB private SystemTools sysTools;
    @EJB private ImageResizer resizer;

    public boolean importAuthor(ViewSession vSession,
            String themeName, Configuration conf) {
        if (String.CASE_INSENSITIVE_ORDER.compare("root", themeName) == 0) {
            log.info("root is a reserved keyword");
            return false;
        }

        Stack<Element> stack = new Stack<Element>();

        log.info("Importing for theme : {}", themeName);

        Theme enrTheme = themeDAO.loadByName(themeName);

        //si l'auteur n'est pas encore dans la base de données,
        //on l'ajoute
        if (enrTheme == null) {
            if (themeName.contains(" ")) {
                log.warn("pas d'espace dans le nom du theme");
                return false;
            }

            log.info("Le theme n'est pas dans la table");
            enrTheme = themeDAO.newTheme(themeName);
            enrTheme = themeDAO.loadByName(enrTheme.getNom()) ;
            log.info("Le theme a correctement été ajouté");

        } //if theme already exists
        else {
            log.info("Le theme est dans la table");
        }

        //if init was performed correctly
        File dirTheme = new File(conf.getFtpPath() + themeName + SEP);
        log.info("Dossier source : {}", dirTheme);
        //creer le dossier d'import s'il n'existe pas encore
        if (!dirTheme.isDirectory()) {
            log.info("Creation du dossier d'import ({})", dirTheme);
            dirTheme.mkdirs();
        }

        boolean correct;
        if (!dirTheme.isDirectory()) {
            log.warn("{} n'est pas un dossier/impossible de le creer  ... ", dirTheme.getAbsolutePath());
            correct = false;
        } else {
            log.info("ID du theme :  {}", enrTheme);
            File[] subfiles = dirTheme.listFiles();

            log.warn("Le dossier '{}' contient {} fichier/s", themeName, subfiles.length);

            correct = true;
            int err = 0;
            for (int i = 0; i < subfiles.length; i++) {
                if (subfiles[i].isDirectory()) {
                    log.info("Important de l'album {}", subfiles[i]);
                    if (!importAlbum(stack, subfiles[i], enrTheme)) {
                        log.warn("An error occured during importation of album ({})...", subfiles[i]);
                        correct = false;
                        err++;
                    }
                    subfiles[i].deleteOnExit();
                }
            }

            log.info( "## Import of theme {} completed", themeName);
            if (err != 0) {
                log.warn("## with {} errors", err);
            }
        }

        if (dirTheme != null) {
            resizer.resize(conf, stack, dirTheme);
        }

        if (!correct) {
            log.warn("An error occured during initialization process ...");
        }

        return correct;
    }
    private static String sansAccents(String source) {
            return Normalizer.normalize(source, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
    }
    private boolean importAlbum(Stack<Element> stack, File album, Theme enrTheme) {
        log.info("##");
        log.info("## Import of: {}", album.getName());
        int annee;
        String dossier;
        String albumPath;
        if (!album.exists() || !album.isDirectory()) {
            log.info("## Le dossier Album '{}' n'existe pas", album.getName());
            return false;
        } else {
            String strDate = null;
            String dirName = album.getName();
            Album enrAlbum;
            // split album name into YYYY-MM-DD NAME
            if (dirName != null && dirName.length() > 11) {
                String nom = dirName.substring(11);
                nom = sansAccents(nom) ;
                log.info("## NOM  : {}", nom);
                try {
                    strDate = album.getName().substring(0, 10);
                    Date date = Album.DATE_STANDARD.parse(strDate);
                    log.info("## DATE : {}", date);

                } catch (ParseException e) {
                    log.warn("## Erreur dans le format de la date ({}), on skip", strDate);
                    return false;
                }
                enrAlbum = albumDAO.loadByNameDate(nom, strDate);
                if (enrAlbum == null) {
                    //si il n'y est pas, on l'ajoute
                    log.info("## L'album n'est pas dans la table");
                    enrAlbum = albumDAO.newAlbum();

                    enrAlbum.setNom(nom);
                    enrAlbum.setDescription("");
                    enrAlbum.setTheme(enrTheme);
                    enrAlbum.setDate(strDate);
                    enrAlbum.setDroit(userDAO.find(DEFAULT_USER));

                    log.info("## On tente d'ajouter l'album dans la base");
                    albumDAO.create(enrAlbum);
                    log.info("## On vient de lui donner l'ID {}", enrAlbum.getId());

                } else {
                    log.info("## L'album est dans la table : ID {}", enrAlbum.getId());
                }
                dossier = enrAlbum.getDate() + " " + enrAlbum.getNom();
            } else {
                try {
                    int albumId = Integer.parseInt(dirName);
                    enrAlbum = albumDAO.find(albumId);
                    if (enrAlbum == null) {
                        log.info("## Can't find an album with id= {}", albumId);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    log.warn("## Format of the album folder ({}) wrong; expected YYYY-MM-DD Title", dirName);
                    return false;
                }
                
                dossier = enrAlbum.getDate() + " " + enrAlbum.getNom();
                for (Photo enrPhoto : enrAlbum.getPhotoList()) {
                    dossier = enrPhoto.getPath(false).split(SEP)[1];
                    break ;
                }
                
                
            }

            if (enrAlbum.getTheme().getId() != enrTheme.getId()) {
                log.warn("## L'album est dans la table ({}),"
                        + " mais le theme n'est pas bon: {}", enrAlbum.getId(), enrAlbum.getTheme());
                return false;
            }
            
            annee = Integer.parseInt(enrAlbum.getDate().substring(0, 4));
            albumPath = annee + SEP + dossier + SEP;
            
            //rechercher s'il est deja dans la liste
            int err = 0;
            File[] subfiles = album.listFiles();
            if (subfiles != null) {
                log.info("## Le répertoire '{}' contient {} fichier/s", dossier, subfiles.length);

                for (int i = 0; i < subfiles.length; i++) {
                    log.info("## Traitement de {}", subfiles[i].getName());
                    if (!importPhoto(stack, albumPath, subfiles[i], enrAlbum)) {
                        err++;
                    }
                }
                log.info("## Import of : {} completed", album.getName() );
                if (err != 0) {
                    log.info("## with {} errors", err);
                }

            } else {
                log.warn("Impossible de connaitre le nombre de fichiers ..."
                        + "(dossier ? ",  album.isDirectory());
            }
            return true;
        }
    }

    private boolean importPhoto(Stack<Element> stack, String albumPath,
            File photo,
            Album enrAlbum) {
        log.info("### Import of : {}", photo.getName());

        if ("Thumbs.db".equals(photo.getName())) {
            log.info("### Supression de {}", photo);
            photo.delete();
            return true;
        }
        //verification du type du fichier
        String type;
        try {
            URL url = photo.toURI().toURL();
            URLConnection connection = url.openConnection();
            type = connection.getContentType();

            log.info("### Type : {}", type);
        } catch (MalformedURLException e) {
            log.warn("### URL mal formée ... ", e);
            return false;
        } catch (IOException e) {
            log.warn("### Erreur d'IO ...", e);
            return false;
        }

        String ext = null;
        int idx = photo.getName().lastIndexOf('.');
        if (idx != -1) {
            ext = photo.getName().substring(idx + 1);
        }
        
        String photoPath = albumPath + sansAccents(photo.getName());
        boolean dontThumbnail = false;
 
        if (!sysTools.supports(type, ext, Capability.THUMBNAIL)) {
            log.warn("### {} n'est pas supportée ... ({})", photo, type);
            return false;
        }

        Photo enrPhoto = photoDAO.loadByPath(photoPath);

        //si l'image (son path) n'est pas encore dans la base
        if (enrPhoto == null) {
            log.info("### Creation d'un nouvel enregistrement");
            //on crée la nouvelle photo
            enrPhoto = photoDAO.newPhoto();
            enrPhoto.setDescription("");
            enrPhoto.setPath(photoPath);
            
            sysTools.retrieveMetadata(type, ext, enrPhoto, photo.getAbsolutePath());
            enrPhoto.setAlbum(enrAlbum);
            enrPhoto.setType(type);
            log.info("### Album {}", enrPhoto.getAlbum());
            photoDAO.create(enrPhoto);
        } else /* sinon on update son nom d'album*/ {
            log.info("### Mise à jour de l'enregistrement");
            enrPhoto.setAlbum(enrAlbum);

            photoDAO.edit(enrPhoto);
        }
        
        if ("gpx".equals(ext)) {
            enrPhoto.setIsGpx(true);
            photoDAO.edit(enrPhoto);
        }
        
        ImageResizer.Element elt = new ImageResizer.Element(enrAlbum.getTheme().getNom()+SEP+photoPath, photo, type, dontThumbnail);
        stack.push(elt);
        log.info("### Import of : {} : completed", photo.getName());
        return true;
    }

    public boolean deleteAlbum(Album enrAlbum, Configuration conf) {
        boolean correct = true;
        for (Iterator<Photo> iter = enrAlbum.getPhotoList().iterator(); iter.hasNext();) {
            Photo enrPhoto = iter.next() ;
            iter.remove();
            if (!deletePhoto(enrPhoto, conf)) {
                log.warn("Problem during the deletion ...");
                correct = false;
            }
        }
        if (correct) {
            albumDAO.remove(enrAlbum);
            return true;
        }

        return false;
    }

    public boolean deletePhoto(Photo enrPhoto, Configuration conf) {

        String url = null;
        try {
            File fichier;

            Theme enrTheme = enrPhoto.getAlbum().getTheme();
            if (enrTheme == null) {
                log.warn("Theme impossible à trouver ... (photo={})", enrPhoto.getId());
                return false;
            }
            log.info("Traitement de la photo {}", enrPhoto.getId());
            //suppression des tags de cette photo
            tagPhotoDAO.deleteByPhoto(enrPhoto);

            if (enrPhoto.equals(enrPhoto.getAlbum().getPicture())) {
                enrPhoto.getAlbum().setPicture(null);
            }
            
            //suppression des photos physiquement
            url = "file://" + conf.getImagesPath(true) + SEP + enrTheme.getNom() + SEP + enrPhoto.getPath(false);

            fichier = new File(new URL(StringUtil.escapeURL(url)).toURI());
            log.info("On supprime sa photo : {}", url);
            if (!fichier.delete()) {
                log.warn("Mais ça marche pas ...");
            }
            //pas de rep vide
            fichier.getParentFile().delete();

            //miniature
            url = "file://" + conf.getMiniPath(true) + SEP + enrTheme.getNom() + SEP + enrPhoto.getPath(false) + ".png";
            fichier = new File(new URL(StringUtil.escapeURL(url)).toURI());
            log.info("On supprime sa miniature : {}", url);
            if (!fichier.delete()) {
                log.warn("mais ça marche pas ...");
            }
            //pas de rep vide
            fichier.getParentFile().delete();
            //suppression du champs dans la table Photo
            photoDAO.remove(enrPhoto);

            log.info("Photo correctement supprimée !");
            return true;
        } catch (MalformedURLException e) {
            log.warn("MalformedURLException {}", url, e);
        } catch (URISyntaxException e) {
            log.warn("URISyntaxException {}", url, e);
        }
        return false;
    }

    public boolean deleteCarnet(Carnet enrCarnet, Configuration configuration) {  
        carnetDAO.remove(enrCarnet);
        
        return true ;
    }

    public void moveAlbum(Album enrAlbum, Theme enrTheme, Configuration conf) {
        log.warn("Move album {} to theme {}", enrAlbum.getNom(), enrTheme.getNom());
        if (enrAlbum.getTheme().equals(enrTheme)) {
            return;
        }
        String albumPathSuffix = enrAlbum.getPhotoList().get(0).getPath(false);
        albumPathSuffix = SEP + albumPathSuffix.substring(0, albumPathSuffix.lastIndexOf(SEP));
        
        File sourceAlbumImage = new File(conf.getImagesPath(true)+enrAlbum.getTheme().getNom()+albumPathSuffix);
        File targetAlbumImage = new File(conf.getImagesPath(true)+enrTheme.getNom()+albumPathSuffix);
        
        targetAlbumImage.getParentFile().mkdirs();
        
        log.warn("{} renameTo {}", sourceAlbumImage, targetAlbumImage);
        if (!sourceAlbumImage.renameTo(targetAlbumImage)) {
            log.error("Couldn't rename {} to {}", sourceAlbumImage, targetAlbumImage);
            return;
        }
        sourceAlbumImage.getParentFile().delete();
        
        File sourceAlbumMini = new File(conf.getMiniPath(true)+enrAlbum.getTheme().getNom()+albumPathSuffix);
        File targetAlbumMini = new File(conf.getMiniPath(true)+enrTheme.getNom()+albumPathSuffix);
                
        targetAlbumMini.getParentFile().mkdirs();
        
        log.warn("{} renameTo {}", sourceAlbumMini, targetAlbumMini);
        if (!sourceAlbumMini.renameTo(targetAlbumMini)) {
            log.error("Couldn't rename {} to {}", sourceAlbumImage, targetAlbumImage);
            return;
        }
        sourceAlbumMini.getParentFile().delete();
        
        enrAlbum.setTheme(enrTheme);
    }
}
