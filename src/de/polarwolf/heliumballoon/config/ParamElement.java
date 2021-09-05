package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;

public enum ParamElement implements HeliumParam {

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


	@Override
	public boolean isSection() {
		return false;
	}

	
	@Override
	public String getAttributeName() {
		return attributeName;
	}
}
