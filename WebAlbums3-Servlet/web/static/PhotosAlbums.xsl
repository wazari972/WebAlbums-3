<?xml version="1.0" encoding="ISO-8859-1"?>
<!DOCTYPE xsl:stylesheet  [
  <!ENTITY % xhtml-lat1 SYSTEM
     "http://www.w3.org/TR/xhtml1/DTD/xhtml-lat1.ent">
  <!ENTITY % xhtml-special SYSTEM
     "http://www.w3.org/TR/xhtml1/DTD/xhtml-special.ent">
  <!ENTITY % xhtml-symbol SYSTEM
     "http://www.w3.org/TR/xhtml1/DTD/xhtml-symbol.ent">
  %xhtml-lat1;
  %xhtml-special;
  %xhtml-symbol;
  ]>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template name="for-stars-loop">
        <xsl:param name="count" select="1"/>
        <xsl:param name="stars"/>
        <xsl:param name="photoId"/>
        
        <xsl:if test="$count > 0">
            <xsl:call-template name="for-stars-loop">
                <xsl:with-param name="count" select="$count - 1"/>
                <xsl:with-param name="stars" select="$stars"/>
                <xsl:with-param name="photoId" select="$photoId"/>
            </xsl:call-template>
            <img>
                <xsl:if test="/webAlbums/affichage/@edit">
                    <xsl:attribute name="class">fastedit_stars</xsl:attribute>
                    <xsl:attribute name="rel">
                        <xsl:value-of select="photoId" />/<xsl:value-of select="$count" />
                    </xsl:attribute>
                </xsl:if>
                <xsl:if test="$count > $stars">
                    <xsl:attribute name="src">static/images/star.off.png</xsl:attribute>
                </xsl:if>
                <xsl:if test="not($count > $stars)">
                    <xsl:attribute name="src">static/images/star.on.png</xsl:attribute>
                </xsl:if>
            </img>
        </xsl:if>
    </xsl:template>
    
  <xsl:template match="details">
    <xsl:if test="../exif">
      <span class="exif_tooltip">
        <xsl:attribute name="id">exif-content-<xsl:value-of select="photoId" /></xsl:attribute>
        <xsl:apply-templates select="../exif" />
      </span>
    </xsl:if>
      <div class="details">
        <div class="pict">
            <a>
              <xsl:if test="/webAlbums/photos or /webAlbums/tags">
                <xsl:attribute name="rel">shadowbox[page];player=img</xsl:attribute>
              </xsl:if>
              <xsl:attribute name="HREF">
                <xsl:if test="/webAlbums/photos or /webAlbums/tags">
                  Images?id=<xsl:value-of select="photoId" />&amp;mode=GRAND
                </xsl:if>
                <xsl:if test="/webAlbums/albums">
                  Photos?albmCount=<xsl:value-of select="../@count" />&amp;album=<xsl:value-of select="../@id" />
                </xsl:if>
                <xsl:if test="/webAlbums/carnets">
                  Carnets?carnetCount=<xsl:value-of select="../@count" />&amp;carnet=<xsl:value-of select="../@id" />
                </xsl:if>
              </xsl:attribute>
              <img class="photo">
                <xsl:attribute name="id">exif-target-<xsl:value-of select="photoId" /></xsl:attribute>
                <xsl:attribute name="alt">
                  <xsl:value-of select="title" />
                </xsl:attribute>

                <xsl:attribute name="src">
                  <xsl:if test="normalize-space(photoId) = ''">
                    static/images/rien.jpg
                  </xsl:if>
                  <xsl:if test="normalize-space(photoId) != ''">
                    Images?id=<xsl:value-of select="photoId" />&amp;mode=PETIT
                  </xsl:if>
                </xsl:attribute>
              </img>
            </a>
        </div>
        <div class="info">
            <xsl:apply-templates select="user" />
            <xsl:if test="/webAlbums/affichage/@massedit and not(/webAlbums/albums or /webAlbums/photos/random or /webAlbums/carnets)">
                <div class="massedit_chk edit">
                    <input type="checkbox" class="massedit_chkbox edit" value="modif">
                        <xsl:attribute name="name">chk<xsl:value-of select="photoId" /></xsl:attribute>
                    </input>
                </div>
            </xsl:if>
            <xsl:if test="not(/webAlbums/albums or /webAlbums/photos/random or /webAlbums/carnets)">
                <div class="fastedit_bt fastedit_desc_bt edit">
                    <xsl:attribute name="rel"><xsl:value-of select="photoId" /></xsl:attribute>
                    Descr.
                </div>
                <div class="fastedit_bt fastedit_tag_bt edit">
                    <xsl:attribute name="rel"><xsl:value-of select="photoId" /></xsl:attribute>
                    Tags.
                </div>
                <div class="fastedit_bt fastedit_stars_bt edit" id="">
                    <xsl:attribute name="rel"><xsl:value-of select="photoId" /></xsl:attribute>
                    Stars.
                </div>
            </xsl:if>
            <div class="options">
                <xsl:if test="not(/webAlbums/albums) and not(/webAlbums/carnets)">
                    <a title="Photo r�duite">
                      <xsl:attribute name="href">Images?id=<xsl:value-of select="photoId" />&amp;mode=SHRINK&amp;width=800&amp;borderWidth=10&amp;borderColor=white</xsl:attribute>
                      <img src="static/images/reduire.gif" width="30px"/>
                    </a>
                </xsl:if>
                <xsl:if test="not(/webAlbums/albums) and not(/webAlbums/carnets)">
                    <xsl:if test="/webAlbums/affichage/remote">
                        <img alt="Photo en plein-ecran"
                           class="fullscreen"
                           src="static/images/out.png" width="30px">
                            <xsl:attribute name="rel">
                                Images?id=<xsl:value-of select="photoId" />&amp;mode=FULLSCREEN
                            </xsl:attribute>
                        </img>
                    </xsl:if>
                </xsl:if>
                <xsl:if test="/webAlbums/tags or /webAlbums/photos/random">
                    <a class="albumTT">
                      <xsl:attribute name="id">album-target-<xsl:value-of select="albumId"/></xsl:attribute>
                      <xsl:attribute name="href">Photos?album=<xsl:value-of select="albumId" /></xsl:attribute>
                      <img src="static/images/dossier.gif" width="30px"/>
                    </a>
                    <span class="album_tooltip">
                        <xsl:attribute name="id">album-content-<xsl:value-of select="albumId"/></xsl:attribute>
                        <xsl:attribute name="rel"><xsl:value-of select="albumId"/></xsl:attribute>
                    </span>
                  </xsl:if>
                  <xsl:if test="/webAlbums/affichage/@edit">
                        <a class="edit" title="Edition">
                          <xsl:attribute name="href">
                            <xsl:if test="/webAlbums/photos">
