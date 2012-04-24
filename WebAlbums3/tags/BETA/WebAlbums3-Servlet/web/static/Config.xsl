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
  <xsl:template match="config">
    <div class="item">
      <a name="import"/>
      <div class="date">
	<span>*</span>
      </div>
      <div class="content">
	<h1>Imports d'albums</h1>
	<div class="body">
	  <xsl:apply-templates select="import"/>
	  <form action='Config#import' method='POST'>
	    <input type='hidden' name='action' value='IMPORT'/>
	    <input type='text' name='importTheme' size='20' maxlenght='20'>
	      <xsl:attribute name="value"><xsl:value-of select="/root/login/theme"/>
	      </xsl:attribute>
	    </input>
	    <br/>
	    <input type='submit' value='Importer'/>
	  </form>
	</div>
      </div>
    </div>

    <div class="item">
      <a name="newTag"/>
      <div class="date">
	<span>*</span>
      </div>
      <div class="content">
	<h1>Ajout d'un tag</h1>
	<div class="body">
	  <form action='Config#newTag' method='POST' name='newTag' >
	    <xsl:apply-templates select="newTag">
	      <xsl:with-param name="mode">TAG_USED</xsl:with-param>
	    </xsl:apply-templates>
	    <input type='hidden' name='action' value='NEWTAG'/>
	    <table>
	      <tr>
		<td>Nom :</td>
		<td>
		  <input id="newTag" name="nom" type='text' size='20' maxlength='40'/>
		  <input type='button' value='Goto' onClick="showAddress(document.newTag.nom.value);return false"/>
		  <input id="mapLoader" type="button" value="load map" onclick="loadMaps();"/>
		</td>
	      </tr>
	      <tr>
		<td>Type :</td> 
		<td>
		  <select name='type' id="lstNewTag" onChange='javacript:checkValidity("valNewTag","lstNewTag")'>  
		    <option value='-1'>========</option>
		    <option value='1' >[WHO]</option>
		    <option value='2' >[WHAT]</option>
		    <option value='3' >[WHERE]</option>
		  </select>
		</td>
	      </tr>
	      <tr>
		<td colspan='2'>
		  <div id='map_search' style='width: 400px; height: 250px'></div><br/> 
		</td>
	      </tr>
	      <tr>	
		<td align='left'> Long</td>
		<td>
		  <input id="lngID" name='long' type='text' size='20' maxlength='20'/>
		</td>	
	      </tr>
	      <tr>	
		<td align='left'> Lat</td>
		<td>
		  <input id="latID" name='lat' type='text' size='20' maxlength='20'/>
		</td>	
	      </tr>
	    </table>
	    <input type='submit' value='Valider' id="valNewTag" disabled="true"/>
	  </form>

	</div>
      </div>
    </div>

    <div class="item">
      <a name="modGeo"/>
      <div class="date">
	<span>*</span>
      </div>
      <div class="content">
	<h1>Modification d'une localisation</h1>
	<div class="body">
	  <xsl:apply-templates select="modGeo"/>
	  <form action='Config#modGeo' method='POST'>
	    <input type='hidden' name='action' value='MODGEO'/>
	    <table>
	      <tr>
		<td align='left'> Tag : </td>
		<td>
		  <xsl:apply-templates select="tags">
		    <xsl:with-param name="style">list</xsl:with-param>
		    <xsl:with-param name="mode">TAG_GEO</xsl:with-param>
		    <xsl:with-param name="name">tag</xsl:with-param>
		    <xsl:with-param name="id">lstModGeo</xsl:with-param>
		    <xsl:with-param name="onChange">javacript:checkValidity("valModGeo","lstModGeo")</xsl:with-param>
		  </xsl:apply-templates>
		</td>
      	      </tr>
	      <tr>	
		<td align='left'> Long</td>
		<td>
		  <input id="lngID_2" name='lng' type='text' size='20' maxlength='20'/>
		</td>	
	      </tr>
	      <tr>	
		<td align='left'> Lat</td>
		<td>
		  <input id="latID_2" name='lat' type='text' size='20' maxlength='20'/>
		</td>	
	      </tr>
	    </table>
	    <input id="valModGeo" type='submit' value='Valider' disabled="true"/>
	  </form>
	</div>
      </div>
    </div>

    <div class="item">
      <a name="modTag"/>
      <div class="date">
	<span>*</span>
      </div>
      <div class="content">
	<h1>Renommage d'un tag</h1>
	<div class="body">
	  <form action='Config#modTag' method='POST' >
	    <xsl:apply-templates select="modTag"/>
	    <input type='hidden' name='action' value='MODTAG'/>
	    <table>
	      <tr>
		<td align='left'> Ancien : </td>
		<td>
		  <xsl:apply-templates select="tags">
		    <xsl:with-param name="style">list</xsl:with-param>
		    <xsl:with-param name="mode">TAG_USED</xsl:with-param>
		    <xsl:with-param name="name">tag</xsl:with-param>
		    <xsl:with-param name="id">lstModTag</xsl:with-param>
		    <xsl:with-param name="onChange">javacript:checkValidity("valModTag","lstModTag")</xsl:with-param>
		  </xsl:apply-templates>
		</td>
	      </tr>
	      <tr>
      		<td align='left'> Nouveau : </td>
		<td><input name='nouveau' type='text' size='20'
			   maxlength='20'/></td>	
	      </tr>
	    </table>
	    <input type='submit' value='Valider' disabled="true" id="valModTag"/>
	  </form>
	</div>
      </div>
    </div>

    <xsl:if test="not(/root/login/root)">
    <div class="item">
      <a name="modVis"/>
      <div class="date">
	<span>*</span>
      </div>
      <div class="content">
	<h1>Changement de visibilit� d'un tag</h1>
	<div class="body">
	  <xsl:apply-templates select="modVis"/>
	  <form action='Config#modVis' method='POST'>
	    <input type='hidden' name='action' value='MODVIS'/>
	    <table>
	      <tr>
		<td align='left'> Tag : </td>	
		<td>
		  <xsl:apply-templates select="tags">
		    <xsl:with-param name="style">list</xsl:with-param>
		    <xsl:with-param name="mode">TAG_GEO</xsl:with-param>
		    <xsl:with-param name="name">tag</xsl:with-param>
		    <xsl:with-param name="id">lstModVis</xsl:with-param>
		    <xsl:with-param name="onChange">javacript:checkValidity("valModVis","lstModVis")</xsl:with-param>
		  </xsl:apply-templates>
		</td>
	      </tr>
	      <tr>
		<td align='left'> Visible ? </td>
		<td><input name='visible' value='y' type='checkbox'/></td>
	      </tr>
	    </table>
	    <input type='submit' value='Valider' id="valModVis" disabled="true"/>
	  </form>
	  <br/><br/>
	</div>
      </div>
    </div>
    </xsl:if>
   
    <div class="item">
      <a name="delTag"/>
      <div class="date">
	<span>*</span>
      </div>
      <div class="content">
	<a name="delTag"/>
	<h1>Suppression d'un tag</h1>
	<div class="body">
	  <xsl:apply-templates select="delTag"/>
	  <form action='Config#delTag' method='POST'>
	    <input type='hidden' name='action' value='DELTAG'/>
	    Tag : 
	    <xsl:apply-templates select="tags">
	      <xsl:with-param name="style">list</xsl:with-param>
	      <xsl:with-param name="mode">TAG_NEVER</xsl:with-param>
	      <xsl:with-param name="name">tag</xsl:with-param>
	      <xsl:with-param name="id">lstDelTag</xsl:with-param>
	      <xsl:with-param name="onChange">javacript:checkValidity("valDelTag","lstDelTag")</xsl:with-param>
	    </xsl:apply-templates>
	    <br/>
	    Yes ? <input type='text' name='sure' size='3' maxlength='3'/><br/>
	    <input type='submit' value='Valider' id="valDelTag" disabled="true" />
	  </form>
	</div>
      </div>
    </div>

    <div class="item">
      <a name="delTheme"/>
      <div class="date">
	<span>*</span>
      </div>
      <div class="content">
	<a name="delTheme"/>
	<h1>/!\Suppression du theme/!\</h1>
	<div class="body">
            <xsl:apply-templates select="delTheme"/>
            <center>
              <form action='Config#delTheme' method='POST'>
                <input type='hidden' name='action' value='DELTHEME'/>
                <input type='submit' value='GO !'/>
              </form>
          </center>
	</div>
      </div>
    </div>
  </xsl:template>

  <xsl:template match="map">
    <script type="text/javascript" src="static/scripts/Config.js"/>
  </xsl:template>

  <xsl:template match="import|newTag|modTag|delTag|modGeo|modVis">
    <xsl:apply-templates select="Exception|message" />

    <xsl:apply-templates select="newName" />
    <xsl:apply-templates select="alreadyName" />
    <xsl:apply-templates select="newLngLat" />
  </xsl:template>
  <xsl:template match="newName">
    Nom correctement chang� de <b><xsl:value-of select="../oldName"/></b> vers <b><xsl:value-of select="."/></b>
  </xsl:template>
  <xsl:template match="alreadyName">
    Le nom <b> <xsl:value-of select="."/></b> est d�j� utilis� ...
  </xsl:template>
  <xsl:template match="newLngLat">
    Nouvelle g�olocalisation: <b> <xsl:value-of select="."/></b>
  </xsl:template>
</xsl:stylesheet>