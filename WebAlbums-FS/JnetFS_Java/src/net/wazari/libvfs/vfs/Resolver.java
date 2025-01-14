/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wazari.libvfs.vfs;

import java.util.HashMap;
import java.util.Map;
import net.wazari.libvfs.annotation.ADirectory;
import net.wazari.libvfs.inteface.IDirectory;
import net.wazari.libvfs.inteface.IFile;
import net.wazari.libvfs.inteface.IResolver;
import net.wazari.libvfs.inteface.IntrosDirectory;
import net.wazari.libvfs.inteface.IntrosDirectory.IntrosRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kevin
 */
public class Resolver {
    private static final Logger log = LoggerFactory.getLogger(LibVFS.class.getCanonicalName());
    
    private final IntrosRoot root ;
    private final String externalPrefix;
    private final IResolver external;
    private final Map<String, IFile> cache = new HashMap<>();
    private final boolean do_cache;
    public Resolver(ADirectory rootDir, String externalPrefix, 
            IResolver external, boolean do_cache) {
        root = new IntrosRoot(rootDir);
        this.externalPrefix = externalPrefix;
        this.external = external;
        this.do_cache = do_cache;
    }
    
    public IFile getFile(String search) {
        if (external != null && search.startsWith(externalPrefix)) {
            search = search.substring(externalPrefix.length());
            IFile found = external.getFile(search);
            log.warn("GET EXTERNAL FILE: {} > {}", search, found);
            return found;
        }
        
        if (search.equals("/") || search.equals("")) {
            return root;
        }
        
        if (do_cache) {
            IFile f = cache.get(search);
            if (f != null) {
                return f;
            }
        }
        
        IFile foundFile = getFile(root, "", search);
        addToCache(search, foundFile);
        return foundFile;
    }
    
    public void addToCache(String path, IFile file) {
        if (do_cache && file != null) {
            cache.put(path, file);
        }
    }
    
    IFile getFile(IDirectory current, String path, String search) {
        for (IFile file : current.listFiles())  {
            file.setParent(current);
                
            String fullname = path + "/" + file.getShortname();
            
            if (search.equals(fullname)) {
                return file;
            }
            
            if (file instanceof IntrosDirectory) {
                if (search.startsWith(fullname)) {
                    return (IFile) getFile((IntrosDirectory) file, fullname, search);
                }
                
            } else if (file instanceof ADirectory) {
                if (search.startsWith(fullname)) {
                    return (IFile) getFile(new IntrosDirectory(current, (ADirectory) file), fullname, search);
                }
            } else {
                //nothing to do here, not the right file, not a directory
            } 
        }
        
        return null;
    }  
}
