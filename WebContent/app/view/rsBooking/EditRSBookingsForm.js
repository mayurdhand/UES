var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.define('UES.view.rsBooking.EditRSBookingsForm', {
	extend : 'Ext.Window',
	alias : 'widget.editRSBookingsForm',
	id : 'editRSBookingsForm',
	bodyPadding : '10 10 10 10',
	autoScroll : true,
	height : 420,
	width : 'auto',
	border : false,
	modal : true,
	title : 'Edit Booking',
	fieldDefaults : {
		msgTarget : 'side'
	},
	defaults : {
		anchor : '100%',
		overflow : 'auto',
		margin : '5 5 5 5'
	},
	layout : {
		type : 'table',
		columns : 2,
		overflow : 'auto'
	},
	items : [ {
		xtype : 'form',
		itemId : 'editRSBookingFrm',
		url : 'admin/BookRSAction!updateBooking.action',
		listeners: {
			actioncomplete : function(self) {
				var button = self.owner.up('window').down('button[itemId=updateRSBooking]');
				button.enable();
			},
			actionfailed : function(self) {
				var button = self.owner.up('window').down('button[itemId=updateRSBooking]');
				button.enable();
			}
		},
		bodyPadding : '5 5 5 5',
		items : [ {
			xtype : 'hiddenfield',
			name : 'lineNo'
		},{
			xtype : 'hiddenfield',
			name : 'region'
		}, {
			xtype : 'hiddenfield',
			name : 'uatRS'
		}, {
			xtype : 'textfield',
			fieldLabel : 'PROD RS to clone :',
			name : 'prodRS',
			labelWidth : 180,
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textfield',
			labelWidth : 180,
			fieldLabel : 'Booked By :',
			name : 'bookedBy',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'combobox',
			store : 'Business',
			queryMode : 'local',
			displayField : 'name',
			valueField : 'code',
			fieldLabel : 'Business :',
			labelWidth : 180,
			name : 'business',
			afterLabelTextTpl : required,
			allowBlank : false,
			editable : false,
			emptyText : ' --Please Select--'
		}, {
			xtype : 'textfield',
			labelWidth : 180,
			fieldLabel : 'Email :',
			name : 'emailAddress',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'datefield',
			fieldLabel : 'Go Live Date',
			name : 'goLiveDate',
			labelWidth : 180,
			format : 'd-m-Y',
			submitFormat : 'd-m-Y',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'datefield',
			fieldLabel : 'Decommissioning Date :',
			format : 'd-m-Y',
			labelWidth : 180,
			submitFormat : 'd-m-Y',
			name : 'decomDate',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textfield',
			labelWidth : 180,
			fieldLabel : 'Emails to Notify (comma\',\' seperated) :',
			name : 'notifies',
			allowBlank : true
		}, {
			xtype : 'textfield',
			labelWidth : 180,
			fieldLabel : 'Project :',
			name : 'projectName',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textfield',
			labelWidth : 180,
			fieldLabel : 'Project Manager :',
			name : 'manager',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textarea',
			labelWidth : 180,
			width : 500,
			fieldLabel : 'RS Testing Summary :',
			name : 'reqSummary',
			colspan : '2',
			afterLabelTextTpl : required,
			allowBlank : false
		},  ]
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
			itemId : 'updateRSBooking'
		} ]
	} ]

});