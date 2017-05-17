Ext.define('UES.view.admin.BookingRequestsGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.bookingRequestsGrid',
	name : 'bookingRequestsGrid',
	collapsible : false,
	preventHeader: true,
	border:false,
	margin : '10 20 20 10',
	width : 'auto',
	height : 600,
	columnLines : true,	
	iconCls : 'icon-grid',
	store : 'REBookingRequests',
	viewConfig : {
		emptyText : '<div class="x-grid-empty">No records found.</div>',
		deferEmptyText: false
	},
	columns : [{
		text : "Project",
		flex : 1,
		dataIndex : 'projectName'
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
		text : "PM Contact",
		flex : 1,
		dataIndex : 'manager'
	}, {
		text : "Booked By",
		flex : 1,
		dataIndex : 'bookedBy'
	}, {
		text : "BA Contact",
		flex : 1,
		dataIndex : 'baContact'
	}, {
		text : "QA Contact",
		flex : 1,
		dataIndex : 'qaContact'
	}, {
		text : "DEV Contact",
		flex : 1,
		dataIndex : 'devContact'
	}, {
		text : "Senior Stakeholder",
		flex : 1,
		dataIndex : 'stakeholder'
	}, {
		text : "From Date",
		flex : 1,
		dataIndex : 'fromDate',
		align : 'center'
	}, {
		text : "To Date",
		flex : 1,
		dataIndex : 'toDate',
		align : 'center'
	} ],
	tbar : [ {
		tooltip : 'Approve',
		xtype : 'button',
		iconCls : 'icon-approve',
		itemId : 'approveRequest'
	}, {
		tooltip : 'Reject',
		xtype : 'button',
		iconCls : 'icon-reject',
		itemId : 'rejectRequest'		
	}, {
		tooltip : 'Edit',
		xtype : 'button',
		iconCls : 'icon-edit',
		itemId : 'editRequest'
	}, {
		tooltip : 'Refresh',
		xtype : 'button',
		iconCls : 'icon-refresh',
		itemId : 'refreshRequest'
	} ],
	plugins : [ {
		ptype : 'rowexpander',
		rowBodyTpl : [ '<p><b>Requirement Summary:</b> {reqSummary}</p><br>' ]
	} ]

});
