Ext.define('UES.controller.UESGatewayController', {
	extend : 'Ext.app.Controller',
	views : ['common.Tabpanel', 'common.Menu' ,'tickets.OpenTicket','tickets.UESGatewayTree'
	         ,'tickets.SearchTicket','tickets.OpenTicketPanel'],
	stores : ['UESGatewayTree','Application','Ticket_Environments'],
	models : ['Region'],

	init : function() {
		this.control({
				'menuPanel > uesSupportGateway' : {
					itemclick : this.uesGatewayTreeClick
				},
				'tabpanel openTicketPanel openTicket radiogroup':{
					change : this.changeTicketType
				},
				'tabpanel openTicketPanel openTicket combobox[itemId=application]':{
					select : this.reloadEnvironments
				},
				'tabpanel openTicketPanel openTicket combobox[itemId=environment]':{
					expand : this.loadEnvironments
				},
				'tabpanel openTicketPanel openTicket textfield[itemId=email]':{
					blur : this.validateEmail
				},
				'tabpanel openTicketPanel openTicket button[itemId = ticketSave]':{
					click : this.saveTicket
				}
			});
	},
	refs : [{
				ref : 'tabPanelRef',
				selector : 'tabpanel[itemId=tabPanel]'
			}],
	
	uesGatewayTreeClick : function(self, record, item, index, e, eOpts) {
		var dbSymphonyURL  = '';
//		var dbribURL  = '';
		if (index == 0) {
			this.manageApplicationTab('openTicketPanel', 'New Ticket','openTicketPanel','icon-admin');
			var envStore = Ext.StoreManager.lookup('Ticket_Environments');
			envStore.filter("parent", "");
		} else if(index == 1) {
			this.manageApplicationTab('searchTicket', 'Search Ticket','searchTicket','icon-search');
		} else if(index == 2) {
			dbSymphonyURL = 'https://dbunity.gto.global.intranet.db.com/incident_list.do?sysparm_userpref_module=1dd22acded403100cb4d24bc017ddced&sysparm_query=active=true^assignment_group=f54be50b95c82100d0debe5efb5dcb9e^state!=6^EQ&active=true^assigned_to=javascript:gs.user_id()';
			window.open(dbSymphonyURL);
		} else if(index == 3) {
			dbSymphonyURL = 'https://dbsymphony.gto.intranet.db.com/snc/u_service_request_list.do?sysparm_query=assignment_group%3D111d32da0af09601130f48697ce3b110^active%3Dtrue^stateNOT%20IN6^numberSTARTSWITHSR';
			window.open(dbSymphonyURL);
		} else if(index == 4) {
			dbSymphonyURL = 'https://dbunity.gto.global.intranet.db.com/incident_list.do?sysparm_query=active%3Dtrue%5Eopened_by%3Djavascript:gs.getUserID()%5Estate!%3D6&sysparm_view=lite';
			window.open(dbSymphonyURL);
		} else if(index == 5) {
			dbSymphonyURL = 'https://dbsymphony.gto.intranet.db.com/snc/u_service_request_list.do?sysparm_userpref_module=3dc560760a81ee300018af77daac3426&sysparm_query=opened_by=javascript:getMyAssignments()%5eEQ';
			window.open(dbSymphonyURL);
		} /*else if(index == 6) {
			dbribURL = 'https://dbrib.uk.db.com/BOE/OpenDocument/opendoc/openDocument.jsp?sIDType=CUID&iDocID=AZHwjZNHR_tFtOz7fbkwMak';
			window.open(dbribURL);
		}*/
		
	},
	
	manageApplicationTab : function(childName, title, gridName, iconCls) {
		var tab = this.getTabPanelRef();
		var grid = tab.child('#' + childName);
		if (grid == null) {
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
			tab.setActiveTab(grid);
		}
	},
	changeTicketType : function(cmp,newValue,oldValue) {
		var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
		var deliveryDate = cmp.up('panel').down('datefield[itemId=deliveryDate]');
		var email = cmp.up('panel').down('textfield[itemId=email]');
		if(newValue.type == 2) {
			cmp.up('panel').down('textfield[name=summary]').setFieldLabel('Request Summary '+required);
			cmp.up('panel').down('textarea[name=reqSummary]').setFieldLabel('Request Description '+required);
			deliveryDate.show();
			email.hide();
		}else {
			deliveryDate.hide();
			email.show();
			cmp.up('panel').down('textfield[name=summary]').setFieldLabel('Incident Summary '+required);
			cmp.up('panel').down('textarea[name=reqSummary]').setFieldLabel('Incident Description '+required);
		}
	},
	loadEnvironments: function(cmp){
		var appCombo = cmp.up('panel').down('combobox[itemId=application]');
		var appComboVal = appCombo.getValue();
		cmp.clearValue();
		
		if(Ext.isEmpty(appComboVal)) {
			cmp.getStore().filterBy(function(item){
				return (item.get('parent')=='' );
				});
		}else {
			var store = cmp.getStore();
			store.clearFilter();
			cmp.getStore().filterBy(function(item){
				return (item.get('parent')== appComboVal );
			});
		}
	},
	reloadEnvironments: function(cmp,records){
		var envCombo = cmp.up('panel').down('combobox[itemId=environment]');
		envCombo.clearValue();
	},
	saveTicket : function(cmp) {
		var myMask=null;	
		var ticOpenURL='';
		var ticketType = cmp.up('panel').down('radiogroup[itemId=tickettype]').getValue().type;
		var shortDesc = cmp.up('panel').down('textfield[name=summary]').getValue();
		var longDesc = cmp.up('panel').down('textarea[name=reqSummary]').getValue();
		var email = cmp.up('panel').down('textfield[name=email]').getValue();
		var envComboCmp = cmp.up('panel').down('combobox[itemId=environment]');
		var envCombo = envComboCmp.getValue();
		var appCombo = cmp.up('panel').down('combobox[itemId=application]').getValue();
		var deliveryDate = cmp.up('panel').down('datefield[itemId=deliveryDate]').getValue(),
			form = cmp.up('form').getForm();
		if(!Ext.isEmpty(envCombo) && !Ext.isEmpty(appCombo) && !Ext.isEmpty(shortDesc) && !Ext.isEmpty(longDesc)) {
			cmp.disable();
			myMask = new Ext.LoadMask(Ext.getBody(), {msg:"Please wait..."});
			myMask.show();
			if(ticketType == 1) {
				envCombo = envComboCmp.lastSelection[0].get('newCode');
//				ticOpenURL='https://dbunity.gto.global.intranet.db.com/incident.do?sys_id=-1&sysparm_query=assignment_group%3Df54be50b95c82100d0debe5efb5dcb9e%5Ecmdb_ci%3D'+envCombo+'%5Econtact_type%3DClient Interface%5Eu_causing_ci%3D'+envCombo+'%5Eu_symptom_code%3DUnavailable%5Eshort_description%3D'+shortDesc+'%5Edescription%3D'+encodeURIComponent(longDesc)+'%5Eimpact%3D3%5Eurgency%3D1';
				Ext.Ajax.request({
		            url: 'TicketAction!execute.action?longDesc='+encodeURIComponent(longDesc)+'&shortDesc='+shortDesc+'&rptBy='+email+'&affectedCi='+envCombo,
		            success: function(response, opts){              	
		            	var inCode = Ext.JSON.decode(response.responseText).model.inCode;
		            	var error = Ext.JSON.decode(response.responseText).model.error;
		            	if(!Ext.isEmpty(inCode,false)){
		            		Ext.Msg.alert('Success','Your ticket number is '+inCode);
		            		if(!Ext.isEmpty(form)){
			            		form.reset();
	            			}
		            	}else if(!Ext.isEmpty(error,false)){
		            		Ext.Msg.alert('Failed',error);
		            	}else{
		            		Ext.Msg.alert('Failed','Error while raising INC');
		            	}
		            	
		            	cmp.enable();
		            	myMask.hide();
		            },						            
		            failure: function(response, opts){    
		            	cmp.enable();
		            	myMask.hide();
	 					Ext.Msg.alert('Failed','Error while raising INC:'+error);
		            }	
		        });	
			}else if((!Ext.isEmpty(deliveryDate) && ticketType == 2)){	
				deliveryDate = Ext.Date.format(new Date(deliveryDate), "Y-m-d");
				ticOpenURL = 'https://dbsymphony.gto.intranet.db.com/snc/u_service_request.do?sys_id=-1&sysparm_query=priority=3^state=9^active=true^u_environment=UAT^cmdb_ci='+envCombo+'^u_requesttype=Operational Task^assignment_group=111d32da0af09601130f48697ce3b110^u_request_summary='+shortDesc+'^description='+encodeURIComponent(longDesc)+'^due_date='+deliveryDate+'&sysparm_stack=u_service_request_list.do?sysparm_query=active=true^opened_by=javascript:gs.getUserID()^ORDERBYDESCnumber';
				Ext.Msg.show({
				    title: 'Alert',
				    msg: 'Click on Save & Stay button on next window. Hit OK to continue.',
				    width: 300,
				    buttons: Ext.Msg.OK,
				    icon: Ext.window.MessageBox.INFO,
					fn: function(btn, text){
						if (btn == 'ok'){	
							window.location = (ticOpenURL);
						}
					}
				});		
				cmp.enable();
				myMask.hide();
			}else{
				Ext.Msg.alert('Warning','Please enter/select all mandatory fields.');
				cmp.enable();
				myMask.hide();
			}	
		} else {
			Ext.Msg.alert('Warning','Please enter/select all mandatory fields.');
			cmp.enable();
			myMask.hide();
		}							
	}
});