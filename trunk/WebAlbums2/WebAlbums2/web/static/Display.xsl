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
  <xsl:output method="html"/>
  <xsl:template match="/">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
	<xsl:apply-templates select="/root/userLogin/valid"/>
	
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>WebAlbums 2 : <xsl:value-of select="/root/login/theme" />  (<xsl:value-of select="/root/login/user" />)</title>

	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<link href="static/styles.css" rel="stylesheet" type="text/css" media="screen" />
      </head>
      <body>
	<script src="static/scripts/tools.js" type="text/javascript" />
	<xsl:apply-templates select="/root/choix/map"/>
	<xsl:apply-templates select="/root/config/map"/>

	<div id="header"> 
	  <div id="logo">
	    <h1>WebAlbums 2</h1>
	    <h2>by Kevin POUGET</h2>
	  </div>
	  
	  <div id="menu">
	    <ul>
	      <li><a href="Index" title="Retour aux th�mes">Th�me</a></li>
	      <li><a href="Users" title="Retour aux utilisateurs">Users</a></li>
	      <li><a href="Choix" title="Choix">Choix</a></li>
	      <xsl:if test="count(/root/login/admin)!=0">
		<li><a href="Config" title="Configuration">Config</a></li>
	      </xsl:if>
	    </ul>	
	  </div>
	</div>
	<div id="main">
	  <div id="top">
	    <div id="bottom">
	      <div id="right">
		
		<h3>Affichage</h3>
		<ul>
		  <li><a href="javascript:updateAffichage('maps')"  title=""><xsl:value-of select="/root/affichage/maps" /></a></li>
		  <li><a href="javascript:updateAffichage('details');" title=""><xsl:choose>
			<xsl:when test="/root/affichage/details = 'false'">Sans D�tails</xsl:when>
			<xsl:when test="not(/root/affichage/details = 'false')">Avec D�tails</xsl:when>
		      </xsl:choose> </a></li>
		  <xsl:if test="count(/root/login/admin)!=0">
		    <li><a href="javascript:updateAffichage('edition');" title=""><xsl:value-of select="/root/affichage/edition" /></a></li>
		  </xsl:if>
		  </ul>

		<h3>Connexion</h3>
		<ul>
		  <li><xsl:value-of select="/root/login/theme" /></li>
		  <li><xsl:value-of select="/root/login/user" /></li>
		</ul>
		<h3>Nuage de tags <input id="cloudLoader" type="button" value="load cloud" onclick="loadCloud();"/></h3>
		<div id="cloud">
		  <img src="static/images/loading.gif"/>
		</div>
	      </div>
	      <div id="left">
		<xsl:apply-templates select="/root/Exception"/>
		<xsl:apply-templates select="/root/message"/>

		<xsl:apply-templates select="/root/index"/>
		<xsl:apply-templates select="/root/userLogin"/>
		<xsl:apply-templates select="/root/choix"/>
		<xsl:apply-templates select="/root/albums"/>
		<xsl:apply-templates select="/root/photos"/>
		<xsl:apply-templates select="/root/tags"/>

		<xsl:apply-templates select="/root/albm_edit"/>
		<xsl:apply-templates select="/root/photo_edit"/>
		<xsl:apply-templates select="/root/config"/>

		<xsl:apply-templates select="/root/*/page"/>
		<xsl:call-template name="print_return_link" />
	      </div>
	      <xsl:apply-templates select="/root/stats"/>
	    </div>
	  </div>
	</div>
      </body>
    </html>
  </xsl:template>

  <xsl:include href="Index.xsl" />
  <xsl:include href="UserLogin.xsl" />
  <xsl:include href="Choix.xsl" />
  <xsl:include href="Albums.xsl" />
  <xsl:include href="Photos.xsl" /> 
  <xsl:include href="PhotosAlbums.xsl" />
  <xsl:include href="Tags.xsl" />
  <xsl:include href="Common.xsl" />
 
  <xsl:include href="ModifAlbum.xsl" /> 
  <xsl:include href="ModifPhoto.xsl" /> 
  <xsl:include href="Config.xsl" /> 
 </xsl:stylesheet>
