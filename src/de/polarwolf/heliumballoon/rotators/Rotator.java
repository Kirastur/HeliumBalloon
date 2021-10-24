package de.polarwolf.heliumballoon.rotators;

import org.bukkit.World;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.chunktickets.ChunkTicketManager;
import de.polarwolf.heliumballoon.config.ConfigPlaceableBalloonSet;
import de.polarwolf.heliumballoon.placings.Placing;

public class Rotator extends Placing {

	public Rotator(ChunkTicketManager chunkTicketManager, BalloonManager balloonManager, ConfigPlaceableBalloonSet configPlaceableBalloonSet, World world) {
		super(chunkTicketManager, balloonManager, configPlaceableBalloonSet, world);
		generateOscillator();
		getOscillator().setSpinState(true);
		getOscillator().setDeflectionState(configPlaceableBalloonSet.getTemplate().isOscillating());
		prepareBalloons(BalloonPurpose.ROTATOR);
	}

}
