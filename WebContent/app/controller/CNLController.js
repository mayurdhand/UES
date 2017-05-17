Ext.define('UES.controller.CNLController', {
	extend : 'Ext.app.Controller',
	views : ['common.Tabpanel','common.Menu','contact_links.CNLTree',
	         'contact_links.ContactsForm','contact_links.ContactsGrid',
			 'contact_links.LinksForm','contact_links.LinksUpdateForm','contact_links.LinksGrid'],
	stores : ['ContactsNLinksTree','Contacts','Links'],
	models : ['Contacts','Links'],

	init : function() {
		this.control({
					'menuPanel > cnlTree' : {
						itemclick : this.contactNlinkTreeClick
					},
					'contactsGrid' : {
						edit : this.updateContact
					},
					'contactsForm button[itemId = addContactBtn]' : {
						click : this.addContact
					},
					'contactsForm button[itemId = addContactFrmReset]' : {
						click : this.resetContact
					},
					'contactsGrid button[itemId = deleteContact]' : {
						click : this.deleteContact
					},
					'linksForm button[itemId = addLinkBtn]' : {
						click : this.addLink
					},
					'linksForm button[itemId = addLinkFrmReset]' : {
						click : this.resetLink
					},
					'linksGrid button[itemId = deleteLink]' : {
						click : this.deleteLink
					},
					'linksGrid button[itemId = updateLink]' : {
						click : this.editLink
					},
					'linksUpdateForm button[itemId = updateLinkBtn]' : {
						click : this.updateLink
					},
					'contactsForm > form' : {
						actioncomplete : this.enableContactFormButton,
						actionfailed : this.enableContactFormButton
					},
					'linksUpdateForm > form' : {
						actioncomplete : this.enableLinkUpdateFormButton,
						actionfailed : this.enableLinkUpdateFormButton
					},
					'linksForm > form' : {
						actioncomplete : this.enableLinkFormButton,
						actionfailed : this.enableLinkFormButton
					}
				});
	},
	refs : [{
				ref : 'tabPanelRef',
				selector : 'tabpanel[itemId=tabPanel]'
			},{
				ref : 'contactsGridRef',
				selector : 'contactsGrid'
			},{
				ref : 'contactFormRef',
				selector : 'contactsForm > form'
			},{
				ref : 'contactFormWindowRef',
				selector : 'contactsForm'
			},{
				ref : 'linksGridRef',
				selector : 'linksGrid'
			},{
				ref : 'linkFormRef',
				selector : 'linksForm > form'
			},{
				ref : 'linkFormWindowRef',
				selector : 'linksForm'
			},{
				ref : 'linkUpdateFormRef',
				selector : 'linksUpdateForm > form'
			},{
				ref : 'linkUpdateFormWindowRef',
				selector : 'linksUpdateForm'
			}],
			
	enableContactFormButton : function(self) {
		var button = self.owner.up('window').down('button[itemId=addContactBtn]');
		button.enable();
	},
	enableLinkUpdateFormButton : function(self) {
		var button = self.owner.up('window').down('button[itemId=updateLinkBtn]');
		button.enable();
	},
	enableLinkFormButton : function(self) {
		var button = self.owner.up('window').down('button[itemId=addLinkBtn]');
		button.enable();
	},

	/***********Contacts************* */
	contactNlinkTreeClick : function(self, record, item, index, e, eOpts) {
		if (index == 0) {
			this.viewContactsTab('viewContactsGrid', 'Contacts','contactsGrid','icon-contacts');
		} else if (index == 1) {
			this.viewContactsTab('viewLinksGrid', 'Links', 'linksGrid','icon-links');
		}
	},
	
	viewContactsTab : function(childName, title, gridName, iconCls) {
		var tab = this.getTabPanelRef();
		var contactsGrid = tab.child('#' + childName);
		if (contactsGrid == null) {
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
			tab.setActiveTab(contactsGrid);
		}
	},
	updateContact : function(editor, e) {
		var gridRef = this.getContactsGridRef();
		var store = gridRef.getStore();
		var teamId = null;
		Ext.Ajax.request({
            url: 'admin/ContactsAction!updateContact.action',
            method: 'POST',
            params: {
            	teamId: teamId,
            	team: e.record.get('team'),
            	emailId: e.record.get('emailId'),
            	contactNumber: e.record.get('contactNumber'),
            	primaryEscalation: e.record.get('primaryEscalation'),
            	secondryEscalation: e.record.get('secondryEscalation')
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
	
	addContact : function(button) {
		var form = this.getContactFormRef().getForm();
		var gridRef = this.getContactsGridRef();
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
	
	resetContact : function(button) {
		var form = this.getContactFormRef().getForm();
		form.reset();
	},
	
	deleteContact : function(button) {
		var me = this;
		var gridRef = this.getContactsGridRef();
		var store = gridRef.getStore();
		var sm = gridRef.getSelectionModel();
		var count = sm.getCount();
		if(count == 0) {
			Ext.Msg.alert('Error',"Please select a row." );
		} else {
			var record = (sm.getSelection())[0];
			var teamId = record.get('team');
			Ext.MessageBox.confirm('Warning', 'Are you sure to delete "' + teamId + '" ?', function(btn){
				if(btn == 'yes'){
					me.confirmDeleteContact(record,store);
				}
			});
		}
	},
	confirmDeleteContact : function(record,store) {
		Ext.Ajax.request({
            url: 'admin/ContactsAction!deleteContact.action',
            method: 'POST',
            params: {
            	teamId: record.get('teamId'),
            	team: record.get('team'),
            	emailId: record.get('emailId'),
            	contactNumber: record.get('contactNumber'),
            	primaryEscalation: record.get('primaryEscalation'),
            	secondryEscalation: record.get('secondryEscalation')
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
	
	/***********Links************* */
	addLink : function(button) {
		var form = this.getLinkFormRef().getForm();
		var gridRef = this.getLinksGridRef();
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
	deleteLink : function(button) {
		var me = this;
		var gridRef = this.getLinksGridRef();
		var store = gridRef.getStore();
		var sm = gridRef.getSelectionModel();
		var count = sm.getCount();
		if(count == 0) {
			Ext.Msg.alert('Error',"Please select a row." );
		} else {
			var record = (sm.getSelection())[0];
			var applicationName = record.get('applicationName');
			Ext.MessageBox.confirm('Warning', 'Are you sure to delete "' + applicationName + '" ?', function(btn){
				if(btn == 'yes'){
					me.confirmDeleteLink(record,store);
				}
			});
		}
	},
	resetLink : function(button) {
		var form = this.getLinkFormRef().getForm();
		form.reset();
	},
	confirmDeleteLink : function(record,store) {
		Ext.Ajax.request({
            url: 'admin/LinksAction!deleteLink.action',
            method: 'POST',
            params: {
            	linkId: record.get('linkId'),
            	applicationName: record.get('applicationName'),
            	url: record.get('url'),
            	applicationDesc: record.get('applicationDesc')
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
	editLink : function(button) {
		var gridRef = this.getLinksGridRef();
		var sm = gridRef.getSelectionModel();
		var count = sm.getCount();
		if(count == 0) {
			Ext.Msg.alert('Error',"Please select a row." );
		} else {
			var record = (sm.getSelection())[0];
			var view = Ext.widget('linksUpdateForm');
			this.getLinkUpdateFormRef().getForm().loadRecord(record);
			view.show();
		}
	},
	updateLink : function(button) {
		var linkUpdateForm = this.getLinkUpdateFormRef().getForm();
		var gridRef = this.getLinksGridRef();
		var store = gridRef.getStore();
		if (linkUpdateForm.isValid()) {
			button.disable();			
			linkUpdateForm.submit({
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
	}
});