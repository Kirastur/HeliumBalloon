package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;

public enum ParamMinecart implements HeliumParam {

	X (false, "x"),
	Y (false, "y"),
	Z (false, "z"),
	LOAD(true, "load"),
	LOAD_OFFSET(false, "loadOffset"),
	CUSTOM (false, "custom");
	
	private final String attributeName;
	private final boolean section;
	

	private ParamMinecart(boolean section, String attributeName) {
		this.attributeName = attributeName;
		this.section = section;
	}


	@Override
	public boolean isSection() {
		return section;
	}


	@Override
	public String getAttributeName() {
		return attributeName;
	}
}