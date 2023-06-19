package de.polarwolf.heliumballoon.behavior.observers;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.behavior.oscillators.Oscillator;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.exception.BalloonException;

public class FixedObserver extends SimpleObserver {

	protected final Location fixedLocation;

	public FixedObserver(BehaviorDefinition behaviorDefinition, ConfigBalloon configBalloon, ConfigElement configElement,
			Oscillator oscillator, Location fixedLocation) throws BalloonException {
		super(behaviorDefinition, configBalloon, configElement, oscillator, fixedLocation.getWorld());
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
