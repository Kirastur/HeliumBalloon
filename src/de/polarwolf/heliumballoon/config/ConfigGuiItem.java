package de.polarwolf.heliumballoon.config;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumName;
import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumSection;
import de.polarwolf.heliumballoon.helium.HeliumText;

public class ConfigGuiItem implements HeliumName {
	
	private final String name;
	private final String fullName;
	private ConfigTemplate template;
	private Material icon;
	private HeliumText title;
	private HeliumText description;


	public ConfigGuiItem(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}
	
	
	public ConfigGuiItem(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
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


	public ConfigTemplate getTemplate() {
		return template;
	}

	
	protected void setTemplate(ConfigTemplate template) {
		this.template = template;
	}

	
	public String getTitle(CommandSender sender) {
		if (title == null) {
			return null;
		} else {
			return title.findLocalizedforSender(sender);
		}
	}

	
	protected void setTitle(HeliumText title) {
		this.title = title;
	}


	public String getDescription(CommandSender sender) {
		if (description == null) {
			return null;
		} else {
			return description.findLocalizedforSender(sender);
		}
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
			throw new BalloonException(getFullName(), "Template is missing", null);
		}

		ConfigTemplate myTemplate = balloonSection.findTemplate(templateName);
		if (myTemplate == null) {
			throw new BalloonException(getFullName(), "Unknown template", templateName);
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
		setIcon(ConfigUtils.getAnyMaterialFromName(getFullName(), iconName));

		setTitle(heliumSection.getHeliumText(ParamGuiItem.TITLE)); 
		setDescription(heliumSection.getHeliumText(ParamGuiItem.DESCRIPTION));		
	}
	

	protected void loadConfigFromFile(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection, balloonSection);
	}
}
