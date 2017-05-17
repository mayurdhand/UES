var myMask;
var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.define('UES.view.tickets.SearchTicket', {
	extend : 'Ext.form.Panel',
	alias : 'widget.searchTicket',
	bodyPadding : '10 10 10 10',
	autoScroll : true,
	height : 170,
	width : 480,
	border : false,
	fieldDefaults : {
		msgTarget : 'side',
		labelWidth : 150
	},
	defaults : {
		overflow : 'auto',
		margin : '10 10 10 10'
	},
	layout : {
		type : 'table',
		columns : 1,
		overflow : 'auto'
	},
	items : [{
		xtype : 'fieldset',
		title : 'Search Ticket',
			items : [ {
	            xtype: 'radiogroup',
	            fieldLabel: 'Ticket Type',
	            cls: 'x-check-group-alt',
	            name: 'tickettype',
	            itemId: 'tickettype',
		            items: [
		                {boxLabel: 'Incident', name: 'type', inputValue: 1, checked: true},
		                {boxLabel: 'Service Request', name: 'type', inputValue: 2}
		            ]
	        }, {
				xtype : 'textfield',
				width : 400,
				fieldLabel : 'Ticket Number ',
				name : 'ticketNumber',
				afterLabelTextTpl : required,
				allowBlank : false
			},{
				xtype : 'toolbar',
				dock : 'bottom',
				ui : 'footer',
				items: ['->', {
					xtype : 'component',
					flex : 1
				}, {
					xtype : 'button',
					text : 'Search',
					itemId : 'ticketSearch',
					handler : function(cmp) {
						var ticketType = cmp.up('panel').down('radiogroup[itemId=tickettype]').getValue().type;
						var ticketNumber = cmp.up('panel').down('textfield[name=ticketNumber]').getValue().replace(/^\s+|\s+$/g, '');	
										
						if(!Ext.isEmpty(ticketNumber)) {
							var url = ''; 
							if(ticketType == 1){
								/*url = 'https://dbsymphony.gto.intranet.db.com/snc/incident_list.do?sysparm_query=number%3D'+ticketNumber;*/
								url = 'https://dbunity.gto.global.intranet.db.com/incident_list.do?sysparm_query=number%3D'+ticketNumber+'&sysparm_view=lite';
							}else if(ticketType == 2){
								url = 'https://dbsymphony.gto.intranet.db.com/snc/u_service_request_list.do?sysparm_query=number%3D'+ticketNumber;
								
							}
							window.open(url);
						} else {
							Ext.Msg.alert('Warning','Please enter ticket number.');
						}
						
					}
				}]
			} ]
	}]
});