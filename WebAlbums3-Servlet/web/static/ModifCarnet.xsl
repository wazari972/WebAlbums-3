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
  <xsl:template match="carnets/edit">
    <div class="item">
      <div class="date">
	<span>*</span>
      </div>
      <div class="content">
          <xsl:if test="not(/webAlbums/carnets/edit/@id)">
              <h1>Création d'un carnet</h1>
          </xsl:if>
          <xsl:if test="/webAlbums/carnets/edit/@id">
              <h1>Modification d'un carnet</h1>
          </xsl:if>
	<div class="body">
	  <center>
	    <img>
	      <xsl:attribute name="src">Images?id=<xsl:value-of select="@picture" />&amp;mode=PETIT</xsl:attribute>
	    </img>
	  </center>
	</div>
      </div>
      <div class="content">
	<div class="body">
	  <form method='post'>
	    <xsl:attribute name="action">Carnets?
&amp;count=<xsl:value-of select="@count" />
&amp;carnet=<xsl:value-of select="@id" />
#<xsl:value-of select="@id" />
	    </xsl:attribute>
	    <input type='hidden' name='action' value='SUBMIT' />
	    <label for="nom">Nom:</label>
	    <input id="nom" type='text' size='40' maxlength='60' name='nom'>
	      <xsl:attribute name="VALUE"><xsl:value-of select="name" /></xsl:attribute>
	    </input>
	    <br/>
            <label for="date">Date:</label> 
	    <input id="date" type='text' size='10' name='date' maxlength='10'>
	      <xsl:attribute name="VALUE"><xsl:value-of select="date" /></xsl:attribute>
	    </input>
            Photo pour représentation: <input type='text' name='carnetRepr' maxlength="4" size="5"/>
            <br/>
            Photos: <input type='text' name='carnetPhoto' />
	    <br/>
	    Droits de visibilité : <xsl:apply-templates select="rights"/>
            <br/>
            <label for="desc">Description:</label>
	    <textarea id="desc" name='desc' rows='2' cols='65'>
	      <xsl:value-of select="description" />
	    </textarea>
            <div class="wmd-panel">
                <div id="wmd-button-bar"></div>
                <textarea class="wmd-input" id="wmd-input" name="carnetText">
                    <xsl:value-of select="text" />
                </textarea>
            </div>
            <div id="wmd-preview" class="wmd-panel wmd-preview"></div>
                    <br/>
	    <input type='submit' value='Valider'/>
            <br/>
            <br/>
            <label for="sure">"Oui je veux supprimer ce carnet" (définitif!)</label>
	    <input id="sure" type='text' autocomplete='off' name='suppr' size='31' maxlength='31'/>
	  </form>
	  <br/>
	  <br/>
          <center>
              <a>
                <xsl:attribute name="href">
                  Carnets?count=<xsl:value-of select="@count"/>&amp;carnet=<xsl:value-of select="@id" />
                </xsl:attribute>
                Retour au carnet
              </a>
          </center>
	</div>
      </div>
    </div>
    <script type="text/javascript" src="static/scripts/ModifCarnet.js"></script>
  </xsl:template>
</xsl:stylesheet>
