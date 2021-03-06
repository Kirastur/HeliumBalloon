package de.polarwolf.heliumballoon.config;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.SECTION;
import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum ParamMinecart implements HeliumParam {

	X(STRING, "x"),
	Y(STRING, "y"),
	Z(STRING, "z"),
	LOAD(SECTION, "load"),
	LOAD_OFFSET(STRING, "loadOffset"),
	CUSTOM(STRING, "custom");

	private final HeliumParamType paramType;
	private final String attributeName;

	private ParamMinecart(HeliumParamType paramType, String attributeName) {
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