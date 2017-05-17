Ext.define('UES.view.admin.RSExpiryPanel', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.rsExpiryPanel',
	itemId : 'rsExpiryPanel',
	closable : false,
	bodyPadding : '0 10 0 10',
	height : 80,
	width : 385,
	border : false,
	fieldDefaults : {
		msgTarget : 'side',
		labelWidth : 80
	},
	defaults : {
		overflow : 'auto',
		margin : '0 10 0 10'
	},
	layout : {
		type : 'anchor'
	},
	autoScroll : true,
	items : [{
		xtype : 'fieldset',
		title : 'Report Server Expiry Checker',
		collapsible : false,
		layout : 'anchor',
		items : [{
				xtype : 'button',
				text : 'Start',
				itemId : 'startRSEx',
				margin : '0 10 0 10'
			},
			{
				xtype : 'button',
				text : 'Stop',
				itemId : 'stopRSEx',
				margin : '0 10 0 10'
			},
			{
				xtype : 'button',
				text : 'Status',
				itemId : 'statusRSEx',
				margin : '0 0 0 10'
			}]
	}]
});