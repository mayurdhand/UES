Ext.define('UES.store.Links', {
			extend : 'Ext.data.Store',
			model : 'UES.model.Links',
			proxy : {
				type : 'ajax',
				url : 'links/LinksAction!showLinks.action',
				reader : {
					type : 'json',
					root : 'linksDTOList'
				}
			},
			autoLoad : true
		});