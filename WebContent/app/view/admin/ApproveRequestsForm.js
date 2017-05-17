var myMask;
var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.define('UES.view.admin.ApproveRequestsForm', {
	extend : 'Ext.Window',
	alias : 'widget.approveRequestsForm',
	bodyPadding : '10 10 10 10',
	autoScroll : true,
	height : 130,
	width : 380,
	border : false,
	modal : true,
	title : 'Approve Request',
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
		itemId : 'approveRequestFrm',
		url : 'admin/BookConfigAction!addToSchedules.action',
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
		}, {
			xtype : 'combobox',
			store : 'AvailableEnvironments',
			queryMode : 'local',
			displayField : 'name',
			valueField : 'name',
			width : 300,
			name : 'envName',
			editable : false,
			emptyText : ' --Please Select a Config to Allot--',
			afterLabelTextTpl : required,
			allowBlank : false,
			blankText : 'This field is required'
		} ]
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
			itemId : 'submitApproveRequest'
		} ]
	} ]

});