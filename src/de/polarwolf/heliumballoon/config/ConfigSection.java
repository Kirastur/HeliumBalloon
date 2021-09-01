package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.system.IntlText;

public class ConfigSection {
	
	public static final String SECTION_WORLDS = "worlds";
	public static final String SECTION_RULES = "rules";
	public static final String SECTION_TEMPLATES = "templates";
	public static final String SECTION_WALLS = "walls";
	public static final String SECTION_GUI = "gui";
	public static final String SECTION_ITEMS = "items";
	
	protected List<ConfigRule> rules = new ArrayList<>();
	protected ConfigWorld worlds = new ConfigWorld();
	protected List<ConfigTemplate> templates = new ArrayList<>();
	protected List<ConfigWall> walls = new ArrayList<>();
	protected Map<String,ConfigGuiItem> guiItems = new TreeMap<>();
	protected IntlText guiTitle = new IntlText("title");
	

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
		throw new BalloonException(SECTION_RULES, "No default rule found", null);
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
		return guiTitle.findLocalizedforSender(sender);
	}
	
	public List<ConfigGuiItem> enumGuiItems() {
		return new ArrayList<>(guiItems.values());
	}
	

	protected void loadGui(ConfigurationSection fileSectionGui) throws BalloonException {
		guiTitle = new IntlText(fileSectionGui, ParamGuiMenu.TITLE.getAttributeName());
		guiItems.clear();
		
		if (!fileSectionGui.contains(SECTION_ITEMS) || !fileSectionGui.isConfigurationSection(SECTION_ITEMS)) {
			throw new BalloonException (null, "No GUI Item section found", null);			
		}
		ConfigurationSection fileSectionGuiItems = fileSectionGui.getConfigurationSection(SECTION_ITEMS);
		
		for (String myItemName : fileSectionGuiItems.getKeys(false)) {
			if (!fileSectionGuiItems.contains(myItemName, true)) { //ignore default from jar
				continue;
			}
			if (!fileSectionGuiItems.isConfigurationSection(myItemName)) {
				throw new BalloonException (null, "Illegal GUI items secton", myItemName);
			}
			guiItems.put(myItemName, new ConfigGuiItem(fileSectionGuiItems.getConfigurationSection(myItemName), this));
		}
	}
	

	public void loadConfig(ConfigurationSection fileSection) throws BalloonException {
		
		loadWorlds(fileSection.getConfigurationSection(SECTION_WORLDS));

		if (!fileSection.isConfigurationSection(SECTION_RULES)) {
			throw new BalloonException(null, "Rules definition is missing", null);
		}
		loadRules(fileSection.getConfigurationSection(SECTION_RULES));

		if (!fileSection.isConfigurationSection(SECTION_TEMPLATES)) {
			throw new BalloonException(null, "Templates definition is missing", null);
		}
		loadTemplates(fileSection.getConfigurationSection(SECTION_TEMPLATES));
		
		if (fileSection.isConfigurationSection(SECTION_WALLS)) {
			loadWalls(fileSection.getConfigurationSection(SECTION_WALLS));
		} // Can be empty
		
		if (fileSection.isConfigurationSection(SECTION_GUI)) {
			loadGui(fileSection.getConfigurationSection(SECTION_GUI));
		} // Can be empty
	}
		
}
