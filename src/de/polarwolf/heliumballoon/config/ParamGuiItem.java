package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;

public enum ParamGuiItem implements HeliumParam {

	TEMPLATE ("template"),
	TITLE ("title"),
	DESCRIPTION ("description"),
	ICON ("icon");
		
	private final String attributeName;
	

	private ParamGuiItem(String attributeName) {
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
