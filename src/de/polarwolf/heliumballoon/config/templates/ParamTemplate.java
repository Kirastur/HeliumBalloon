package de.polarwolf.heliumballoon.config.templates;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum ParamTemplate implements HeliumParam {

	CUSTOM(STRING, "custom"),
	WORLDSET(STRING, "worldset"),
	RULE(STRING, "rule"),
	OSCILLATING(STRING, "oscillating");

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