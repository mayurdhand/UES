Ext.define('UES.store.InventoryTree', {
			extend : 'Ext.data.TreeStore',
			root : {
				expanded : true,
				children : [{
							text : "London RE Inventory",
							iconCls : 'icon-inventory',
							leaf : true
						}, {
							text : "NY RE Inventory",
							iconCls : 'icon-inventory',
							leaf : true
						}, {
							text : "APAC RE Inventory",
							iconCls : 'icon-inventory',
							leaf : true
						}]
			}

		});