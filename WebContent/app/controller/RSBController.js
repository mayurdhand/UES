Ext.define('UES.controller.RSBController', {
	extend : 'Ext.app.Controller',
	views : ['common.Tabpanel', 'common.Menu',
			'rsBooking.ReserveRS','rsBooking.RSBookingTree','rsBooking.SearchRSBookingPanel',
			'rsBooking.SearchRSBookingsForm','rsBooking.RSBookingResultsGrid','rsBooking.EditRSBookingsForm'],
	stores : ['Region','RegionWithALL','RSBookingTree','SearchRSBookingResult','BookingStatus'],
	models : ['Region','BookingStatus'],

	init : function() {
		this.control({
					'button[itemId = saveRS]' : {
						click : this.saveForm
					},
					'menuPanel > rsBookingTree' : {
						itemclick : this.rsBookingClick
					},
					'button[itemId = searchRS]' : {
						click : this.searchRSBookings
					},
					'button[itemId = exportRS]' : {
						click : this.exportRSBookings
					},
					'button[itemId = editRSBooking]' : {
						click : this.editRSBookings
					},
					'button[itemId = updateRSBooking]' : {
						click : this.updateRSBookings
					}
				});
	},
	refs : [{ 	ref : 'reserveRSFormRef',
				selector : 'reserveRSForm'
			}, {
				ref : 'tabPanelRef',
				selector : 'tabpanel[itemId=tabPanel]'
			}, {
				ref : 'rsBookingResultsGridRef',
				selector : 'rsBookingResultsGrid'
			}],

	rsBookingClick : function(self, record, item, index, e, eOpts) {
		if (index == 0) {
			this.addNewRSTab();
		} else if (index == 1) {
			this.addSearchRSBookingTab();
		}
	},

	/***********RS Booking************* */
	addNewRSTab : function() {
		var tab = this.getTabPanelRef();
		var reserveEnvForm = tab.child('#reserveRSForm');
		if (reserveEnvForm == null) {
			var newTab = tab.add({
						xtype : 'panel',
						title : 'Request New RS ',
						itemId : 'reserveRSForm',
						iconCls : 'icon-booking',
						closable : true,
						autoScroll : true,
						items : [{
									xtype : 'reserveRSForm'
								}]
					}).show();
			tab.setActiveTab(newTab);
		} else {
			tab.setActiveTab(reserveEnvForm);
		}
	},
	addSearchRSBookingTab : function() {
		var tab = this.getTabPanelRef();
		var searchRSBookingsPanel = tab.child('#searchRSBookingsPanel');
		if (searchRSBookingsPanel == null) {
			var bookingTab = tab.add({
						xtype : 'searchRSBookingPanel',
						iconCls : 'icon-search',
						itemId : 'searchRSBookingsPanel'
					}).show();
			var bookingGrid = this.getRsBookingResultsGridRef();
			bookingGrid.hide();
			tab.setActiveTab(bookingTab);
		} else {
			tab.setActiveTab(searchRSBookingsPanel);
		}
	},
	searchRSBookings : function(button) {
		var form = button.up('form').getForm();
		if (form.isValid()) {
			this.submitRSSearchForm(form);
		}
	},
	saveForm : function(button) {
		var form = this.getReserveRSFormRef().getForm();
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
    submitRSSearchForm : function(form){
    	var bookingGrid = this.getRsBookingResultsGridRef();
    	form.submit({
			success : function(form, response) {
				var records = response.result.bookRSDTOList;
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
    },
    exportRSBookings: function(btn) {
		var form = btn.up('searchRSBookingPanel').down('form').getForm();
		try {
			var region = form.findField('region').getValue();			
			var uatRS = form.findField('uatRS').getValue();
			var bookingStatus = form.findField('bookingStatus').getValue();
			window.location = "BookRSAction!downloadFile.action?bookingStatus="+ bookingStatus+
				"&uatRS="+uatRS+"&region="+region;
		} catch (err) {
			alert(err.message);
		}        
    },
    editRSBookings : function(button) {
		var gridRef = button.up('grid');
		var sm = gridRef.getSelectionModel();
		var count = sm.getCount();
		Ext.Ajax.request({
            url: 'admin/FileUploadAction!execute.action',
            success: function(response, opts){ 
            	if(count == 0) {
        			Ext.Msg.alert('Error',"Please select a row." );
        		} else {
	            	var record = (sm.getSelection())[0];
	    			var view = Ext.widget('editRSBookingsForm');
	    			Ext.getCmp('editRSBookingsForm').down('form').getForm().loadRecord(record);
	    			view.show();
        		}
            },
            failure: function(response, opts) {
            	Ext.Msg.alert('Failed','Your request could not be processed. Please try again later.');
            }
        });			
	},
	updateRSBookings : function(button) {
		var editBookingsForm = button.up('window').down('form').getForm();
		if (editBookingsForm.isValid()) {
			button.disable();
			editBookingsForm.submit({
				success : function(form, response) {	
					button.up('.window').close();
					Ext.Msg.alert('Message',response.result.model.message);
					var form = Ext.ComponentQuery.query('rsBookingResultsGrid')[0].up('panel').down('form');
					this.submitRSSearchForm(form);
				},
				failure : function(form, response) {
					Ext.Msg.alert('Failed',response.result.model.message);
				}
			});
		}		
	}
});