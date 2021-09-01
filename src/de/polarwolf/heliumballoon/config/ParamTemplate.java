package de.polarwolf.heliumballoon.config;

public enum ParamTemplate {

	RULE ("rule", false),
	ANIMAL ("animal", true),
	ELEMENTS ("elements", true),
	CUSTOM ("custom", false);
		
	private final String attributeName;
	private final boolean section;
	

	private ParamTemplate(String attributeName, boolean section) {
		this.attributeName = attributeName;
		this.section = section;
	}


	public String getAttributeName() {
		return attributeName;
	}
	
	
	public boolean isSection() {
		return section;
	}

}

