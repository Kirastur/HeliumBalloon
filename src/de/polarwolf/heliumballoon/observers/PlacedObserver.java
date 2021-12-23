package de.polarwolf.heliumballoon.observers;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigBalloonSet;
import de.polarwolf.heliumballoon.config.ConfigPart;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.oscillators.Oscillator;

public class PlacedObserver extends SimpleObserver {

	protected final Location fixedLocation;

	public PlacedObserver(Location fixedLocation, ConfigBalloonSet configBalloonSet, ConfigPart part,
			Oscillator oscillator) throws BalloonException {
		super(fixedLocation.getWorld(), configBalloonSet, part, oscillator);
		this.fixedLocation = fixedLocation;
	}

	@Override
	public Vector getTargetPosition() {
		return fixedLocation.toVector().clone();
	}

	@Override
	protected Vector getVelocity(Vector currentPosition, Vector nextPosition) {
		return nextPosition.clone().subtract(currentPosition);
	}

}
