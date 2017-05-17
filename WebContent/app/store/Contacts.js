Ext.define('UES.store.Contacts', {
			extend : 'Ext.data.Store',
			model : 'UES.model.Contacts',
			proxy : {
				type : 'ajax',
				url : 'contacts/ContactsAction!showContacts.action',
				reader : {
					type : 'json',
					root : 'contactsDTOList'
				}
			},
			autoLoad : true
		});