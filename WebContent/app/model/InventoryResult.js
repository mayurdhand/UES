Ext.define('UES.model.InventoryResult', {
	extend : 'Ext.data.Model',
	fields : [ 'sno', 'configName', 'region', 'env', 'reVersion', 'jsm',
			'cacheTrades', 'dbName','defaultDBAX', 'dbaxParserVersion',
			'jsmInstance', 'jsmVersion', 'ccInstance', 'ccVersion','tdcInstance', 
			'tdcVersion', 'jsmSchema', 'jsmSchemaVersion']
});
