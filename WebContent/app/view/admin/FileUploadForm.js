Ext.define('UES.view.admin.FileUploadForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.fileUploadForm',
	bodyPadding : '10 10 0 10',
	autoScroll : true,
	height : 150,
	width : 400,
	border : false,
	fileUpload: true,
	fieldDefaults : {
		msgTarget : 'side',
		labelWidth : 80
	},
	defaults : {
		overflow : 'auto',
		margin : '10 10 0 10'
	},
	url : 'admin/FileUploadAction!upload.action',// The form will submit an AJAX
	layout : {
		type : 'anchor'
	},
	items : [{
				xtype : 'fieldset',
				title : 'Upload File',
				collapsible : false,
				layout : 'anchor',
				items : [{
					xtype : 'combobox',
					queryMode : 'local',
					displayField : 'fileType',
					valueField : 'fileType',
					name : 'fileType',
					store : 'AppFileType',
					editable : false,
					allowBlank : false,
					blankText : 'This field is required',
					emptyText : ' --Please select file type--',
					anchor: '100%'
				},{
		            xtype: 'filefield',
		            id: 'formfile',
		            emptyText: 'Select a file',
		            name: 'file',
		            buttonText: 'Browse',
		            msgTarget: 'side',
		            anchor: '100%'
		            
		        }, {
					xtype : 'button',
					text : 'Upload',
					itemId : 'upload'
				}]
			}]		
	});