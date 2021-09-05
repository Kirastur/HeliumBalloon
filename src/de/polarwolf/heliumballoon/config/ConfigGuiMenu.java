package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumText;

public class ConfigGuiMenu {

	private final String name;
	protected Map<String,ConfigGuiItem> guiItems = new TreeMap<>();
	protected HeliumText guiTitle = new HeliumText("title");
	protected HeliumText deassignTitle = null;
	protected HeliumText deassignDescription = null;
	

	public ConfigGuiMenu(String name) {
		this.name = name;
	}
	
	
	public ConfigGuiMenu(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		this.name = fileSection.getName();
		loadConfig(fileSection, balloonSection);
	}
	
	
	public String getName() {
		return name;
	}


	public String getGuiTitle(CommandSender sender) {
		return guiTitle.findLocalizedforSender(sender);
	}

	
	public List<ConfigGuiItem> enumGuiItems() {
		return new ArrayList<>(guiItems.values());
	}

	
	public boolean hasDeassign() {
		return deassignTitle != null;
	}
	
	
	public String getDeassignTitle(CommandSender sender) {
		if (deassignTitle == null) {
			return "";
		}
		return deassignTitle.findLocalizedforSender(sender);
	}
	

	public String getDeassignDescription(CommandSender sender) {
		if (deassignDescription == null) {
			return "";
		}
		return deassignDescription.findLocalizedforSender(sender);
	}

	
	// The GUI is not participating in HeliumParam
	// because the HeliumParam syntax checker is incompatible with HeliumMessages.
	// This will be resolved in a future version.
	protected void loadConfig(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		guiTitle = new HeliumText(fileSection, ParamGuiMenu.TITLE.getAttributeName());
		guiItems.clear();
		
		if (!fileSection.contains(ParamGuiMenu.ITEMS.getAttributeName(), true) || !fileSection.isConfigurationSection(ParamGuiMenu.ITEMS.getAttributeName())) {
			throw new BalloonException (null, "No GUI Item section found", null);			
		}
		ConfigurationSection fileSectionGuiItems = fileSection.getConfigurationSection(ParamGuiMenu.ITEMS.getAttributeName());
		
		for (String myItemName : fileSectionGuiItems.getKeys(false)) {
			if (!fileSectionGuiItems.contains(myItemName, true)) { //ignore default from jar
				continue;
			}
			if (!fileSectionGuiItems.isConfigurationSection(myItemName)) {
				throw new BalloonException (null, "Illegal GUI items secton", myItemName);
			}
			guiItems.put(myItemName, new ConfigGuiItem(fileSectionGuiItems.getConfigurationSection(myItemName), balloonSection));
		}
		
		if (fileSection.contains(ParamGuiMenu.DEASSIGN.getAttributeName(), true) && fileSection.isConfigurationSection(ParamGuiMenu.DEASSIGN.getAttributeName())) {
			ConfigurationSection fileSectionGuiDeassign = fileSection.getConfigurationSection(ParamGuiMenu.DEASSIGN.getAttributeName());
			deassignTitle = new HeliumText(fileSectionGuiDeassign, ParamGuiItem.TITLE.getAttributeName());
			deassignDescription = new HeliumText(fileSectionGuiDeassign, ParamGuiItem.DESCRIPTION.getAttributeName());
		}	
	}
	
}
