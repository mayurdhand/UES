Ext.define('UES.controller.AdminController', {
	extend : 'Ext.app.Controller',
	views : ['common.Tabpanel', 'common.Menu', 'admin.AdminTree','admin.ManageApplicationPanel',
	         'admin.FileUploadForm','admin.FileDownloadForm','admin.RiskEngineExpiryPanel',
	         'admin.BookingRequestsGrid','admin.EditRequestsForm','admin.ApproveRequestsForm',
	         'admin.RejectRequestsForm','admin.RSBookingRequestsGrid','admin.EditRSRequestsForm',
	         'admin.RejectRSRequestsForm','admin.ApproveRSRequestsForm','admin.RSExpiryPanel',
	         'admin.RSDecomGrid','admin.CCGrid','admin.CCForm'],
	stores : ['AdminTree','AppFileType','REBookingRequests','Region','AvailableEnvironments',
	          'RSBookingRequests','RSPendingDecomResult','CC'],
	models : ['AppFileType','Region','AvailableEnvironments','RSPendingDecomResult','CC'],

	init : function() {
		this.control({
				'menuPanel > adminTree' : {
					itemclick : this.adminTreeClick
				},
				'button[itemId = upload]' : {
					click : this.fileUpload
				},
				'button[itemId = download]' : {
					click : this.fileDownload
				},
				'button[itemId = start]' : {
					click : this.startExChecker
				},
				'button[itemId = stop]' : {
					click : this.stopExChecker
				},
				'button[itemId = status]' : {
					click : this.statusExChecker
				},
				'button[itemId = startRSEx]' : {
					click : this.startRSExChecker
				},
				'button[itemId = stopRSEx]' : {
					click : this.stopRSExChecker
				},
				'button[itemId = statusRSEx]' : {
					click : this.statusRSExChecker
				},
				'button[itemId = editRequest]' : {
					click : this.editRequest
				},
				'button[itemId = refreshRequest]' : {
					click : this.refreshRequest
				},
				'button[itemId = updateRequest]' : {
					click : this.updateRequest
				},
				'bookingRequestsGrid' : {
					render : this.loadBookingRequestsGrid
				},
				'button[itemId = rejectRequest]' :{
					click : this.rejectRequest
				},
				'button[itemId = submitRejectRequest]' :{
					click : this.submitRejectRequest
				},
				'button[itemId = approveRequest]' :{
					click : this.approveRequest
				},
				'button[itemId = submitApproveRequest]' :{
					click : this.submitApproveRequest
				},
				'editRequestsForm > form' : {
					actioncomplete : this.enableEditRequestFormButton,
					actionfailed : this.enableEditRequestFormButton
				},
				'rejectRequestsForm > form' : {
					actioncomplete : this.enableRejectRequestFormButton,
					actionfailed : this.enableRejectRequestFormButton
				},
				'approveRequestsForm > form' : {
					actioncomplete : this.enableApproveRequestFormButton,
					actionfailed : this.enableApproveRequestFormButton
				},
				'button[itemId = editRSRequest]' : {
					click : this.editRSRequest
				},
				'button[itemId = refreshRSRequest]' : {
					click : this.refreshRSRequest
				},
				'button[itemId = updateRSRequest]' : {
					click : this.updateRSRequest
				},
				'rsBookingRequestsGrid' : {
					render : this.loadRSBookingRequestsGrid
				},
				'button[itemId = rejectRSRequest]' :{
					click : this.rejectRSRequest
				},
				'button[itemId = submitRejectRSRequest]' :{
					click : this.submitRejectRSRequest
				},
				'button[itemId = approveRSRequest]' :{
					click : this.approveRSRequest
				},
				'button[itemId = submitApproveRSRequest]' :{
					click : this.submitApproveRSRequest
				},
				'editRSRequestsForm > form' : {
					actioncomplete : this.enableRSEditRequestFormButton,
					actionfailed : this.enableRSEditRequestFormButton
				},
				'rejectRSRequestsForm > form' : {
					actioncomplete : this.enableRSRejectRequestFormButton,
					actionfailed : this.enableRSRejectRequestFormButton
				},
				'approveRSRequestsForm > form' : {
					actioncomplete : this.enableRSApproveRequestFormButton,
					actionfailed : this.enableRSApproveRequestFormButton
				},
				'rsDecomGrid' : {
					render : this.loadRSDecomGrid
				},
				'ccGrid button[itemId = reloadCache]' : {
					click : this.reloadCache
				},
				'ccForm button[itemId = addCCBtn]' : {
					click : this.addConfig
				},
				'ccForm > form' : {
					actioncomplete : this.enableCCFormButton,
					actionfailed : this.enableCCFormButton
				},
				'ccGrid button[itemId = deleteCCConfig]' : {
					click : this.deleteCCConfig
				},
				'ccGrid' : {
					edit : this.updateConfig
				}
			});
	},
	refs : [{
				ref : 'tabPanelRef',
				selector : 'tabpanel[itemId=tabPanel]'
			},{ 
				ref : 'fileUploadFormRef',
				selector : 'fileUploadForm'
			},{
				ref : 'fileDownloadFormRef',
				selector : 'fileDownloadForm'
			},{
				ref : 'bookingRequestsGridRef',
				selector : 'bookingRequestsGrid'
			},{
				ref : 'rsBookingRequestsGridRef',
				selector : 'rsBookingRequestsGrid'
			},{
				ref : 'rsDecomGridRef',
				selector : 'rsDecomGrid'
			},{
				ref : 'editRequestsFormRef',
				selector : 'editRequestsForm > form'
			},{
				ref : 'rejectRequestsFormRef',
				selector : 'rejectRequestsForm > form'
			},{
				ref : 'approveRequestsFormRef',
				selector : 'approveRequestsForm > form'
			},{
				ref : 'editRSRequestsFormRef',
				selector : 'editRSRequestsForm > form'
			},{
				ref : 'rejectRSRequestsFormRef',
				selector : 'rejectRSRequestsForm > form'
			},{
				ref : 'approveRSRequestsFormRef',
				selector : 'approveRSRequestsForm > form'
			},{
				ref : 'ccGridRef',
				selector : 'ccGrid'
			},{
				ref : 'ccFormRef',
				selector : 'ccForm > form'
			}],
	
	enableEditRequestFormButton : function(self) {
		var button = self.owner.up('window').down('button[itemId=updateRequest]');
		button.enable();
	},
	enableRejectRequestFormButton : function(self) {
		var button = self.owner.up('window').down('button[itemId=submitRejectRequest]');
		button.enable();
	},
	enableApproveRequestFormButton : function(self) {
		var button = self.owner.up('window').down('button[itemId=submitApproveRequest]');
		button.enable();
	},
	enableRSEditRequestFormButton : function(self) {
		var button = self.owner.up('window').down('button[itemId=updateRSRequest]');
		button.enable();
	},
	enableRSRejectRequestFormButton : function(self) {
		var button = self.owner.up('window').down('button[itemId=submitRejectRSRequest]');
		button.enable();
	},
	enableRSApproveRequestFormButton : function(self) {
		var button = self.owner.up('window').down('button[itemId=submitApproveRSRequest]');
		button.enable();
	},
	enableCCFormButton : function(self) {
		var button = self.owner.up('window').down('button[itemId=addCCBtn]');
		button.enable();
	},
	/** *********Admin Tree Click************* */
	adminTreeClick : function(self, record, item, index, e, eOpts) {
		if (index == 0) {
			this.manageApplicationTab('bookingRequestsGrid', 'Risk Engine Requests','bookingRequestsGrid','icon-admin');
		} else if (index == 1) {
			this.manageApplicationTab('rsBookingRequestsGrid', 'Report Server Requests','rsBookingRequestsGrid','icon-admin');			
		} else if (index == 2) {
			this.manageApplicationTab('rsDecomGrid', 'Report Server Decommission','rsDecomGrid','icon-admin');			
		}  else if (index == 3) {
			this.manageApplicationTab('manageApplicationPanel', 'Services & Data','manageApplicationPanel','icon-settings');			
		}  else if (index == 4) {
			this.manageApplicationTab('ccGrid', 'RE Config Cache','ccGrid','icon-settings');			
		}
	},
	
	manageApplicationTab : function(childName, title, gridName, iconCls) {
		var tab = this.getTabPanelRef();
		var childTab = tab.child('#' + childName);
		Ext.Ajax.request({
            url: 'admin/FileUploadAction!execute.action',
            success: function(response, opts){            	
        		if (childTab == null) {
        			var newTab = tab.add({
        						xtype : 'panel',
        						title : title,
        						iconCls : iconCls,
        						itemId : childName,
        						closable : true,
        						autoScroll : true,
        						items : [{
        							xtype : gridName
        						}]
        					}).show();
        			tab.setActiveTab(newTab);
        		} else {
        			tab.setActiveTab(childTab);
        		}
        		
            },
            failure: function(response, opts) {
            	Ext.Msg.alert('Failed','Your request could not be processed. Please try again later.');
            }
        });
	/************** Risk Engine Admin *************/	
	},	
    loadBookingRequestsGrid : function() {
    	var grid = this.getBookingRequestsGridRef();
    	grid.getStore().load();
    },
    loadRSBookingRequestsGrid : function() {
    	var grid = this.getRsBookingRequestsGridRef();
    	grid.getStore().load();
    },
    loadRSDecomGrid : function() {
    	var grid = this.getRsDecomGridRef();
    	grid.getStore().load();
    },
	fileUpload: function(btn) {
        var form = this.getFileUploadFormRef().getForm();
        if(form.isValid()){
            form.submit({
                waitMsg: 'Uploading your file...',
                timeout : 10,
                params : {
                	uploadFile : form.findField('file').getValue()
                },
                success : function(form,response) {
					Ext.Msg.alert('Upload Successful', response.result.model.message);
				},
				failure : function(form, response) {
					Ext.Msg.alert('Upload Failure', response.result.model.message);
				}				
            });
        }
    },
	fileDownload: function(btn) {
		try {
			var form = this.getFileDownloadFormRef().getForm();
			if(form.isValid()){	
//			    var link = document.createElement("a");
			    var ft = form.findField('fileType1').getValue(); 
//			    link.href = "admin/FileUploadAction!downloadFile.action?fileType="+ft;
//			    link.click();
			    window.location = "admin/FileUploadAction!downloadFile.action?fileType="+ft;
			}
		}catch(err){
		  alert(err.message);
		}        
    },
	startExChecker: function(btn) {
		this.executeButtonClick("admin/BookConfigAction!start.action");
    },
    stopExChecker: function(btn) {
    	this.executeButtonClick("admin/BookConfigAction!stop.action");
    },
    statusExChecker: function(btn) {
    	this.executeButtonClick("admin/BookConfigAction!status.action");
    },
	startRSExChecker: function(btn) {
		this.executeButtonClick("admin/BookRSAction!start.action");
    },
    stopRSExChecker: function(btn) {
    	this.executeButtonClick("admin/BookRSAction!stop.action");
    },
    statusRSExChecker: function(btn) {
    	this.executeButtonClick("admin/BookRSAction!status.action");
    },
    executeButtonClick : function(url) {
    	Ext.Ajax.request({
            url: url,
            success: function(response, opts){  
            	var respText = Ext.decode(response.responseText);            	
            	Ext.Msg.alert('Message',respText.model.message);
            },
            failure: function(response, opts) {
            	Ext.Msg.alert('Failed','Your request could not be processed. Please try again later.');
            }
        });
    },
    refreshRequest : function(button) {
    	button.up('grid').getStore().load();
    },
    editRequest : function(button) {
		var gridRef = this.getBookingRequestsGridRef();
		var sm = gridRef.getSelectionModel();
		var count = sm.getCount();
		if(count == 0) {
			Ext.Msg.alert('Error',"Please select a row." );
		} else {
			var record = (sm.getSelection())[0];
			var view = Ext.widget('editRequestsForm');
			this.getEditRequestsFormRef().getForm().loadRecord(record);
			view.show();
		}
	},
	updateRequest : function(button) {
		var editRequestsForm = this.getEditRequestsFormRef().getForm();
		var gridRef = this.getBookingRequestsGridRef();
		var store = gridRef.getStore();
		if (editRequestsForm.isValid()) {	
			button.disable();
			editRequestsForm.submit({
				success : function(form, response) {
					button.up('.window').close();
					Ext.Msg.alert('Message',response.result.model.message);
					store.load();					
				},
				failure : function(form, response) {
					Ext.Msg.alert('Failed',response.result.model.message);
				}
			});
		}		
	},
	rejectRequest : function(button) {
		var gridRef = this.getBookingRequestsGridRef();
		var sm = gridRef.getSelectionModel();
		var count = sm.getCount();
		if(count == 0) {
			Ext.Msg.alert('Error',"Please select a row." );
		} else {
			var record = (sm.getSelection())[0];
			var view = Ext.widget('rejectRequestsForm');
			this.getRejectRequestsFormRef().getForm().loadRecord(record);
			view.show();
		}
	},
	submitRejectRequest : function(button) {
		var rejectRequestForm = this.getRejectRequestsFormRef().getForm();
		var gridRef = this.getBookingRequestsGridRef();
		var store = gridRef.getStore();
		if (rejectRequestForm.isValid()) {	
			button.disable();
			rejectRequestForm.submit({				
				success : function(form, response) {	
					button.up('.window').close();
					Ext.Msg.alert('Message',response.result.model.message);
					store.load();					
				},
				failure : function(form, response) {
					Ext.Msg.alert('Failed',response.result.model.message);
				}
			});
		}		
	},
	approveRequest : function(button) {
		var gridRef = this.getBookingRequestsGridRef();
		var sm = gridRef.getSelectionModel();
		var count = sm.getCount();
		var record = (sm.getSelection())[0];
		var view = Ext.widget('approveRequestsForm');
		if(count == 0) {
			Ext.Msg.alert('Error',"Please select a row." );
		} else {			
			var approveForm = this.getApproveRequestsFormRef().getForm();			
			var fromDate = record.get('fromDate');
			var toDate = record.get('toDate');
			var region = record.get('region');					
			Ext.Ajax.request({
	            url: 'admin/BookConfigAction!allotConfig.action?fromDate='+fromDate+'&toDate='+toDate+'&region='+region,
	            success: function(response, opts){              	
// 					var raw = '{"freeEnvSet": [{"name": "xcv"},{"name": "xcvsdas"}]}';
	            	var json = Ext.JSON.decode(response.responseText).freeEnvSet;
	            	approveForm.findField('envName').getStore().loadData(json);
	            }			
	        });	
			approveForm.loadRecord(record);
			view.show();
		}		
	},
	submitApproveRequest : function(button) {
		var approveRequestForm = this.getApproveRequestsFormRef().getForm();
		var gridRef = this.getBookingRequestsGridRef();
		var store = gridRef.getStore();
		if (approveRequestForm.isValid()) {
			button.disable();
			approveRequestForm.submit({
				success : function(form, response) {
					button.up('.window').close();
					Ext.Msg.alert('Message',response.result.model.message);
					store.load();					
				},
				failure : function(form, response) {
					Ext.Msg.alert('Failed',response.result.model.message);
				}
			});
		}		
	}  	
	/**************** RS Admin ****************/
	,
    refreshRSRequest : function(button) {
    	button.up('grid').getStore().load();
    },
    editRSRequest : function(button) {
		var gridRef = this.getRsBookingRequestsGridRef();
		var sm = gridRef.getSelectionModel();
		var count = sm.getCount();
		if(count == 0) {
			Ext.Msg.alert('Error',"Please select a row." );
		} else {
			var record = (sm.getSelection())[0];
			var view = Ext.widget('editRSRequestsForm');
			this.getEditRSRequestsFormRef().getForm().loadRecord(record);
			view.show();
		}
	},
	updateRSRequest : function(button) {
		var editRSRequestsForm = this.getEditRSRequestsFormRef().getForm();
		var gridRef = this.getRsBookingRequestsGridRef();
		var store = gridRef.getStore();
		if (editRSRequestsForm.isValid()) {	
			button.disable();
			editRSRequestsForm.submit({
				success : function(form, response) {
					button.up('.window').close();
					Ext.Msg.alert('Message',response.result.model.message);
					store.load();					
				},
				failure : function(form, response) {
					Ext.Msg.alert('Failed',response.result.model.message);
				}
			});
		}		
	},
	rejectRSRequest : function(button) {
		var gridRef = this.getRsBookingRequestsGridRef();
		var sm = gridRef.getSelectionModel();
		var count = sm.getCount();
		if(count == 0) {
			Ext.Msg.alert('Error',"Please select a row." );
		} else {
			var record = (sm.getSelection())[0];
			var view = Ext.widget('rejectRSRequestsForm');
			this.getRejectRSRequestsFormRef().getForm().loadRecord(record);
			view.show();
		}
	},
	submitRejectRSRequest : function(button) {
		var rejectRSRequestForm = this.getRejectRSRequestsFormRef().getForm();
		var gridRef = this.getRsBookingRequestsGridRef();
		var store = gridRef.getStore();
		if (rejectRSRequestForm.isValid()) {	
			button.disable();
			rejectRSRequestForm.submit({				
				success : function(form, response) {	
					button.up('.window').close();
					Ext.Msg.alert('Message',response.result.model.message);
					store.load();					
				},
				failure : function(form, response) {
					Ext.Msg.alert('Failed',response.result.model.message);
				}
			});
		}		
	},
	approveRSRequest : function(button) {
		var gridRef = this.getRsBookingRequestsGridRef();
		var sm = gridRef.getSelectionModel();
		var count = sm.getCount();
		var record = (sm.getSelection())[0];
		var view = Ext.widget('approveRSRequestsForm');
		if(count == 0) {
			Ext.Msg.alert('Error',"Please select a row." );
		} else {	
			var approveRSForm = this.getApproveRSRequestsFormRef().getForm();			
			approveRSForm.loadRecord(record);
			view.show();			
		}		
	},
	submitApproveRSRequest : function(button) {
		var approveRSRequestForm = this.getApproveRSRequestsFormRef().getForm();
		var gridRef = this.getRsBookingRequestsGridRef();
		var store = gridRef.getStore();
		if (approveRSRequestForm.isValid()) {
			button.disable();
			approveRSRequestForm.submit({
				success : function(form, response) {
					button.up('.window').close();
					Ext.Msg.alert('Message',response.result.model.message);
					store.load();					
				},
				failure : function(form, response) {
					Ext.Msg.alert('Failed',response.result.model.message);
				}
			});
		}		
	},
	/**************** RE Config Cache ****************/
	reloadCache : function(button) {
		var myMask=null;
		var gridRef = this.getCcGridRef();
		var sm = gridRef.getSelectionModel();
		var count = sm.getCount();
		if(count == 0) {
			Ext.Msg.alert('Error',"Please select a config." );
		} else {
			var record = (sm.getSelection())[0];
			myMask = new Ext.LoadMask(button.up('grid'), {msg:"Please wait..."});
			myMask.show();
			Ext.Ajax.request({
	            url: 'admin/CCAction!reloadCache.action',
	            method: 'POST',
	            params: {
	            	config: record.get('config'),
	            	database: record.get('database'),
	            	ccHost: record.get('ccHost'),
	            	ccPort: record.get('ccPort'),
	            },
	            success : function(response) {
	            	var respText = Ext.decode(response.responseText);   
	        		Ext.Msg.alert('Message',respText.model.message);
					myMask.hide();
				},
				failure : function(form, action) {
					Ext.Msg.alert('Failed','Your request could not be processed. Please try again later.');
					myMask.hide();
				}
	           });
		}
	},	
	
	addConfig : function(button) {
		var form = this.getCcFormRef().getForm();
		var gridRef = this.getCcGridRef();
		var store = gridRef.getStore();
		if (form.isValid()) {
			button.disable();
			form.submit({				
				success : function(form, response) {	
					button.up('.window').close();
					Ext.Msg.alert('Message',response.result.model.message);					
					store.load();					
				},
				failure : function(form, action) {
					Ext.Msg.alert('Failed','Your request could not be processed. Please try again later.');
					
				}
			});		
		}
	},
	deleteCCConfig : function(button) {
		var me = this;
		var gridRef = this.getCcGridRef();
		var store = gridRef.getStore();
		var sm = gridRef.getSelectionModel();
		var count = sm.getCount();
		if(count == 0) {
			Ext.Msg.alert('Error',"Please select a Config." );
		} else {
			var record = (sm.getSelection())[0];
			var config = record.get('config');
			Ext.MessageBox.confirm('Warning', 'Are you sure to delete "' + config + '" ?', function(btn){
				if(btn == 'yes'){
					me.confirmDeleteCCConfig(record,store);
				}
			});
		}
	},
	confirmDeleteCCConfig : function(record,store) {
		Ext.Ajax.request({
            url: 'admin/CCAction!deleteConfig.action',
            method: 'POST',
            params: {
            	configId: record.get('configId'),            	
            },
            	success : function(response) {
            		var respText = Ext.decode(response.responseText);   
            		Ext.Msg.alert('Message',respText.model.message);
            		store.load();					
			},
				failure : function(form, action) {
					Ext.Msg.alert('Failed','Your request could not be processed. Please try again later.');
			}
           });
	},
	updateConfig : function(editor, e) {
		var gridRef = this.getCcGridRef();
		var store = gridRef.getStore();
		var teamId = null;
		Ext.Ajax.request({
            url: 'admin/CCAction!updateConfig.action',
            method: 'POST',
            params: {
            	teamId: teamId,
            	config: e.record.get('config'),
            	database: e.record.get('database'),
            	ccHost: e.record.get('ccHost'),
            	ccPort: e.record.get('ccPort'),
            },
            success : function(response) {
            	var respText = Ext.decode(response.responseText);   
        		Ext.Msg.alert('Message',respText.model.message);
				store.load();					
			},
			failure : function(form, action) {
				Ext.Msg.alert('Failed','Your request could not be processed. Please try again later.');
			}
           });
	}
});