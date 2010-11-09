/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wazari.dao.jpa.entity;

import java.io.Serializable;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kevinpouget
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "Theme",
    uniqueConstraints = {@UniqueConstraint(columnNames={"Nom"})}
)
public class JPATheme implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(JPATheme.class.getName());
    
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @XmlElement
    @Basic(optional = false)
    @Column(name = "Nom", nullable = false, length = 100)
    private String nom;

    @XmlTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "theme", fetch = FetchType.LAZY)
    private List<JPATagTheme> jPATagThemeList;

    @XmlTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "theme", fetch = FetchType.LAZY)
    private List<JPAAlbum> jPAAlbumList;

    @XmlAttribute
    @Column(name = "Picture", nullable = true)
    private Integer picture;


    public JPATheme() {
    }

    public JPATheme(Integer id) {
        this.id = id;
    }

    public JPATheme(Integer id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public String getNom() {
        return nom;
    }

    
    public void setNom(String nom) {
        this.nom = nom;
    }

    
    public List<JPATagTheme> getTagThemeList() {
        return (List) jPATagThemeList;
    }

    
    public void setTagThemeList(List<JPATagTheme> jPATagThemeList) {
        this.jPATagThemeList = (List) jPATagThemeList;
    }

    
    public List<JPAAlbum> getAlbumList() {
        return (List) jPAAlbumList;
    }

    
    public void setAlbumList(List<JPAAlbum> jPAAlbumList) {
        this.jPAAlbumList = (List) jPAAlbumList;
    }

    
    public Integer getPicture() {
        return picture;
    }

    
    public void setPicture(Integer picture) {
        this.picture = picture;
    }

    
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    
    public boolean equals(Object object) {
        if (!(object instanceof JPATheme)) {
            return false;
        }
        JPATheme other = (JPATheme) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    public String toString() {
        return "net.wazari.dao.jpa.entity.JPATheme[id=" + id + "]";
    }

}
