Ext.define('UES.store.Ticket_Environments', {
	extend : 'Ext.data.Store',
	fields : ['newCode','code', 'name','parent'],
	proxy : {
		type : 'ajax',
		url : 'BookConfigAction!loadDBSymphonyEnvCombos.action',
		reader : {
			type : 'json',
			root : 'dbSymphonyEnvSet'
		}
	},
	autoLoad : true
});