Ext.define('UES.view.configBooking.SearchBookingPanel', {
			extend : 'Ext.panel.Panel',
			alias : 'widget.searchBookingPanel',
			title : 'Search RE Bookings',
			itemId : 'searchBookingsPanel',
			closable : true,
			border : true,
			autoScroll : true,
			items : [{
						xtype : 'searchBookingsForm'
					}, {
						xtype : 'bookingResultsGrid'
					}]
		});