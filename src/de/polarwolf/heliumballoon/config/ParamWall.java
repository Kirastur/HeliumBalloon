package de.polarwolf.heliumballoon.config;

import static de.polarwolf.heliumballoon.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumParamType;

public enum ParamWall implements HeliumParam {

	TEMPLATE(STRING, "template"),
	WORLD(STRING, "world"),
	X(STRING, "x"),
	Y(STRING, "y"),
	Z(STRING, "z");

	private final HeliumParamType paramType;
	private final String attributeName;

	private ParamWall(HeliumParamType paramType, String attributeName) {
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