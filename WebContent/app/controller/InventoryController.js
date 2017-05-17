Ext.define('UES.controller.InventoryController', {
	extend : 'Ext.app.Controller',
	views : ['common.Tabpanel','common.Menu','inventory.LDNInventoryGrid',
	         'inventory.NYInventoryGrid','inventory.APACInventoryGrid',
			 'inventory.InventoryTree'],
	stores : ['InventoryTree', 'LDNInventoryResult','NYInventoryResult',
	          'APACInventoryResult'],
	models : ['InventoryResult'],

	init : function() {
		this.control({
					'menuPanel > inventoryTree' : {
						itemclick : this.inventoryTreeClick
					},
					'button[itemId = exportLDN]' : {
						click : this.exportLDNBookings
					},
					'button[itemId = exportNY]' : {
						click : this.exportNYBookings
					},
					'button[itemId = exportASPAC]' : {
						click : this.exportASPACBookings
					}
				});
	},
	refs : [{
				ref : 'tabPanelRef',
				selector : 'tabpanel[itemId=tabPanel]'
			},{
				ref : 'ldnInventoryGridRef',
				selector : 'ldnInventoryGrid'
			},{
				ref : 'nyInventoryGridRef',
				selector : 'nyInventoryGrid'
			},{
				ref : 'apacInventoryGridRef',
				selector : 'apacInventoryGrid'
			}],

	/***********Inventory************* */
	inventoryTreeClick : function(self, record, item, index, e, eOpts) {
		if (index == 0) {
			this.viewInventory("londonInventory", "London RE Inventory", "viewLondonInventory", "ldnInventoryGrid");
		} else if (index == 1) {
			this.viewInventory("nyInventory", "NY RE Inventory", "viewNYInventory", "nyInventoryGrid");
		} else if (index == 2) {
			this.viewInventory("apacInventory", "APAC RE Inventory", "viewASPACInventory", "apacInventoryGrid");
		}
	},
	viewInventory : function(childName, title, method, gridName) {
		var tab = this.getTabPanelRef();
		var inventory = tab.child('#' + childName);
		if (inventory == null) {
			var newTab = tab.add({
						xtype : 'panel',
						title : title,
						iconCls: 'icon-inventory',
						itemId : childName,
						closable : true,
						autoScroll : true,
						items : [{
									xtype : gridName
								}]
					}).show();
			tab.setActiveTab(newTab);
		} else {
			tab.setActiveTab(inventory);
		}
		var store = null;
		if (gridName == 'nyInventoryGrid') {
			store = this.getNyInventoryGridRef().getStore();
		} else if (gridName == 'ldnInventoryGrid') {
			store = this.getLdnInventoryGridRef().getStore();
		} else if (gridName == 'apacInventoryGrid') {
			store = this.getApacInventoryGridRef().getStore();
		}
		store.load();
	},
	exportLDNBookings: function(btn) {
		try {			
			window.location = "InventoryAction!downloadLondonInventory.action";
		} catch (err) {
			alert(err.message);
		}        
    },
	exportNYBookings: function(btn) {
		try {			
			window.location = "InventoryAction!downloadNYInventory.action";
		} catch (err) {
			alert(err.message);
		}        
    },
	exportASPACBookings: function(btn) {
		try {			
			window.location = "InventoryAction!downloadASPACInventory.action";
		} catch (err) {
			alert(err.message);
		}        
    }
});