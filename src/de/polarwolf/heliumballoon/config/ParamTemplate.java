package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumParamType;
import static de.polarwolf.heliumballoon.helium.HeliumParamType.*;

public enum ParamTemplate implements HeliumParam {

	RULE (STRING, "rule"),
	OSCILLATING (STRING, "oscillating"),
	LIVING (SECTION, "living"),
	ELEMENTS (SECTION, "elements"),
	MINECART (SECTION, "minecart"),
	ARMORSTAND (SECTION, "armorstand"),
	CUSTOM (STRING, "custom");
		
	private final HeliumParamType paramType;
	private final String attributeName;
	

	private ParamTemplate(HeliumParamType paramType, String attributeName) {
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