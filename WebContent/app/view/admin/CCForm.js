var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.define('UES.view.admin.CCForm', {
    extend: 'Ext.Window',    
    alias : 'widget.ccForm',
    bodyPadding : '5 5 5 5',
	autoScroll : true,
	height : 230,
	width : 510,
	border : false,
	modal: true,
	title: 'Add Config',
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
    	itemId: 'addCCFrm',
    	url : 'admin/CCAction!addConfig.action',
    	height : 140,
    	width : 470,
    	bodyPadding : '5 5 5 5',
    	items: [{
    		xtype: 'textfield',
            fieldLabel: 'Config',
            labelWidth : 150,
    		labelAlign : 'left',
    		width : 450,
    		afterLabelTextTpl : required,
			allowBlank : false,
			valueField : 'config',
			name : 'config'
        },{
            xtype: 'textfield',
            fieldLabel: 'Database',
            labelWidth : 150,
    		labelAlign : 'left',
    		width : 450,
    		afterLabelTextTpl : required,
			allowBlank : false,
			valueField : 'database',
			name : 'database'
        },{
            xtype: 'textfield',
            fieldLabel: 'CC Host',
            labelWidth : 150,
    		labelAlign : 'left',
    		width : 450,
    		afterLabelTextTpl : required,
			allowBlank : false,
			valueField : 'ccHost',
			name : 'ccHost'
        },{
            xtype: 'textfield',
            fieldLabel: 'CC Port',
            labelWidth : 150,
    		labelAlign : 'left',
    		width : 450,
    		afterLabelTextTpl : required,
			allowBlank : false,
			valueField : 'ccPort',
			name : 'ccPort'
        }]
    }],
    buttons: [{
        text: 'Submit',
        itemId : 'addCCBtn'
    },{
        text: 'Reset',
        itemId: 'addCCFrmReset'
    }]
});