/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wazari.dao.jpa;

import java.util.logging.Level;
import net.wazari.dao.*;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import net.wazari.dao.entity.Utilisateur;
import net.wazari.dao.jpa.entity.JPAUtilisateur;

/**
 *
 * @author kevin
 */
@Stateless
public class UtilisateurFacade implements UtilisateurFacadeLocal {
    private static final Logger log = Logger.getLogger(UtilisateurFacade.class.getCanonicalName()) ;

    static {
        log.warning("Loading WebAlbums3-DAO-JPABeans");
    }
    @EJB AlbumFacadeLocal albumDAO ;

    @PersistenceContext(unitName=WebAlbumsDAOBean.PERSISTENCE_UNIT)
    private EntityManager em;

    @Override
    public void newUser(int id, String nom) {
        em.merge(new JPAUtilisateur(id, nom));
    }

    @Override
    public Utilisateur loadByName(String name) {
        try {
            String rq = "FROM JPAUtilisateur u WHERE u.nom = :nom";
            return (JPAUtilisateur) em.createQuery(rq)
                    .setParameter("nom", name)
                    .setHint("org.hibernate.cacheable", true)
                    .setHint("org.hibernate.readOnly", true)
                    .getSingleResult();
        } catch (NoResultException e) {
            log.log(Level.INFO, "No user with name +{0}+", name);
            return null ;
        }
    }

    @Override
    public Utilisateur loadUserOutside(int albumId) {
        String rq = "SELECT u FROM JPAUtilisateur u, JPAAlbum a WHERE u.id = a.droit AND a.id = :albumId";
        return (JPAUtilisateur) em.createQuery(rq)
                .setParameter("albumId", albumId)
                .setHint("org.hibernate.cacheable", true)
                .setHint("org.hibernate.readOnly", true)
                .getSingleResult();
    }

    @Override
    public List<Utilisateur> loadUserInside(int albumId) {
        String rq = "SELECT DISTINCT u " +
                " FROM JPAPhoto p, JPAUtilisateur u " +
                " WHERE u.id = p.droit " +
                " AND p.album.id = :albumId " +
                " AND p.droit != null AND p.droit != 0";
        return  em.createQuery(rq)
                .setParameter("albumId", albumId)
                .setHint("org.hibernate.cacheable", true)
                .setHint("org.hibernate.readOnly", true)
                .getResultList();
    }

    @Override
    public JPAUtilisateur find(Integer droit) {
        try {
            String rq = "FROM JPAUtilisateur u WHERE u.id = :id";
            return (JPAUtilisateur) em.createQuery(rq)
                    .setParameter("id", droit)
                    .setHint("org.hibernate.cacheable", true)
                    .setHint("org.hibernate.readOnly", true)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null ;
        }
    }

    @Override
    public List<Utilisateur> findAll() {
        String rq = "FROM JPAUtilisateur u";
        return em.createQuery(rq)
                .setHint("org.hibernate.cacheable", true)
                .setHint("org.hibernate.readOnly", true)
                .getResultList() ;
    }
}