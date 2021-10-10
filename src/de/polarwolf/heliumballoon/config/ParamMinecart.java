package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumParamType;
import static de.polarwolf.heliumballoon.helium.HeliumParamType.*;

public enum ParamMinecart implements HeliumParam {

	X (STRING, "x"),
	Y (STRING, "y"),
	Z (STRING, "z"),
	LOAD(SECTION, "load"),
	LOAD_OFFSET(STRING, "loadOffset"),
	CUSTOM (STRING, "custom");
	
	private final HeliumParamType paramType;
	private final String attributeName;
	

	private ParamMinecart(HeliumParamType paramType, String attributeName) {
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