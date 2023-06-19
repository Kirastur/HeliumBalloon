package de.polarwolf.heliumballoon.balloons.rotators;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum ParamRotator implements HeliumParam {

	TEMPLATE(STRING, "template"),
	WORLD(STRING, "world"),
	X(STRING, "x"),
	Y(STRING, "y"),
	Z(STRING, "z"),
	BEHAVIOR(STRING, "behavior");

	private final HeliumParamType paramType;
	private final String attributeName;

	private ParamRotator(HeliumParamType paramType, String attributeName) {
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