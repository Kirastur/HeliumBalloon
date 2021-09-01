package de.polarwolf.heliumballoon.balloons;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigTemplate;

public abstract class PlayerBalloon extends SimpleBalloon {
	

	protected PlayerBalloon(Player player, ConfigTemplate template) {
		super(player, template);
	}


	protected Vector getPlayerXZDirection() {
		Vector myDirection = getPlayer().getLocation().getDirection().clone();
		myDirection.setY(0);
		myDirection.normalize();		
		return myDirection;
	}
	
		
	protected Vector calculateOffset() {
		Vector newOffset = getPlayerXZDirection();
		newOffset.rotateAroundY (getRule().getAngleFromPlayerDirection() / 57.295779513);
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
