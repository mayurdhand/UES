Ext.define('UES.store.APACInventoryResult', {
			extend : 'Ext.data.Store',
			model : 'UES.model.InventoryResult',
			proxy : {
				type : 'ajax',
				url : 'InventoryAction!viewASPACInventory.action',
				reader : {
					type : 'json',
					root : 'inventoryDTOList'
				}
			},
			autoLoad : false
		});