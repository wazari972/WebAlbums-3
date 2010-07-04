/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wazari.common.plugins;

import java.io.File;
import net.wazari.common.plugins.Importer.SanityStatus;

/**
 *
 * @author kevinpouget
 */
public interface System {
    String getName() ;
    String getVersion() ;
    
    SanityStatus sanityCheck(ProcessCallback cb) ;
    boolean link(ProcessCallback cb, String source, File dest);
}
