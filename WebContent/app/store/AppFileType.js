Ext.define('UES.store.AppFileType', {
			extend : 'Ext.data.Store',
			model : 'UES.model.AppFileType',
			data : [
			         {fileType: 'RE Config Booking'},
			         {fileType: 'RE Config Booking Requests'},
			         {fileType: 'Inventory'},
			         {fileType: 'Contacts And Links'},
			         {fileType: 'RS Booking Requests'},
			         {fileType: 'RS Booking'}
		     ]
		});