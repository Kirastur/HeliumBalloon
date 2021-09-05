package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;

public enum ParamWall implements HeliumParam {

	TEMPLATE ("template"),
	WORLDS ("worlds"),
	X ("x"),
	Y ("y"),
	Z ("z");
		
	private final String attributeName;
	

	private ParamWall(String attributeName) {
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