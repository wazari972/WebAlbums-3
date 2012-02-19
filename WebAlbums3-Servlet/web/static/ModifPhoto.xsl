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
  <xsl:template match="photos/edit | tags/edit">
    <div class="item">
      <div class="date">
	<span>*</span>
      </div>
      <div class="content">
	<h1>Modification d'une photo</h1>
	<div class="body">
	  <center>
	    <img>
	      <xsl:attribute name="SRC">Images?id=<xsl:value-of select="@id" />&amp;mode=PETIT</xsl:attribute>
	    </img>
	  </center>
	</div>
      </div>
      <div class="content">
	<div class="body">
	  <form method='POST'>
	    <xsl:attribute name="ACTION">
	      <xsl:call-template name="get_validate_addr" />
	    </xsl:attribute>
	    <input type='hidden' name='action' value='SUBMIT' />
	    <label for="desc">Description:</label>
	    <textarea id="desc" name='desc' rows='5' cols='60'>
	      <xsl:value-of select="description" />
	    </textarea>
	    <br/>
	    <xsl:apply-templates select="tagList">
	      <xsl:with-param name="mode">TAG_USED</xsl:with-param>
	      <xsl:with-param name="name">newTag</xsl:with-param>
	      <xsl:with-param name="style">multiple</xsl:with-param>
	    </xsl:apply-templates>
	    <xsl:apply-templates select="tagList">
	      <xsl:with-param name="mode">TAG_NUSED</xsl:with-param>
	      <xsl:with-param name="style">multiple</xsl:with-param>
	      <xsl:with-param name="name">newTag</xsl:with-param>
	    </xsl:apply-templates>
	    <xsl:apply-templates select="tagList">
	      <xsl:with-param name="mode">TAG_NEVER</xsl:with-param>
	      <xsl:with-param name="style">multiple</xsl:with-param>
	      <xsl:with-param name="name">newTag</xsl:with-param>
	    </xsl:apply-templates>
	    <br/>
            <label for="sure">"Oui je veux supprimer cette photo" (d�finitif !)</label>
	    <input id="sure" autocomplete='off' type='text' name='suppr' size='33' maxlength='33'/>
	    <br/>
	    <br/>
	    <label for="represent"></label>Representer l'album ? <input type='checkbox' id="represent" name='represent' value='y' /><br/>
	    <label for="tagPhoto">Representer le tag ?</label> 
	    <xsl:apply-templates select="tagList">
	      <xsl:with-param name="mode">TAG_USED</xsl:with-param>
	      <xsl:with-param name="box">LIST</xsl:with-param>
	      <xsl:with-param name="style">list</xsl:with-param>
	      <xsl:with-param name="name">tagPhoto</xsl:with-param>
              <xsl:with-param name="id">tagPhoto</xsl:with-param>
	    </xsl:apply-templates>
	    <br/>
            <label for="bg">Theme picture ? </label><input id="bg" type='checkbox' name='themePicture' value='y' />
            <br/>
            <label for="bg">Theme background ? </label><input id="bg" type='checkbox' name='themeBackground' value='y' />
            <input type="button" value="Try it!">
                <xsl:attribute name="ONCLICK">
                  updateBackground(<xsl:value-of select="@id" />) ;
                </xsl:attribute>
            </input><br />
	    <label>Droits de visibilit� : </label><xsl:apply-templates select="rights"/>
	    <br/>
	    <br/>

	    <input type='submit' value='Valider'/>
	  </form>
	  <br/>
	  <br/>
          <center>
              <A>
                <xsl:attribute name="HREF">
                  <xsl:call-template name="get_validate_addr" />
                </xsl:attribute>
                Retour aux <xsl:value-of select="../return_to/name" />
              </A>
          </center>
	</div>
      </div>
    </div>
  </xsl:template>
</xsl:stylesheet>
