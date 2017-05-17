Ext.define('UES.view.common.Menu', {
			extend : 'Ext.panel.Panel',
			alias : 'widget.menuPanel',
			width : 300,
			height : 300,
			defaults : {
				// applied to each contained panel
				bodyStyle : 'padding:15px'
			},
			layout : {
				// layout-specific configs go here
				type : 'accordion',
				titleCollapse : false,
				hideCollapseTool : true,
				animate : true,
				activeOnTop : true,
				collapseFirst : true
			},
			items : [{
						title : '<b>UES Support Gateway</b>',
						xtype : 'uesSupportGateway'
					}, {
						title : '<b>Risk Engine Config Booking</b>',
						xtype : 'riskEngineConfigBooking'
					}, {
						title : '<b>Report Server Booking</b>',
						xtype : 'rsBookingTree'
					},{
						title : '<b>Inventory</b>',
						xtype : 'inventoryTree'
					}, {
						title : '<b>Contacts & Links</b>',
						xtype : 'cnlTree'
					}, {
						title : '<b>Admin</b>',
						xtype : 'adminTree'
					}]
		});