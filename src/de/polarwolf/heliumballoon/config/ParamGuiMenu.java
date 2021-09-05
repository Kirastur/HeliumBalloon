package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;

public enum ParamGuiMenu implements HeliumParam {

	TITLE (false, "title"),
	ITEMS (true, "items"),
	DEASSIGN (true, "deassign");
	
		
	private final String attributeName;
	private final boolean section;
	

	private ParamGuiMenu(boolean section, String attributeName) {
		this.section = section;
		this.attributeName = attributeName;
	}


	@Override
	public boolean isSection() {
		return section;
	}

	
	@Override
	public String getAttributeName() {
		return attributeName;
	}

}
