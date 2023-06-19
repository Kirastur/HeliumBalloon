package de.polarwolf.heliumballoon.balloons.pets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.config.ConfigHelper;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.config.ConfigUtils;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.config.templates.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumSection;
import de.polarwolf.heliumballoon.tools.helium.HeliumText;

public class ConfigPet implements ConfigBalloon {

	private final String name;
	private final String fullName;
	private final BalloonDefinition balloonDefinition;
	private BehaviorDefinition behavior;
	private List<ConfigTemplate> templates = new ArrayList<>();
	private Material icon;
	private HeliumText title;
	private HeliumText description;

	protected final ConfigHelper configHelper;

	public ConfigPet(String name, String fullName, BalloonDefinition balloonDefinition, ConfigHelper configHelper) {
		this.name = name;
		this.fullName = fullName;
		this.balloonDefinition = balloonDefinition;
		this.configHelper = configHelper;
	}

	public ConfigPet(BalloonDefinition balloonDefinition, ConfigHelper configHelper, ConfigurationSection fileSection,
			ConfigSection balloonSection) throws BalloonException {
		this.name = fileSection.getName();
		this.fullName = fileSection.getCurrentPath();
		this.balloonDefinition = balloonDefinition;
		this.configHelper = configHelper;
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
	public BalloonDefinition getBalloonDefinition() {
		return balloonDefinition;
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

	@Override
	public List<ConfigTemplate> listUsedTemplates() {
		return new ArrayList<>(templates);
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

	@Override
	public BehaviorDefinition getBehavior() {
		return behavior;
	}

	protected void setBehavior(BehaviorDefinition newBehavior) {
		behavior = newBehavior;
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

		String iconName = heliumSection.getString(ParamPet.ICON, "");
		if (iconName.isEmpty()) {
			setIcon(Material.STONE);
		} else {
			setIcon(ConfigUtils.getAnyMaterialFromName(getFullName(), iconName));
		}

		setTitle(heliumSection.getHeliumText(ParamPet.TITLE));
		setDescription(heliumSection.getHeliumText(ParamPet.DESCRIPTION));

		String behaviorName = heliumSection.getString(ParamPet.BEHAVIOR);
		if ((behaviorName == null) || behaviorName.isEmpty()) {
			behaviorName = balloonDefinition.getDefaultBehaviorName();
		}
		setBehavior(configHelper.getBehaviorDefinition(behaviorName));
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
		if (!balloonDefinition.getDefaultBehaviorName().equals(behavior.getName())) {
			newParamListDump.add(String.format(fs, ParamPet.BEHAVIOR.getAttributeName(), behavior.getName()));
		}
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
