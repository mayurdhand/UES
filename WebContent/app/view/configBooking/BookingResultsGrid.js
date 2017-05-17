Ext.define('UES.view.configBooking.BookingResultsGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.bookingResultsGrid',
	name : 'bookingResultsGrid',
	margin : '10 20 20 10',
	width : 'auto',
	height : 380,
	frameHeader: false,
	preventHeader: true,
	columnLines : '1',
	columnWidth : '20',
	store : 'SearchBookingResult',
	defaults: {
		   border: 0
		},
	viewConfig : {
		emptyText : '<div class="x-grid-empty">No records found.</div>'
	},
	columns : [ {
		text : "Env Name",
		width : 240,
		dataIndex : 'envName'
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
	}, {
		text : "Region",
		flex : 1,
		dataIndex : 'region',
		hidden: true,
		align : 'center'
	} ],
	tbar : [{
		tooltip : 'Edit',
		xtype : 'button',
		iconCls : 'icon-edit',
		itemId : 'editBooking'
	}, {
		tooltip : 'Export',
		xtype : 'button',
		iconCls : 'icon-excel',
		itemId : 'export'
	}],
	plugins : [ {
		ptype : 'rowexpander',
		rowBodyTpl : [ '<p><b>Requirement Summary:</b> {reqSummary}</p><br>' ]
	} ]	

});
