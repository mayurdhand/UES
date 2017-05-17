Ext.define('UES.model.SearchBookingResult', {
	extend : 'Ext.data.Model',
	fields : [ 'envName', 'projectName', 'manager', 'bookedBy', 'baContact',
			'qaContact', 'devContact', 'stakeholder', 'fromDateInDF',
			'toDateInDF', 'reqSummary', 'region', 'fromDate', 'toDate',
			'lineNo', 'emailAddress', 'notifies', 'business' ]
});