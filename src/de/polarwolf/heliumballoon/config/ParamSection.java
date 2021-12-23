package de.polarwolf.heliumballoon.config;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.SECTION;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum ParamSection implements HeliumParam {

	WORLDSETS(SECTION, "worldsets"),
	RULES(SECTION, "rules"),
	TEMPLATES(SECTION, "templates"),
	PETS(SECTION, "pets"),
	WALLS(SECTION, "walls"),
	ROTATORS(SECTION, "rotators"),
	GUI(SECTION, "gui");

	private final HeliumParamType paramType;
	private final String attributeName;

	private ParamSection(HeliumParamType paramType, String attributeName) {
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
