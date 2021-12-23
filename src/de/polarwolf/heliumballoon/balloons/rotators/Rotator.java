package de.polarwolf.heliumballoon.balloons.rotators;

import org.bukkit.World;

import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.balloons.placings.Placing;
import de.polarwolf.heliumballoon.config.ConfigPlaceableBalloonSet;
import de.polarwolf.heliumballoon.observers.ObserverManager;
import de.polarwolf.heliumballoon.tools.chunktickets.ChunkTicketManager;

public class Rotator extends Placing {

	public Rotator(ChunkTicketManager chunkTicketManager, ObserverManager observerManager,
			ConfigPlaceableBalloonSet configPlaceableBalloonSet, World world) {
		super(chunkTicketManager, observerManager, configPlaceableBalloonSet, world);
		generateOscillator();
		getOscillator().setSpinState(true);
		getOscillator().setDeflectionState(configPlaceableBalloonSet.findTemplate(world).isOscillating());
		prepareObservers(BalloonPurpose.ROTATOR);
	}

}
