Ext.define('UES.store.LDNInventoryResult', {
			extend : 'Ext.data.Store',
			model : 'UES.model.InventoryResult',
			proxy : {
				type : 'ajax',
				url : 'InventoryAction!viewLondonInventory.action',
				reader : {
					type : 'json',
					root : 'inventoryDTOList'
				}
			},
			autoLoad : false
		});