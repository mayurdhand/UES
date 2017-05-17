var myMask; 
var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.define('UES.view.admin.RejectRequestsForm', {
	extend : 'Ext.Window',
	alias : 'widget.rejectRequestsForm',
	bodyPadding : '10 10 10 10',
	autoScroll : true,
	height : 200,
	width : 580,
	border : false,
	modal : true,
	title : 'Reject Request',
	fieldDefaults : {
		msgTarget : 'side',
		labelWidth : 150
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
		url : 'admin/BookConfigAction!changeRequestToRejected.action',
		bodyPadding : '5 5 5 5',
		items : [ {
			xtype : 'hiddenfield',
			name : 'baContact'
		}, {
			xtype : 'hiddenfield',
			name : 'lineNo'
		}, {
			xtype : 'hiddenfield',
			name : 'notifies'
		}, {
			xtype : 'hiddenfield',
			name : 'bookedBy'
		}, {
			xtype : 'hiddenfield',
			name : 'emailAddress'
		}, {
			xtype : 'hiddenfield',
			name : 'qaContact'		
		}, {
			xtype : 'hiddenfield',
			name : 'devContact'
		}, {
			xtype : 'hiddenfield',
			name : 'stakeholder'
		}, {
			xtype : 'hiddenfield',			
			name : 'region'
		}, {
			xtype : 'hiddenfield',
			name : 'projectName'			
		}, {
			xtype : 'hiddenfield',
			name : 'manager'
		}, {
			xtype : 'hiddenfield',
			name : 'business'
		}, {
			xtype : 'hiddenfield',
			name : 'fromDate'
		}, {
			xtype : 'hiddenfield',			
			name : 'toDate'
		}, {
			xtype : 'hiddenfield',
			name : 'reqSummary'
		},{
			xtype : 'textarea',
			width : 500,
			fieldLabel : 'Reject Reason :',
			name : 'reason',
			colspan : '2',
			afterLabelTextTpl : required,
			allowBlank : false
		}  ]
	} ],
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
			text : 'Submit',
			itemId : 'submitRejectRequest'
		}]
	} ]	

});