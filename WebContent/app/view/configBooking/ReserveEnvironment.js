var myMask;
var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.define('UES.view.configBooking.ReserveEnvironment', {
	extend : 'Ext.form.Panel',
	alias : 'widget.reserveEnvForm',
	bodyPadding : '10 10 10 10',
	autoScroll : true,
	height : 440,
	width : 780,
	border : false,
	fieldDefaults : {
		msgTarget : 'side',
		labelWidth : 150
	},
	defaults : {
		overflow : 'auto',
		margin : '10 10 10 10'
	},
	url : 'BookConfigAction!bookConfig.action',// The form will submit an AJAX request to this URL when submitted
	layout : {
		type : 'table',
		columns : 2,
		overflow : 'auto'
	},
	items : [{
	xtype : 'fieldset',
	title : 'Booking Details',
	items : [ {
		xtype : 'textfield',
		fieldLabel : 'Booked By :',
		name : 'bookedBy',
		afterLabelTextTpl : required,
		allowBlank : false
	}, {
		xtype : 'textfield',
		fieldLabel : 'Your Email Address :',
		name : 'emailAddress',
		afterLabelTextTpl : required,
		allowBlank : false
	}, {
		xtype : 'textfield',
		fieldLabel : 'Emails to Notify (comma\',\' seperated) :',
		name : 'notifies',
		allowBlank : true
	}, {
		xtype : 'combobox',
		store : 'Region',
		queryMode : 'local',
		displayField : 'name',
		valueField : 'code',
		fieldLabel : 'Region :',
		name : 'region',
		afterLabelTextTpl : required,
		allowBlank : false,
		editable : false,
		emptyText : ' --Please Select--'
	} ]
},
      {
		xtype : 'fieldset',
		title : 'Contact Details',
		items : [ {
			xtype : 'textfield',
			fieldLabel : 'BA Contact :',
			name : 'baContact',
			afterLabelTextTpl : required,
			allowBlank : false,
			blankText : 'This field is required'
		}, {
			xtype : 'textfield',
			fieldLabel : 'QA Contact :',
			name : 'qaContact',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textfield',
			fieldLabel : 'DEV Contact :',
			name : 'devContact',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textfield',
			fieldLabel : 'Senior Stakeholder :',
			name : 'stakeholder',
			afterLabelTextTpl : required,
			allowBlank : false
		} ]
	}, {
		xtype : 'fieldset',
		title : 'Project Details',
		items : [ {
			xtype : 'textfield',
			fieldLabel : 'Project Name :',
			name : 'projectName',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'textfield',
			fieldLabel : 'Project Manager :',
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
			name : 'business',
			afterLabelTextTpl : required,
			allowBlank : false,
			editable : false,
			emptyText : ' --Please Select--'
		} ]
	}, {
		xtype : 'fieldset',
		title : 'Date',
		items : [ {
			xtype : 'datefield',
			fieldLabel : 'From Date',
			name : 'fromDate',
			format : 'd-m-Y',
			submitFormat : 'd-m-Y',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'datefield',
			fieldLabel : 'To Date',
			format : 'd-m-Y',
			submitFormat : 'd-m-Y',
			name : 'toDate',
			afterLabelTextTpl : required,
			allowBlank : false
		} ]
	}, {
		xtype : 'textarea',
		width : 700,
		fieldLabel : 'Requirement Summary :',
		name : 'reqSummary',
		colspan : '2',
		afterLabelTextTpl : required,
		emptyText:'If you want to book any specific CONFIG then you can mention it here.',
		allowBlank : false
	}

	],
	dockedItems : [ {
		xtype : 'toolbar',
		dock : 'bottom',
		ui : 'footer',
		margin : '0 50 0 0',
		defaults : {
			minWidth : 60
		},
		items : [ {
			xtype : 'component',
			flex : 1
		}, {
			xtype : 'button',
			text : 'Submit',
			itemId : 'save'
		}, {
			xtype : 'button',
			text : 'Cancel',
			handler : function() {
				this.up('form').getForm().reset();
			}
		} ]
	} ],
	listeners: {
			beforeaction : function() {
				myMask = new Ext.LoadMask(Ext.getBody(), {msg:"Please wait..."});
				myMask.show();
			},
			actioncomplete : function() {
				var bbar = this.getDockedItems('toolbar[dock="bottom"]')[0];
				var button = bbar.getComponent('save'); 
				button.enable();
				myMask.hide();
			},
			actionfailed : function() {
				var bbar = this.getDockedItems('toolbar[dock="bottom"]')[0];
				var button = bbar.getComponent('save'); 
				button.enable();
				myMask.hide();
			}
		}
});