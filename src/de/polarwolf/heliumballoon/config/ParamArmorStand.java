package de.polarwolf.heliumballoon.config;

import static de.polarwolf.heliumballoon.helium.HeliumParamType.SECTION;
import static de.polarwolf.heliumballoon.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumParamType;

public enum ParamArmorStand implements HeliumParam {

	X(STRING, "x"),
	Y(STRING, "y"),
	Z(STRING, "z"),
	LOAD(SECTION, "load"),
	CUSTOM(STRING, "custom");

	private final HeliumParamType paramType;
	private final String attributeName;

	private ParamArmorStand(HeliumParamType paramType, String attributeName) {
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