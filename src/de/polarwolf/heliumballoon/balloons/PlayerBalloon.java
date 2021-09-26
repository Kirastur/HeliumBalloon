package de.polarwolf.heliumballoon.balloons;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.oscillators.Oscillator;

public abstract class PlayerBalloon extends SimpleBalloon {
	

	protected PlayerBalloon(Player player, ConfigTemplate template, Oscillator oscillator, int delay) {
		super(player, template, oscillator, delay);
	}


	protected Vector getPlayerXZDirection() {
		Vector myDirection = getPlayer().getLocation().getDirection().clone();
		myDirection.setY(0);
		myDirection.normalize();		
		return myDirection;
	}
	
		
	protected Vector calculateOffset() {
		Vector newOffset = getPlayerXZDirection();
		double angle = getRule().getAngleFromPlayerDirection();
		angle = Math.toRadians(angle);
		newOffset.rotateAroundY (angle);
		newOffset.multiply(getRule().getDistanceFromPlayer());
		newOffset.setY(getRule().getHighAbovePlayer());
		return newOffset;
	}
	
	
	@Override
	public Location getTargetLocation() {
		if (getPlayer().isOnline()) {
			return getPlayer().getLocation().clone().add(calculateOffset());
		} else {
			return null;
		}
	}
	
}
