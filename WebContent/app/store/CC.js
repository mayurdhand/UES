Ext.define('UES.store.CC', {
			extend : 'Ext.data.Store',
			model : 'UES.model.CC',
			proxy : {
				type : 'ajax',
				url : 'cc/CCAction!showCCConfigs.action',
				reader : {
					type : 'json',
					root : 'ccDTOList'
				}
			},
			autoLoad : true
		});