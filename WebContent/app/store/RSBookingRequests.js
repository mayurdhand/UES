Ext.define('UES.store.RSBookingRequests', {
	extend : 'Ext.data.Store',
	model : 'UES.model.SearchRSBookingResult',
	proxy : {
		type : 'ajax',
		url : 'admin/BookRSAction!showPendingRequests.action',
		reader : {
			type : 'json',
			root : 'bookRSDTOList'
		}
	},
	autoLoad : false
	});