Ext.define('UES.view.admin.RSBookingRequestsGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.rsBookingRequestsGrid',
	name : 'rsBookingRequestsGrid',
	collapsible : false,
	preventHeader: true,
	border:false,
	margin : '10 20 20 10',
	width : 'auto',
	height : 600,
	columnLines : true,	
	iconCls : 'icon-grid',
	store : 'RSBookingRequests',
	viewConfig : {
		emptyText : '<div class="x-grid-empty">No records found.</div>',
		deferEmptyText: false
	},
	columns : [{
		text : "Clone From",
		flex : 1,
		dataIndex : 'prodRS'
	}, {
		text : "Region",
		flex : 1,
		itemId:'region',
		dataIndex : 'region',
		renderer:function(value,metaData,view) {
			var store = Ext.getStore('Region'),
				recordId = -1;
			if(!Ext.isEmpty(store)) {
				recordId = store.findExact('code', value);
				if(recordId >=0) {
					return store.getAt(recordId).get('name');
				} 
			}
			return value;
		}
	}, {
		text : "Business",
		flex : 1,
		itemId:'business',
		dataIndex : 'business',
		renderer:function(value,metaData,view) {
			var store = Ext.getStore('Business'),
				recordId = -1;
			if(!Ext.isEmpty(store)) {
				recordId = store.findExact('code', value);
				if(recordId >=0) {
					return store.getAt(recordId).get('name');
				} 
			}
			return value;
		}
	}, {
		text : "Project",
		flex : 1,
		dataIndex : 'projectName'
	}, {
		text : "PM Contact",
		flex : 1,
		dataIndex : 'manager'
	}, {
		text : "Booked By",
		flex : 1,
		dataIndex : 'bookedBy'
	}, {
		text : "Go Live Date",
		flex : 1,
		dataIndex : 'goLiveDate',
		align : 'center'
	}, {
		text : "Decommissioning Date",
		flex : 1,
		dataIndex : 'decomDate',
		align : 'center'
	} ],
	tbar : [ {
		tooltip : 'Approve',
		xtype : 'button',
		iconCls : 'icon-approve',
		itemId : 'approveRSRequest'
	}, {
		tooltip : 'Reject',
		xtype : 'button',
		iconCls : 'icon-reject',
		itemId : 'rejectRSRequest'		
	}, {
		tooltip : 'Edit',
		xtype : 'button',
		iconCls : 'icon-edit',
		itemId : 'editRSRequest'
	}, {
		tooltip : 'Refresh',
		xtype : 'button',
		iconCls : 'icon-refresh',
		itemId : 'refreshRSRequest'
	} ],
	plugins : [ {
		ptype : 'rowexpander',
		rowBodyTpl : [ '<p><b>Testing Summary:</b> {reqSummary}</p><br>' ]
	} ]

});
