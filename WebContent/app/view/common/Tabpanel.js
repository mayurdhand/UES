Ext.define('UES.view.common.Tabpanel', {
			extend : 'Ext.tab.Panel',
			alias : 'widget.tabPanel',
			activeTab : 0,
			itemId : 'tabPanel',
			items : [{
						xtype : 'panel',
						title : 'New Ticket',
						iconCls : 'icon-admin',
						itemId : 'openTicketPanel',
						closable : false,
						autoScroll : true,
						items : [{
									xtype : 'openTicketPanel',
									itemId : 'openTicketPanel'
								}]

					}]

		});