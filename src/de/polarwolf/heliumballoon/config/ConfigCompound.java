package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;

public class ConfigCompound {

	private final String name;
	private List<ConfigElement> elements = new ArrayList<>();
	
	
	public ConfigCompound(String name) {
		this.name = name;
	}


	public ConfigCompound(ConfigurationSection fileSection) throws BalloonException {
		this.name = fileSection.getName();
		loadConfig(fileSection);
	}

	
	public String getName() {
		return name;
	}


	public boolean isEmpty() {
		return elements.isEmpty();
	}
	

	public List<ConfigElement> enumElements() {
		return new ArrayList<>(elements);
	}
	
	
	protected void loadConfig(ConfigurationSection fileSection) throws BalloonException {
		elements.clear();
		for (String elementName : fileSection.getKeys(false)) {
			if (!fileSection.contains(elementName, true)) { // ignore default from jar
				continue;
			}
			if (!fileSection.isConfigurationSection(elementName)) {
				throw new BalloonException (getName(), "Illegal elements section", elementName);
			}
			elements.add(new ConfigElement(fileSection.getConfigurationSection(elementName)));
		}
	}

}
