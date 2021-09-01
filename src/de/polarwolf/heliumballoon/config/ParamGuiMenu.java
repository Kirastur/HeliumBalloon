package de.polarwolf.heliumballoon.config;

public enum ParamGuiMenu {

	TITLE ("title");
	
		
	private final String attributeName;
	

	private ParamGuiMenu(String attributeName) {
		this.attributeName = attributeName;
	}


	public String getAttributeName() {
		return attributeName;
	}
}
