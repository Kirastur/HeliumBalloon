package de.polarwolf.heliumballoon.config;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum ParamGuiHelperItem implements HeliumParam {

	TITLE(STRING, "title"),
	DESCRIPTION(STRING, "description"),
	ICON(STRING, "icon");

	private final HeliumParamType paramType;
	private final String attributeName;

	private ParamGuiHelperItem(HeliumParamType paramType, String attributeName) {
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
