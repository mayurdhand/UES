Ext.define('UES.store.RegionWithALL', {
			extend : 'Ext.data.Store',
			model : 'UES.model.Region',

			data : [{
						code : 'ALL',
						name : 'ALL'
					}, {
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