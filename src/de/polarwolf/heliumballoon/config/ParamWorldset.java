package de.polarwolf.heliumballoon.config;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.LIST;
import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum ParamWorldset implements HeliumParam {

	IS_DEFAULT(STRING, "isDefault"),
	INCLUDE_ALL(STRING, "includeAllWorlds"),
	INCLUDE_REGEX(STRING, "includeRegex"),
	INCLUDE_WORLDS(LIST, "includeWorlds"),
	EXCLUDE_WORLDS(LIST, "excludeWorlds");

	private final HeliumParamType paramType;
	private final String attributeName;

	private ParamWorldset(HeliumParamType paramType, String attributeName) {
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
