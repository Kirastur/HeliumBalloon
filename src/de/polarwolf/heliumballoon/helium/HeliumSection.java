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
	protected Map<HeliumParam,String> attributes = new HashMap<>();
	protected List<HeliumParam> sections = new ArrayList<>();
	
	
	public HeliumSection(String name) {
		this.name = name;
	}
	
	
	public HeliumSection(ConfigurationSection fileSection, List<HeliumParam> validParams) throws BalloonException {
		this.name = fileSection.getName();
		loadConfig(fileSection, validParams);
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
	
	
	protected void loadConfig(ConfigurationSection fileSection, List<HeliumParam> validParams) throws BalloonException {
		sections.clear();
		attributes.clear();
		Set<String> attributeNames = fileSection.getKeys(false);
		for (String myAttributeName : attributeNames) {
			if (!fileSection.contains(myAttributeName, true)) { // ignore default from jar
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
				String myAttributeValue = fileSection.getString(myAttributeName);
				attributes.put(myParam, myAttributeValue);
			}
		}
	}
	
	
	public boolean isSection(HeliumParam param) {
		return sections.contains(param);
	}
	
	
	public String getString(HeliumParam param) {
		return attributes.get(param);
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
			return true;
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
}
