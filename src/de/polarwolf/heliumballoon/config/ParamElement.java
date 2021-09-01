package de.polarwolf.heliumballoon.config;

public enum ParamElement {

	MATERIAL ("material"),
	HALF ("half"),
	SLAB ("slab"),
	FACE ("face"),
	CUSTOM ("custom"),
	X ("x"),
	Y ("y"),
	Z ("z");
		
	private final String attributeName;
	

	private ParamElement(String attributeName) {
		this.attributeName = attributeName;
	}


	public String getAttributeName() {
		return attributeName;
	}
}
