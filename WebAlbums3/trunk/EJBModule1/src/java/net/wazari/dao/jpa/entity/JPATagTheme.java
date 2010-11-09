/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wazari.dao.jpa.entity;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kevinpouget
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "TagTheme",
    uniqueConstraints = {@UniqueConstraint(columnNames={"Tag", "Theme"})}
)
public class JPATagTheme implements  Serializable {
    private static final Logger log = LoggerFactory.getLogger(JPATagTheme.class.getName());

    private static final long serialVersionUID = 1L;

    @XmlTransient
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @XmlAttribute
    @Column(name = "Photo")
    private Integer photo;

    @XmlAttribute
    @Column(name = "isVisible")
    private Boolean isVisible;

    @XmlTransient
    @JoinColumn(name = "Theme", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private JPATheme theme;

    @XmlTransient
    @JoinColumn(name = "Tag", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private JPATag tag;


    @XmlTransient
    @Transient
    private Integer themeId ;

    @XmlTransient
    @Transient
    private Integer tagId ;

    public JPATagTheme() {
    }

    public JPATagTheme(Integer id) {
        this.id = id;
    }

    
    public Integer getPhoto() {
        return photo;
    }

    
    public void setPhoto(Integer photo) {
        this.photo = photo;
    }

    
    public Boolean getIsVisible() {
        return isVisible;
    }

    
    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    
    public JPATheme getTheme() {
        return (JPATheme) theme;
    }

    
    public void setTheme(JPATheme theme) {
        this.theme = (JPATheme) theme;
    }

    
    public JPATag getTag() {
        return tag;
    }

    
    public void setTag(JPATag tag) {
        this.tag = (JPATag) tag;
    }

    
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @XmlAttribute
    public Integer getThemeId() {
        if (theme == null) {
            return themeId ;
        } else {
            return theme.getId() ;
        }
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId ;
    }

    @XmlAttribute
    public Integer getTagId() {
        if (tag == null) {
            return tagId ;
        } else {
            return tag.getId() ;
        }
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId ;
    }

    
    public boolean equals(Object object) {
        if (!(object instanceof JPATagTheme)) {
            return false;
        }
        JPATagTheme other = (JPATagTheme) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    public String toString() {
        return "net.wazari.dao.jpa.entity.JPATagTheme[Tag=" + (getTag() == null ? "null" : getTag().getId()) + ", Theme="+(getTheme() == null ? "null" : getTheme().getId())+"]";
    }

}
