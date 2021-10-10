package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumName;
import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumSection;

public class ConfigWorld implements HeliumName {
	
	private final String name;
	private final String fullName;

	private boolean includeAllWorlds = true;
	private List<String> includeWorldNames = new ArrayList<>();
	private List<String> excludeWorldNames = new ArrayList<>();
	
	
	public ConfigWorld(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}


	public ConfigWorld(ConfigurationSection fileSection) throws BalloonException {
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

	
	protected List<HeliumParam> getValidParams() {
		return  Arrays.asList(ParamWorld.values());
	}
	
	
	protected void importHeliumSection(HeliumSection heliumSection) throws BalloonException { 
		setIncludeAllWorlds(heliumSection.getBoolean(ParamWorld.INCLUDE_ALL, isIncludeAllWorlds()));
	}
	
	
	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);
		
		if (heliumSection.isList(ParamWorld.INCLUDE_LIST)) {
			List <String> newIncludeWorldNames = fileSection.getStringList(ParamWorld.INCLUDE_LIST.getAttributeName());
			addIncludeWorldNames (newIncludeWorldNames);
		}						

		if (heliumSection.isList(ParamWorld.EXCLUDE_LIST)) {
			List <String> newExcludeWorldNames = fileSection.getStringList(ParamWorld.EXCLUDE_LIST.getAttributeName());
			addExcludeWorldNames (newExcludeWorldNames);
		}						
	}
}
