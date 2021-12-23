package de.polarwolf.heliumballoon.observers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigBalloonSet;
import de.polarwolf.heliumballoon.config.ConfigPart;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.oscillators.Oscillator;

public class PetObserver extends FollowingObserver {
	
	protected Vector lastPlayerPosition = null;

	public PetObserver(Player player, ConfigBalloonSet configBalloonSet, ConfigPart part, Oscillator oscillator)
			throws BalloonException {
		super(player, configBalloonSet, part, oscillator);
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
