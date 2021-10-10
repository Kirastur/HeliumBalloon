package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumName;
import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumSection;
import de.polarwolf.heliumballoon.helium.HeliumText;

public class ConfigGuiMenu implements HeliumName {

	private final String name;
	private final String fullName;
	private HeliumText guiTitle = new HeliumText("title");
	private Map<String,ConfigGuiItem> guiItems = new TreeMap<>();
	private ConfigGuiDeassign guiDeassign = null;
	

	public ConfigGuiMenu(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}
	
	
	public ConfigGuiMenu(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		this.name = fileSection.getName();
		this.fullName = fileSection.getCurrentPath();
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


	public String getGuiTitle(CommandSender sender) {
		return guiTitle.findLocalizedforSender(sender);
	}

	
	protected void setGuiTitle(HeliumText guiTitle) {
		this.guiTitle = guiTitle;
	}


	public List<ConfigGuiItem> getGuiItems() {
		return new ArrayList<>(guiItems.values());
	}
	
	
	protected void addGuiItem(ConfigGuiItem newItem) {
		guiItems.put(newItem.getName(), newItem);
	}
	
	
	public ConfigGuiDeassign getGuiDeassign() {
		return guiDeassign;
	}


	protected void setGuiDeassign(ConfigGuiDeassign guiDeassign) {
		this.guiDeassign = guiDeassign;
	}


	protected List<HeliumParam> getValidParams() {
		return  Arrays.asList(ParamGuiMenu.values());
	}

	
	protected void importHeliumSection(HeliumSection heliumSection) { 
		setGuiTitle(heliumSection.getHeliumText(ParamGuiMenu.TITLE));
	}
	
	
	protected void loadItemsFromFile(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		ConfigurationSection fileSectionGuiItems = fileSection.getConfigurationSection(ParamGuiMenu.ITEMS.getAttributeName());
		
		for (String myItemName : fileSectionGuiItems.getKeys(false)) {
			if (!fileSectionGuiItems.contains(myItemName, true)) { //ignore default from jar
				continue;
			}
			if (!fileSectionGuiItems.isConfigurationSection(myItemName)) {
				throw new BalloonException (getFullName(), "Illegal GUI items secton", myItemName);
			}
			addGuiItem(new ConfigGuiItem(fileSectionGuiItems.getConfigurationSection(myItemName), balloonSection));
		}
	}
	
	
	protected void loadDeassignFromFile(ConfigurationSection fileSection) throws BalloonException {
		ConfigurationSection fileSectionDeassign = fileSection.getConfigurationSection(ParamGuiMenu.DEASSIGN.getAttributeName());
		setGuiDeassign(new ConfigGuiDeassign(fileSectionDeassign));
	}
		

	protected void loadConfigFromFile(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);
		
		if (heliumSection.isSection(ParamGuiMenu.ITEMS)) {
			loadItemsFromFile(fileSection, balloonSection);
		} else {
			throw new BalloonException (getFullName(), "No GUI Item section found", null);			
		}
		
		if (heliumSection.isSection(ParamGuiMenu.DEASSIGN)) {
			loadDeassignFromFile(fileSection);
		}
	}
	
}
