package de.polarwolf.heliumballoon.balloons.walls;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.World;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.balloons.placings.Placing;
import de.polarwolf.heliumballoon.balloons.placings.PlacingManager;
import de.polarwolf.heliumballoon.config.ConfigPlaceableBalloonSet;
import de.polarwolf.heliumballoon.config.ConfigWall;
import de.polarwolf.heliumballoon.exception.BalloonException;

public class WallManager extends PlacingManager {

	public WallManager(HeliumBalloonOrchestrator orchestrator) {
		super(orchestrator);
		logger.printDebug("Wallmanager started");
	}

	@Override
	protected Set<String> getBalloonSetNames() {
		return configManager.getWallNames();
	}

	@Override
	protected ConfigPlaceableBalloonSet findBalloonSet(String placingName) {
		return configManager.findWall(placingName);
	}

	@Override
	protected Wall createPlacing(ConfigPlaceableBalloonSet configPlaceableBalloonSet, World world) {
		return new Wall(chunkTicketManager, observerManager, configPlaceableBalloonSet, world);
	}

	@Override
	public int reload() {
		int count = super.reload();
		if (count > 0) {
			logger.printInfo(String.format("%d walls created in total.", count));
		}
		return count;
	}

	public List<Wall> getWalls() {
		List<Wall> wallList = new ArrayList<>();
		for (Placing myPlacing : getPlacings()) {
			if (myPlacing instanceof Wall) {
				Wall myWall = (Wall) myPlacing;
				wallList.add(myWall);
			}
		}
		return wallList;
	}

	public Wall createWall(ConfigWall configWall, World world) throws BalloonException {
		Wall wall = createPlacing(configWall, world);
		registerPlacing(wall);
		return wall;
	}

	public void destroyWall(Wall wall) {
		unregisterPlacing(wall);
	}

}