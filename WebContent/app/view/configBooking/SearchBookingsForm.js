var myMask; 
var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';

Ext.define('UES.view.configBooking.SearchBookingsForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.searchBookingsForm',
	bodyPadding : '10 10 0 10',
	autoScroll : true,
	height : 230,
	width : 400,
	loadMask : true,
	border : false,
	fieldDefaults : {
		msgTarget : 'side',
		labelWidth : 80
	},
	defaults : {
		overflow : 'auto',
		margin : '10 10 0 10'
	},
	url : 'BookConfigAction!searchConfigBooking.action',// The form will submit an AJAX
	layout : {
		type : 'anchor'
	},
	items : [{
				xtype : 'fieldset',
				title : 'Environments',
				collapsible : true,
				layout : 'anchor',
				defaults : {
					anchor : '100%'
				},
				items : [{
							xtype : 'combobox',
							store : 'Environments',
							multiSelect : true,
							queryMode : 'local',
							displayField : 'name',
							valueField : 'name',
							name : 'envName',
							editable : false,
							emptyText : ' --Please Select--',
							afterLabelTextTpl : required,
							allowBlank : false,
							blankText : 'This field is required'
						}]
			}, {
				xtype : 'fieldset',
				collapsed : false,
				layout : 'anchor',
				defaults : {
					anchor : '100%'
				},
				items : [{
					xtype : 'checkbox',
					boxLabel : 'Show Expired Bookings',
					name : 'showExpiredBookings',
					inputValue : true,
					value : false,
					listeners : {
						change : function(self, newValue, oldValue, eOpts) {
							if (newValue == true) {
								this.up('form').getForm().findField('fromDate')
										.setValue(null);
								this.up('form').getForm().findField('fromDate')
										.hide();
								this.up('form').getForm().findField('toDate')
										.setValue(null);
								this.up('form').getForm().findField('toDate')
										.hide();
							} else {
								this.up('form').getForm().findField('fromDate')
										.setValue(null);
								this.up('form').getForm().findField('fromDate')
										.show();
								this.up('form').getForm().findField('toDate')
										.setValue(null);
								this.up('form').getForm().findField('toDate')
										.show();
							}

						}
					}
				}, {
					xtype : 'datefield',
					fieldLabel : 'From Date',
					name : 'fromDate',
					submitFormat : 'm-d-Y',
					format : 'd-m-Y'
				}, {
					xtype : 'datefield',
					fieldLabel : 'To Date',
					submitFormat : 'm-d-Y',
					format : 'd-m-Y',
					name : 'toDate'
				}]

			}, {
				xtype : 'button',
				text : 'Search',
				itemId : 'search'
			}, {
				xtype : 'button',
				text : 'Reset',
				handler : function() {
					this.up('form').getForm().reset();
				}
			}],
			listeners: {
				beforeaction : function() {
					myMask = new Ext.LoadMask(Ext.getBody(), {msg:"Please wait..."});
					myMask.show();
				},
				actioncomplete : function() {
					myMask.hide();
				},
				actionfailed : function() {
					myMask.hide();
				}
			}
	});