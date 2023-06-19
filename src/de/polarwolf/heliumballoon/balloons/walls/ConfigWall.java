package de.polarwolf.heliumballoon.balloons.walls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.balloons.placeable.ConfigPlaceable;
import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.config.ConfigHelper;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.config.templates.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumSection;

public class ConfigWall implements ConfigPlaceable {

	private final String name;
	private final String fullName;
	private final BalloonDefinition balloonDefinition;
	private ConfigTemplate template;
	private BehaviorDefinition behavior;
	private Vector absolutePosition = new Vector(0, 0, 0);
	private String worldName = null;

	protected final ConfigHelper configHelper;

	public ConfigWall(String name, String fullName, BalloonDefinition balloonDefinition, ConfigHelper configHelper) {
		this.name = name;
		this.fullName = fullName;
		this.balloonDefinition = balloonDefinition;
		this.configHelper = configHelper;
	}

	public ConfigWall(String name, String fullName, BalloonDefinition balloonDefinition, ConfigHelper configHelper,
			ConfigTemplate template, BehaviorDefinition behavior, Location location) {
		this.name = name;
		this.fullName = fullName;
		this.balloonDefinition = balloonDefinition;
		this.configHelper = configHelper;
		setTemplate(template);
		setBehavior(behavior);
		setAbsolutePosition(location.toVector());
		setWorldName(location.getWorld().getName());
	}

	public ConfigWall(BalloonDefinition balloonDefinition, ConfigHelper configHelper, ConfigurationSection fileSection,
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
		if (template.hasWorld(world.getName())) {
			return template;
		} else {
			return null;
		}
	}

	@Override
	public List<ConfigTemplate> listUsedTemplates() {
		List<ConfigTemplate> templateList = new ArrayList<>();
		templateList.add(template);
		return templateList;
	}

	protected ConfigTemplate getTemplate() {
		return template;
	}

	protected void setTemplate(ConfigTemplate template) {
		this.template = template;
	}

	@Override
	public BehaviorDefinition getBehavior() {
		return behavior;
	}

	protected void setBehavior(BehaviorDefinition newBehavior) {
		behavior = newBehavior;
	}

	@Override
	public Vector getAbsolutePosition() {
		return absolutePosition;
	}

	protected void setAbsolutePosition(Vector absolutePosition) {
		this.absolutePosition = absolutePosition;
	}

	public String getWorldName() {
		return worldName;
	}

	protected void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	@Override
	public boolean isMatchingWorld(World world) {
		return ((worldName == null) || worldName.isEmpty() || worldName.equals(world.getName()));
	}

	protected ConfigTemplate getTemplateFromName(String templateName, ConfigSection balloonSection)
			throws BalloonException {
		if ((templateName == null) || (templateName.isEmpty())) {
			throw new BalloonException(getFullName(), "Template is missing", null);
		}

		ConfigTemplate myTemplate = balloonSection.findTemplate(templateName);
		if (myTemplate == null) {
			throw new BalloonException(getFullName(), "Unknown template", templateName);
		}
		return myTemplate;
	}

	protected void checkY64Bug() throws BalloonException {
		if (!template.getRule().isEnableWarnY64Walls()) {
			return;
		}
		for (ConfigElement myElement : template.getElements()) {
			double y = getAbsolutePosition().getY();
			double yMin = y + myElement.getMinYOffset();
			double yMax = y + myElement.getMaxYOffset();
			if ((yMin < 64.1) && (yMax > 63.7)) {
				String range = String.format("%f-%f", yMin, yMax);
				throw new BalloonException(getFullName(),
						"A wall element cannot be positioned between y=63.7 and y=64.1", range);
			}
		}
	}

	protected List<HeliumParam> getValidParams() {
		return Arrays.asList(ParamWall.values());
	}

	protected void importHeliumSection(HeliumSection heliumSection, ConfigSection balloonSection)
			throws BalloonException {
		String templateName = heliumSection.getString(ParamWall.TEMPLATE);
		setWorldName(heliumSection.getString(ParamWall.WORLD));
		Double x = heliumSection.getDouble(ParamWall.X, getAbsolutePosition().getX());
		Double y = heliumSection.getDouble(ParamWall.Y, getAbsolutePosition().getY());
		Double z = heliumSection.getDouble(ParamWall.Z, getAbsolutePosition().getZ());

		setTemplate(getTemplateFromName(templateName, balloonSection));
		setAbsolutePosition(new Vector(x, y, z));

		String behaviorName = heliumSection.getString(ParamWall.BEHAVIOR);
		if ((behaviorName == null) || behaviorName.isEmpty()) {
			behaviorName = balloonDefinition.getDefaultBehaviorName();
		}
		behavior = configHelper.getBehaviorDefinition(behaviorName);
	}

	protected void validateConfig() throws BalloonException {
		checkY64Bug();
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection, ConfigSection balloonSection)
			throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection, balloonSection);
		validateConfig();
	}

	protected List<String> paramListAsDump() {
		String fs = "%s: \"%s\"";
		String fb = "%s: %s";
		List<String> newParamListDump = new ArrayList<>();
		if (template != null)
			newParamListDump.add(String.format(fs, ParamWall.TEMPLATE.getAttributeName(), template.getName()));
		if (worldName != null)
			newParamListDump.add(String.format(fs, ParamWall.WORLD.getAttributeName(), worldName));
		if (absolutePosition.getX() != 0)
			newParamListDump
					.add(String.format(fb, ParamWall.X.getAttributeName(), Double.toString(absolutePosition.getX())));
		if (absolutePosition.getY() != 0)
			newParamListDump
					.add(String.format(fb, ParamWall.Y.getAttributeName(), Double.toString(absolutePosition.getY())));
		if (absolutePosition.getZ() != 0)
			newParamListDump
					.add(String.format(fb, ParamWall.Z.getAttributeName(), Double.toString(absolutePosition.getZ())));
		if (!balloonDefinition.getDefaultBehaviorName().equals(behavior.getName())) {
			newParamListDump.add(String.format(fs, ParamWall.BEHAVIOR.getAttributeName(), behavior.getName()));
		}
		return newParamListDump;
	}

	@Override
	public String toString() {
		return String.format("%s: { %s }", getName(), String.join(", ", paramListAsDump()));
	}

}
