var myMask; 
var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';

Ext.define('UES.view.rsBooking.SearchRSBookingsForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.searchRSBookingsForm',
	bodyPadding : '10 10 0 10',
	autoScroll : true,
	height : 200,
	width : 400,
	loadMask : true,
	border : false,
	fieldDefaults : {
		msgTarget : 'side',
		labelWidth : 100
	},
	defaults : {
		overflow : 'auto',
		margin : '10 10 0 10'
	},
	url : 'BookRSAction!searchRSBooking.action',// The form will submit an AJAX
	layout : {
		type : 'anchor'
	},
	items : [{	
				xtype : 'fieldset',
				title : 'Search',
				collapsible : false,
				layout : 'anchor',
				defaults : {
					anchor : '100%'
				},
				items : [{
							xtype : 'combobox',
							fieldLabel : 'Booking Status :',
							store : 'BookingStatus',
							queryMode : 'local',
							displayField : 'name',
							valueField : 'code',
							name : 'bookingStatus',
							editable : false,
							emptyText : '--Please Select--',
							afterLabelTextTpl : required,
							allowBlank : false,
							blankText : 'This field is required'
						}, {
							xtype : 'combobox',
							fieldLabel : 'Region :',
							store : 'RegionWithALL',
							queryMode : 'local',
							displayField : 'name',
							valueField : 'code',
							name : 'region',
							editable : false,
							emptyText : '--Please Select--',
							afterLabelTextTpl : required,
							allowBlank : false,
							blankText : 'This field is required'
						}, {
							xtype : 'textfield',
							fieldLabel : 'Report Server:',
							name : 'uatRS',
							colspan : '2',
							allowBlank : true
						}]
			}, {
				xtype : 'button',
				text : 'Search',
				itemId : 'searchRS'
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