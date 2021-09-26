package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigWorld {
	
	public static final String PARAM_INCLUDE_ALL_WORLDS = "includeAllWorlds";
	public static final String PARAM_INCLUDE_LIST = "include";
	public static final String PARAM_EXCLUDE_LIST = "exclude";

	private boolean includeAllWorlds = true;
	private List<String> includeWorldNames = new ArrayList<>();
	private List<String> excludeWorldNames = new ArrayList<>();
	
	
	public ConfigWorld() {
		// Dummy
	}
	
		
	public ConfigWorld(boolean includeAllWorlds, List<String> includeWorldNames, List<String> excludeWorldNames) {
		importWorlds(includeAllWorlds, includeWorldNames, excludeWorldNames);
	}
	

	public ConfigWorld(ConfigurationSection fileSection) {
		loadConfig(fileSection);		
	}
	
	
	public boolean isIncludeAllWorlds() {
		return includeAllWorlds;
	}


	protected void setIncludeAllWorlds(boolean includeAllWorlds) {
		this.includeAllWorlds = includeAllWorlds;
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
		
		return false;
	}

	
	protected void importWorlds(boolean newIncludeAllWorlds, List<String> newIncludeWorldNames, List<String> newExcludeWorldNames) {
		setIncludeAllWorlds(newIncludeAllWorlds);
		addIncludeWorldNames(newIncludeWorldNames);
		addExcludeWorldNames(newExcludeWorldNames);
	}

	
	// The GUI is not participating in HeliumParam
	// because the HeliumParam syntax checker is incompatible with entries of type List
	protected void loadConfig(ConfigurationSection fileSection) {
		Boolean newIncludeAllWorlds = fileSection.getBoolean(PARAM_INCLUDE_ALL_WORLDS, true);
		List <String> newIncludeworldNames = fileSection.getStringList(PARAM_INCLUDE_LIST);
		List <String > newExcludeWorldNames = fileSection.getStringList(PARAM_EXCLUDE_LIST);
		importWorlds(newIncludeAllWorlds, newIncludeworldNames, newExcludeWorldNames);
	}
}
