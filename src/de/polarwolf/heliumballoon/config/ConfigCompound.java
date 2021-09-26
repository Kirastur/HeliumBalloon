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
		loadConfigFromFile(fileSection);
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
	
	
	protected void addElement(ConfigElement newElement) {
		elements.add(newElement);
	}
	
	
	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		for (String myElementName : fileSection.getKeys(false)) {
			if (!fileSection.contains(myElementName, true)) { // ignore default from jar
				continue;
			}
			if (!fileSection.isConfigurationSection(myElementName)) {
				throw new BalloonException (getName(), "Illegal elements section", myElementName);
			}
			addElement(new ConfigElement(fileSection.getConfigurationSection(myElementName)));
		}
	}

}
