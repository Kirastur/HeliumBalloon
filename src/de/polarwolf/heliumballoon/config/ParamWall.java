package de.polarwolf.heliumballoon.config;

public enum ParamWall {

	TEMPLATE ("template"),
	WORLDS ("worlds"),
	X ("x"),
	Y ("y"),
	Z ("z");
		
	private final String attributeName;
	

	private ParamWall(String attributeName) {
		this.attributeName = attributeName;
	}


	public String getAttributeName() {
		return attributeName;
	}
}