var myMask;
var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.define('UES.view.tickets.OpenTicketPanel', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.openTicketPanel',
	bodyPadding : '0 10 10 10',
	autoScroll : true,
	border : false,
	fieldDefaults : {
		msgTarget : 'side',
		labelWidth : 150
	},
	defaults : {
		overflow : 'auto',
		margin : '10 10 0 10'
	},
	layout : {
		type : 'table',
		columns : 2,
		overflow : 'auto'
	},
	items : [{
		xtype : 'component',
		itemId : 'openTicketHeader',
		html: '<p align="center"><span>Select the appropriate Application Instance & Environment from the lists below to create UES Incident or Service Requests.</span></p>',
		margin: '10 10 10 10',
	    height: 10,
	    colspan: 2,
	    border: true,
	    style: {
	        color: '#15428b'
	    }
	},{
		xtype: 'container',
		colspan: 2,
		layout: 'column',
		items:[{
				xtype : 'openTicket',
				itemId : 'openTicket',
				columnWidth: .7
			},{
				xtype : 'fieldset',
				title: 'Support Details',
				columnWidth: .3,
				items: [{
					xtype: 'component',
					html: "<p><span style='font-weight:bold'>DB Chat Channel :</span> GTO_RPL_FO_SL3 <br/><br/><span style='font-weight:bold'>Email : </span><a href='mailto:risk-it_uat_support@list.db.com'>risk-it_uat_support@list.db.com</a> <br/><br/><span style='font-weight:bold'>Hotline :</span> +65-64369497 </p>",
				    padding: 5
				}]		
			}]
	},{
		xtype: 'container',
		colspan: 2,
		layout: 'column',
		items:[{
			xtype : 'fieldset',
			columnWidth: .5,
			margin : '0 0 20 0',
			title:'UES Service Request',
			items:[{
				xtype : 'component',
				html: "<p>To request information or a change/task to be performed on a Risk Engine Environment, raise a Service Request (SR)...<br/><br/>"+			 
				"<span style='font-weight:bold'><u>Request Summary</u></span>: Brief summary of the information or change/task that is being requested and on which environment.<br/>"+
				"<span style='font-weight:bold'><u>Request Description</u></span>: Detailed explanation of the information or change/task that is being requested<br/>"+
				"<span style='font-weight:bold'><u>Target Delivery Date</u></span>: What date do you want or need this request to be fulfilled?<br/><br/>"+
				"*Any screenshots or supporting documents (including logs, scripts, etc.) can be attached to the ticket.</p>",
				padding: 5
			}]
		},{
			xtype : 'fieldset',
			columnWidth: .5,
			margin : '0 10 10 10',
			title:'UES Incident',
			items:[{
				xtype:'component',
				html: "<p>If you encounter an environment issue or service failure in a Risk Engine Environment, raise an Incident...<br/><br/>"+
				"<span><span style='font-weight:bold'><u>Incident Summary</u></span>: Brief summary of the error & symptoms experienced and on which environment(s).</span><br/>"+
				"<span style='font-weight:bold'><u>Incident Description</u></span>: Detailed explanation of the issue faced, the scenario that brought on the issue and the testing/project impact & deadlines.<br/><br/>"+ 
				"*Any screenshots or supporting documents (including logs, scripts, etc.) can be attached to the ticket.<br/>" +
				"** If multiple environments are impacted, be sure to note that in the Summary and description</p>",
				padding:5
			}]
		}]
	}]
});