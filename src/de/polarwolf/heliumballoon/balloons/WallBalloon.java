package de.polarwolf.heliumballoon.balloons;

import org.bukkit.Location;

import de.polarwolf.heliumballoon.config.ConfigWall;
import de.polarwolf.heliumballoon.elements.CompoundElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public class WallBalloon extends SimpleBalloon {
	
	protected final ConfigWall configWall;
	protected final Location fixedLocation;
	
	
	public WallBalloon(ConfigWall configWall, Location fixedLocation) {
		super(null, configWall.getTemplate());
		this.configWall = configWall;
		this.fixedLocation = fixedLocation;
	}
	

	@Override
	public Location getTargetLocation() {
		return fixedLocation;
	}


	@Override
	public String getName() {
		return configWall.getName();
	}


	@Override
	protected Element createElement(SpawnModifier spawnModifier) {
		return new CompoundElement(null, getTemplate(), spawnModifier);
	}
	
}
