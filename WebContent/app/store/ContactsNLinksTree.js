Ext.define('UES.store.ContactsNLinksTree', {
	extend : 'Ext.data.TreeStore',
	root : {
		expanded : true,
		children : [ {
			text : "Contacts",
			iconCls : 'icon-contacts',
			leaf : true
		}, {
			text : "Links",
			iconCls : 'icon-links',
			leaf : true
		} ]
	}

});