Ext.define('UES.view.inventory.LDNInventoryGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.ldnInventoryGrid',
	margin : '10 20 20 10',
	enableLocking : true,
	preventHeader: true,
	width : 'auto',
	height : 580,
	loadMask : true,
	columnLines : '1',
	iconCls : 'icon-grid',
	store : 'LDNInventoryResult',
	viewConfig : {
		emptyText : '<div class="x-grid-empty">No records found.</div>'
	},
	columns : [ {
		text : "Config",
		width:200,
		locked:true,
		dataIndex : 'configName'
	}, {
		text : "Region",
		width:50,
		dataIndex : 'region'
	}, {
		text : "Env",
		width:70,
		dataIndex : 'env'
	}, {
		text : "Re Version",
		dataIndex : 'reVersion'
	}, {
		text : "JSM",
		width:50,
		dataIndex : 'jsm'
	}, {
		text : "TDC",
		width:50,
		dataIndex : 'cacheTrades'
	},/* {
		text : "Physical Host",
		dataIndex : 'physicalHost'
	}, {
		text : "SS Host",
		dataIndex : 'statusServerHost'
	},*/ {
		text : "DB Name",
		dataIndex : 'dbName'
	},/* {
		text : "DB Host",
		dataIndex : 'dbHost'
	}, {
		text : "DB Port",
		dataIndex : 'dbPort'
	}, {
		text : "RE Hard Bounce Time",
		dataIndex : 'reHardBounceTime'
	}, {
		text : "RE Hard Bounce Days",
		dataIndex : 'reHardBounceDays'
	}, {
		text : "RE Soft Bounce Time",
		dataIndex : 'reSoftBounceTime'
	}, {
		text : "RE Soft Bounce Days",
		dataIndex : 'reSoftBounceDays'
	}, {
		text : "SS Bounce Time",
		dataIndex : 'ssBounceTime'
	}, {
		text : "SS Bounce Days",
		dataIndex : 'ssBounceDays'
	},*/ {
		text : "Default DBAX",
		dataIndex : 'defaultDBAX'
	}, {
		text : "DBAX Parser Version",
		dataIndex : 'dbaxParserVersion'
	},/* {
		text : "Dweb Node",
		dataIndex : 'dwebNode'
	},*/ {
		text : "CC Instance",
		dataIndex : 'ccInstance'
	}, {
		text : "CC Version",
		dataIndex : 'ccVersion'
	}, {
		text : "JSM Instance",
		dataIndex : 'jsmInstance'
	}, {
		text : "JSM Version",
		dataIndex : 'jsmVersion'
	}, {
		text : "JSM Schema",
		dataIndex : 'jsmSchema'
	}, {
		text : "JSM Schema Version",
		dataIndex : 'jsmSchemaVersion'
	}, {
		text : "TDC Instance",
		dataIndex : 'tdcInstance'
	}, {
		text : "TDC Version",
		dataIndex : 'tdcVersion'
	}  ],
	tbar : [{
		tooltip : 'Export',
		xtype : 'button',
		iconCls : 'icon-excel',
		itemId : 'exportLDN'
	}]
});
