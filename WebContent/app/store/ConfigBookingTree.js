Ext.define('UES.store.ConfigBookingTree', {
			extend : 'Ext.data.TreeStore',
			root : {
				expanded : true,
				children : [{
							text : "Reserve RE CONFIG",
							iconCls : 'icon-booking',
							leaf : true
						}, {
							text : "Search RE Bookings",
							iconCls : 'icon-search',
							leaf : true
						}]
			}

		});