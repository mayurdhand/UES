Ext.define('UES.store.Environments', {
			extend : 'Ext.data.Store',
			model : 'UES.model.Environments',
			proxy : {
				type : 'ajax',
				url : 'BookConfigAction!loadCombosWithAll.action',
				reader : {
					type : 'json',
					root : 'envSet'
				}
			},
			autoLoad : true
		});