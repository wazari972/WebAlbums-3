/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wazari.service.exchange.xml.album;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kevin
 */
@XmlRootElement
public class XmlAlbumYears {
    public XmlAlbumYears() {
        year = new LinkedList<XmlAlbumYear>() ;
    }
    @XmlElement
    public final List<XmlAlbumYear> year ;
}
