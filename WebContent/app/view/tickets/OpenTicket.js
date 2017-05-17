var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.define('UES.view.tickets.OpenTicket', {
	extend : 'Ext.form.Panel',
	alias : 'widget.openTicket',
	bodyPadding : '0 10 10 0',
	autoScroll : true,
	border : false,
	fieldDefaults : {
		msgTarget : 'side',
		labelWidth : 150
	},
	defaults : {
		overflow : 'auto',
		margin : '0 10 10 0'
	},
	layout : {
		type : 'table',
		columns : 1
	},
	items : [{
		xtype : 'fieldset',
		title : 'New Ticket',
			items : [ {
	            xtype: 'radiogroup',
	            fieldLabel: 'Ticket Type',
	            cls: 'x-check-group-alt',
	            name: 'tickettype',
	            itemId: 'tickettype',
		            items: [
		            	{boxLabel: 'Service Request', name: 'type', inputValue: 2,checked: true},
		                {boxLabel: 'Incident', name: 'type', inputValue: 1}		                
		            ]
	        }, {
				xtype : 'datefield',
				fieldLabel : 'Target Delivery Date',
				name : 'deliveryDate',
				itemId : 'deliveryDate',
				format : 'Y-m-d',
				submitFormat : 'Y-m-d',
				afterLabelTextTpl : required,
				allowBlank : false,
				editable : false,
				width : 400
			}, {
				xtype : 'textfield',
				width : 400,
				fieldLabel : 'Your Email ',
				name : 'email',
				itemId : 'email',
				afterLabelTextTpl : required,
				hidden:true,
				allowBlank : false,
				vtype: 'email',
				vtypeText: 'Invalid Email'
			}, {
				xtype : 'combobox',
				store : 'Application',
				queryMode : 'local',
				displayField : 'name',
				valueField : 'code',
				fieldLabel : 'Application ',
				name : 'application',
				itemId : 'application',
				afterLabelTextTpl : required,
				allowBlank : false,
				editable : false,
				width : 400,
				emptyText : ' --Please Select--'
			},{
				xtype : 'combobox',
				store : 'Ticket_Environments',
				queryMode : 'local',
				displayField : 'name',
				valueField : 'code',
				fieldLabel : 'Environment ',
				name : 'environment',
				itemId : 'environment',
				afterLabelTextTpl : required,
				allowBlank : false,
				editable : false,
				width : 400,
				emptyText : ' --Please Select--'
			}, {
				xtype : 'textfield',
				width : 650,
				fieldLabel : 'Request Summary ',
				name : 'summary',
				afterLabelTextTpl : required,
				allowBlank : false
			}, {
				xtype : 'textarea',
				width : 650,
				fieldLabel : 'Request Description ',
				name : 'reqSummary',
				colspan : '2',
				afterLabelTextTpl : required,
				allowBlank : false
			},  {
	            xtype: 'toolbar',
	            border:false,
	            id:'toolsave',
	            ui : 'footer',
	            items: ['->',{
					xtype : 'button',
					ctCls: 'x-toolbar-grey-btn',
					text : 'Submit',
					itemId : 'ticketSave',
				}]
          }]
	}]
});