/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wazari.service.exchange.xml.common;

import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import net.wazari.service.exchange.xml.photo.XmlPhotoId;
import net.wazari.service.exchange.xml.tag.XmlTag;

/**
 *
 * @author kevin
 */

@XmlRootElement
public class XmlDetails {
    @XmlAttribute
    public Boolean isGpx = null;
    public XmlPhotoId photoId;
    public List<String> userInside;
    @XmlElementWrapper
    @XmlElement(name="line")
    public List<String> description;
    @XmlAttribute
    public Integer albumId;
    public String albumName;
    public String albumDate;
    public XmlPhotoAlbumUser user;
    @XmlElement(name = "tagList")
    public XmlWebAlbumsList tag_used;
    @XmlElement
    public XmlTag tagTree;
    @XmlAttribute
    public Integer stars;

    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            return;
        }
        
        this.description = Arrays.asList(description.split("\n")) ;
    }
}
