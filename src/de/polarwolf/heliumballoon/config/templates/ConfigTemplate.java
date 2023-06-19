package de.polarwolf.heliumballoon.config.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.config.ConfigHelper;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;
import de.polarwolf.heliumballoon.config.worldsets.ConfigWorldset;
import de.polarwolf.heliumballoon.elements.ElementDefinition;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumName;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumSection;

public class ConfigTemplate implements HeliumName {

	private final String name;
	private final String fullName;
	protected final ConfigHelper configHelper;

	private ConfigWorldset worldset;
	private ConfigRule rule;
	private boolean oscillating = false;
	private List<ConfigElement> elements = new ArrayList<>();
	private String custom;

	public ConfigTemplate(String name, String fullName, ConfigHelper configHelper) {
		this.name = name;
		this.fullName = fullName;
		this.configHelper = configHelper;
	}

	public ConfigTemplate(ConfigurationSection fileSection, ConfigSection balloonSection, ConfigHelper configHelper)
			throws BalloonException {
		this.name = fileSection.getName();
		this.fullName = fileSection.getCurrentPath();
		this.configHelper = configHelper;
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

	public ConfigTemplate findTemplate(World world) {
		if (hasWorld(world.getName())) {
			return this;
		} else {
			return null;
		}
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

	public String getCustom() {
		return custom;
	}

	protected void setCustom(String custom) {
		this.custom = custom;
	}

	public List<ConfigElement> getElements() {
		return new ArrayList<>(elements);
	}

	protected void addElement(ConfigElement newElement) {
		elements.add(newElement);
	}

	protected void addElements(List<ConfigElement> newElements) {
		elements.addAll(newElements);
	}

	public boolean hasWorld(String worldName) {
		return worldset.hasWorld(worldName);
	}

	public ConfigWorldset getWorldset() {
		return worldset;
	}

	protected void setWorldset(ConfigWorldset worldset) {
		this.worldset = worldset;
	}

	protected ConfigWorldset getWorldsetFromName(String worldsetName, ConfigSection balloonSection)
			throws BalloonException {
		if ((worldsetName == null) || (worldsetName.isEmpty())) {
			return balloonSection.getDefaultWorldset();
		} else {
			ConfigWorldset configWorldset = balloonSection.findWorldset(worldsetName);
			if (configWorldset == null) {
				throw new BalloonException(getFullName(), "Unknown worldset", worldsetName);
			}
			return configWorldset;
		}
	}

	protected ConfigRule getRuleFromName(String ruleName, ConfigSection balloonSection) throws BalloonException {
		if ((ruleName == null) || (ruleName.isEmpty())) {
			return balloonSection.getDefaultRule();
		} else {
			ConfigRule configRule = balloonSection.findRule(ruleName);
			if (configRule == null) {
				throw new BalloonException(getFullName(), "Unknown rule", ruleName);
			}
			return configRule;
		}
	}

	protected List<HeliumParam> getValidParams() {
		List<HeliumParam> validParams = new ArrayList<>();
		validParams.addAll(Arrays.asList(ParamTemplate.values()));
		validParams.addAll(configHelper.getValidElementConfigParams());
		return validParams;
	}

	protected List<ConfigElement> loadElementsFromFile(HeliumSection heliumSection) throws BalloonException {
		List<ConfigElement> configElements = new ArrayList<>();
		for (ElementDefinition myDefinition : configHelper.listElementDefinitions()) {
			if (heliumSection.isSection(myDefinition)) {
				String myName = myDefinition.getAttributeName();
				ConfigurationSection mySection = heliumSection.getfileSection().getConfigurationSection(myName);
				ConfigElement myConfigElement = myDefinition.createConfigElement(mySection);
				if (myConfigElement != null) {
					configElements.add(myConfigElement);
				}
			}
		}
		return configElements;
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection, ConfigSection balloonSection)
			throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());

		String worldsetName = heliumSection.getString(ParamTemplate.WORLDSET);
		setWorldset(getWorldsetFromName(worldsetName, balloonSection));

		String ruleName = heliumSection.getString(ParamTemplate.RULE);
		setRule(getRuleFromName(ruleName, balloonSection));

		setOscillating(heliumSection.getBoolean(ParamTemplate.OSCILLATING, isOscillating()));
		setCustom(heliumSection.getString(ParamTemplate.CUSTOM));

		addElements(loadElementsFromFile(heliumSection));
	}

	protected List<String> paramListAsDump() {
		String fb = "%s: %s";
		List<String> newParamListDump = new ArrayList<>();
		if (worldset != null)
			newParamListDump.add(String.format(fb, ParamTemplate.WORLDSET.getAttributeName(), worldset.getName()));
		if (rule != null)
			newParamListDump.add(String.format(fb, ParamTemplate.RULE.getAttributeName(), rule.getName()));
		if (oscillating)
			newParamListDump
					.add(String.format(fb, ParamTemplate.OSCILLATING.getAttributeName(), Boolean.toString(true)));
		for (ConfigElement myElement : elements)
			newParamListDump.add(myElement.toString());
		return newParamListDump;
	}

	@Override
	public String toString() {
		return String.format("%s: { %s }", getName(), String.join(", ", paramListAsDump()));
	}

}
