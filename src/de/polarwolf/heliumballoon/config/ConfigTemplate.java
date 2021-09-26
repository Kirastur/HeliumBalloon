package de.polarwolf.heliumballoon.config;

import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumParam;
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
		loadConfigFromFile(fileSection, balloonSection);
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
	
	
	protected List<HeliumParam> getValidParams() {
		return  Arrays.asList(ParamTemplate.values());
	}
	
	
	protected ConfigRule getRuleFromName(String ruleName, ConfigSection balloonSection) throws BalloonException {
		if ((ruleName == null) || (ruleName.isEmpty())) {
			return balloonSection.getDefaultRule();
		} else {
			ConfigRule configRule = balloonSection.findRule(ruleName);
			if (configRule == null) {
				throw new BalloonException (getName(), "Unknown rule", ruleName);
			}
			return configRule;
		}		
	}
	
	
	protected void loadAnimalConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		ConfigurationSection fileSectionAnimal = fileSection.getConfigurationSection(ParamTemplate.ANIMAL.getAttributeName());
		setAnimal(new ConfigAnimal (fileSectionAnimal));
	}
	
	
	protected void loadElementsConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		ConfigurationSection fileSectionCompound = fileSection.getConfigurationSection(ParamTemplate.ELEMENTS.getAttributeName());
		setCompound(new ConfigCompound (fileSectionCompound));
	}
	
	
	protected void importHeliumSection(HeliumSection heliumSection, ConfigSection balloonSection) throws BalloonException { 
		String ruleName = heliumSection.getString(ParamTemplate.RULE);
		setRule(getRuleFromName(ruleName, balloonSection));
		
		setOscillating(heliumSection.getBoolean(ParamTemplate.OSCILLATING, isOscillating()));
		setCustom(heliumSection.getString(ParamTemplate.CUSTOM));
	}
		

	protected void loadConfigFromFile(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection, balloonSection);
		
		if (heliumSection.isSection(ParamTemplate.ANIMAL)) {
			loadAnimalConfigFromFile(fileSection);
		}

		if (heliumSection.isSection(ParamTemplate.ELEMENTS)) {
			loadElementsConfigFromFile(fileSection);
		}						
	}

}
