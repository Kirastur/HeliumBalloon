package de.polarwolf.heliumballoon.behavior.observers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.behavior.oscillators.Oscillator;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.exception.BalloonException;

public abstract class FollowingObserver extends SimpleObserver {

	protected boolean highSpeedMode = false;
	protected Vector lastPosition = null;

	protected FollowingObserver(BehaviorDefinition behaviorDefinition, ConfigBalloon configBalloon,
			ConfigElement configElement, Oscillator oscillator, Player player) throws BalloonException {
		super(behaviorDefinition, configBalloon, configElement, oscillator, player);
	}

	protected FollowingObserver(BehaviorDefinition behaviorDefinition, ConfigBalloon configBalloon,
			ConfigElement configElement, Oscillator oscillator, World world) throws BalloonException {
		super(behaviorDefinition, configBalloon, configElement, oscillator, world);
	}

	protected abstract Location getMasterLocation();

	protected Vector getMasterXZDirection() {
		Vector myDirection = getMasterLocation().getDirection().clone();
		myDirection.setY(0);
		myDirection.normalize();
		return myDirection;
	}

	protected Vector calculateOffset() {
		Vector newOffset = getMasterXZDirection();
		double angle = getRule().getAngleFromPlayerDirection();
		angle = Math.toRadians(angle);
		newOffset.rotateAroundY(angle);
		newOffset.multiply(getRule().getDistanceFromPlayer());
		newOffset.setY(getRule().getHighAbovePlayer());
		return newOffset;
	}

	@Override
	public Vector getTargetPosition() {
		Location location = getMasterLocation();
		if (location != null) {
			return location.toVector().add(calculateOffset());
		} else {
			return null;
		}
	}

	@Override
	protected Vector getVelocity(Vector currentPosition, Vector nextPosition) {
		Vector movingDirection = nextPosition.clone().subtract(currentPosition);
		double movingDistance = movingDirection.length();
		double normalSpeed = getRule().getNormalSpeed();

		// First test if we can reach the destination in one step
		if (movingDistance < normalSpeed) {
			lastPosition = nextPosition;
			return movingDirection;
		}

		// Calculate possible high speed
		double highSpeed = normalSpeed;
		if (lastPosition != null) {
			double positionDistance = nextPosition.distance(lastPosition);
			if (positionDistance > normalSpeed) {
				highSpeed = positionDistance;
			}
		}
		lastPosition = nextPosition;

		// Normal speed means follow the pet with a given speed.
		// High speed means move with the same speed as the player (or master).

		// Avoid tremble between HighSpeed and NormalSpeed
		double switchToFastSpeedAtDistance = getRule().getSwitchToFastSpeedAtDistance();
		if (movingDistance - switchToFastSpeedAtDistance > 0.25) {
			highSpeedMode = true;
		}
		if (movingDistance - switchToFastSpeedAtDistance <= -0.25) {
			highSpeedMode = false;
		}

		movingDirection.normalize();
		if (highSpeedMode) {
			movingDirection.multiply(highSpeed);
		} else {
			movingDirection.multiply(normalSpeed);
		}

		return movingDirection;
	}

}
