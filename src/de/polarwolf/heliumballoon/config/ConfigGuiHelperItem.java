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

public class ConfigGuiHelperItem implements HeliumName {

	private final String name;
	private final String fullName;
	private HeliumText title;
	private HeliumText description;
	private Material icon;

	public ConfigGuiHelperItem(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}

	public ConfigGuiHelperItem(ConfigurationSection fileSection) throws BalloonException {
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

	protected List<HeliumParam> getValidParams() {
		return Arrays.asList(ParamGuiHelperItem.values());
	}

	protected void importHeliumSection(HeliumSection heliumSection) throws BalloonException {
		setTitle(heliumSection.getHeliumText(ParamGuiHelperItem.TITLE));
		setDescription(heliumSection.getHeliumText(ParamGuiHelperItem.DESCRIPTION));
		String iconName = heliumSection.getString(ParamGuiHelperItem.ICON);
		setIcon(ConfigUtils.getAnyMaterialFromName(getFullName(), iconName));
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);
	}
}
