var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
    clicksToMoveEditor: 1,
    autoCancel: true    
});

Ext.define('UES.view.admin.CCGrid', {
			extend : 'Ext.grid.Panel',
			alias : 'widget.ccGrid',
			margin : '10 20 20 10',
			width : 'auto',
			preventHeader: true,
			border:false,
			height : 600,
			loadMask : true,
			columnLines : true,
			iconCls : 'icon-grid',
			store : 'CC',
			viewConfig : {
				emptyText : '<div class="x-grid-empty">No records found.</div>'
			},
			columns : [{
						text : "Config",
						flex : 1,
						dataIndex : 'config'						
					}, {
						text : "Database",
						flex : 1,
						dataIndex : 'database',
						editor: {
							xtype: 'textfield',
			                allowBlank: false,
							emptyText: 'Database is required',
							blankText: 'Database is required'
			            }
					}, {
						text : "CC Host",
						flex : 1,
						dataIndex : 'ccHost',
						editor: {
							xtype: 'textfield',
			                allowBlank: false,
							emptyText: 'CC Host is required',
							blankText: 'CC Host is required'
			            }
					}, {
						text : "CC Port",
						flex : 1,
						dataIndex : 'ccPort',
						editor: {
			                xtype: 'textfield',
			                allowBlank: false,
			                emptyText: 'CC Port is required',
							blankText: 'CC Port is required'
			            }
					}],
			        tbar: [{
			        	tooltip: 'Add',
			            xtype: 'button',
			            iconCls: 'icon-add',
			            itemId: 'addCCConfig',
			            handler : function() {
			            	var loadmask = new Ext.LoadMask(this.up('grid'), {
			        			msg : "Please wait..."
			        		});
			        		loadmask.show();
			            	var view = Ext.widget('ccForm');
			         		view.show();
			        		loadmask.hide();
			            }
			            	
	            },{
            	tooltip: 'Delete',
	            xtype: 'button',
	            iconCls: 'icon-delete',
	            itemId: 'deleteCCConfig'
	            /*handler : function(grid, rowIndex, colIndex) {
					//Ext.data.StoreManager.lookup('Contacts').removeAt(rowIndex);
				}*/
	        },{
            	tooltip: 'ReloadCache',
	            xtype: 'button',
	            iconCls: 'icon-refresh',
	            itemId: 'reloadCache'
	            /*handler : function(grid, rowIndex, colIndex) {
					//Ext.data.StoreManager.lookup('Contacts').removeAt(rowIndex);
				}*/
	        }],
			 plugins : [ rowEditing ]
		});


