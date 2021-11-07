package de.polarwolf.heliumballoon.balloons;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigBalloonSet;
import de.polarwolf.heliumballoon.config.ConfigPart;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.oscillators.Oscillator;

public class PlacedBalloon extends SimpleBalloon {

	protected final Location fixedLocation;

	public PlacedBalloon(Location fixedLocation, ConfigBalloonSet configBalloonSet, ConfigPart part,
			Oscillator oscillator) throws BalloonException {
		super(fixedLocation.getWorld(), configBalloonSet, part, oscillator);
		this.fixedLocation = fixedLocation;
	}

	@Override
	public Vector getTargetPosition() {
		return fixedLocation.toVector().clone();
	}

}
