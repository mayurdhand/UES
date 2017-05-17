Ext.Loader.setConfig({
    enabled: true
});
Ext.Loader.setPath('Ext.ux', './ext-4/examples/ux');
Ext.application({
    requires: ['Ext.container.Viewport',
               'Ext.data.*',
               'Ext.grid.*',,
               'Ext.ux.RowExpander',
               'Ext.selection.CheckboxModel',
               'Ext.util.*',
               'Ext.state.*',
               'Ext.form.*',
               'Ext.ux.CheckColumn',
               'Ext.ux.form.MultiSelect',
               'Ext.window.Window','Ext.form.field.File','Ext.ux.IFrame'],
    name: 'UES',
    appFolder: 'app',
    controllers: ['REBController','InventoryController','CNLController','AdminController','RSBController','UESGatewayController'],
    listener :{ },
    launch: function() {
    	Ext.onReady(function() {
    		setTimeout(function(){
    		Ext.get('loading').remove();
    		Ext.get('loading-mask').fadeOut({remove:true});
    		}, 250);
    		});
        Ext.create('Ext.container.Viewport', {
            layout: {
            	type :'border',
            	overflow:'auto' 
            },
            items: [
				/*{
				    region: 'north',
				    xtype: 'tbtext',
				    text:'<span style="margin: 3px !important;font-size:12\px;">Current UES Support Gateway URL(<a href="http://ais.cib.intranet.db.com:8081/UES">http://ais.cib.intranet.db.com:8081/UES</a>) will be decommissioned on 30th March\'15 as the server on which the application is hosted is going to retire. Please use the new UES Support Gateway URL(<a href="http://risk-it.ues.intranet.db.com:8081/UES">http://risk-it.ues.intranet.db.com:8081/UES</a>) from 30th March\'15.</span>',
				    border: false,
				    cls:'gridbar-error',
				    height : 40
				},*/ {
				    region: 'west',
				    collapsible: true,
				    width: 220,
				    xtype: 'menuPanel'
				},/* {
				    region: 'south',
				    title: 'South Panel',
				    collapsible: true,
				    html: 'Information goes here',
				    split: true,
				    height: 100,
				    minHeight: 100
				}, {
				    region: 'east',
				    title: 'East Panel',
				    collapsible: true,
				    split: true,
				    width: 150
				    
				}*/, {
			        region: 'center',
			        autoScroll : 'true',
			        border: false,
			        xtype: 'tabPanel'
				}
            ]
        });
       
    }
});