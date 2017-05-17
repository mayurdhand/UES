Ext.define('UES.store.RSPendingDecomResult', {
	extend : 'Ext.data.Store',
	model : 'UES.model.RSPendingDecomResult',
	proxy : {
		type : 'ajax',
		url : 'admin/BookRSAction!showRSPendingDecom.action',
		reader : {
			type : 'json',
			root : 'bookRSDTOList'
		}
	},
	autoLoad : false
	});