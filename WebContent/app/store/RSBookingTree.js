Ext.define('UES.store.RSBookingTree', {
			extend : 'Ext.data.TreeStore',
			root : {
				expanded : true,
				children : [{
							text : "New RS Request",
							iconCls : 'icon-booking',
							leaf : true
						}, {
							text : "Search RS Bookings",
							iconCls : 'icon-search',
							leaf : true
						}]
			}

		});