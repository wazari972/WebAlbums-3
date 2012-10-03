/* Might be overriden in Display.xsl */
var mapCenter = {
    lon: 5.7779693603516,
    lat: 45.212268217196,
    zoom: 10
}

function add_osm_layers(map) {
    map.addLayer(new OpenLayers.Layer.OSM.Mapnik("OpenStreetMap"));
    
    var openCycle = new OpenLayers.Layer.OSM( "OSM Cycle Map", "http://tile.opencyclemap.org/cycle/${z}/${x}/${y}.png",
        {displayOutsideMaxExtent: true, isBaseLayer: true, visibility: false, numZoomLevels:17, permalink: "cycle" })
        
    map.addLayer(openCycle);
}

function add_google_layers(map) {
    /**/
    if (google.maps.MapTypeId == undefined) {
        /* HUGLY hack, but Google Map script tries to 
         * document.write to load its inner script, which
         * is not allowed in XTHML/XSL. We just do it manually
         * here :) 
         * */
        
        var reload_google_layers = function() {
            if (google.maps.MapTypeId == undefined) {
                alert("Couldn't load Google maps ...")
            } else {
                add_google_layers(map)
            }
        }

        $.getScript("http://maps.gstatic.com/intl/en_us/mapfiles/api-3/9/14/main.js", reload_google_layers)
        
        return
    }
    
    var gphy = new OpenLayers.Layer.Google(
        "Google Physical",
        {type: google.maps.MapTypeId.TERRAIN}
    );
    map.addLayer(gphy)
    
    var gmap = new OpenLayers.Layer.Google("Google Streets", {visibility: false});
    map.addLayer(gmap)
}

function add_geoportail_layers(map) {
    var apiKEYs = {
        //'localhost:8080': 'r6muu3lqjvkg22q8dw893k7z'
        "localhost:8080":"f4ezpirs8jd63cwbhkstbkqd"
    };
    var apiKEY = apiKEYs[window.location.host];
    
    var options = {
        name: "Cartes IGN",
        url: "http://gpp3-wxs.ign.fr/" + apiKEY + "/wmts",
        layer: "GEOGRAPHICALGRIDSYSTEMS.MAPS",
        matrixSet: "PM",
        style: "normal",
        attribution: '&copy;IGN <a href="http://www.geoportail.fr/" target="_blank"><img src="http://api.ign.fr/geoportail/api/js/latest/theme/geoportal/img/logo_gp.gif"></a> <a href="http://www.geoportail.gouv.fr/depot/api/cgu/licAPI_CGUF.pdf" alt="TOS" title="TOS" target="_blank">Terms of Service</a>'
    };
    var ign = new OpenLayers.Layer.WMTS(options)
    
    map.addLayer(ign)
}

function init_osm_box(divName) {
    var map = new OpenLayers.Map (divName, {
        controls:[
            new OpenLayers.Control.Navigation(),
            new OpenLayers.Control.LayerSwitcher()
            ],
        maxExtent: new OpenLayers.Bounds(-20037508.34,-20037508.34,20037508.34,20037508.34),
        maxResolution: 156543.0399,
        numZoomLevels: 19,
        units: 'm',
        projection: new OpenLayers.Projection("EPSG:900913"),
        displayProjection: new OpenLayers.Projection("EPSG:4326")
    } );
    
    add_osm_layers(map)
    add_geoportail_layers(map)
    add_google_layers(map)
    
    map.addControl(new OpenLayers.Control.LayerSwitcher());

    map.setCenter(transformLonLat(new OpenLayers.LonLat(mapCenter.lon, mapCenter.lat)), mapCenter.zoom);

    map.div.style[OpenLayers.String.camelize('background-image')]= 'none';
   
    return map
}

var gpx_layers = [];
function init_gpx_layer(map, name, file_id, ready_callback) {
    file = "GPX__"+file_id+".gpx"
    
    // Add the Layer with the GPX Track
    var lgpx = new OpenLayers.Layer.Vector(name+" "+file_id, {
        protocol: new OpenLayers.Protocol.HTTP({
            url: file,
            format: new OpenLayers.Format.GPX
        }),
        projection: new OpenLayers.Projection("EPSG:4326"),
        style: {strokeColor: "red", strokeWidth: 5, strokeOpacity: 1},
        strategies: [new OpenLayers.Strategy.Fixed()]
    })

    map.addLayer(lgpx);
    
    lgpx.events.register("loadend", lgpx , function (e) {
        if (ready_callback == undefined)
            zoomTo(map, lgpx, false)
        else
            ready_callback(map, lgpx)
    });
    
    return lgpx
}

function zoomTo(map, layer, closest) {
    map.zoomToExtent(layer.getDataExtent(), closest);
}

function get_mm_bikeTracks(bounds) {

    llbounds = new OpenLayers.Bounds();
    llbounds.extend(OpenLayers.Layer.SphericalMercator.inverseMercator(bounds.left,bounds.bottom));
    llbounds.extend(OpenLayers.Layer.SphericalMercator.inverseMercator(bounds.right,bounds.top));
    url = "http://mm-lbserver.dnsalias.com/mm-mapserver_v2/wms/wms.php?REQUEST=GetMap&SERVICE=WMS&VERSION=1.1.1&LAYERS=MM_BIKETRACKS&STYLES=&FORMAT=image/png&BGCOLOR=0xFFFFFF&TRANSPARENT=TRUE&SRS=EPSG:4326&BBOX="
    url = url + llbounds.toBBOX() + "&WIDTH=256&HEIGHT=256"
    return url
}

function transformLonLat(lonlat) {
    if (!lonlat || !lonlat.transform)
        return lonlat
    else {
        return lonlat.transform(
        new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
        new OpenLayers.Projection("EPSG:900913") // to Spherical Mercator Projection
    );
    }
}

function point_to_lonlat(point) {
    return transformLonLat(new OpenLayers.LonLat(point.lng, point.lat))
}

function markerClick (evt) {
    if (this.popup == null) {
        alert("oups")
        return;
    }
    
    if (currentPopup != null)
        currentPopup.hide()
    
    currentPopup = this.popup;
    currentPopup.show()
    OpenLayers.Event.stop(evt);
}

var currentPopup = null;
function addMarker2(map, markers, point, pointToContent_p, lnglat) {
    var feature = new OpenLayers.Feature(markers, lnglat);      
    var marker = feature.createMarker();
    feature.closeBox = true;
    feature.popupClass =  OpenLayers.Class(OpenLayers.Popup.FramedCloud, {
        'autoSize': true
    });
    feature.data.popupContentHTML = pointToContent_p(point);
    feature.data.overflow = "hidden";
   
    marker = feature.createMarker();
    marker.events.register("mousedown", feature, markerClick);
    markers.addMarker(marker);

    feature.popup = feature.createPopup(feature.closeBox);
    map.addPopup(feature.popup);
    feature.popup.hide();
    
    return marker;
}

function addMarker(map, markers, point, pointToContent_p, lnglat) {
    info = new OpenLayers.Control.WMSGetFeatureInfo({
            url: 'http://demo.opengeo.org/geoserver/wms', 
            title: 'Identify features by clicking',
            queryVisible: true,
            eventListeners: {
                getfeatureinfo: function(event) {
                    map.addPopup(new OpenLayers.Popup.FramedCloud(
                        "chicken", 
                        map.getLonLatFromPixel(event.xy),
                        null,
                        event.text,
                        null,
                        true
                    ));
                }
            }
        });
        map.addControl(info);
        info.activate();
}