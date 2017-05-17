Ext.define('UES.store.REBookingRequests', {
	extend : 'Ext.data.Store',
	model : 'UES.model.SearchBookingResult',
	proxy : {
		type : 'ajax',
		url : 'admin/BookConfigAction!showPendingRequests.action',
		reader : {
			type : 'json',
			root : 'bookConfigDTOList'
		}
	},
	autoLoad : false
	});