package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;

public class ConfigSection {
	
	protected List<ConfigRule> rules = new ArrayList<>();
	protected ConfigWorld worlds = new ConfigWorld();
	protected List<ConfigTemplate> templates = new ArrayList<>();
	protected List<ConfigWall> walls = new ArrayList<>();
	protected ConfigGuiMenu guiMenu = new ConfigGuiMenu(ParamSection.GUI.getAttributeName());
	

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
	
	
	protected void loadRules(ConfigurationSection fileSectionRules) throws BalloonException {
		rules.clear();
		for (String myRuleName : fileSectionRules.getKeys(false)) {
			if (!fileSectionRules.contains(myRuleName, true)) { //ignore default from jar
				continue;
			}
			if (!fileSectionRules.isConfigurationSection(myRuleName)) {
				throw new BalloonException (null, "Illegal rule secton", myRuleName);
			}
			rules.add(new ConfigRule(fileSectionRules.getConfigurationSection(myRuleName)));
		}
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
	

	protected void loadTemplates(ConfigurationSection fileSectionTemplates) throws BalloonException {
		templates.clear();
		for (String myTemplateName : fileSectionTemplates.getKeys(false)) {
			if (!fileSectionTemplates.contains(myTemplateName, true)) { //ignore default from jar
				continue;
			}
			if (!fileSectionTemplates.isConfigurationSection(myTemplateName)) {
				throw new BalloonException (null, "Illegal template secton", myTemplateName);
			}
			templates.add(new ConfigTemplate(fileSectionTemplates.getConfigurationSection(myTemplateName), this));
		}
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

	
	protected void loadWalls(ConfigurationSection fileSectionWalls) throws BalloonException {
		walls.clear();
		for (String myWallName : fileSectionWalls.getKeys(false)) {
			if (!fileSectionWalls.contains(myWallName, true)) { //ignore default from jar
				continue;
			}
			if (!fileSectionWalls.isConfigurationSection(myWallName)) {
				throw new BalloonException (null, "Illegal wall secton", myWallName);
			}
			walls.add(new ConfigWall(fileSectionWalls.getConfigurationSection(myWallName), this));
		}
	}

	
	// Worlds
	public boolean hasWorld(String worldName) {
		return worlds.hasWorld(worldName);
	}
	
	
	protected void loadWorlds(ConfigurationSection fileSectionWorlds) {
		worlds = new ConfigWorld(fileSectionWorlds);
	}
	
	
	// GUI
	public String getGuiTitle(CommandSender sender) {
		return guiMenu.getGuiTitle(sender);
	}
	
	public List<ConfigGuiItem> enumGuiItems() {
		return guiMenu.enumGuiItems();
	}
	

	public boolean hasGuiDeassign() {
		return guiMenu.hasDeassign();
	}
	
	
	public String getGuiDeassignTitle(CommandSender sender) {
		return guiMenu.getDeassignTitle(sender);
	}
	

	public String getGuiDeassignDescription(CommandSender sender) {
		return guiMenu.getDeassignDescription(sender);
	}

	
	protected void loadGui(ConfigurationSection fileSectionGui) throws BalloonException {
		guiMenu = new ConfigGuiMenu(fileSectionGui, this); 
	}
	

	public void loadConfig(ConfigurationSection fileSection) throws BalloonException {
		
		loadWorlds(fileSection.getConfigurationSection(ParamSection.WORLDS.getAttributeName()));

		if (!fileSection.contains(ParamSection.RULES.getAttributeName(), true) || !fileSection.isConfigurationSection(ParamSection.RULES.getAttributeName())) {
			throw new BalloonException(null, "Rules definition is missing", null);
		}
		loadRules(fileSection.getConfigurationSection(ParamSection.RULES.getAttributeName()));

		if (!fileSection.contains(ParamSection.TEMPLATES.getAttributeName(), true) || !fileSection.isConfigurationSection(ParamSection.TEMPLATES.getAttributeName())) {
			throw new BalloonException(null, "Templates definition is missing", null);
		}
		loadTemplates(fileSection.getConfigurationSection(ParamSection.TEMPLATES.getAttributeName()));
		
		if (fileSection.contains(ParamSection.WALLS.getAttributeName(), true) && fileSection.isConfigurationSection(ParamSection.WALLS.getAttributeName())) {
			loadWalls(fileSection.getConfigurationSection(ParamSection.WALLS.getAttributeName()));
		} // Can be empty
		
		if (fileSection.contains(ParamSection.GUI.getAttributeName(), true) && fileSection.isConfigurationSection(ParamSection.GUI.getAttributeName())) {
			loadGui(fileSection.getConfigurationSection(ParamSection.GUI.getAttributeName()));
		} // Can be empty
	}
		
}
