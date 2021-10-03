package de.polarwolf.heliumballoon.config;

import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumName;
import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumSection;

public class ConfigTemplate implements HeliumName {
	
	private final String name;
	private final String fullName;
	private ConfigRule rule;
	private boolean oscillating = false;
	private ConfigLiving living = null;
	private ConfigCompound compound = null;
	private ConfigMinecart minecart = null;
	private String custom;
	
	
	public ConfigTemplate(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}
	
	
	public ConfigTemplate(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		this.name = fileSection.getName();
		this.fullName = fileSection.getCurrentPath();
		loadConfigFromFile(fileSection, balloonSection);
	}
	
	
	@Override
	public String getName() {
		return name;
	}


	@Override
	public String getFullName() {
		return fullName;
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


	public boolean hasLiving() {
		return (living != null);
	}
	
	
	public ConfigLiving getLiving() {
		return living;
	}


	protected void setLiving(ConfigLiving living) {
		this.living = living;
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


	public boolean hasMinecart() {
		return (minecart != null);
	}
	
	
	public ConfigMinecart getMinecart() {
		return minecart;
	}


	protected void setMinecart(ConfigMinecart minecart) {
		this.minecart = minecart;
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
				throw new BalloonException (getFullName(), "Unknown rule", ruleName);
			}
			return configRule;
		}		
	}
	
	
	protected void loadLivingConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		ConfigurationSection fileSectionLiving = fileSection.getConfigurationSection(ParamTemplate.LIVING.getAttributeName());
		setLiving(new ConfigLiving (fileSectionLiving));
	}
	
	
	protected void loadElementsConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		ConfigurationSection fileSectionCompound = fileSection.getConfigurationSection(ParamTemplate.ELEMENTS.getAttributeName());
		setCompound(new ConfigCompound (fileSectionCompound));
	}
	
	
	protected void loadMinecartConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		ConfigurationSection fileSectionMinecart = fileSection.getConfigurationSection(ParamTemplate.MINECART.getAttributeName());
		setMinecart(new ConfigMinecart (fileSectionMinecart));
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
		
		if (heliumSection.isSection(ParamTemplate.LIVING)) {
			loadLivingConfigFromFile(fileSection);
		}
			
		if (heliumSection.isSection(ParamTemplate.ELEMENTS)) {
			loadElementsConfigFromFile(fileSection);
		}						

		if (heliumSection.isSection(ParamTemplate.MINECART)) {
			loadMinecartConfigFromFile(fileSection);
		}						
	}

}
