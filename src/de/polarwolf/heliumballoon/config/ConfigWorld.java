package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigWorld {
	
	public static final String PARAM_INCLUDE_ALL_WORLDS = "includeAllWorlds";
	public static final String PARAM_INCLUDE_LIST = "include";
	public static final String PARAM_EXCLUDE_LIST = "exclude";

	protected boolean includeAllWorlds = true;
	protected final List<String> includeWorldNames = new ArrayList<>();
	protected final List<String> excludeWorldNames = new ArrayList<>();
	
	
	public ConfigWorld() {
		// Dummy
	}
	
		
	public ConfigWorld(boolean includeAllWorlds, List<String> includeWorldNames, List<String> excludeWorldNames) {
		this.includeAllWorlds = includeAllWorlds;
		this.includeWorldNames.addAll(includeWorldNames);
		this.excludeWorldNames.addAll(excludeWorldNames);
	}
	

	public ConfigWorld(ConfigurationSection fileSection) {
		loadConfig(fileSection);		
	}
	
	
	protected void loadConfig(ConfigurationSection fileSection) {
		includeAllWorlds = fileSection.getBoolean(PARAM_INCLUDE_ALL_WORLDS, true);

		includeWorldNames.clear();
		includeWorldNames.addAll(fileSection.getStringList(PARAM_INCLUDE_LIST));
		
		excludeWorldNames.clear();
		excludeWorldNames.addAll(fileSection.getStringList(PARAM_EXCLUDE_LIST));	
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
		
		return false;
	}

}
