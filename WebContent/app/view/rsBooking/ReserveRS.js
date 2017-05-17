var myMask;
var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.define('UES.view.rsBooking.ReserveRS', {
	extend : 'Ext.form.Panel',
	alias : 'widget.reserveRSForm',
	bodyPadding : '10 10 10 10',
	autoScroll : true,
	height : 580,
	width : 580,
	border : false,
	fieldDefaults : {
		msgTarget : 'side',
		labelWidth : 150
	},
	defaults : {
		overflow : 'auto',
		margin : '10 10 10 10'
	},
	url : 'BookRSAction!bookRS.action',// The form will submit an AJAX request to this URL when submitted
	layout : {
		type : 'table',
		columns : 1,
		overflow : 'auto'
	},
	items : [ 
, {
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
		title : 'RS Clone Details and Dates',
		items : [ {
			xtype : 'textfield',
			fieldLabel : 'PROD RS to clone from :',
			name : 'prodRS',
			afterLabelTextTpl : required,
			allowBlank : false,
			blankText : 'This field is required'
		}, {
			xtype : 'datefield',
			fieldLabel : 'Go Live Date',
			name : 'goLiveDate',
			format : 'd-m-Y',
			submitFormat : 'd-m-Y',
			afterLabelTextTpl : required,
			allowBlank : false
		}, {
			xtype : 'datefield',
			fieldLabel : 'Decommissioning Date :',
			format : 'd-m-Y',
			submitFormat : 'd-m-Y',
			name : 'decomDate',
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
		xtype : 'textarea',
		width : 500,
		fieldLabel : 'RS Testing Summary :',
		name : 'reqSummary',
		colspan : '2',
		afterLabelTextTpl : required,
		emptyText:'Please mention what are you testing in not more than 500 characters.',
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
			itemId : 'saveRS'
		}, {
			xtype : 'button',
			text : 'Cancel',
			handler : function() {
				this.up('form').getForm().reset();
			}
		} ]
	} ]	,listeners: {
			beforeaction : function() {
				myMask = new Ext.LoadMask(Ext.getBody(), {msg:"Please wait..."});
				myMask.show();
			},
			actioncomplete : function() {
				var bbar = this.getDockedItems('toolbar[dock="bottom"]')[0];
				var button = bbar.getComponent('saveRS'); 
				button.enable();
				myMask.hide();
			},
			actionfailed : function() {
				var bbar = this.getDockedItems('toolbar[dock="bottom"]')[0];
				var button = bbar.getComponent('saveRS'); 
				button.enable();
				myMask.hide();
			}
		}
});