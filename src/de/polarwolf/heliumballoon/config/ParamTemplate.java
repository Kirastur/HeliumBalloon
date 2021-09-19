package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;

public enum ParamTemplate implements HeliumParam {

	RULE (false, "rule"),
	OSCILLATING (false, "oscillating"),
	ANIMAL (true, "animal"),
	ELEMENTS (true, "elements"),
	CUSTOM (false, "custom");
		
	
	private final String attributeName;
	private final boolean section;
	

	private ParamTemplate(boolean section, String attributeName) {
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