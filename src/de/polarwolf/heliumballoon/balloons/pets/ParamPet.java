package de.polarwolf.heliumballoon.balloons.pets;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum ParamPet implements HeliumParam {

	TEMPLATES(STRING, "templates"),
	BEHAVIOR(STRING, "behavior"),
	TITLE(STRING, "title"),
	DESCRIPTION(STRING, "description"),
	ICON(STRING, "icon");

	private final HeliumParamType paramType;
	private final String attributeName;

	private ParamPet(HeliumParamType paramType, String attributeName) {
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
