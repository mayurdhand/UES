Ext.define('UES.store.AvailableEnvironments', {
	extend : 'Ext.data.Store',
	model : 'UES.model.AvailableEnvironments',
	reader: {
        type: 'json',
        root: 'freeEnvSet'
    },
	autoLoad : false
});