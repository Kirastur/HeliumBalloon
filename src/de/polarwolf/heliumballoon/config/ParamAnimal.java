package de.polarwolf.heliumballoon.config;

public enum ParamAnimal {

	TYPE ("type"),
	X ("x"),
	Y ("y"),
	Z ("z"),
	LEASH ("leash"),
	HIDDEN ("hidden"),
	CATTYPE ("cattype"),
	COLOR ("color"),
	CUSTOM ("custom");
	
		
	private final String attributeName;
	

	private ParamAnimal(String attributeName) {
		this.attributeName = attributeName;
	}


	public String getAttributeName() {
		return attributeName;
	}
}
