package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;

public enum ParamAnimal implements HeliumParam {

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
	
	
	@Override
	public boolean isSection() {
		return false;
	}


	@Override
	public String getAttributeName() {
		return attributeName;
	}
}
