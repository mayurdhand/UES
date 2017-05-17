Ext.define('UES.view.rsBooking.RSBookingResultsGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.rsBookingResultsGrid',
	name : 'rsBookingResultsGrid',
	margin : '10 20 20 10',
	width : 'auto',
	height : 380,
	frameHeader: false,
	preventHeader: true,
	columnLines : '1',
	columnWidth : '20',
	store : 'SearchRSBookingResult',
	defaults: {
		   border: 0
		},
	viewConfig : {
		emptyText : '<div class="x-grid-empty">No records found.</div>'
	},
	columns : [ {
		text : "Report Server",
		flex : 1,
		dataIndex : 'uatRS'
	}, {
		text : "Cloned From",
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
	tbar : [{
		tooltip : 'Edit',
		xtype : 'button',
		iconCls : 'icon-edit',
		itemId : 'editRSBooking'
	}, {
		tooltip : 'Export',
		xtype : 'button',
		iconCls : 'icon-excel',
		itemId : 'exportRS'
	}],
	plugins : [ {
		ptype : 'rowexpander',
		rowBodyTpl : [ '<p><b>Testing Summary:</b> {reqSummary}</p><br>' ]
	} ]	

});


