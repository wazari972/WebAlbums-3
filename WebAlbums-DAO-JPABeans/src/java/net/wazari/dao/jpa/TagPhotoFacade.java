/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wazari.dao.jpa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import net.wazari.dao.*;
import net.wazari.dao.entity.*;
import net.wazari.dao.jpa.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kevin
 */
@Stateless
@DeclareRoles({UtilisateurFacadeLocal.MANAGER_ROLE, UtilisateurFacadeLocal.VIEWER_ROLE})
public class TagPhotoFacade implements TagPhotoFacadeLocal {
    private static final Logger log = LoggerFactory.getLogger(TagPhotoFacade.class.getName()) ;
    
    @PersistenceContext(unitName=WebAlbumsDAOBean.PERSISTENCE_UNIT)
    private EntityManager em;

    @EJB PhotoFacadeLocal photoDAO ;
    @EJB AlbumFacadeLocal albumDAO ;
    @EJB TagFacadeLocal   tagDAO ;
    @EJB ThemeFacadeLocal themeDAO ;
    @EJB TagThemeFacadeLocal   tagThemeDAO ;
    
    @Override
    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    public void create(TagPhoto tagPhoto) {
        tagPhoto.getPhoto().getTagPhotoList().add(tagPhoto);
        tagPhoto.getTag().getTagPhotoList().add(tagPhoto);
        em.persist(tagPhoto);
    }

    @Override
    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    public void edit(TagPhoto tagPhoto) {
        em.merge(tagPhoto);
    }

    @Override
    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    public void remove(TagPhoto tagPhoto) {
        tagPhoto.getPhoto().getTagPhotoList().remove(tagPhoto);
        tagPhoto.getTag().getTagPhotoList().remove(tagPhoto);
        em.remove(tagPhoto);
    }

    @Override
    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    public void deleteByPhoto(Photo enrPhoto) {
        int themeId = enrPhoto.getAlbum().getTheme().getId();
        Iterator<TagPhoto> it = enrPhoto.getTagPhotoList().iterator() ;
        while (it.hasNext()) {
            TagPhoto enrTagPhoto = it.next() ;
            enrTagPhoto.getTag().getTagPhotoList().remove(enrTagPhoto);
            
            TagTheme enrTagTheme = tagThemeDAO.loadByTagTheme(enrTagPhoto.getTag().getId(), themeId);
            if (enrTagTheme != null && enrPhoto.equals(enrTagTheme.getPhoto()))
                tagThemeDAO.remove(enrTagTheme);
            
            it.remove();
            em.remove(enrTagPhoto);
        }
    }

    @Override
    @RolesAllowed(UtilisateurFacadeLocal.VIEWER_ROLE)
    public List<TagPhoto> queryByAlbum(Album enrAlbum) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JPATagPhoto> cq = cb.createQuery(JPATagPhoto.class) ;
        Root<JPATagPhoto> tp = cq.from(JPATagPhoto.class);
        cq.where(cb.equal(tp.get(JPATagPhoto_.photo).get(JPAPhoto_.album), enrAlbum)) ;

        return (List) em.createQuery(cq.distinct(true))
                .setHint("org.hibernate.cacheable", true)
                .setHint("org.hibernate.readOnly", true)
                .getResultList();
    }

    @Override
    @RolesAllowed(UtilisateurFacadeLocal.VIEWER_ROLE)
    public TagPhoto loadByTagPhoto(int tagId, int photoId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JPATagPhoto> cq = cb.createQuery(JPATagPhoto.class) ;
        Root<JPATagPhoto> tp = cq.from(JPATagPhoto.class);
        cq.where(cb.and(
                cb.equal(tp.get(JPATagPhoto_.photo).get(JPAPhoto_.id), photoId)),
                cb.equal(tp.get(JPATagPhoto_.tag).get(JPATag_.id), tagId)) ;

        return (JPATagPhoto) em.createQuery(cq)
                .setHint("org.hibernate.cacheable", true)
                .setHint("org.hibernate.readOnly", true)
                .getSingleResult();
    }

    @Override
    @RolesAllowed(UtilisateurFacadeLocal.VIEWER_ROLE)
    public List<Tag> selectDistinctTags() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JPATag> cq = cb.createQuery(JPATag.class) ;
        Root<JPATagPhoto> tp = cq.from(JPATagPhoto.class);
        return (List) em.createQuery(cq.select(tp.get(JPATagPhoto_.tag))
                .distinct(true))
                .getResultList();
    }

    @Override
    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    public TagPhoto newTagPhoto() {
        return new JPATagPhoto() ;
    }

    @Override
    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    public List<TagPhoto> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JPATagPhoto> cq = cb.createQuery(JPATagPhoto.class) ;
        cq.from(JPATagPhoto.class);
        return (List) em.createQuery(cq)
                .setHint("org.hibernate.cacheable", true)
                .setHint("org.hibernate.readOnly", true)
                .getResultList();
    }

    @Override
    @RolesAllowed(UtilisateurFacadeLocal.VIEWER_ROLE)
    /* Returns all the TagPhoto associated with this Carnet.  */
    public List<TagPhoto> queryByCarnet(Carnet enrCarnet) {
        if (enrCarnet.getPhotoList() == null 
                || enrCarnet.getPhotoList().isEmpty())
            return new ArrayList<>(0);
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JPATagPhoto> cq = cb.createQuery(JPATagPhoto.class) ;
        Root<JPATagPhoto> tp = cq.from(JPATagPhoto.class);
        Root<JPAPhoto> p = cq.from(JPAPhoto.class);
        cq.where(cb.equal(p, tp.get(JPATagPhoto_.photo)),
                p.in(enrCarnet.getPhotoList())) ;

        return (List) em.createQuery(cq.select(tp).distinct(true))
                .setHint("org.hibernate.cacheable", true)
                .setHint("org.hibernate.readOnly", true)
                .getResultList();
    }
}
