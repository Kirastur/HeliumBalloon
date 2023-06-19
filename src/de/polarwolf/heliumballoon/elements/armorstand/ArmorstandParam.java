package de.polarwolf.heliumballoon.elements.armorstand;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.SECTION;
import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum ArmorstandParam implements HeliumParam {

	CUSTOM(STRING, "custom"),
	X(STRING, "x"),
	Y(STRING, "y"),
	Z(STRING, "z"),
	LOAD(SECTION, "load");

	private final HeliumParamType paramType;
	private final String attributeName;

	private ArmorstandParam(HeliumParamType paramType, String attributeName) {
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