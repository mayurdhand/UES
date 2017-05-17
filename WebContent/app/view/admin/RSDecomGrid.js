Ext.define('UES.view.admin.RSDecomGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.rsDecomGrid',
	name : 'rsDecomGrid',
	collapsible : false,
	preventHeader: true,
	border:false,
	margin : '10 20 20 10',
	width : 'auto',
	height : 600,
	columnLines : true,	
	iconCls : 'icon-grid',
	store : 'RSPendingDecomResult',
	viewConfig : {
		emptyText : '<div class="x-grid-empty">No records found.</div>',
		deferEmptyText: false
	},
	selModel: Ext.create('Ext.selection.CheckboxModel'),
	columns : [{
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
	tbar : [  {
		tooltip : 'Mark as Decommissioned',
		xtype : 'button',
		iconCls : 'icon-reject',
		itemId : 'decommision',
		handler: function(me) {
			var grid = me.up('grid');
			var sm = Ext.isEmpty(grid)? null:grid.getSelectionModel();
			if(Ext.isEmpty(grid.getSelectionModel().getSelection())) {
				Ext.Msg.alert('Message',"Please select a row." );
			} else {
				var toBeDecom='';
				Ext.Object.each(sm.getSelection(), function(key, value, myself) {
					toBeDecom = toBeDecom + value.data.lineNo +",";
				});
				toBeDecom = Ext.util.Format.substr(toBeDecom, 0, (toBeDecom.length)-1);
				Ext.Ajax.request({
		            url: 'admin/BookRSAction!decommission.action',
		            params: {decomIds: toBeDecom},
		            success: function(response, opts){              	
		            	var json = Ext.JSON.decode(response.responseText);
		            	var store = grid.getStore();
		            	Ext.Object.each(sm.getSelection(), function(key, value, myself) {
		            		Ext.Object.each(sm.getSelection(), function(key, value, myself) {
								store.remove(value);
							});
						});
		            	Ext.Msg.alert('Message',json.bookRSDTO.message);
		            }			
		        });	
			}
		}
	} ]
});
