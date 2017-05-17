Ext.define('UES.store.Application', {
			extend : 'Ext.data.Store',
			model : 'UES.model.Region',
			proxy : {
				type : 'ajax',
				url : 'BookConfigAction!loadDBSymphonyEnvCombos.action',
				reader : {
					type : 'json',
					root : 'dbSymphonyAppSet'
				}
			},
			autoLoad : true
		});