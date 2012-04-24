/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wazari.dao;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 *
 * @author kevinpouget
 */
@Local
@DeclareRoles({UtilisateurFacadeLocal.MANAGER_ROLE, UtilisateurFacadeLocal.VIEWER_ROLE})
public interface MaintFacadeLocal {
    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    void treatImportXML(String path) ;

    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    void treatExportXML(String path) ;

    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    void treatTruncateDB() ;
    
    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    void treatFullImport(String path) ;

    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    void treatDumpStats() ;

    @RolesAllowed(UtilisateurFacadeLocal.MANAGER_ROLE)
    void treatUpdate();
}