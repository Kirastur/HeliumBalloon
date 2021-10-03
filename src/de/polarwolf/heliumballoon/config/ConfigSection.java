package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;

public class ConfigSection {
	
	private List<ConfigRule> rules = new ArrayList<>();
	private ConfigWorld worlds = null;
	private List<ConfigTemplate> templates = new ArrayList<>();
	private List<ConfigWall> walls = new ArrayList<>();
	private ConfigGuiMenu guiMenu = null;
	
	
	public ConfigSection() {
		// Nothing to do
	}
	
	
	public ConfigSection(ConfigurationSection fileSection) throws BalloonException {
		loadConfigFromFile(fileSection);
	}
	

	// Rules
	public ConfigRule findRule(String ruleName) {
		for (ConfigRule myRule : rules) {
			if (myRule.getName().equalsIgnoreCase(ruleName)) {
				return myRule;
			}
		}
		return null;
	}
	
	
	public ConfigRule getDefaultRule() throws BalloonException {
		for (ConfigRule myRule : rules) {
			if (myRule.isDefaultRule()) {
				return myRule;
			}
		}
		throw new BalloonException(ParamSection.RULES.getAttributeName(), "No default rule found", null);
	}
	
	
	public Set<String> getRuleNames() {
		Set<String> ruleNamesSet = new TreeSet<>();
		for (ConfigRule myRule : rules) {
			ruleNamesSet.add(myRule.getName());
		}
		return ruleNamesSet;
	}
	
	
	protected void addRule(ConfigRule newRule) {
		rules.add(newRule);
	}
	
	
	
	// Templates
	public ConfigTemplate findTemplate(String templateName) {
		for (ConfigTemplate myTemplate : templates) {
			if (myTemplate.getName().equalsIgnoreCase(templateName)) {
				return myTemplate;
			}
		}
		return null;
	}
	
	
	public Set<String> getTemplateNames() {
		Set<String> templateNamesSet = new TreeSet<>();
		for (ConfigTemplate myTemplate : templates) {
			templateNamesSet.add(myTemplate.getName());
		}
		return templateNamesSet;
	}
	
	
	protected void addTemplate(ConfigTemplate newTemplate) {
		templates.add(newTemplate);
	}
	
		
	// Walls
	public ConfigWall findWall(String wallName) {
		for (ConfigWall myWall : walls) {
			if (myWall.getName().equalsIgnoreCase(wallName)) {
				return myWall;
			}
		}
		return null;
	}
	
	
	public Set<String> getWallNames() {
		Set<String> wallNamesSet = new TreeSet<>();
		for (ConfigWall myWall : walls) {
			wallNamesSet.add(myWall.getName());
		}
		return wallNamesSet;
	}
	
	
	protected void addWall(ConfigWall newWall) {
		walls.add(newWall);
	}

		
	// Worlds
	public boolean hasWorld(String worldName) {
		if (worlds == null) {
			return false;
		} else {
			return worlds.hasWorld(worldName);
		}
	}
	
	
	protected void setWorlds(ConfigWorld newWorlds) {
		worlds = newWorlds;
	}
	
	
	// GUI
	public ConfigGuiMenu getGuiMenu() {
		return guiMenu;
	}
	
	
	protected void setGuiMenu(ConfigGuiMenu newGuiMenu) {
		guiMenu = newGuiMenu;
	}
	
	
	public String getGuiTitle(CommandSender sender) {
		if (guiMenu == null) {
			return "";
		} else {
			return guiMenu.getGuiTitle(sender);
		}
	}
	
	
	public List<ConfigGuiItem> enumGuiItems() {
		if (guiMenu == null) {
			return new ArrayList<>();
		} else {
			return guiMenu.enumGuiItems();
		}
	}
	

