package de.polarwolf.heliumballoon.elements;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;

public interface ElementDefinition extends HeliumParam {

	public ConfigElement createConfigElement(ConfigurationSection fileSection) throws BalloonException;

}
