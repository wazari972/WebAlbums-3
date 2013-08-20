/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wazari.dao;

import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import net.wazari.dao.entity.*;

/**
 *
 * @author kevin
 */
@Local
@DeclareRoles({UtilisateurFacadeLocal.MANAGER_ROLE, UtilisateurFacadeLocal.VIEWER_ROLE})
public interface TagPhotoFacadeLocal {

    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    void create(TagPhoto tagPhoto);

    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    void edit(TagPhoto tagPhoto);

    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    void remove(TagPhoto tagPhoto);

    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    void deleteByPhoto(Photo enrPhoto) ;

    @RolesAllowed(UtilisateurFacadeLocal.VIEWER_ROLE)
    List<TagPhoto> queryByAlbum(Album album) ;

    @RolesAllowed(UtilisateurFacadeLocal.VIEWER_ROLE)
    TagPhoto loadByTagPhoto(int tagID, int photoId) ;

    @RolesAllowed(UtilisateurFacadeLocal.VIEWER_ROLE)
    List<Tag> selectDistinctTags() ;

    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    TagPhoto newTagPhoto();

    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    List<TagPhoto> findAll();
    
    @RolesAllowed(UtilisateurFacadeLocal.VIEWER_ROLE)
    List<TagPhoto> queryByCarnet(Carnet carnet);

}