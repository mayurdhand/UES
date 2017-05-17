var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
    clicksToMoveEditor: 1,
    autoCancel: true    
});

Ext.define('UES.view.contact_links.ContactsGrid', {
			extend : 'Ext.grid.Panel',
			alias : 'widget.contactsGrid',
			margin : '10 20 20 10',
			width : 'auto',
			preventHeader: true,
			border:false,
			height : 600,
			loadMask : true,
			columnLines : true,
			iconCls : 'icon-grid',
			store : 'Contacts',
			viewConfig : {
				emptyText : '<div class="x-grid-empty">No records found.</div>'
			},
			columns : [{
			            xtype: 'rownumberer'
			        },{
						text : "Team",
						flex : 1,
						dataIndex : 'team'						
					}, {
						text : "Email Id",
						flex : 1,
						dataIndex : 'emailId',
						editor: {
							xtype: 'textfield',
			                allowBlank: false,
							emptyText: 'Email Id is required',
							blankText: 'Email Id is required'
			            }
					}, {
						text : "Contact No",
						flex : 1,
						dataIndex : 'contactNumber',
						editor: {
			                xtype: 'textfield',
			                allowBlank: false,
			                emptyText: 'Contact number is required',
							blankText: 'Contact number is required'
			            }
					}, {
						text : "Primary Escalation",
						flex : 1,
						dataIndex : 'primaryEscalation',
						editor: {
							xtype: 'textfield',
			                allowBlank: false,
							emptyText: 'Primary Escalation is required',
							blankText: 'Primary Escalation is required'
			            }
					}, {
						text : "Secondary Escalation",
						flex : 1,
						dataIndex : 'secondryEscalation',
						editor: {
							xtype: 'textfield',
			                allowBlank: false,
							emptyText: 'Secondary Escalation is required',
							blankText: 'Secondary Escalation is required'
			            }
					}],
			        tbar: [{
			        	tooltip: 'Add',
			            xtype: 'button',
			            iconCls: 'icon-add',
			            itemId: 'addContact',
			            handler : function() {
			            	var loadmask = new Ext.LoadMask(this.up('grid'), {
			        			msg : "Please wait..."
			        		});
			        		loadmask.show();
			            	var view = Ext.widget('contactsForm');
			         		view.show();
			        		loadmask.hide();
			            }
			            	
	            },{
            	tooltip: 'Delete',
	            xtype: 'button',
	            iconCls: 'icon-delete',
	            itemId: 'deleteContact'
	            /*handler : function(grid, rowIndex, colIndex) {
					//Ext.data.StoreManager.lookup('Contacts').removeAt(rowIndex);
				}*/
	        }],
			 plugins : [ rowEditing ]
		});


