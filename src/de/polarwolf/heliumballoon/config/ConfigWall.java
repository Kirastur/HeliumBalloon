package de.polarwolf.heliumballoon.config;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumSection;

public class ConfigWall {
	
	public static final String DEFAULT_WORLDS = "^world$";

	private final String name;
	private ConfigTemplate template;
	private Vector absolutePosition = new Vector (0,0,0);
	private Pattern worlds;
	
	
	public ConfigWall(String name) {
		this.name = name;
	}


	public ConfigWall(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
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


	public boolean isMatchingWorld(World world) {
		if (worlds == null) {
			return true;			
		}
		String worldName = world.getName();
		Matcher matcher = worlds.matcher(worldName);
		return matcher.matches();
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
	
	
	protected Pattern getWorldPatternFromName(String worldName) throws BalloonException {
		if ((worldName == null) || (worldName.isEmpty())) {
			throw new BalloonException(getName(), "World Regex is missing", null);
		}

		if (!worldName.startsWith("^")) {
			throw new BalloonException(getName(), "Word Regex should start with \"^\"", worldName);
		}
		try {
			return Pattern.compile(worldName);
		} catch (Exception e) {
			throw new BalloonException(getName(), "World Value is not RegEx", worldName);
		}
	}
	
	
	protected void checkY64Bug() throws BalloonException {
		if (!getTemplate().hasCompound() || !getTemplate().getRule().isEnableWarnY64Walls()) {
			return;
		}
		double y = getAbsolutePosition().getY();
		for (ConfigElement myConfigElement : getTemplate().getCompound().enumElements()) {
			double eY = y + myConfigElement.getOffset().getY();
			if ((eY > 63.7) && (eY < 64.1)) {
				throw new BalloonException(getName(), "Due to a bug a wall element cannot be positioned between y=63.7 and y=64.1", null);
			}	
		}	
	}

		
	protected void loadConfig(ConfigurationSection fileSection, ConfigSection balloonSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, Arrays.asList(ParamWall.values()));

		String templateName = heliumSection.getString(ParamWall.TEMPLATE);
		String worldName = heliumSection.getString(ParamWall.WORLDS, DEFAULT_WORLDS);
		Double x = heliumSection.getDouble(ParamWall.X, getAbsolutePosition().getX());
		Double y = heliumSection.getDouble(ParamWall.Y, getAbsolutePosition().getY());
		Double z = heliumSection.getDouble(ParamWall.Z, getAbsolutePosition().getZ());
		
		setTemplate(getTemplateFromName(templateName, balloonSection));		
		setAbsolutePosition(new Vector(x, y, z));
		setWorlds(getWorldPatternFromName(worldName));
		
		checkY64Bug();
	}

}

