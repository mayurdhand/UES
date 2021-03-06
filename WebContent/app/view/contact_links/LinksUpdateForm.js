var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.define('UES.view.contact_links.LinksUpdateForm', {
    extend: 'Ext.Window',    
    alias : 'widget.linksUpdateForm',
    bodyPadding : '5 5 5 5',
	autoScroll : true,
	height : 290,
	width : 510,
	border : false,
	modal: true,
	title: 'Update Link',
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
    	itemId: 'updateLinkFrm',
    	url : 'admin/LinksAction!updateLink.action',
    	height : 180,
    	width : 470,
    	bodyPadding : '5 5 5 5',
    	items: [{
    		xtype: 'textfield',
            fieldLabel: 'Application',
            readOnly: true,
            labelWidth : 150,
    		labelAlign : 'left',
    		width : 450,
    		afterLabelTextTpl : required,
			allowBlank : false,
			valueField : 'applicationName',
			name : 'applicationName'
        },{
            xtype: 'textfield',
            fieldLabel: 'Link',
            labelWidth : 150,
    		labelAlign : 'left',
    		width : 450,
    		afterLabelTextTpl : required,
			allowBlank : false,
			valueField : 'url',
			name : 'url'
        },{
            xtype: 'textarea',
            fieldLabel: 'Description',
            labelWidth : 150,
    		labelAlign : 'left',
    		width : 450,
    		afterLabelTextTpl : required,
			allowBlank : false,
			valueField : 'applicationDesc',
			name : 'applicationDesc'
        }]
    }],
    buttons: [{
        text: 'Update',
        itemId : 'updateLinkBtn'
    }]
});