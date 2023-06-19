package de.polarwolf.heliumballoon.balloons.placeable;

import org.bukkit.World;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;

public interface ConfigPlaceable extends ConfigBalloon {

	public Vector getAbsolutePosition();

	public boolean isMatchingWorld(World world);

}
