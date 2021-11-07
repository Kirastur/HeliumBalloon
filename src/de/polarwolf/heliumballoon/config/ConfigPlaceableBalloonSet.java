package de.polarwolf.heliumballoon.config;

import org.bukkit.World;
import org.bukkit.util.Vector;

public interface ConfigPlaceableBalloonSet extends ConfigBalloonSet {

	public Vector getAbsolutePosition();

	public boolean isMatchingWorld(World world);

}
