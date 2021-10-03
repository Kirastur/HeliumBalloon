package de.polarwolf.heliumballoon.balloons;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigPart;
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.oscillators.Oscillator;

public class PetBalloon extends SimpleBalloon {
	
	private final ConfigTemplate template;
	

	public PetBalloon(Player player, ConfigTemplate template, ConfigPart part, Oscillator oscillator) {
		super(player, template.getRule(), part, oscillator);
		this.template = template;
	}
	
	
	@Override
	public String getName() {
		return template.getName();
	}
	
	
	@Override
	public String getFullName() {
		return template.getFullName();
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
