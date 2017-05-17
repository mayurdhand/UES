Ext.define('UES.store.Region', {
			extend : 'Ext.data.Store',
			model : 'UES.model.Region',

			data : [{
						code : 'LDN',
						name : 'LONDON'
					}, {
						code : 'NY',
						name : 'NEW YORK'
					}, {
						code : 'SYD',
						name : 'SYDNEY'
					}, {
						code : 'SIN',
						name : 'SINGAPORE'
					}, {
						code : 'TOK',
						name : 'TOKYO'
					}]
		});