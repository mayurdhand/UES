Ext.define('UES.view.admin.RiskEngineExpiryPanel', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.riskEngineExpiryPanel',
	itemId : 'riskEngineExpiryPanel',
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
		title : 'Risk Engine Expiry Checker',
		collapsible : false,
		layout : 'anchor',
		items : [{
				xtype : 'button',
				text : 'Start',
				itemId : 'start',
				margin : '0 10 0 10'
			},
			{
				xtype : 'button',
				text : 'Stop',
				itemId : 'stop',
				margin : '0 10 0 10'
			},
			{
				xtype : 'button',
				text : 'Status',
				itemId : 'status',
				margin : '0 0 0 10'
			}]
	}]
});