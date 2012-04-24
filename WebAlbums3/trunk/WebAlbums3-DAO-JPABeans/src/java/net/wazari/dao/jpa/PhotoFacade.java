/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wazari.dao.jpa;

import java.util.Collection;
import net.wazari.dao.exchange.ServiceSession;
import net.wazari.dao.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import net.wazari.dao.AlbumFacadeLocal.Restriction;
import net.wazari.dao.entity.Album;
import net.wazari.dao.entity.Photo;
import net.wazari.dao.entity.Tag;
import net.wazari.dao.entity.facades.SubsetOf;
import net.wazari.dao.entity.facades.SubsetOf.Bornes;
import net.wazari.dao.exchange.ServiceSession.ListOrder;
import net.wazari.dao.jpa.entity.JPAPhoto;
import net.wazari.dao.jpa.entity.JPAPhoto_;
import net.wazari.dao.jpa.entity.JPATagPhoto;
import net.wazari.dao.jpa.entity.JPATagPhoto_;

/**
 *
 * @author kevin
 */
@Stateless
public class PhotoFacade implements PhotoFacadeLocal {
    private static final Logger log = LoggerFactory.getLogger(PhotoFacade.class.getCanonicalName()) ;

    @EJB
    WebAlbumsDAOBean webDAO;
    @EJB
    AlbumFacadeLocal albumDAO;

    @PersistenceContext(unitName=WebAlbumsDAOBean.PERSISTENCE_UNIT)
    private EntityManager em;

    @Override
    public void create(Photo photo) {
        em.persist(photo);
    }

    @Override
    public void edit(Photo photo) {
        em.merge(photo);
    }

    @Override
    public void remove(Photo photo) {
        photo.getAlbum().getPhotoList().remove(photo);
        em.remove(photo);
    }

    @Override
    public Photo loadIfAllowed(ServiceSession session, int id) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Predicate TRUE = cb.conjunction() ;
            CriteriaQuery<JPAPhoto> cq = cb.createQuery(JPAPhoto.class) ;
            Root<JPAPhoto> p = cq.from(JPAPhoto.class) ;
            cq.where(cb.and(
                    cb.equal(p.get(JPAPhoto_.id), id)),
                    session == null ?
                        TRUE : webDAO.getRestrictionToPhotosAllowed(session, p, cq.subquery(JPAPhoto.class))) ;

            return (JPAPhoto) em.createQuery(cq)
                    .setHint("org.hibernate.cacheable", true)
                    .setHint("org.hibernate.readOnly", false)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public SubsetOf<Photo> loadFromAlbum(ServiceSession session, Album album, Bornes bornes, ListOrder order) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JPAPhoto> cq = cb.createQuery(JPAPhoto.class) ;
        Root<JPAPhoto> p = cq.from(JPAPhoto.class) ;
        cq.where(cb.and(
                cb.equal(p.get(JPAPhoto_.album), album)),
                webDAO.getRestrictionToPhotosAllowed(session, p, cq.subquery(JPAPhoto.class))) ;
        webDAO.setOrder(cq, cb, order, p.get(JPAPhoto_.path)) ;

        Query q = em.createQuery(cq) ;
        int size = q.getResultList().size() ;

        if (bornes != null && bornes.getFirstElement() != null) {
            q.setFirstResult(bornes.getFirstElement());
            q.setMaxResults(session.getPhotoSize());
        }

        List<Photo> subset = q
                    .setHint("org.hibernate.cacheable", true)
                    .setHint("org.hibernate.readOnly", true)
                    .getResultList() ;
        if (bornes == null || bornes.getFirstElement() == null)
            return new SubsetOf<Photo>(subset) ;
        else
            return new SubsetOf<Photo>(bornes, subset, (long) size);

    }

    @Override
    public Photo loadByPath(String path) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<JPAPhoto> cq = cb.createQuery(JPAPhoto.class) ;
            Root<JPAPhoto> p = cq.from(JPAPhoto.class);
            cq.where(cb.equal(p.get(JPAPhoto_.path), path)) ;
            return (JPAPhoto) em.createQuery(cq)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null ;
        }
    }

    @Override
    public SubsetOf<Photo> loadByTags(ServiceSession session, Collection<Tag> listTag, Bornes bornes, ListOrder order) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JPAPhoto> cq = cb.createQuery(JPAPhoto.class) ;
        Root<JPAPhoto> p = cq.from(JPAPhoto.class);
        ListJoin<JPAPhoto, JPATagPhoto> tp = p.join(JPAPhoto_.jPATagPhotoList) ;
        cq.where(cb.and(
                cb.equal(tp.get(JPATagPhoto_.photo), p),
                tp.get(JPATagPhoto_.tag).in(listTag)),
                webDAO.getRestrictionToPhotosAllowed(session, p, cq.subquery(JPAPhoto.class)),
                webDAO.getRestrictionToCurrentTheme(session, p.get(JPAPhoto_.album), Restriction.THEME_ONLY)
                );
        cq.groupBy(p.get(JPAPhoto_.id)) ;
        webDAO.setOrder(cq, cb, order, p.get(JPAPhoto_.path)) ;
        
        Query q = em.createQuery(cq.select(p)) 
                      .setHint("org.hibernate.cacheable", true)
                      .setHint("org.hibernate.readOnly", true);
        //TODO this might not be the better implementation ...
        int size = q.getResultList().size() ;
        if (bornes != null && bornes.getFirstElement() != null) {
            q.setFirstResult(bornes.getFirstElement());
            q.setMaxResults(session.getPhotoSize());
        }
        
        return new SubsetOf<Photo>(bornes, q.getResultList(), (long) size);
    }

    @Override
    public Photo find(Integer id) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<JPAPhoto> cq = cb.createQuery(JPAPhoto.class) ;
            Root<JPAPhoto> p = cq.from(JPAPhoto.class);
            cq.where(cb.equal(p.get(JPAPhoto_.id), id)) ;
            return em.createQuery(cq)
                    .setHint("org.hibernate.cacheable", true)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null ;
        }
    }

    @Override
    public Photo newPhoto() {
        return new JPAPhoto() ;
    }

    @Override
    public List<Photo> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JPAPhoto> cq = cb.createQuery(JPAPhoto.class) ;
        cq.from(JPAPhoto.class) ;
        return (List) em.createQuery(cq)
                .setHint("org.hibernate.cacheable", true)
                .setHint("org.hibernate.readOnly", false)
                .getResultList();
    }

    @Override
    public Photo loadRandom(ServiceSession session) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<JPAPhoto> cq = cb.createQuery(JPAPhoto.class) ;
            Root<JPAPhoto> p = cq.from(JPAPhoto.class) ;
            cq.where(cb.and(
                    webDAO.getRestrictionToPhotosAllowed(session, p, cq.subquery(JPAPhoto.class)),
                    webDAO.getRestrictionToCurrentTheme(session, p.get(JPAPhoto_.album), Restriction.THEME_ONLY)
                    ));
            webDAO.setOrder(cq, cb, ListOrder.RANDOM, null) ;
            
            return (JPAPhoto) em.createQuery(cq)
                    .setFirstResult(0)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null ;
        }
    }
}