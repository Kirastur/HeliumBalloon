package de.polarwolf.heliumballoon.elements.minecart;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.SECTION;
import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum MinecartParam implements HeliumParam {

	CUSTOM(STRING, "custom"),
	X(STRING, "x"),
	Y(STRING, "y"),
	Z(STRING, "z"),
	LOAD(SECTION, "load"),
	LOAD_OFFSET(STRING, "loadOffset");

	private final HeliumParamType paramType;
	private final String attributeName;

	private MinecartParam(HeliumParamType paramType, String attributeName) {
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