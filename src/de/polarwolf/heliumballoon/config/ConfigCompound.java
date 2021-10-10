package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.elements.CompoundElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public class ConfigCompound implements ConfigPart {

	private final String name;
	private final String fullName;
	private Map<String,ConfigElement> elements = new TreeMap<>();
	
	
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
	public boolean isSuitableFor(BalloonPurpose purpose) {
		switch(purpose) {
			case PET: return true;
			case WALL: return true;
			case ROTATION: return false;
			default: return false;
		
		}
	}

	
	@Override
	public Element createElement(Player player, ConfigRule rule, SpawnModifier spawnModifier) {
		return new CompoundElement(player, rule, this, spawnModifier);
	}

	
	@Override
	public double getMinYOffset() {
		double minYOffset = 0;
		for (ConfigElement myElement : elements.values()) {
			if (myElement.getMinYOffset() < minYOffset) {
				minYOffset = myElement.getMinYOffset();
			}
		}
		return minYOffset;		
	}

	
	@Override
	public double getMaxYOffset() {
		double maxYOffset = 0;
		for (ConfigElement myElement : elements.values()) {
			if (myElement.getMaxYOffset() > maxYOffset) {
				maxYOffset = myElement.getMinYOffset();
			}
		}
		return maxYOffset;		
	}
	

	public boolean isEmpty() {
		return elements.isEmpty();
	}
	

	public List<ConfigElement> getElements() {
		return new ArrayList<>(elements.values());
	}
	
	
	protected void addElement(ConfigElement newElement) {
		elements.put(newElement.getName(), newElement);
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
