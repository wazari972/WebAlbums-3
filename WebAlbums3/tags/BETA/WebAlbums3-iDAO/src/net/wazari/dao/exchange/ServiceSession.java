/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wazari.dao.exchange;

import net.wazari.dao.entity.Theme;
import net.wazari.dao.entity.Utilisateur;


/**
 *
 * @author kevin
 */
public interface ServiceSession {

    Theme getTheme();

    Utilisateur getUser();

    boolean isRootSession();

    boolean isSessionManager();

    int getAlbumSize() ;
    
    int getPhotoSize() ;
}
