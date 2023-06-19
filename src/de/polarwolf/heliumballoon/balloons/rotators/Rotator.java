package de.polarwolf.heliumballoon.balloons.rotators;

import org.bukkit.World;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.balloons.placeable.ConfigPlaceable;
import de.polarwolf.heliumballoon.balloons.placeable.Placeable;
import de.polarwolf.heliumballoon.behavior.observers.ObserverManager;
import de.polarwolf.heliumballoon.tools.chunktickets.ChunkTicketManager;

public class Rotator extends Placeable {

	public Rotator(BalloonDefinition balloonDefinition, ChunkTicketManager chunkTicketManager,
			ObserverManager observerManager, ConfigPlaceable configPlaceable, World world) {
		super(balloonDefinition, chunkTicketManager, observerManager, configPlaceable, world);
		generateOscillator();
		getOscillator().setSpinState(true);
		getOscillator().setDeflectionState(configPlaceable.findTemplate(world).isOscillating());
		prepareObservers();
	}

}
