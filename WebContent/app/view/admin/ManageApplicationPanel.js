Ext.define('UES.view.admin.ManageApplicationPanel', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.manageApplicationPanel',
	itemId : 'manageApplicationPanel',
	closable : false,
	border : false,
	autoScroll : true,
	items : [{
				xtype : 'fileUploadForm'
			},
			{
				xtype : 'fileDownloadForm'
			},
			{
				xtype : 'riskEngineExpiryPanel'
			},
			{
				xtype : 'rsExpiryPanel'
			}]
	});