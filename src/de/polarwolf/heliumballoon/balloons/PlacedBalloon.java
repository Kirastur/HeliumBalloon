package de.polarwolf.heliumballoon.balloons;

import org.bukkit.Location;

import de.polarwolf.heliumballoon.config.ConfigBalloonSet;
import de.polarwolf.heliumballoon.config.ConfigPart;
import de.polarwolf.heliumballoon.oscillators.Oscillator;

public class PlacedBalloon extends SimpleBalloon {
	
	protected final Location fixedLocation;
	
	
	public PlacedBalloon(Location fixedLocation, ConfigBalloonSet configBalloonSet, ConfigPart part, Oscillator oscillator) {
		super(null, configBalloonSet, part, oscillator);
		this.fixedLocation = fixedLocation;
	}
	

	@Override
	public Location getTargetLocation() {
		return fixedLocation;
	}

}
