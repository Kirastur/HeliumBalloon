package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;

public enum ParamSection implements HeliumParam {

	WORLDS (true, "worlds"),
	RULES (true, "rules"),
	TEMPLATES (true, "templates"),
	WALLS (true, "walls"),
	GUI (true, "gui");

	private final String attributeName;
	private final boolean section;
	

	private ParamSection(boolean section, String attributeName) {
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
