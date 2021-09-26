package de.polarwolf.heliumballoon.config;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumSection;
import de.polarwolf.heliumballoon.helium.HeliumText;

public class ConfigGuiDeassign {

	private final String name;
	private HeliumText title;
	private HeliumText description;


	public ConfigGuiDeassign(String name) {
		this.name = name;
	}
	
	
	public ConfigGuiDeassign(ConfigurationSection fileSection) throws BalloonException {
		this.name = fileSection.getName();
		loadConfigFromFile(fileSection);
	}
	
	
	public String getName() {
		return name;
	}


	public String getTitle(CommandSender sender) {
		return title.findLocalizedforSender(sender);
	}

	
	protected void setTitle(HeliumText title) {
		this.title = title;
	}

	
	public String getDescription(CommandSender sender) {
		return description.findLocalizedforSender(sender);
	}


	protected void setDescription(HeliumText description) {
		this.description = description;
	}

	
	protected List<HeliumParam> getValidParams() {
		return  Arrays.asList(ParamGuiDeassign.values());
	}
	
	
	protected void importHeliumSection(HeliumSection heliumSection) { 
		setTitle(heliumSection.getHeliumText(ParamGuiDeassign.TITLE)); 
		setDescription(heliumSection.getHeliumText(ParamGuiDeassign.DESCRIPTION));		
	}
	

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);
	}
}
