package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumParamType;
import static de.polarwolf.heliumballoon.helium.HeliumParamType.*;

public enum ParamGuiMenu implements HeliumParam {

	TITLE (STRING, "title"),
	ITEMS (SECTION, "items"),
	DEASSIGN (SECTION, "deassign");
		
	private final HeliumParamType paramType;
	private final String attributeName;
	

	private ParamGuiMenu(HeliumParamType paramType, String attributeName) {
		this.paramType = paramType;
		this.attributeName = attributeName;
	}


	@Override
	public boolean isType(HeliumParamType testParamType) {
		return testParamType==paramType;
	}

	
	@Override
	public String getAttributeName() {
		return attributeName;
	}

}
