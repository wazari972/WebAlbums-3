/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wazari.view.vfs.entity;

import java.util.LinkedList;
import java.util.List;
import net.wazari.dao.entity.Theme;
import net.wazari.libvfs.annotation.ADirectory;
import net.wazari.libvfs.annotation.Directory;
import net.wazari.libvfs.annotation.File;
import net.wazari.libvfs.inteface.SDirectory;
import net.wazari.libvfs.inteface.VFSException;
import net.wazari.service.exception.WebAlbumsServiceException;
import net.wazari.service.exchange.xml.album.XmlAlbum;
import net.wazari.service.exchange.xml.album.XmlAlbumYear;
import net.wazari.service.exchange.xml.album.XmlAlbumYears;
import net.wazari.service.exchange.xml.common.XmlDetails;
import net.wazari.view.vfs.FSConnector;
import net.wazari.view.vfs.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kevin
 */
public class Random implements ADirectory {
    private static final Logger log = LoggerFactory.getLogger(Random.class.getCanonicalName()) ;
    
    @File(name="random.jpg")
    public RandomPhoto photo;

    @File(name="By Years")
    @Directory
    public RandYears years;
    
    private final Theme theme;
    private final FSConnector aThis;
    private final Root root;
    
    public Random(Root root, net.wazari.dao.entity.Theme theme, FSConnector aThis) {
        this.theme = theme;
        this.aThis = aThis;
        this.root = root;
    }

    @Override
    public void load() throws VFSException {
        try {
            Session session = new Session(theme, this.root);
            try {
                photo = new RandomPhoto(aThis, session);
            } catch(NullPointerException e) {
                try {
                    photo = new RandomPhoto(aThis, session);
                } catch(NullPointerException ex) {
                    log.warn("treatRANDOM failed twice ...");
                    photo = null;
                }
            }
            years = new RandYears(theme, aThis);
        } catch (Exception ex) {
            throw new VFSException(ex);
        }
    }

    public class RandYear extends SDirectory implements ADirectory {
        @Directory
        @File
        public List<Album> albums = new LinkedList<>();
        
        private final Theme theme;
        private final FSConnector aThis;
        private final String name;
        private final List<XmlAlbum> thealbums;

        public RandYear(Integer year, List<XmlAlbum> thealbums, net.wazari.dao.entity.Theme theme, 
                FSConnector aThis) throws WebAlbumsServiceException {
            this.name = year.toString();
            this.thealbums = thealbums;
            
            this.theme = theme;
            this.aThis = aThis;
        }
        
        @Override
        public String getShortname() {
            return name;
        }

        @Override
        public void load() throws VFSException {
            for (XmlAlbum anAlbum : thealbums) {
                Album album = new Album(Random.this.root, anAlbum.date, anAlbum.name, anAlbum.id, theme, aThis);
                album.noYears();
                albums.add(album);
            }
        }
    }

    public class RandYears implements ADirectory {
        @Directory
        @File
        public List<RandYear> years = new LinkedList<>();
        
        private final Theme theme;
        private final FSConnector aThis;

        public RandYears(net.wazari.dao.entity.Theme theme, FSConnector aThis) throws WebAlbumsServiceException {
            this.theme = theme;
            this.aThis = aThis;
        }

        @Override
        public void load() throws VFSException {
            try {
                Session session = new Session(theme, Random.this.root);
                XmlAlbumYears theYears = aThis.albumService.treatYEARS(session.getSessionAlbumYear());
                
                for (XmlAlbumYear year : theYears.year) {
                    years.add(new RandYear(year.year, year.album, theme, aThis));
                }
            } catch (WebAlbumsServiceException ex) {
                throw new VFSException(ex);
            }
        }
    }

    public class RandomPhoto extends Photo {
        private final Session session;
        private final FSConnector aThis;
        
        public RandomPhoto(FSConnector aThis, Session session) throws WebAlbumsServiceException {
            super(Random.this.root, aThis.photoService.treatRANDOM(session).details);
            this.session = session;
            this.aThis = aThis;
        }
        
        @Override
        public void unlink() throws WebAlbumsServiceException {
            XmlDetails details = aThis.photoService.treatRANDOM(session).details;
            this.setPhoto(details.photoId.path, details.photoId.id);
        }
    }
}

