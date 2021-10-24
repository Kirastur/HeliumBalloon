package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumParamType;
import static de.polarwolf.heliumballoon.helium.HeliumParamType.*;

public enum ParamSection implements HeliumParam {

	WORLDS (SECTION, "worlds"),
	RULES (SECTION, "rules"),
	TEMPLATES (SECTION, "templates"),
	WALLS (SECTION, "walls"),
	ROTATORS (SECTION, "rotators"),
	GUI (SECTION, "gui");

	private final HeliumParamType paramType;
	private final String attributeName;
	

	private ParamSection(HeliumParamType paramType, String attributeName) {
		this.paramType = paramType;
		this.attributeName = attributeName;
	}


	@Override
	public boolean isType(HeliumParamType testParamType) {
		return testParamType==paramType;
	}


	@Override
	public String getAttributeName() {
		return attributeName;
	}

}
