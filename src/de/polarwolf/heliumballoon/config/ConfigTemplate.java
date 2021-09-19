package de.polarwolf.heliumballoon.config;

import java.util.Arrays;
import org.bukkit.configuration.ConfigurationSection;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumSection;

public class ConfigTemplate {
	
	private final String name;
	private ConfigRule rule;
	private boolean oscillating = false;
	private ConfigAnimal animal = null;
	private ConfigCompound compound = null;
	private String custom;
	
	
	public ConfigTemplate(String name) {
		this.name = name;
	}
	
	
	public ConfigTemplate(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		this.name = fileSection.getName();
		loadConfig(fileSection, balloonSection);
	}
	
	
	public String getName() {
		return name;
	}


	public ConfigRule getRule() {
		return rule;
	}


	protected void setRule(ConfigRule rule) {
		this.rule = rule;
	}


	public boolean isOscillating() {
		return oscillating;
	}


	protected void setOscillating(boolean oscillating) {
		this.oscillating = oscillating;
	}


	public boolean hasAnimal() {
		return (animal != null);
	}
	
	
	public ConfigAnimal getAnimal() {
		return animal;
	}


	protected void setAnimal(ConfigAnimal animal) {
		this.animal = animal;
	}


	public boolean hasCompound() {
		return ((compound != null)  && (!compound.isEmpty()));
	}
	
	
	public ConfigCompound getCompound() {
		return compound;
	}


	protected void setCompound(ConfigCompound compound) {
		this.compound = compound;
	}


	public String getCustom() {
		return custom;
	}


	protected void setCustom(String custom) {
		this.custom = custom;
	}
	
	
	protected void loadAnimalConfig(ConfigurationSection fileSection) throws BalloonException {
		ConfigurationSection fileSectionAnimal = fileSection.getConfigurationSection(ParamTemplate.ANIMAL.getAttributeName());
		animal = new ConfigAnimal (fileSectionAnimal);
	}
	
	
	protected void loadElementsConfig(ConfigurationSection fileSection) throws BalloonException {
		ConfigurationSection fileSectionCompound = fileSection.getConfigurationSection(ParamTemplate.ELEMENTS.getAttributeName());
		compound = new ConfigCompound (fileSectionCompound);
	}


	protected void loadConfig(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, Arrays.asList(ParamTemplate.values()));

		String ruleName = heliumSection.getString(ParamTemplate.RULE);
		if ((ruleName == null) || (ruleName.isEmpty())) {
			setRule(balloonSection.getDefaultRule());
		} else {
			ConfigRule myRule = balloonSection.findRule(ruleName);
			if (myRule == null) {
				throw new BalloonException (getName(), "Unknown rule", ruleName);
			}
			setRule(myRule);
		}
		
		setOscillating(heliumSection.getBoolean(ParamTemplate.OSCILLATING, isOscillating()));
		
		if (heliumSection.isSection(ParamTemplate.ANIMAL)) {
			loadAnimalConfig(fileSection);
		}
		if (heliumSection.isSection(ParamTemplate.ELEMENTS)) {
			loadElementsConfig(fileSection);
		}
						
		setCustom(heliumSection.getString(ParamTemplate.CUSTOM));
	}

}
