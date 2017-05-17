Ext.define('UES.store.NYInventoryResult', {
			extend : 'Ext.data.Store',
			model : 'UES.model.InventoryResult',
			proxy : {
				type : 'ajax',
				url : 'InventoryAction!viewNYInventory.action',
				reader : {
					type : 'json',
					root : 'inventoryDTOList'
				}
			},
			autoLoad : false
		});