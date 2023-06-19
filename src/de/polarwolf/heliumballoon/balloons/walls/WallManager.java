package de.polarwolf.heliumballoon.balloons.walls;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.balloons.placeable.ConfigPlaceable;
import de.polarwolf.heliumballoon.balloons.placeable.Placeable;
import de.polarwolf.heliumballoon.balloons.placeable.PlaceableManager;
import de.polarwolf.heliumballoon.config.ConfigHelper;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;

public class WallManager extends PlaceableManager {

	public static final String BALLOON_TYPE = "walls";

	public WallManager(HeliumBalloonOrchestrator orchestrator, String defaultBehaviorName) {
		super(orchestrator, defaultBehaviorName);
		logger.printDebug("Wallmanager started");
	}

	//
	// Implement the Definition
	//

	@Override
	public String getAttributeName() {
		return BALLOON_TYPE;
	}

	@Override
	public ConfigBalloon createConfigBalloon(ConfigHelper configHelper, ConfigurationSection fileSection,
			ConfigSection balloonSection) throws BalloonException {
		return new ConfigWall(this, configHelper, fileSection, balloonSection);
	}

	//
	// Implements the placeable
	//

	@Override
	protected Wall createPlaceable(ConfigPlaceable configPlaceable, World world) {
		return new Wall(this, chunkTicketManager, observerManager, configPlaceable, world);
	}

	//
	// Now the things specific for rotators
	//

	public ConfigWall findConfigWall(String wallName) {
		ConfigPlaceable configPlaceable = findRelevantConfigBalloon(wallName);
		if (configPlaceable instanceof ConfigWall configWall) {
			return configWall;
		}
		return null;
	}

	public List<Wall> getWalls() {
		List<Wall> wallList = new ArrayList<>();
		for (Placeable myPlacing : getPlaceables()) {
			wallList.add((Wall) myPlacing);
		}
		return wallList;
	}

	public Wall createWall(ConfigWall configWall, World world) throws BalloonException {
		Wall wall = createPlaceable(configWall, world);
		registerPlaceable(wall);
		return wall;
	}

	public void destroyWall(Wall wall) {
		unregisterPlaceable(wall);
	}

}