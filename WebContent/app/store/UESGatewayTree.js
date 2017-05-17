Ext.define('UES.store.UESGatewayTree', {
			extend : 'Ext.data.TreeStore',
			root : {
				expanded : true,
				children : [{
							text : "New Ticket",
							iconCls : 'icon-admin',
							leaf : true
						}, {
							text : "Search Ticket",
							iconCls : 'icon-search',
							leaf : true
						}, {
							text : "UES Incidents",
							iconCls : 'icon-search',
							leaf : true
						}, {
							text : "UES Service Requests",
							iconCls : 'icon-search',
							leaf : true
						}, {
							text : "Your Open Incidents",
							iconCls : 'icon-search',
							leaf : true
						}, {
							text : "Your Service Requests",
							iconCls : 'icon-search',
							leaf : true
						}/*, {
							text : "UES Daily Report",
							iconCls : 'icon-report',
							leaf : true
						}*/]
			}

		});