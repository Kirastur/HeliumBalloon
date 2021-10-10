package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumParamType;
import static de.polarwolf.heliumballoon.helium.HeliumParamType.*;

public enum ParamWorld implements HeliumParam {

	INCLUDE_ALL (STRING, "includeAllWorlds"),
	INCLUDE_LIST (LIST, "include"),
	EXCLUDE_LIST(LIST, "exclude");
		
	private final HeliumParamType paramType;
	private final String attributeName;
	

	private ParamWorld(HeliumParamType paramType, String attributeName) {
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
