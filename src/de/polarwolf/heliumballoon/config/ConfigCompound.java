package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.elements.CompoundElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public class ConfigCompound implements ConfigPart {

	private final String name;
	private final String fullName;
	private List<ConfigElement> elements = new ArrayList<>();
	
	
	public ConfigCompound(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}


	public ConfigCompound(ConfigurationSection fileSection) throws BalloonException {
		this.name = fileSection.getName();
		this.fullName = fileSection.getCurrentPath();
		loadConfigFromFile(fileSection);
	}

	
	@Override
	public String getName() {
		return name;
	}


	@Override
	public String getFullName() {
		return fullName;
	}


	@Override
	public Element createElement(Player player, ConfigRule rule, SpawnModifier spawnModifier) {
		return new CompoundElement(player, rule, this, spawnModifier);
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
				throw new BalloonException (getFullName(), "Illegal elements section", myElementName);
			}
			addElement(new ConfigElement(fileSection.getConfigurationSection(myElementName)));
		}
	}

}
