package de.polarwolf.heliumballoon.config;

public enum ParamGuiItem {

	TEMPLATE ("template"),
	TITLE ("title"),
	DESCRIPTION ("description"),
	ICON ("icon");
		
	private final String attributeName;
	

	private ParamGuiItem(String attributeName) {
		this.attributeName = attributeName;
	}


	public String getAttributeName() {
		return attributeName;
	}
}
