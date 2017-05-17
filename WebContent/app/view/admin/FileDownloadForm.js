Ext.define('UES.view.admin.FileDownloadForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.fileDownloadForm',
	bodyPadding : '0 10 0 10',
	autoScroll : true,
	height : 100,
	width : 400,
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
	items : [{
				xtype : 'fieldset',
				title : 'Download File',
				collapsible : false,
				layout : 'anchor',
				items : [{
					xtype : 'combobox',
					queryMode : 'local',
					displayField : 'fileType',
					valueField : 'fileType',
					name : 'fileType1',
					store : 'AppFileType',
					editable : false,
					allowBlank : false,
					blankText : 'This field is required',
					emptyText : ' --Please select file type--',
					anchor: '100%'
				},{
					xtype : 'button',
					text : 'Download',
					itemId : 'download'
				}]
			}]		
	});