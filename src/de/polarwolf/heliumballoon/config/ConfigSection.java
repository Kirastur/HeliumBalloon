package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.config.gui.ConfigGuiMenu;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;
import de.polarwolf.heliumballoon.config.templates.ConfigTemplate;
import de.polarwolf.heliumballoon.config.worldsets.ConfigWorldset;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumName;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumSection;

public class ConfigSection implements HeliumName {

	protected static final String DEFAULT_RULE_NAME = "default";
	protected static final String DEFAULT_WORLDSET_NAME = "default";

	private final String name;
	protected final ConfigHelper configHelper;

	private List<ConfigWorldset> worldsets = new ArrayList<>();
	private List<ConfigRule> rules = new ArrayList<>();
	private List<ConfigTemplate> templates = new ArrayList<>();
	private List<ConfigBalloon> balloons = new ArrayList<>();
	private ConfigGuiMenu guiMenu = null;

	public ConfigSection(String name, ConfigHelper configHelper) {
		this.name = name;
		this.configHelper = configHelper;
	}

	public ConfigSection(String name, ConfigHelper configHelper, ConfigurationSection fileSection)
			throws BalloonException {
		this.name = name;
		this.configHelper = configHelper;
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

	public List<String> getWorlsetNames() {
		List<String> worldsetNames = new ArrayList<>();
		for (ConfigWorldset myWorldset : worldsets) {
			worldsetNames.add(myWorldset.getName());
		}
		return worldsetNames;
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

	public List<String> getRuleNames() {
		List<String> ruleNames = new ArrayList<>();
		for (ConfigRule myRule : rules) {
			ruleNames.add(myRule.getName());
		}
		return ruleNames;
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

	public List<String> getTemplateNames() {
		List<String> templateNames = new ArrayList<>();
		for (ConfigTemplate myTemplate : templates) {
			templateNames.add(myTemplate.getName());
		}
		return templateNames;
	}

	protected void addTemplate(ConfigTemplate newTemplate) {
		templates.add(newTemplate);
	}

	// Balloons
	public ConfigBalloon findBalloon(String balloonName) {
		for (ConfigBalloon myBalloon : balloons) {
			if (myBalloon.getName().equalsIgnoreCase(balloonName)) {
				return myBalloon;
			}
		}
		return null;
	}

	public List<String> getBalloonNames(BalloonDefinition balloonDefinition) {
		List<String> balloonNames = new ArrayList<>();
		for (ConfigBalloon myBalloon : balloons) {
			if (myBalloon.getBalloonDefinition().equals(balloonDefinition)) {
				balloonNames.add(myBalloon.getName());
			}
		}
		return balloonNames;
	}

	public boolean hasBalloon(ConfigBalloon balloonToTest) {
		for (ConfigBalloon myBalloon : balloons) {
			if (myBalloon.equals(balloonToTest)) {
				return true;
			}
		}
		return false;
	}

	protected void addBalloon(ConfigBalloon newBalloon) {
		balloons.add(newBalloon);
	}

	protected void addBalloons(List<ConfigBalloon> newBalloons) {
		balloons.addAll(newBalloons);
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
		List<HeliumParam> validParams = new ArrayList<>();
		validParams.addAll(Arrays.asList(ParamSection.values()));
		validParams.addAll(configHelper.getValidBalloonConfigParams());
		return validParams;
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
		return new ConfigRule(configHelper, fileSection);
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
		return new ConfigTemplate(fileSection, this, configHelper);
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

	protected List<ConfigBalloon> loadBalloonFromFileForDefinition(ConfigurationSection fileSection,
			ConfigSection balloonSection, BalloonDefinition balloonDefinition) throws BalloonException {
		List<ConfigBalloon> configBalloons = new ArrayList<>();
		for (String myBalloonName : fileSection.getKeys(false)) {
			if (!fileSection.contains(myBalloonName, true)) { // ignore default from jar
				continue;
			}
			if (!fileSection.isConfigurationSection(myBalloonName)) {
				throw new BalloonException(null, "Illegal balloon section", myBalloonName);
			}
			ConfigBalloon myConfigBalloon = balloonDefinition.createConfigBalloon(configHelper,
					fileSection.getConfigurationSection(myBalloonName), balloonSection);
			if (myConfigBalloon != null) {
				configBalloons.add(myConfigBalloon);
			}
		}
		return configBalloons;

	}

	protected List<ConfigBalloon> loadBalloonsFromFile(HeliumSection heliumSection, ConfigSection balloonSection)
			throws BalloonException {
		List<ConfigBalloon> configBalloons = new ArrayList<>();
		for (BalloonDefinition myDefinition : configHelper.listBalloonDefinitions()) {
			if (heliumSection.isSection(myDefinition)) {

				ConfigurationSection myFileSectionBalloons = heliumSection.getfileSection()
						.getConfigurationSection(myDefinition.getAttributeName());
				configBalloons
						.addAll(loadBalloonFromFileForDefinition(myFileSectionBalloons, balloonSection, myDefinition));
			}
		}
		return configBalloons;
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

		addBalloons(loadBalloonsFromFile(heliumSection, this));

		if (heliumSection.isSection(ParamSection.GUI)) {
			loadGuiFromFile(fileSection.getConfigurationSection(ParamSection.GUI.getAttributeName()));
		}
	}

	protected List<String> worldsetListAsDump() {
		List<String> newWorldsetListDump = new ArrayList<>();
		for (ConfigWorldset myWorldset : worldsets)
			newWorldsetListDump.add(myWorldset.toString());
		return newWorldsetListDump;
	}

	protected List<String> ruleListAsDump() {
		List<String> newRuleListDump = new ArrayList<>();
		for (ConfigRule myRule : rules)
			newRuleListDump.add(myRule.toString());
		return newRuleListDump;
	}

	protected List<String> templateListAsDump() {
		List<String> newTemplateListDump = new ArrayList<>();
		for (ConfigTemplate myTemplate : templates)
			newTemplateListDump.add(myTemplate.toString());
		return newTemplateListDump;
	}

	protected List<String> balloonListAsDump(BalloonDefinition balloonDefinition) {
		List<String> newBalloonListDump = new ArrayList<>();
		for (ConfigBalloon myBalloon : balloons)
			if (myBalloon.getBalloonDefinition().equals(balloonDefinition)) {
				newBalloonListDump.add(myBalloon.toString());
			}
		return newBalloonListDump;
	}

	protected List<String> paramListAsDump() {
		String fl = "%s: { %s }";
		List<String> newParamListDump = new ArrayList<>();
		newParamListDump.add(
				String.format(fl, ParamSection.WORLDSETS.getAttributeName(), String.join(", ", worldsetListAsDump())));
		newParamListDump
				.add(String.format(fl, ParamSection.RULES.getAttributeName(), String.join(", ", ruleListAsDump())));
		newParamListDump.add(
				String.format(fl, ParamSection.TEMPLATES.getAttributeName(), String.join(", ", templateListAsDump())));
		for (BalloonDefinition myDefinition : configHelper.listBalloonDefinitions()) {
			List<String> myBalloonList = balloonListAsDump(myDefinition);
			if (!myBalloonList.isEmpty()) {
				newParamListDump
						.add(String.format(fl, myDefinition.getAttributeName(), String.join(", ", myBalloonList)));
			}
		}
		if (guiMenu != null)
			newParamListDump.add(guiMenu.toString());
		return newParamListDump;
	}

	@Override
	public String toString() {
		return String.format("%s: { %s }", getName(), String.join(", ", paramListAsDump()));
	}

}
