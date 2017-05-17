package com.db.riskit.utils.properties;



public class PropertyConfigurationFactory extends Properties {

	private static final long serialVersionUID = 1L;
	private static PropertyConfigurationFactory _instance;

	PropertyConfigurationFactory() {
		super();
	}

	public static PropertyConfigurationFactory getInstance() {
		if (_instance == null ) {
			_instance = new PropertyConfigurationFactory();
		} 
		return _instance;
	}

	public static void refresh() {
		_instance = null;
		_instance = new PropertyConfigurationFactory();
	}

}