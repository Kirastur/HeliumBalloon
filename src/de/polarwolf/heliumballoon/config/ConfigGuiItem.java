package de.polarwolf.heliumballoon.config;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumSection;
import de.polarwolf.heliumballoon.helium.HeliumText;

public class ConfigGuiItem {
	
	private final String name;
	private ConfigTemplate template;
	private Material icon;
	private HeliumText title;
	private HeliumText description;


	public ConfigGuiItem(String name) {
		this.name = name;
	}
	
	
	public ConfigGuiItem(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		this.name = fileSection.getName();
		loadConfigFromFile(fileSection, balloonSection);
	}
	
	
	public String getName() {
		return name;
	}


	public ConfigTemplate getTemplate() {
		return template;
	}

	
	protected void setTemplate(ConfigTemplate template) {
		this.template = template;
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


	public Material getIcon() {
		return icon;
	}

	
	protected void setIcon(Material icon) {
		this.icon = icon;
	}


	protected ConfigTemplate getTemplateFromName(String templateName, ConfigSection balloonSection) throws BalloonException {
		if ((templateName == null) || (templateName.isEmpty())) {
			throw new BalloonException(getName(), "Template is missing", null);
		}

		ConfigTemplate myTemplate = balloonSection.findTemplate(templateName);
		if (myTemplate == null) {
			throw new BalloonException(getName(), "Unknown template", templateName);
		}
		return myTemplate;
	}

		
	protected List<HeliumParam> getValidParams() {
		return  Arrays.asList(ParamGuiItem.values());
	}
	
	
	protected void importHeliumSection(HeliumSection heliumSection, ConfigSection balloonSection) throws BalloonException { 
		String templateName = heliumSection.getString(ParamGuiItem.TEMPLATE);
		setTemplate(getTemplateFromName(templateName, balloonSection));
		
		String iconName =  heliumSection.getString(ParamGuiItem.ICON);
		setIcon(ConfigUtils.getMaterialFromName(getName(), iconName));

		setTitle(heliumSection.getHeliumText(ParamGuiItem.TITLE)); 
		setDescription(heliumSection.getHeliumText(ParamGuiItem.DESCRIPTION));		
	}
	

	protected void loadConfigFromFile(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection, balloonSection);
	}
}