Photos?action=EDIT
&amp;id=<xsl:value-of select="photoId" />
&amp;count=<xsl:value-of select="../@count"	/>
&amp;albmCount=<xsl:value-of select="../../../album/@count" />
&amp;album=<xsl:value-of select="../../../album/@id"	/>
                              </xsl:if>
                              <xsl:if test="/webAlbums/tags">
Tags?action=EDIT
&amp;id=<xsl:value-of select="photoId" />
                                <xsl:for-each select="/webAlbums/tags/display/title/tagList/*">
&amp;tagAsked=<xsl:value-of select="@id" />
                                </xsl:for-each>
                                <xsl:if test="/webAlbums/*/page/@current">
&amp;page=<xsl:value-of select="/webAlbums/*/page/@current" />
                                </xsl:if>
                              </xsl:if>
                              <xsl:if test="/webAlbums/albums">
Albums?action=EDIT
&amp;id=<xsl:value-of select="../@id" />
&amp;count=<xsl:value-of select="../@count"/>
                              </xsl:if>
                              <xsl:if test="/webAlbums/carnets">
Carnets?action=EDIT
&amp;carnet=<xsl:value-of select="../@id" />
&amp;count=<xsl:value-of select="../@count"/>
                              </xsl:if>
                          </xsl:attribute>
                          <img src="static/images/edit.png" height="30px"/>
                        </a>
                    </xsl:if>
                    <xsl:if test="not(/webAlbums/albums)">
                        <div class="stars">
                            <xsl:attribute name="id">stars_<xsl:value-of select="photoId" /></xsl:attribute>
                            <xsl:call-template name="for-stars-loop">
                              <xsl:with-param name="count" select="5"/>
                              <xsl:with-param name="stars" select="@stars"/>
                              <xsl:value-of name="stars" select="photoId" />
                            </xsl:call-template>
                        </div>
                        <div class="fastedit">
                            <xsl:attribute name="id">fastedit_div_stars_<xsl:value-of select="photoId" /></xsl:attribute>
                            <xsl:attribute name="rel"><xsl:value-of select="@stars" /></xsl:attribute>
                             <p>
                                  <input name="stars" type='button' value="-" class="fastedit_stars_dec">
                                      <xsl:attribute name="rel"><xsl:value-of select="photoId" /></xsl:attribute>
                                  </input>
                                  <input name="stars" type='button' value="+" class="fastedit_stars_inc">
                                      <xsl:attribute name="rel"><xsl:value-of select="photoId" /></xsl:attribute>
                                  </input>
                              </p>
                       </div>
                    </xsl:if>
                    <xsl:apply-templates select="tagList">
                      <xsl:with-param name="style">none</xsl:with-param>
                      <xsl:with-param name="mode">TAG_USED</xsl:with-param>
                      <xsl:with-param name="box">NONE</xsl:with-param>
                    </xsl:apply-templates>
                    <div class="fastedit">
                          <xsl:attribute name="id">fastedit_div_tag_<xsl:value-of select="photoId" /></xsl:attribute>
                          <p>
                                <xsl:apply-templates select="../../massEdit/tagList">
                                    <xsl:with-param name="style">list</xsl:with-param>
                                    <xsl:with-param name="id">fastedit_tag_<xsl:value-of select="photoId" /></xsl:with-param>
                                    <xsl:with-param name="mode">TAG_USED</xsl:with-param>
                                    <xsl:with-param name="mode2">TAG_NEVER</xsl:with-param>
                                </xsl:apply-templates><br/>           
                                <input value="+" type="button" class="fastedit_addtag">
                                    <xsl:attribute name="rel"><xsl:value-of select="photoId" /></xsl:attribute>
                                </input>
                                <input value="-" type="button" class="fastedit_rmtag">
                                    <xsl:attribute name="rel"><xsl:value-of select="photoId" /></xsl:attribute>
                                </input>
                         </p>
                    </div>
                    <div class="description">
                        <xsl:attribute name="id">desc_<xsl:value-of select="photoId" /></xsl:attribute>
                        <xsl:value-of select="description" />
                    </div>
                    <div class="fastedit">
                            <xsl:attribute name="id">fastedit_div_desc_<xsl:value-of select="photoId" /></xsl:attribute>
                             <p>
                                  <textarea cols="30" >
                                      <xsl:attribute name="id">fastedit_desc_<xsl:value-of select="photoId" /></xsl:attribute>
                                      <xsl:value-of select="description" />
                                  </textarea>
                                  <input value="edit" type="button" class="fastedit_desc">
                                    <xsl:attribute name="rel"><xsl:value-of select="photoId" /></xsl:attribute>
                                  </input>
                              </p>
                       </div>
                <if test="../carnet">
                    <div class="carnets_opt">
                        <xsl:apply-templates select="../carnet"/>
                    </div>
                </if>
              </div>
          </div>
        </div>
  </xsl:template>
  
  <xsl:template match="rights">
    <select name="user">
      <xsl:apply-templates select="user"/>
    </select>
  </xsl:template>
  
  <xsl:template match="rights/user">
    <option>
      <xsl:attribute name="value"><xsl:value-of select="@id" /></xsl:attribute>
      <xsl:if test="@selected">
	<xsl:attribute name="selected">true</xsl:attribute>
      </xsl:if>
      <xsl:value-of select="." />
    </option>
  </xsl:template>
  
  <xsl:template match="user">
      <div class="visibility">
        <xsl:if test="/webAlbums/albums">
          <xsl:value-of select="."/><xsl:apply-templates select="../userInside"/>
        </xsl:if>
        <xsl:if test="/webAlbums/photos and not(@album)">
          <xsl:if test="@outside = 'true'">[</xsl:if>
          <xsl:value-of select="."/>
          <xsl:if test="@outside  = 'true'">]</xsl:if>
        </xsl:if>
        <xsl:if test="/webAlbums/tags and @album">
          [<xsl:value-of select="."/>]
        </xsl:if>
        <xsl:if test="/webAlbums/tags and not(@album)">
         <xsl:value-of select="."/>
        </xsl:if>
    </div>
  </xsl:template>

  <xsl:template match="userInside">(<xsl:value-of select="."/>)</xsl:template>
</xsl:stylesheet>
