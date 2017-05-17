Ext.define('UES.store.BookingStatus', {
			extend : 'Ext.data.Store',
			model : 'UES.model.BookingStatus',

			data : [{
						code : 'ALL',
						name : 'ALL'
					}, {
						code : 'ACTIVE',
						name : 'ACTIVE'
					}, {
						code : 'PENDING DECOMMISSIONING',
						name : 'PENDING DECOMMISSIONING'
					}, {
						code : 'DECOMMISSIONED',
						name : 'DECOMMISSIONED'
					}]
		});