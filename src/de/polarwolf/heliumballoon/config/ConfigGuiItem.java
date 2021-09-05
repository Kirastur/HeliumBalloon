package de.polarwolf.heliumballoon.config;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumText;

public class ConfigGuiItem {
	
	private final String name;
	private ConfigTemplate template;
	private Material icon;
	protected HeliumText title;
	private HeliumText description;


	public ConfigGuiItem(String name) {
		this.name = name;
	}
	
	
	public ConfigGuiItem(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		this.name = fileSection.getName();
		loadConfig(fileSection, balloonSection);
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

	
	public String getDescription(CommandSender sender) {
		return description.findLocalizedforSender(sender);
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

		
	// The GUI is not participating in HeliumParam
	// because the HeliumParam syntax checker is incompatible with HeliumMessages.
	// This will be resolved in a future version.
	protected void loadConfig(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		String templateName = fileSection.getString(ParamGuiItem.TEMPLATE.getAttributeName());
		setTemplate(getTemplateFromName(templateName, balloonSection));
		
		String iconName =  fileSection.getString(ParamGuiItem.ICON.getAttributeName());
		setIcon(ConfigUtils.getMaterialFromName(getName(), iconName));

		title = new HeliumText(fileSection, ParamGuiItem.TITLE.getAttributeName());
		description = new HeliumText(fileSection, ParamGuiItem.DESCRIPTION.getAttributeName());
		
	}
}
