package de.polarwolf.heliumballoon.tools.helium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;

public class HeliumSection {

	private final String name;
	private final ConfigurationSection fileSection;
	protected Map<HeliumParam, HeliumText> attributes = new HashMap<>();
	protected List<HeliumParam> sections = new ArrayList<>();
	protected List<HeliumParam> lists = new ArrayList<>();

	public HeliumSection(String name) {
		this.name = name;
		this.fileSection = null;
	}

	public HeliumSection(ConfigurationSection fileSection, List<HeliumParam> validParams) throws BalloonException {
		this.name = fileSection.getName();
		this.fileSection = fileSection;
		loadFromConfig(validParams, false);
	}

	public HeliumSection(ConfigurationSection fileSection, List<HeliumParam> validParams,
			boolean ignoreUnknownAttributes) throws BalloonException {
		this.name = fileSection.getName();
		this.fileSection = fileSection;
		loadFromConfig(validParams, ignoreUnknownAttributes);
	}

	public String getName() {
		return name;
	}

	public ConfigurationSection getfileSection() {
		return fileSection;
	}

	protected HeliumParam findParam(String attributeName, List<HeliumParam> validParams) {
		for (HeliumParam myParam : validParams) {
			if ((myParam.isType(HeliumParamType.STRING) && attributeName.equalsIgnoreCase(myParam.getAttributeName()))
					|| (!myParam.isType(HeliumParamType.STRING) && attributeName.equals(myParam.getAttributeName()))) {
				return myParam;
			}
		}
		return null;
	}

	protected boolean isValidLocalizedAttribute(String attributeName, List<HeliumParam> validParams) {
		int separatorPosition = attributeName.indexOf("_");
		if (separatorPosition <= 0) {
			return false;
		}
		return (findParam(attributeName.substring(0, separatorPosition), validParams) != null);
	}

	protected void loadStringFromConfig(String attributeName, HeliumParam param) {
		HeliumText myHeliumText = new HeliumText(attributeName, fileSection);
		attributes.put(param, myHeliumText);
	}

	protected void loadSectionFromConfig(String attributeName, HeliumParam param) throws BalloonException {
		if (!fileSection.isConfigurationSection(attributeName)) {
			throw new BalloonException(getName(), "Attribute is not a section", attributeName);
		}
		sections.add(param);
	}

	protected void loadListFromConfig(String attributeName, HeliumParam param) throws BalloonException {
		if (!fileSection.isList(attributeName)) {
			throw new BalloonException(getName(), "Attribute is not a list", attributeName);
		}
		lists.add(param);
	}

	protected void dispatchParam(String attributeName, HeliumParam param) throws BalloonException {
		if (param.isType(HeliumParamType.STRING)) {
			loadStringFromConfig(attributeName, param);
		}
		if (param.isType(HeliumParamType.SECTION)) {
			loadSectionFromConfig(attributeName, param);
		}
		if (param.isType(HeliumParamType.LIST)) {
			loadListFromConfig(attributeName, param);
		}
	}

	protected void loadFromConfig(List<HeliumParam> validParams, boolean ignoreUnknownAttributes)
			throws BalloonException {
		Set<String> attributeNames = fileSection.getKeys(false);
		for (String myAttributeName : attributeNames) {
			if (!fileSection.contains(myAttributeName, true)
					|| isValidLocalizedAttribute(myAttributeName, validParams)) { // ignore default from jar
				continue;
			}
			HeliumParam myParam = findParam(myAttributeName, validParams);
			if (myParam != null) {
				dispatchParam(myAttributeName, myParam);
			} else {
				if (!ignoreUnknownAttributes) {
					throw new BalloonException(getName(), "Unknown attribute", myAttributeName);
				}
			}
		}
	}

	public boolean isSection(HeliumParam param) {
		return sections.contains(param);
	}

	public boolean isList(HeliumParam param) {
		return lists.contains(param);
	}

	public String getString(HeliumParam param) {
		HeliumText heliumText = attributes.get(param);
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
		throw new BalloonException(param.getAttributeName(), "Not a boolean value", value);
	}

	public int getInt(HeliumParam param, int defaultValue) throws BalloonException {
		String value = getString(param);
		if ((value == null) || value.isEmpty()) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			throw new BalloonException(param.getAttributeName(), "Not an integer value", value);
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
			throw new BalloonException(param.getAttributeName(), "Not a numeric value", value);
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
