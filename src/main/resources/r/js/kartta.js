var kartta = function(renderer, opacity) {
    var consts = constants();
    var utils = util();
    var olstuffs = olstuff(consts, utils);
    
    var root = 'latest/';
    
    var layers = [
        olstuffs.newWMTSGroup('WMTS PNG', root + 'wmts.xml', 'ETRS-TM35FIN'),
        new ol.layer.Group({
            title: '<span class="fi">Vektorina</span><span class="en">Vectors</span>',
            layers: [].concat(
                olstuffs.newVectorLayer(root + 'examples'  , 'Examples'  , 'Examples', opacity)
            )})];

    var overlay = olstuffs.overlay(document.getElementById('hovertitle'));
    
    var taustaGroup = olstuffs.taustaGroup(opacity);
    
    window.map = olstuffs.map([overlay], layers, taustaGroup, renderer)
    
    var $list = $('#list');
    var $popup = olstuffs.createPopup($('#popup'));
    var $hovertitlecontent = $('#hovertitle-content');
    
    var select = function(feature, coordinate) {
        $hovertitlecontent.html(utils.prettyPrint(utils.withoutProp(feature.getProperties(), 'geometry')));
        $popup.html('<iframe src="' + root + feature.getProperties().tunniste + '.html' + '"></iframe>');
        $popup.stop().show().css({opacity:'100'});
        if (coordinate) {
            overlay.setPosition(coordinate);
        }
        $('ul ul .value:contains(' + feature.getProperties().tunniste + ')', $list).closest('ul').addClass('selected');
    }
    var unselect = function() {
        overlay.setPosition(undefined);
        $hovertitlecontent.blur();
        $popup.fadeOut(2000);
        $('ul ul.selected', $list).removeClass('selected');
        return false;
    };
    
    var interaction = hover(map, layers, select, unselect);
    
    var poly = olstuffs.createPolygonInteraction(map, function(e, coordinate) {
        $hovertitlecontent.html(olstuffs.toWKT(e.feature));
        overlay.setPosition(coordinate);
        map.removeInteraction(poly);
    });
    $('#drawPolygon').click(function() { map.addInteraction(poly); });

    tilet(map, olstuffs.projection, $('#nayta-tilet'), olstuffs);
    
    olstuffs.registerListView(map, $list, interaction, select, unselect);
};
