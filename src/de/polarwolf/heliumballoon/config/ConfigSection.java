package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumName;
import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumSection;

public class ConfigSection implements HeliumName {

	protected static final String DEFAULT_RULE_NAME = "default";
	protected static final String DEFAULT_WORLDSET_NAME = "default";

	private final String name;
	private List<ConfigWorldset> worldsets = new ArrayList<>();
	private List<ConfigRule> rules = new ArrayList<>();
	private List<ConfigTemplate> templates = new ArrayList<>();
	private List<ConfigPet> pets = new ArrayList<>();
	private List<ConfigWall> walls = new ArrayList<>();
	private List<ConfigRotator> rotators = new ArrayList<>();
	private ConfigGuiMenu guiMenu = null;

	public ConfigSection(String name) {
		this.name = name;
	}

	public ConfigSection(String name, ConfigurationSection fileSection) throws BalloonException {
		this.name = name;
		loadConfigFromFile(fileSection);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getFullName() {
		return getName();
	}

	// Worldsets
	public ConfigWorldset findWorldset(String worldsetName) {
		for (ConfigWorldset myWorldset : worldsets) {
			if (myWorldset.getName().equalsIgnoreCase(worldsetName)) {
				return myWorldset;
			}
		}
		return null;
	}

	public ConfigWorldset getDefaultWorldset() throws BalloonException {
		for (ConfigWorldset myWorldset : worldsets) {
			if (myWorldset.isDefaultWorldset()) {
				return myWorldset;
			}
		}
		throw new BalloonException(ParamSection.WORLDSETS.getAttributeName(), "No default Worldset found", null);
	}

	public Set<String> getWorlsetNames() {
		Set<String> worldsetNamesSet = new TreeSet<>();
		for (ConfigWorldset myWorldset : worldsets) {
			worldsetNamesSet.add(myWorldset.getName());
		}
		return worldsetNamesSet;
	}

	protected void addWorldset(ConfigWorldset newWorldset) {
		worldsets.add(newWorldset);
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

	// Pets
	public ConfigPet findPet(String petName) {
		for (ConfigPet myPet : pets) {
			if (myPet.getName().equalsIgnoreCase(petName)) {
				return myPet;
			}
		}
		return null;
	}

	public Set<String> getPetNames() {
		Set<String> petNamesSet = new TreeSet<>();
		for (ConfigPet myPet : pets) {
			petNamesSet.add(myPet.getName());
		}
		return petNamesSet;
	}

	public boolean hasPet(ConfigPet petToTest) {
		for (ConfigPet myPet : pets) {
			if (myPet.equals(petToTest)) {
				return true;
			}
		}
		return false;
	}

	protected void addPet(ConfigPet newPet) {
		pets.add(newPet);
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

	// Rotators
	public ConfigRotator findRotator(String rotatorName) {
		for (ConfigRotator myRotator : rotators) {
			if (myRotator.getName().equalsIgnoreCase(rotatorName)) {
				return myRotator;
			}
		}
		return null;
	}

	public Set<String> getRotatorNames() {
		Set<String> rotatorNamesSet = new TreeSet<>();
		for (ConfigRotator myRotator : rotators) {
			rotatorNamesSet.add(myRotator.getName());
		}
		return rotatorNamesSet;
	}

	protected void addRotator(ConfigRotator newRotator) {
		rotators.add(newRotator);
	}

	// GUI
	public ConfigGuiMenu getGuiMenu() {
		return guiMenu;
	}

	protected void setGuiMenu(ConfigGuiMenu newGuiMenu) {
		guiMenu = newGuiMenu;
	}

	// LoadFromFile
	protected List<HeliumParam> getValidParams() {
		return Arrays.asList(ParamSection.values());
	}

	protected ConfigWorldset buildDefaultConfigWorldset() {
		return new ConfigWorldset(DEFAULT_WORLDSET_NAME, getFullName() + "." + DEFAULT_WORLDSET_NAME, true, true);
	}

	protected ConfigWorldset buildConfigWorldsetFromFile(ConfigurationSection fileSection) throws BalloonException {
		return new ConfigWorldset(fileSection);
	}

	protected void loadWorldsetsFromFile(ConfigurationSection fileSectionWorldsets) throws BalloonException {
		for (String myWorldsetName : fileSectionWorldsets.getKeys(false)) {
			if (!fileSectionWorldsets.contains(myWorldsetName, true)) { // ignore default from jar
				continue;
			}
			if (!fileSectionWorldsets.isConfigurationSection(myWorldsetName)) {
				throw new BalloonException(null, "Illegal worldset section", myWorldsetName);
			}
			addWorldset(buildConfigWorldsetFromFile(fileSectionWorldsets.getConfigurationSection(myWorldsetName)));
		}
	}

	protected ConfigRule buildDefaultConfigRule() {
		return new ConfigRule(DEFAULT_RULE_NAME, getFullName() + "." + DEFAULT_RULE_NAME, true);
	}

	protected ConfigRule buildConfigRuleFromFile(ConfigurationSection fileSection) throws BalloonException {
		return new ConfigRule(fileSection);
	}

	protected void loadRulesFromFile(ConfigurationSection fileSectionRules) throws BalloonException {
		for (String myRuleName : fileSectionRules.getKeys(false)) {
			if (!fileSectionRules.contains(myRuleName, true)) { // ignore default from jar
				continue;
			}
			if (!fileSectionRules.isConfigurationSection(myRuleName)) {
				throw new BalloonException(null, "Illegal rule section", myRuleName);
			}
			addRule(buildConfigRuleFromFile(fileSectionRules.getConfigurationSection(myRuleName)));
		}
	}

	protected ConfigTemplate buildConfigTemplateFromFile(ConfigurationSection fileSection) throws BalloonException {
		return new ConfigTemplate(fileSection, this);
	}

	protected void loadTemplatesFromFile(ConfigurationSection fileSectionTemplates) throws BalloonException {
		for (String myTemplateName : fileSectionTemplates.getKeys(false)) {
			if (!fileSectionTemplates.contains(myTemplateName, true)) { // ignore default from jar
				continue;
			}
			if (!fileSectionTemplates.isConfigurationSection(myTemplateName)) {
				throw new BalloonException(null, "Illegal template section", myTemplateName);
			}
			addTemplate(buildConfigTemplateFromFile(fileSectionTemplates.getConfigurationSection(myTemplateName)));
		}
	}

	protected ConfigPet buildConfigPetFromFile(ConfigurationSection fileSection) throws BalloonException {
		return new ConfigPet(fileSection, this);
	}

	protected void loadPetsFromFile(ConfigurationSection fileSectionPets) throws BalloonException {
		for (String myPetName : fileSectionPets.getKeys(false)) {
			if (!fileSectionPets.contains(myPetName, true)) { // ignore default from jar
				continue;
			}
			if (!fileSectionPets.isConfigurationSection(myPetName)) {
				throw new BalloonException(null, "Illegal pet section", myPetName);
			}
			addPet(buildConfigPetFromFile(fileSectionPets.getConfigurationSection(myPetName)));
		}
	}

	protected ConfigWall buildConfigWallFromFile(ConfigurationSection fileSection) throws BalloonException {
		return new ConfigWall(fileSection, this);
	}

	protected void loadWallsFromFile(ConfigurationSection fileSectionWalls) throws BalloonException {
		for (String myWallName : fileSectionWalls.getKeys(false)) {
			if (!fileSectionWalls.contains(myWallName, true)) { // ignore default from jar
				continue;
			}
			if (!fileSectionWalls.isConfigurationSection(myWallName)) {
				throw new BalloonException(null, "Illegal wall section", myWallName);
			}
			addWall(buildConfigWallFromFile(fileSectionWalls.getConfigurationSection(myWallName)));
		}
	}

	protected ConfigRotator buildConfigRotatorFromFile(ConfigurationSection fileSection) throws BalloonException {
		return new ConfigRotator(fileSection, this);
	}

	protected void loadRotatorsFromFile(ConfigurationSection fileSectionRotators) throws BalloonException {
		for (String myRotatorName : fileSectionRotators.getKeys(false)) {
			if (!fileSectionRotators.contains(myRotatorName, true)) { // ignore default from jar
				continue;
			}
			if (!fileSectionRotators.isConfigurationSection(myRotatorName)) {
				throw new BalloonException(null, "Illegal rotatator section", myRotatorName);
			}
			addRotator(buildConfigRotatorFromFile(fileSectionRotators.getConfigurationSection(myRotatorName)));
		}
	}

	protected ConfigGuiMenu buildConfigGuiMenuFromFile(ConfigurationSection fileSection) throws BalloonException {
		return new ConfigGuiMenu(fileSection);
	}

	protected void loadGuiFromFile(ConfigurationSection fileSectionGui) throws BalloonException {
		setGuiMenu(buildConfigGuiMenuFromFile(fileSectionGui));
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams(), true);

		if (heliumSection.isSection(ParamSection.WORLDSETS)) {
			loadWorldsetsFromFile(fileSection.getConfigurationSection(ParamSection.WORLDSETS.getAttributeName()));
		} else {
			addWorldset(buildDefaultConfigWorldset());
		}

		if (heliumSection.isSection(ParamSection.RULES)) {
			loadRulesFromFile(fileSection.getConfigurationSection(ParamSection.RULES.getAttributeName()));
		} else {
			addRule(buildDefaultConfigRule());
		}

		if (heliumSection.isSection(ParamSection.TEMPLATES)) {
			loadTemplatesFromFile(fileSection.getConfigurationSection(ParamSection.TEMPLATES.getAttributeName()));
		} else {
			throw new BalloonException(null, "Templates definition is missing", null);
		}

		if (heliumSection.isSection(ParamSection.PETS)) {
			loadPetsFromFile(fileSection.getConfigurationSection(ParamSection.PETS.getAttributeName()));
		}

		if (heliumSection.isSection(ParamSection.WALLS)) {
			loadWallsFromFile(fileSection.getConfigurationSection(ParamSection.WALLS.getAttributeName()));
		}

		if (heliumSection.isSection(ParamSection.ROTATORS)) {
			loadRotatorsFromFile(fileSection.getConfigurationSection(ParamSection.ROTATORS.getAttributeName()));
		}

		if (heliumSection.isSection(ParamSection.GUI)) {
			loadGuiFromFile(fileSection.getConfigurationSection(ParamSection.GUI.getAttributeName()));
		}
	}

}
