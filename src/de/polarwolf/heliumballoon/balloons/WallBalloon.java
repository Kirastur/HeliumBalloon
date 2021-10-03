package de.polarwolf.heliumballoon.balloons;

import org.bukkit.Location;

import de.polarwolf.heliumballoon.config.ConfigWall;
import de.polarwolf.heliumballoon.oscillators.Oscillator;

public class WallBalloon extends SimpleBalloon {
	
	protected final ConfigWall configWall;
	protected final Location fixedLocation;
	
	
	public WallBalloon(ConfigWall configWall, Location fixedLocation, Oscillator oscillator) {
		super(null, configWall.getTemplate().getRule(), configWall.getTemplate().getCompound(), oscillator);
		this.configWall = configWall;
		this.fixedLocation = fixedLocation;
	}
	

	@Override
	public String getName() {
		return configWall.getName();
	}
	
	
	@Override
	public String getFullName() {
		return configWall.getFullName();
	}


	@Override
	public Location getTargetLocation() {
		return fixedLocation;
	}

}
