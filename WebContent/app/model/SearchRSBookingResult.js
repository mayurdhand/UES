Ext.define('UES.model.SearchRSBookingResult', {
	extend : 'Ext.data.Model',
	fields : [ 'prodRS', 'uatRS', 'projectName','manager', 'bookedBy', 'goLiveDateInDF',
			'decomDateInDF', 'reqSummary', 'region', 'goLiveDate', 'decomDate',
			'lineNo', 'emailAddress', 'notifies', 'business' ]
});