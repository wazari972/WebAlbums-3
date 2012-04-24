package net.wazari.service.engine;

import java.util.logging.Level;
import net.wazari.service.SessionManagerLocal;
import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import net.wazari.service.exchange.ViewSessionSession;
import net.wazari.util.system.SystemTools;

@Stateless
public class SessionManagerBean implements SessionManagerLocal {

    private static final Logger log = Logger.getLogger(SessionManagerBean.class.toString());

    @EJB SystemTools sysTools ;
    /* Session Listener */
    @Override
    public void sessionCreated(ViewSessionSession vSession) {
        log.log(Level.INFO, "Session created {0}", getUID());
        File temp = new File(vSession.getConfiguration().getTempPath() + vSession.getConfiguration().getSep() + getUID());
        log.info(temp.toString());
        if (!temp.mkdir()) {
            temp = null;
        } else {
            temp.deleteOnExit();
        }
        log.log(Level.INFO, "temp dir created: {0}", temp);
        vSession.setTempDir(temp) ;
    }

    @Override
    public void sessionDestroyed(ViewSessionSession vSession) {
        File temp = vSession.getTempDir() ;
        if (temp != null) {
            log.log(Level.INFO, "temp dir deleted: {0}", temp);
            temp.delete();
        }
        log.log(Level.INFO, "Session destroyed {0}", getUID());
    }
    
    private String getUID() {
        UUID id = UUID.randomUUID() ;
        return id.toString() ;
    }
}