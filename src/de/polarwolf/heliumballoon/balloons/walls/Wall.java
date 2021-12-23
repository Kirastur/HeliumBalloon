package de.polarwolf.heliumballoon.balloons.walls;

import org.bukkit.World;

import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.balloons.placings.Placing;
import de.polarwolf.heliumballoon.config.ConfigPlaceableBalloonSet;
import de.polarwolf.heliumballoon.observers.ObserverManager;
import de.polarwolf.heliumballoon.tools.chunktickets.ChunkTicketManager;

public class Wall extends Placing {

	public Wall(ChunkTicketManager chunkTicketManager, ObserverManager observerManager,
			ConfigPlaceableBalloonSet configPlaceableBalloonSet, World world) {
		super(chunkTicketManager, observerManager, configPlaceableBalloonSet, world);
		if (configPlaceableBalloonSet.findTemplate(world).isOscillating()) {
			generateOscillator();
			getOscillator().setDeflectionState(true);
		}
		prepareObservers(BalloonPurpose.WALL);
	}

}
