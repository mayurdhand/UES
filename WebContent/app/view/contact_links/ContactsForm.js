var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.define('UES.view.contact_links.ContactsForm', {
    extend: 'Ext.Window',    
    alias : 'widget.contactsForm',
    bodyPadding : '5 5 5 5',
	autoScroll : true,
	height : 290,
	width : 510,
	border : false,
	modal: true,
	title: 'Add Contact',
	fieldDefaults : {
		msgTarget : 'side',
		width : 150
	},
	defaults : {
		overflow : 'auto',
		margin : '10 10 10 10'
	},
	
	layout : {
		type : 'anchor'
	},
    items: [{
    	xtype: 'form',
    	itemId: 'addContactFrm',
    	url : 'admin/ContactsAction!addContact.action',
    	height : 180,
    	width : 470,
    	bodyPadding : '5 5 5 5',
    	items: [{
    		xtype: 'textfield',
            fieldLabel: 'Team',
            labelWidth : 150,
    		labelAlign : 'left',
    		width : 450,
    		afterLabelTextTpl : required,
			allowBlank : false,
			valueField : 'team',
			name : 'team'
        },{
            xtype: 'textfield',
            fieldLabel: 'Email Id',
            labelWidth : 150,
    		labelAlign : 'left',
    		width : 450,
    		afterLabelTextTpl : required,
			allowBlank : false,
			valueField : 'emailId',
			name : 'emailId'
        },{
            xtype: 'textfield',
            fieldLabel: 'Contact Number',
            labelWidth : 150,
    		labelAlign : 'left',
    		width : 450,
    		afterLabelTextTpl : required,
			allowBlank : false,
			valueField : 'contactNumber',
			name : 'contactNumber'
        },{
            xtype: 'textfield',
            fieldLabel: 'Primary Escalation',
            labelWidth : 150,
    		labelAlign : 'left',
    		width : 450,
    		afterLabelTextTpl : required,
			allowBlank : false,
			valueField : 'primaryEscalation',
			name : 'primaryEscalation'
        },{
            xtype: 'textfield',
            fieldLabel: 'Secondary Escalation',
            labelWidth : 150,
    		labelAlign : 'left',
    		width : 450,
    		afterLabelTextTpl : required,
			allowBlank : false,
			valueField : 'secondryEscalation',
			name : 'secondryEscalation'
    	}]
    }],
    buttons: [{
        text: 'Submit',
        itemId : 'addContactBtn'
    },{
        text: 'Reset',
        itemId: 'addContactFrmReset'
    }]
});