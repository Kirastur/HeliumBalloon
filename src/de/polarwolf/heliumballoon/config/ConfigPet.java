package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumSection;
import de.polarwolf.heliumballoon.tools.helium.HeliumText;

public class ConfigPet implements ConfigBalloonSet {

	private final String name;
	private final String fullName;
	private List<ConfigTemplate> templates = new ArrayList<>();
	private Material icon;
	private HeliumText title;
	private HeliumText description;

	public ConfigPet(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}

	public ConfigPet(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
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

	@Override
	public ConfigTemplate findTemplate(World world) {
		String worldName = world.getName();
		for (ConfigTemplate myTemplate : templates) {
			if (myTemplate.hasWorld(worldName)) {
				return myTemplate;
			}
		}
		return null;
	}

	public List<ConfigTemplate> getTemplates() {
		return new ArrayList<>(templates);
	}

	protected void addTemplate(ConfigTemplate newTemplate) {
		templates.add(newTemplate);
	}

	protected void addAllTemplates(List<ConfigTemplate> newTemplates) {
		templates.addAll(newTemplates);
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

	@Override
	public boolean isSuitableFor(BalloonPurpose purpose) {
		boolean isSuitable = true;
		for (ConfigTemplate myTemplate : templates) {
			isSuitable = isSuitable && myTemplate.isSuitableFor(purpose);
		}
		return isSuitable;
	}

	protected List<ConfigTemplate> getTemplatesFromNames(String templateNames, ConfigSection balloonSection)
			throws BalloonException {
		List<ConfigTemplate> templateList = new ArrayList<>();
		if ((templateNames == null) || (templateNames.isEmpty())) {
			throw new BalloonException(getFullName(), "Template is missing", null);
		}

		for (String myTemplateName : templateNames.split(" ")) {
			ConfigTemplate myTemplate = balloonSection.findTemplate(myTemplateName);
			if (myTemplate == null) {
				throw new BalloonException(getFullName(), "Unknown template", myTemplateName);
			}
			templateList.add(myTemplate);
		}
		return templateList;
	}

	protected List<HeliumParam> getValidParams() {
		return Arrays.asList(ParamPet.values());
	}

	protected void importHeliumSection(HeliumSection heliumSection, ConfigSection balloonSection)
			throws BalloonException {
		String templateNames = heliumSection.getString(ParamPet.TEMPLATES);
		addAllTemplates(getTemplatesFromNames(templateNames, balloonSection));

		String iconName = heliumSection.getString(ParamPet.ICON);
		setIcon(ConfigUtils.getAnyMaterialFromName(getFullName(), iconName));

		setTitle(heliumSection.getHeliumText(ParamPet.TITLE));
		setDescription(heliumSection.getHeliumText(ParamPet.DESCRIPTION));
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection, ConfigSection balloonSection)
			throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection, balloonSection);
	}

	protected List<String> templateListAsDump() {
		List<String> newTemplateListDump = new ArrayList<>();
		for (ConfigTemplate myTemplate : templates)
			newTemplateListDump.add(myTemplate.getName());
		return newTemplateListDump;
	}

	protected List<String> paramListAsDump() {
		String fs = "%s: \"%s\"";
		List<String> newParamListDump = new ArrayList<>();
		newParamListDump
				.add(String.format(fs, ParamPet.TEMPLATES.getAttributeName(), String.join(" ", templateListAsDump())));
		if (icon != null)
			newParamListDump.add(String.format(fs, ParamPet.ICON.getAttributeName(), icon.toString()));
		if (title != null)
			for (Entry<String, String> myEntry : title.dump().entrySet())
				newParamListDump.add(String.format(fs, myEntry.getKey(), myEntry.getValue()));
		if (description != null)
			for (Entry<String, String> myEntry : description.dump().entrySet())
				newParamListDump.add(String.format(fs, myEntry.getKey(), myEntry.getValue()));
		return newParamListDump;
	}

	@Override
	public String toString() {
		return String.format("%s: { %s }", getName(), String.join(", ", paramListAsDump()));
	}
}
