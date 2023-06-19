package de.polarwolf.heliumballoon.behavior.observers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.behavior.oscillators.Oscillator;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.exception.BalloonException;

public class PlayerFollowingObserver extends FollowingObserver {

	protected Vector lastPlayerPosition = null;

	public PlayerFollowingObserver(BehaviorDefinition behaviorDefinition, ConfigBalloon configBalloon,
			ConfigElement configElement, Oscillator oscillator, Player player) throws BalloonException {
		super(behaviorDefinition, configBalloon, configElement, oscillator, player);
	}

	@Override
	protected Location getMasterLocation() {
		if (getPlayer().isOnline() && getPlayer().getWorld().equals(this.getWorld())) {
			return getPlayer().getLocation();
		} else {
			return null;
		}
	}

	@Override
	public Vector getTargetPosition() {
		Location playerLocation = getMasterLocation();
		if (playerLocation == null) {
			return null;
		}
		Vector currentPlayerPosition = playerLocation.toVector();
		if (lastPlayerPosition == null) {
			lastPlayerPosition = currentPlayerPosition;
		}
		double distance = currentPlayerPosition.distance(lastPlayerPosition);
		if (distance >= getRule().getStartMoveAtDeviation()) {
			lastPlayerPosition = currentPlayerPosition;
		}
		return lastPlayerPosition.clone().add(calculateOffset());
	}

}
