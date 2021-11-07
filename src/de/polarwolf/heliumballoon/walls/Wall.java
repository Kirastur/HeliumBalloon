package de.polarwolf.heliumballoon.walls;

import org.bukkit.World;

import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.chunktickets.ChunkTicketManager;
import de.polarwolf.heliumballoon.config.ConfigPlaceableBalloonSet;
import de.polarwolf.heliumballoon.placings.Placing;

public class Wall extends Placing {

	public Wall(ChunkTicketManager chunkTicketManager, BalloonManager balloonManager,
			ConfigPlaceableBalloonSet configPlaceableBalloonSet, World world) {
		super(chunkTicketManager, balloonManager, configPlaceableBalloonSet, world);
		if (configPlaceableBalloonSet.findTemplate(world).isOscillating()) {
			generateOscillator();
			getOscillator().setDeflectionState(true);
		}
		prepareBalloons(BalloonPurpose.WALL);
	}

}
