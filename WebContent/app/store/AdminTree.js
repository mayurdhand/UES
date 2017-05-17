Ext.define('UES.store.AdminTree', {
	extend : 'Ext.data.TreeStore',
	root : {
		expanded : true,
		children : [ {
			text : "Risk Engine Requests",
			iconCls : 'icon-admin',
			leaf : true
		}, {
			text : "Report Server Requests",
			iconCls : 'icon-admin',
			leaf : true
		}, {
			text : "Report Server Decommission",
			iconCls : 'icon-admin',
			leaf : true
		} , {
			text : "Services & Data",
			iconCls : 'icon-settings',
			leaf : true
		} , {
			text : "RE Config Cache",
			iconCls : 'icon-settings',
			leaf : true
		} ]
	}

});