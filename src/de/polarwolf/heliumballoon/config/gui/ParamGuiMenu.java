package de.polarwolf.heliumballoon.config.gui;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.SECTION;
import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum ParamGuiMenu implements HeliumParam {

	TITLE(STRING, "title"),
	DEASSIGN(SECTION, "deassign"),
	FILLER(SECTION, "filler");

	private final HeliumParamType paramType;
	private final String attributeName;

	private ParamGuiMenu(HeliumParamType paramType, String attributeName) {
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
