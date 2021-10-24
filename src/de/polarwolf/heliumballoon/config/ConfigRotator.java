package de.polarwolf.heliumballoon.config;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumSection;

public class ConfigRotator implements ConfigPlaceableBalloonSet {
	
	public static final String DEFAULT_WORLDS = "^world$";

	private final String name;
	private final String fullName;
	private ConfigTemplate template;
	private Vector absolutePosition = new Vector (0,0,0);
	private Pattern worlds;
	
	
	public ConfigRotator(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}


	public ConfigRotator(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
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
	public ConfigTemplate getTemplate() {
		return template;
	}


	protected void setTemplate(ConfigTemplate template) {
		this.template = template;
	}


	@Override
	public Vector getAbsolutePosition() {
		return absolutePosition;
	}


	protected void setAbsolutePosition(Vector absolutePosition) {
		this.absolutePosition = absolutePosition;
	}
	
	
	public Pattern getWorlds() {
		return worlds;
	}


	protected void setWorlds(Pattern worlds) {
		this.worlds = worlds;
	}


	@Override
	public boolean isMatchingWorld(World world) {
		if (worlds == null) {
			return true;			
		}
		String worldName = world.getName();
		Matcher matcher = getWorlds().matcher(worldName);
		return matcher.matches();
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
	
	
	protected Pattern getWorldPatternFromName(String worldName) throws BalloonException {
		if ((worldName == null) || (worldName.isEmpty())) {
			throw new BalloonException(getFullName(), "World Regex is missing", null);
		}

		if (!worldName.startsWith("^")) {
			throw new BalloonException(getFullName(), "Word Regex should start with \"^\"", worldName);
		}
		try {
			return Pattern.compile(worldName);
		} catch (Exception e) {
			throw new BalloonException(getFullName(), "World Value is not RegEx", worldName);
		}
	}
	
	
	protected List<HeliumParam> getValidParams() {
		return  Arrays.asList(ParamWall.values());
	}
	
	
	protected void importHeliumSection(HeliumSection heliumSection, ConfigSection balloonSection) throws BalloonException {
		String templateName = heliumSection.getString(ParamWall.TEMPLATE);
		String worldName = heliumSection.getString(ParamWall.WORLDS, DEFAULT_WORLDS);
		Double x = heliumSection.getDouble(ParamWall.X, getAbsolutePosition().getX());
		Double y = heliumSection.getDouble(ParamWall.Y, getAbsolutePosition().getY());
		Double z = heliumSection.getDouble(ParamWall.Z, getAbsolutePosition().getZ());
		
		setTemplate(getTemplateFromName(templateName, balloonSection));		
		setAbsolutePosition(new Vector(x, y, z));
		setWorlds(getWorldPatternFromName(worldName));
	}
	
	
	protected void loadConfigFromFile(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection, balloonSection);
	}

}

