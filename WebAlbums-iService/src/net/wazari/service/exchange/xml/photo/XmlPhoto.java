/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wazari.service.exchange.xml.photo;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import net.wazari.service.exchange.xml.common.XmlDetails;
import net.wazari.service.exchange.xml.common.XmlInfoException;
import net.wazari.service.exchange.xml.common.XmlWebAlbumsList.XmlWebAlbumsTagWho;

/**
 *
 * @author kevin
 */
@XmlRootElement
public class XmlPhoto extends XmlInfoException {
    public XmlPhotoSubmit submit;
    @XmlAttribute
    public Boolean checked;
    public XmlDetails details = new XmlDetails();
    public XmlPhotoExif exif;
    public XmlWebAlbumsTagWho author;
}
