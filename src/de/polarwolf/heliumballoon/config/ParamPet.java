package de.polarwolf.heliumballoon.config;

import static de.polarwolf.heliumballoon.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumParamType;

public enum ParamPet implements HeliumParam {

	TEMPLATES(STRING, "templates"),
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