	public ConfigGuiDeassign getGuiDeassign() {
		if (guiMenu == null) {
			return null;
		} else {
			return guiMenu.getGuiDeassign();
		}
	}
	
	
	// LoadFromFile
	protected ConfigRule buildConfigRuleFromFile(ConfigurationSection fileSection) throws BalloonException {
		return new ConfigRule(fileSection);
	}
	
	
	protected void loadRulesFromFile(ConfigurationSection fileSectionRules) throws BalloonException {
		for (String myRuleName : fileSectionRules.getKeys(false)) {
			if (!fileSectionRules.contains(myRuleName, true)) { //ignore default from jar
				continue;
			}
			if (!fileSectionRules.isConfigurationSection(myRuleName)) {
				throw new BalloonException (null, "Illegal rule section", myRuleName);
			}
			addRule(buildConfigRuleFromFile(fileSectionRules.getConfigurationSection(myRuleName)));
		}
	}
	
	
	protected ConfigTemplate buildConfigTemplateFromFile(ConfigurationSection fileSection) throws BalloonException {
		return new ConfigTemplate(fileSection, this);
	}

	
	protected void loadTemplatesFromFile(ConfigurationSection fileSectionTemplates) throws BalloonException {
		for (String myTemplateName : fileSectionTemplates.getKeys(false)) {
			if (!fileSectionTemplates.contains(myTemplateName, true)) { //ignore default from jar
				continue;
			}
			if (!fileSectionTemplates.isConfigurationSection(myTemplateName)) {
				throw new BalloonException (null, "Illegal template section", myTemplateName);
			}
			addTemplate(buildConfigTemplateFromFile(fileSectionTemplates.getConfigurationSection(myTemplateName)));
		}
	}
	
	
	protected ConfigWall buildConfigWallFromFile(ConfigurationSection fileSection) throws BalloonException {
		return new ConfigWall(fileSection, this);
	}


	protected void loadWallsFromFile(ConfigurationSection fileSectionWalls) throws BalloonException {
		for (String myWallName : fileSectionWalls.getKeys(false)) {
			if (!fileSectionWalls.contains(myWallName, true)) { //ignore default from jar
				continue;
			}
			if (!fileSectionWalls.isConfigurationSection(myWallName)) {
				throw new BalloonException (null, "Illegal wall section", myWallName);
			}
			addWall(buildConfigWallFromFile(fileSectionWalls.getConfigurationSection(myWallName)));
		}
	}


	protected ConfigWorld buildConfigWorldFromFile(ConfigurationSection fileSection) {
		return new ConfigWorld(fileSection);
	}
	
	
	protected void loadWorldsFromFile(ConfigurationSection fileSectionWorlds) {
		setWorlds(buildConfigWorldFromFile(fileSectionWorlds));
	}
	
	
	protected ConfigGuiMenu buildConfigGuiMenuFromFile(ConfigurationSection fileSection) throws BalloonException {
		return new ConfigGuiMenu(fileSection, this);
	}
	
	
	protected void loadGuiFromFile(ConfigurationSection fileSectionGui) throws BalloonException {
		setGuiMenu(buildConfigGuiMenuFromFile(fileSectionGui)); 
	}
	

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		
		if (fileSection.contains(ParamSection.WORLDS.getAttributeName(), true) && fileSection.isConfigurationSection(ParamSection.WORLDS.getAttributeName())) {
			loadWorldsFromFile(fileSection.getConfigurationSection(ParamSection.WORLDS.getAttributeName()));
		}

		if (fileSection.contains(ParamSection.RULES.getAttributeName(), true) && fileSection.isConfigurationSection(ParamSection.RULES.getAttributeName())) {
			loadRulesFromFile(fileSection.getConfigurationSection(ParamSection.RULES.getAttributeName()));
		} else {
			throw new BalloonException(null, "Rules definition is missing", null);
		}

		if (fileSection.contains(ParamSection.TEMPLATES.getAttributeName(), true) && fileSection.isConfigurationSection(ParamSection.TEMPLATES.getAttributeName())) {
			loadTemplatesFromFile(fileSection.getConfigurationSection(ParamSection.TEMPLATES.getAttributeName()));
		} else {
			throw new BalloonException(null, "Templates definition is missing", null);
		}
		
		if (fileSection.contains(ParamSection.WALLS.getAttributeName(), true) && fileSection.isConfigurationSection(ParamSection.WALLS.getAttributeName())) {
			loadWallsFromFile(fileSection.getConfigurationSection(ParamSection.WALLS.getAttributeName()));
		}
		
		if (fileSection.contains(ParamSection.GUI.getAttributeName(), true) && fileSection.isConfigurationSection(ParamSection.GUI.getAttributeName())) {
			loadGuiFromFile(fileSection.getConfigurationSection(ParamSection.GUI.getAttributeName()));
		}
	}
		
}
