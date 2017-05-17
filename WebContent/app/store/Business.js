Ext.define('UES.store.Business', {
			extend : 'Ext.data.Store',
			model : 'UES.model.Business',

			data : [{
						code : 'RATES',
						name : 'RATES'
					}, {
						code : 'EM',
						name : 'EMERGING MARKETS'
					}, {
						code : 'CREDIT',
						name : 'CREDIT'
					}, {
						code : 'CPSG',
						name : 'CPSG'
					}, {
						code : 'GLM',
						name : 'GLM'
					}, {
						code : 'GED',
						name : 'GED'
					}, {
						code : 'OTHERS',
						name : 'OTHERS'
					}]
		});