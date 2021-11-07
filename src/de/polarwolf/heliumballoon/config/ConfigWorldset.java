package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumName;
import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumSection;

public class ConfigWorldset implements HeliumName {

	private final String name;
	private final String fullName;

	private boolean defaultWorldset = false;
	private boolean includeAllWorlds = false;
	private Pattern includeRegex = null;
	private List<String> includeWorldNames = new ArrayList<>();
	private List<String> excludeWorldNames = new ArrayList<>();

	public ConfigWorldset(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}

	public ConfigWorldset(String name, String fullName, boolean defaultWorldset, boolean includeAllWorlds) {
		this.name = name;
		this.fullName = fullName;
		this.defaultWorldset = defaultWorldset;
		this.includeAllWorlds = includeAllWorlds;
	}

	public ConfigWorldset(ConfigurationSection fileSection) throws BalloonException {
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

	public boolean isIncludeAllWorlds() {
		return includeAllWorlds;
	}

	protected void setIncludeAllWorlds(boolean includeAllWorlds) {
		this.includeAllWorlds = includeAllWorlds;
	}

	public Pattern getIncludeRegex() {
		return includeRegex;
	}

	protected void setIncludeRegex(Pattern includeRegex) {
		this.includeRegex = includeRegex;
	}

	public List<String> getIncludeWorldNames() {
		return new ArrayList<>(includeWorldNames);
	}

	protected void addIncludeWorldNames(List<String> addIncludeWorldNames) {
		includeWorldNames.addAll(addIncludeWorldNames);
	}

	public List<String> getExcludeWorldNames() {
		return new ArrayList<>(excludeWorldNames);
	}

	protected void addExcludeWorldNames(List<String> addExcludeWorldNames) {
		excludeWorldNames.addAll(addExcludeWorldNames);
	}

	public boolean hasWorld(String worldName) {
		for (String myExcludeWorldName : excludeWorldNames) {
			if (myExcludeWorldName.equals(worldName)) {
				return false;
			}
		}

		if (includeAllWorlds) {
			return true;
		}

		for (String myIncludeWorldName : includeWorldNames) {
			if (myIncludeWorldName.equals(worldName)) {
				return true;
			}
		}

		Matcher matcher = includeRegex.matcher(worldName);
		return matcher.matches();
	}

	public boolean isDefaultWorldset() {
		return defaultWorldset;
	}

	protected void setDefaultWorldset(boolean defaultWorldset) {
		this.defaultWorldset = defaultWorldset;
	}

	protected List<HeliumParam> getValidParams() {
		return Arrays.asList(ParamWorldset.values());
	}

	protected Pattern getPatternFromRegex(String regexString) throws BalloonException {
		if ((regexString == null) || regexString.isEmpty()) {
			return null;
		}
		try {
			return Pattern.compile(regexString);
		} catch (Exception e) {
			throw new BalloonException(getFullName(), "Value is not RegEx", regexString);
		}
	}

	protected void importHeliumSection(HeliumSection heliumSection) throws BalloonException {
		setDefaultWorldset(heliumSection.getBoolean(ParamWorldset.IS_DEFAULT, isDefaultWorldset()));
		setIncludeAllWorlds(heliumSection.getBoolean(ParamWorldset.INCLUDE_ALL, isIncludeAllWorlds()));
		setIncludeRegex(getPatternFromRegex(heliumSection.getString(ParamWorldset.INCLUDE_REGEX, "")));
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);

		if (heliumSection.isList(ParamWorldset.INCLUDE_WORLDS)) {
			List<String> newIncludeWorldNames = fileSection
					.getStringList(ParamWorldset.INCLUDE_WORLDS.getAttributeName());
			addIncludeWorldNames(newIncludeWorldNames);
		}

		if (heliumSection.isList(ParamWorldset.EXCLUDE_WORLDS)) {
			List<String> newExcludeWorldNames = fileSection
					.getStringList(ParamWorldset.EXCLUDE_WORLDS.getAttributeName());
			addExcludeWorldNames(newExcludeWorldNames);
		}
	}
}
