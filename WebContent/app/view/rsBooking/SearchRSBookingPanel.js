Ext.define('UES.view.rsBooking.SearchRSBookingPanel', {
			extend : 'Ext.panel.Panel',
			alias : 'widget.searchRSBookingPanel',
			title : 'Search RS Bookings',
			itemId : 'searchRSBookingsPanel',
			closable : true,
			border : true,
			autoScroll : true,
			items : [{
						xtype : 'searchRSBookingsForm'
					}, {
						xtype : 'rsBookingResultsGrid'
					}]
		});