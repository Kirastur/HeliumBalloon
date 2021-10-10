package de.polarwolf.heliumballoon.walls;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.World;
import de.polarwolf.heliumballoon.balloons.Balloon;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.balloons.WallBalloon;
import de.polarwolf.heliumballoon.chunktickets.ChunkTicketManager;
import de.polarwolf.heliumballoon.chunktickets.ChunkTicketOwner;
import de.polarwolf.heliumballoon.config.ConfigPart;
import de.polarwolf.heliumballoon.config.ConfigWall;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.oscillators.VerticalSinusOscillator;

public class Wall implements ChunkTicketOwner {
		
	protected final ChunkTicketManager chunkTicketManager;
	protected final BalloonManager balloonManager;
	protected final ConfigWall config;
	protected final Oscillator oscillator;
	protected final World world;
	protected Map<ConfigPart,Balloon> balloons = new HashMap<>();
	protected boolean hasChunkTicket = false;
	
	
	public Wall(ChunkTicketManager chunkTicketManager, BalloonManager balloonManager, ConfigWall config, World world) {
		this.chunkTicketManager = chunkTicketManager;
		this.balloonManager = balloonManager;
		this.config = config;
		this.world = world;
		if (config.getTemplate().isOscillating()) {
			oscillator = new VerticalSinusOscillator(config.getTemplate().getRule());
		} else {
			oscillator = null;
		}		
		for (ConfigPart part : config.getTemplate().getParts()) {
			if (part.isSuitableFor(BalloonPurpose.WALL)) {
				balloons.put(part, null);
			}
		}
	}
	
	
	public World getWorld() {
		return world;
	}
	
	
	protected Location getLocation() {
		return config.getAbsolutePosition().toLocation(world); 
	}
	
		
	public void buildBalloons() throws BalloonException {
		chunkTicketManager.addChunkTicket(this, getLocation());
		for (Entry<ConfigPart,Balloon> myEntry : balloons.entrySet()) {
			ConfigPart myPart = myEntry.getKey();
			Balloon myBalloon = myEntry.getValue();
			if (myBalloon != null) {
				continue;
			}

			Balloon newBalloon = new WallBalloon(getLocation(), config, myPart, oscillator);
			myEntry.setValue(newBalloon);
			try {
				balloonManager.addBalloon(newBalloon);
			} catch (Exception e) {
				newBalloon.cancel();
				throw e;
			}
		}
	}
	
	
	public void cancel() {
		for (Balloon myBalloon : balloons.values()) {
			if (myBalloon != null) {
				myBalloon.cancel();
			}
		}
		chunkTicketManager.removeChunkTicket(this);
	}

}
