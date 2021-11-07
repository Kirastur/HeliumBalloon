package de.polarwolf.heliumballoon.config;

import org.bukkit.World;

import de.polarwolf.heliumballoon.helium.HeliumName;

public interface ConfigBalloonSet extends HeliumName, ConfigPurpose {

	public ConfigTemplate findTemplate(World world);

}
