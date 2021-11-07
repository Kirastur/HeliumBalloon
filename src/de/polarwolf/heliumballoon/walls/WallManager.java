package de.polarwolf.heliumballoon.walls;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.World;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.config.ConfigPlaceableBalloonSet;
import de.polarwolf.heliumballoon.placings.Placing;
import de.polarwolf.heliumballoon.placings.PlacingManager;

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
	protected Placing createPlacing(ConfigPlaceableBalloonSet configPlaceableBalloonSet, World world) {
		return new Wall(chunkTicketManager, balloonManager, configPlaceableBalloonSet, world);
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

}