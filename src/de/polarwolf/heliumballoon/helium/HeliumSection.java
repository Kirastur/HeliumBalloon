package de.polarwolf.heliumballoon.helium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;

public class HeliumSection {
	
	private final String name;
	protected Map<HeliumParam,HeliumText> attributes = new HashMap<>();
	protected List<HeliumParam> sections = new ArrayList<>();
	
	
	public HeliumSection(String name) {
		this.name = name;
	}
	
	
	public HeliumSection(ConfigurationSection fileSection, List<HeliumParam> validParams) throws BalloonException {
		this.name = fileSection.getName();
		loadFromConfig(fileSection, validParams);
	}
	
	
	public String getName() {
		return name;
	}


	public static HeliumParam findParam(String attributeName, List<HeliumParam> validParams) {
		for (HeliumParam myParam: validParams) {
			if ((!myParam.isSection()) && attributeName.equalsIgnoreCase(myParam.getAttributeName())) {
				return myParam;
			}
			if (myParam.isSection() && attributeName.equals(myParam.getAttributeName())) {
				return myParam;
			}
		}
		return null;		
	}
	
	
	public static boolean isValidLocalizedAttribute(String attributeName, List<HeliumParam> validParams) {
		int separatorPosition = attributeName.indexOf("_");
		if (separatorPosition <= 0) {
			return false;
		}
		return (findParam(attributeName.substring(0, separatorPosition), validParams) != null);
	}
	
	
	protected void loadFromConfig(ConfigurationSection fileSection, List<HeliumParam> validParams) throws BalloonException {
		Set<String> attributeNames = fileSection.getKeys(false);
		for (String myAttributeName : attributeNames) {
			if (!fileSection.contains(myAttributeName, true) || isValidLocalizedAttribute(myAttributeName, validParams)) { // ignore default from jar
				continue;
			}
			HeliumParam myParam = findParam(myAttributeName, validParams);
			if (myParam == null) {
				throw new BalloonException(getName(), "Unknown attribute", myAttributeName);
			}
			
			if (myParam.isSection()) {
				if (!fileSection.isConfigurationSection(myAttributeName)) {
					throw new BalloonException(getName(), "Attribute is not a section", myAttributeName);
				}
				sections.add(myParam);
			} else {
				HeliumText myHeliumText = new HeliumText(myAttributeName, fileSection); 
				attributes.put(myParam, myHeliumText);
			}
		}
	}
	
	
	public boolean isSection(HeliumParam param) {
		return sections.contains(param);
	}
	
	
	public String getString(HeliumParam param) {
		HeliumText heliumText =  attributes.get(param);
		if (heliumText == null) {
			return null;
		} else {
			return heliumText.findText();
		}
	}


	public boolean getBoolean(HeliumParam param, boolean defaultValue) throws BalloonException {
		String value = getString(param);
		if ((value == null) || value.isEmpty()) {
			return defaultValue;
		}
		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes")) {
			return true;
		}
		if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no")) {
			return false;
		}
		throw new BalloonException (param.getAttributeName(), "Not a boolean value", value);
	}
	
	
	public int getInt(HeliumParam param, int defaultValue) throws BalloonException {
		String value = getString(param);
		if ((value == null) || value.isEmpty()) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			throw new BalloonException (param.getAttributeName(), "Not an integer value", value);
		}
	}
	
	
	public double getDouble(HeliumParam param, double defaultValue) throws BalloonException {
		String value = getString(param);
		if ((value == null) || value.isEmpty()) {
			return defaultValue;
		}
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			throw new BalloonException (param.getAttributeName(), "Not a numeric value", value);
		}
	}
	
	
	public String getString(HeliumParam param, String defaultValue) {
		String value = getString(param);
		if ((value == null) || (value.isEmpty())) {
			return defaultValue;
		} else {
			return value;
		}		
	}
	
	
	public HeliumText getHeliumText(HeliumParam param) {
		return attributes.get(param);
	}
}
