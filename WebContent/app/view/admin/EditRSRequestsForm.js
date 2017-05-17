var myMask; 
var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.define('UES.view.admin.EditRSRequestsForm', {
	extend : 'Ext.Window',
	alias : 'widget.editRSRequestsForm',
	bodyPadding : '10 10 10 10',
	autoScroll : true,
	height : 390,
	width : 580,
	border : false,
	modal : true,
	title : 'Edit RS Booking Request',
	fieldDefaults : {
		msgTarget : 'side',
		labelWidth : 300
	},
	defaults : {
		anchor : '100%',
		overflow : 'auto',
		margin : '10 10 10 10'
	},
	layout : {
		type : 'table',
		columns : 2,
		overflow : 'auto'
	},
	items : [ {
		xtype : 'form',
		itemId : 'editRequestFrm',
		url : 'admin/BookRSAction!updateRequest.action',
		bodyPadding : '5 5 5 5',
		items : [{
			xtype : 'hiddenfield',
			name : 'lineNo'
		},{
			xtype : 'hiddenfield',
			name : 'bookedBy'
		},{
			xtype : 'hiddenfield',
			name : 'emailAddress'
		}, {
			xtype : 'textfield',
			fieldLabel : 'PROD RS to Clone :',
			labelWidth : 140,
			name : 'prodRS',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textfield',
			fieldLabel : 'Project Name :',
			labelWidth : 140,
			name : 'projectName',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textfield',
			fieldLabel : 'Project Manager :',
			labelWidth : 140,
			name : 'manager',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'combobox',
			store : 'Business',
			queryMode : 'local',
			displayField : 'name',
			valueField : 'code',
			fieldLabel : 'Business :',
			labelWidth : 140,
			name : 'business',
			afterLabelTextTpl : required,
			allowBlank : false,
			editable : false,
			emptyText : ' --Please Select--'
		}, {
			xtype : 'combobox',
			store : 'Region',
			labelWidth : 140,
			queryMode : 'local',
			displayField : 'name',
			valueField : 'code',
			fieldLabel : 'Region :',
			name : 'region',
			afterLabelTextTpl : required,
			allowBlank : false,
			editable : false,
			emptyText : ' --Please Select--'
		}, {
			xtype : 'datefield',
			fieldLabel : 'Go Live Date :',
			labelWidth : 140,
			name : 'goLiveDate',
			format : 'd-m-Y',
			submitFormat : 'd-m-Y',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'datefield',
			fieldLabel : 'Decommissioning Date :',
			name : 'decomDate',
			labelWidth : 140,
			format : 'd-m-Y',
			submitFormat : 'd-m-Y',			
			afterLabelTextTpl : required,
			allowBlank : false
		},{
			xtype : 'textfield',
			labelWidth : 140,
			fieldLabel : 'Emails to Notify (comma\',\' seperated) :',
			name : 'notifies',
			allowBlank : true
		},{
			xtype : 'textarea',
			width : 500,
			labelWidth : 140,
			fieldLabel : 'Testing Summary :',
			name : 'reqSummary',
			colspan : '2',
			afterLabelTextTpl : required,
			allowBlank : false
		}]
	}],
	dockedItems : [ {
		xtype : 'toolbar',
		dock : 'bottom',
		ui : 'footer',
		margin : '0 20 0 0',
		defaults : {
			minWidth : 60
		},
		items : [ {
			xtype : 'component',
			flex : 1
		}, {
			xtype : 'button',
			text : 'Update',
			itemId : 'updateRSRequest'
		} ]
	}]
	

});