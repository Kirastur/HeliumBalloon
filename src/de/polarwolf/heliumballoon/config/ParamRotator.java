package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumParamType;
import static de.polarwolf.heliumballoon.helium.HeliumParamType.*;

public enum ParamRotator implements HeliumParam {

	TEMPLATE (STRING, "template"),
	WORLDS (STRING, "worlds"),
	X (STRING, "x"),
	Y (STRING, "y"),
	Z (STRING, "z");
		
	private final HeliumParamType paramType;
	private final String attributeName;
	

	private ParamRotator(HeliumParamType paramType, String attributeName) {
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