package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;

public enum ParamGuiDeassign implements HeliumParam {

	TITLE ("title"),
	DESCRIPTION ("description");
		
	private final String attributeName;
	

	private ParamGuiDeassign(String attributeName) {
		this.attributeName = attributeName;
	}


	@Override
	public boolean isSection() {
		return false;
	}

	
	@Override
	public String getAttributeName() {
		return attributeName;
	}
}
