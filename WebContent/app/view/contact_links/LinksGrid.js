Ext.define('UES.view.contact_links.LinksGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.linksGrid',
	collapsible : false,
	preventHeader: true,
	border:false,
	margin : '10 20 20 10',
	width : 'auto',
	title : 'Links',
	height : 600,
	loadMask : true,
	columnLines : true,
	iconCls : 'icon-grid',
	layout : 'fit',
	store : 'Links',	
	viewConfig : {
		emptyText : '<div class="x-grid-empty">No records found.</div>'
	},
	columns : [ {
		text : "Application",
		flex : 30 / 100,
		dataIndex : 'applicationName'
	}, {
		text : "Link",
		flex : 70 / 100,
		dataIndex : 'url',
		renderer : function(url, myDontKnow, myRecord) {
			return '<a href="' + url + '">' + url + '</a>';
		}
	} ],
	tbar : [ {
		tooltip : 'Add',
		xtype : 'button',
		iconCls : 'icon-add',
		itemId : 'addLink',
		handler : function() {
			var loadmask = new Ext.LoadMask(this.up('grid'), {
				msg : "Please wait..."
			});
			loadmask.show();
			var view = Ext.widget('linksForm');
			view.show();
			loadmask.hide();
		}
	},{
		tooltip : 'Delete',
		xtype : 'button',
		iconCls : 'icon-delete',
		itemId : 'deleteLink'
	},{
		tooltip: 'Edit',
        xtype: 'button',
        iconCls: 'icon-edit',
        itemId: 'updateLink'    	
    } ],
	plugins : [ {
		ptype : 'rowexpander',
		expandOnDblClick : false,
		rowBodyTpl : [ '<p>{applicationDesc}</p><br>' ]
	} ]

});
