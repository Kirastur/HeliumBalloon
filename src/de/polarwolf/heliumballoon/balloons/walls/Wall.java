package de.polarwolf.heliumballoon.balloons.walls;

import org.bukkit.World;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.balloons.placeable.ConfigPlaceable;
import de.polarwolf.heliumballoon.balloons.placeable.Placeable;
import de.polarwolf.heliumballoon.behavior.observers.ObserverManager;
import de.polarwolf.heliumballoon.tools.chunktickets.ChunkTicketManager;

public class Wall extends Placeable {

	public Wall(BalloonDefinition balloonDefinition, ChunkTicketManager chunkTicketManager,
			ObserverManager observerManager, ConfigPlaceable configPlaceable, World world) {
		super(balloonDefinition, chunkTicketManager, observerManager, configPlaceable, world);
		if (configPlaceable.findTemplate(world).isOscillating()) {
			generateOscillator();
			getOscillator().setDeflectionState(true);
		}
		prepareObservers();
	}

}
