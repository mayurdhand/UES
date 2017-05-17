var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.define('UES.view.configBooking.EditBookingsForm', {
	extend : 'Ext.Window',
	alias : 'widget.editBookingsForm',
	id : 'editBookingsForm',
	bodyPadding : '10 10 10 10',
	autoScroll : true,
	height : 490,
	width : 580,
	border : false,
	modal : true,
	title : 'Edit Booking',
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
		itemId : 'editBookingFrm',
		url : 'admin/BookConfigAction!updateBooking.action',
		listeners: {
			actioncomplete : function(self) {
				var button = self.owner.up('window').down('button[itemId=updateBooking]');
				button.enable();
			},
			actionfailed : function(self) {
				var button = self.owner.up('window').down('button[itemId=updateBooking]');
				button.enable();
			}
		},
		bodyPadding : '5 5 5 5',
		items : [{
			xtype : 'hiddenfield',
			name : 'envName'
		}, {
			xtype : 'hiddenfield',
			name : 'lineNo'
		}, {
			xtype : 'hiddenfield',
			name : 'region'
		}, {
			xtype : 'textfield',
			fieldLabel : 'Project Name :',
			labelWidth : 140,
			name : 'projectName',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textfield',
			fieldLabel : 'PM Contact :',
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
			xtype : 'textfield',
			fieldLabel : 'BA Contact :',
			labelWidth : 140,
			name : 'baContact',
			afterLabelTextTpl : required,
			allowBlank : false,
			blankText : 'This field is required'
		}, {
			xtype : 'textfield',
			fieldLabel : 'QA Contact :',
			labelWidth : 140,
			name : 'qaContact',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textfield',
			fieldLabel : 'DEV Contact :',
			labelWidth : 140,
			name : 'devContact',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textfield',
			fieldLabel : 'Senior Stakeholder :',
			labelWidth : 140,
			name : 'stakeholder',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textfield',
			fieldLabel : 'Booked By :',
			labelWidth : 140,
			name : 'bookedBy',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textfield',
			fieldLabel : 'Email :',
			labelWidth : 140,
			name : 'emailAddress',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'datefield',
			fieldLabel : 'From Date',
			labelWidth : 140,
			name : 'fromDate',
			format : 'd-m-Y',
			submitFormat : 'd-m-Y',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'datefield',
			fieldLabel : 'To Date',
			labelWidth : 140,
			format : 'd-m-Y',
			submitFormat : 'd-m-Y',
			name : 'toDate',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textfield',
			labelWidth : 140,
			fieldLabel : 'Emails to Notify (comma\',\' seperated) :',
			name : 'notifies',
			allowBlank : true
		}, {
			xtype : 'textarea',
			width : 500,
			labelWidth : 140,
			fieldLabel : 'Requirement Summary :',
			name : 'reqSummary',
			colspan : '2',
			afterLabelTextTpl : required,
			allowBlank : false
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
			text : 'Update',
			itemId : 'updateBooking'
		} ]
	} ]

});