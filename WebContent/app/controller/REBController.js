Ext.define('UES.controller.REBController', {
	extend : 'Ext.app.Controller',
	views : ['common.Tabpanel', 'common.Menu',
			'configBooking.ReserveEnvironment','configBooking.ConfigBookingTree',
			'configBooking.SearchBookingsForm','configBooking.SearchBookingPanel',
			'configBooking.BookingResultsGrid','configBooking.EditBookingsForm'],
	stores : ['Region', 'ConfigBookingTree', 'Environments','SearchBookingResult','Business'],
	models : ['Region', 'Environments','Business'],

	init : function() {
		this.control({
					'button[itemId = save]' : {
						click : this.saveForm
					},
					'button[itemId = search]' : {
						click : this.searchAllBookings
					},
					'menuPanel > riskEngineConfigBooking' : {
						itemclick : this.riskEngineConfigBookingClick
					},
					'button[itemId = editBooking]' : {
						click : this.editBooking
					},
					'button[itemId = updateBooking]' : {
						click : this.updateBooking
					},
					'button[itemId = export]' : {
						click : this.exportBookings
					}
				});
	},
	refs : [{ 	ref : 'reserveEnvFormRef',
				selector : 'reserveEnvForm'
			}, {
				ref : 'tabPanelRef',
				selector : 'tabpanel[itemId=tabPanel]'
			}, {
				ref : 'searchBookingsRef',
				selector : 'searchBookingsForm'
			}, {
				ref : 'bookingResultsGridRef',
				selector : 'bookingResultsGrid'
			},{
				ref : 'editBookingsFormRef',
				selector : 'editBookingsForm > form'
			}],

	riskEngineConfigBookingClick : function(self, record, item, index, e, eOpts) {
		if (index == 0) {
			this.addReserveEnvTab();
		} else if (index == 1) {
			this.addSearchBookingTab();
		}
	},

	/***********Config Booking************* */
	addReserveEnvTab : function() {
		var tab = this.getTabPanelRef();
		var reserveEnvForm = tab.child('#reserveEnvForm');
		if (reserveEnvForm == null) {
			var newTab = tab.add({
						xtype : 'panel',
						title : 'Reserve RE CONFIG',
						itemId : 'reserveEnvForm',
						iconCls : 'icon-booking',
						closable : true,
						autoScroll : true,
						items : [{
									xtype : 'reserveEnvForm'
								}]
					}).show();
			tab.setActiveTab(newTab);
		} else {
			tab.setActiveTab(reserveEnvForm);
		}
	},
	addSearchBookingTab : function() {
		var tab = this.getTabPanelRef();
		var searchBookingsPanel = tab.child('#searchBookingsPanel');
		if (searchBookingsPanel == null) {
			var bookingTab = tab.add({
						xtype : 'searchBookingPanel',
						iconCls : 'icon-search',
						itemId : 'searchBookingsPanel'
					}).show();
			var bookingGrid = this.getBookingResultsGridRef();
			bookingGrid.hide();
			tab.setActiveTab(bookingTab);
		} else {
			tab.setActiveTab(searchBookingsPanel);
		}
	},
	searchAllBookings : function(button) {
		var bookingGrid = this.getBookingResultsGridRef();
		var form = this.getSearchBookingsRef().getForm();
		if (form.isValid()) {
			form.submit({
				success : function(form, response) {
					var records = response.result.bookConfigDTOList;
					var message = response.result.model.message;
					if (records == null) {
						Ext.Msg.alert('Message', message);
						bookingGrid.hide();
					} else {										
						bookingGrid.getStore().loadData(records);
						bookingGrid.show();	
					}					
				},
				failure : function(form, response) {
					Ext.Msg.alert('Failed','Your request could not be processed. Please try again later.');
				}
			});
		}
	},
	saveForm : function(button) {
		var form = this.getReserveEnvFormRef().getForm();
		if (form.isValid()) {
			button.disable();
			form.submit({				
				success : function(form, response) {
					form.reset();
					Ext.Msg.alert('Message', response.result.model.message);					
				},
				failure : function(form, response) {
					Ext.Msg.alert('Failed',response.result.model.message);
				}
			});
		}
	},
    editBooking : function(button) {
		var gridRef = this.getBookingResultsGridRef();
		var sm = gridRef.getSelectionModel();
		var count = sm.getCount();
		Ext.Ajax.request({
            url: 'admin/FileUploadAction!execute.action',
            success: function(response, opts){ 
            	if(count == 0) {
        			Ext.Msg.alert('Error',"Please select a row." );
        		} else {
	            	var record = (sm.getSelection())[0];
	    			var view = Ext.widget('editBookingsForm');
	    			Ext.getCmp('editBookingsForm').down('form').getForm().loadRecord(record);
	    			view.show();
        		}
            },
            failure: function(response, opts) {
            	Ext.Msg.alert('Failed','Your request could not be processed. Please try again later.');
            }
        });			
	},
	updateBooking : function(button) {
		var editBookingsForm = this.getEditBookingsFormRef().getForm();
		var gridRef = this.getBookingResultsGridRef();
		var store = gridRef.getStore();
		if (editBookingsForm.isValid()) {
			button.disable();
			editBookingsForm.submit({
				success : function(form, response) {	
					button.up('.window').close();
					Ext.Msg.alert('Message',response.result.model.message);
					var tempRec = store.findRecord('lineNo',response.result.bookConfigDTO.lineNo);
					var index = store.indexOf(tempRec);
					store.removeAt(index);
					store.insert(index,response.result.bookConfigDTO);
				},
				failure : function(form, response) {
					Ext.Msg.alert('Failed',response.result.model.message);
				}
			});
		}		
	},
	exportBookings: function(btn) {
		var form = this.getSearchBookingsRef().getForm();
		try {
			var envName = form.findField('envName').getValue();			
			var fromDate = form.findField('fromDate').getValue();
			var toDate = form.findField('toDate').getValue();
			var showExpiredBookings = form.findField('showExpiredBookings').getValue();
						
			if(fromDate == null){
				fromDate="";
			}
			if(toDate == null){
				toDate="";
			}
			window.location = "BookConfigAction!downloadFile.action?envName="+ envName+
				"&fromDate="+fromDate+"&toDate="+toDate+"&showExpiredBookings="+showExpiredBookings;
		} catch (err) {
			alert(err.message);
		}        
    }
});