package de.polarwolf.heliumballoon.config;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.SECTION;
import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum ParamTemplate implements HeliumParam {

	WORLDSET(STRING, "worldset"),
	RULE(STRING, "rule"),
	OSCILLATING(STRING, "oscillating"),
	LIVING(SECTION, "living"),
	ELEMENTS(SECTION, "elements"),
	MINECART(SECTION, "minecart"),
	ARMORSTAND(SECTION, "armorstand"),
	CUSTOM(STRING, "custom");

	private final HeliumParamType paramType;
	private final String attributeName;

	private ParamTemplate(HeliumParamType paramType, String attributeName) {
		this.paramType = paramType;
		this.attributeName = attributeName;
	}

	@Override
	public boolean isType(HeliumParamType testParamType) {
		return testParamType == paramType;
	}

	@Override
	public String getAttributeName() {
		return attributeName;
	}

}