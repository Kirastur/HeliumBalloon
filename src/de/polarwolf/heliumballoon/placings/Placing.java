package de.polarwolf.heliumballoon.placings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.World;

import de.polarwolf.heliumballoon.balloons.Balloon;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.balloons.PlacedBalloon;
import de.polarwolf.heliumballoon.chunktickets.ChunkTicketManager;
import de.polarwolf.heliumballoon.chunktickets.ChunkTicketOwner;
import de.polarwolf.heliumballoon.config.ConfigPart;
import de.polarwolf.heliumballoon.config.ConfigPlaceableBalloonSet;
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.oscillators.DefaultOscillator;
import de.polarwolf.heliumballoon.oscillators.Oscillator;

public abstract class Placing implements ChunkTicketOwner {

	protected final ChunkTicketManager chunkTicketManager;
	protected final BalloonManager balloonManager;
	protected final ConfigPlaceableBalloonSet configPlaceableBalloonSet;
	protected final ConfigTemplate template;
	private Oscillator oscillator = null;
	private final World world;
	protected Map<ConfigPart, Balloon> balloons = new HashMap<>();

	protected Placing(ChunkTicketManager chunkTicketManager, BalloonManager balloonManager,
			ConfigPlaceableBalloonSet configPlaceableBalloonSet, World world) {
		this.chunkTicketManager = chunkTicketManager;
		this.balloonManager = balloonManager;
		this.configPlaceableBalloonSet = configPlaceableBalloonSet;
		this.template = configPlaceableBalloonSet.findTemplate(world);
		this.world = world;
	}

	protected Oscillator getOscillator() {
		return oscillator;
	}

	protected void generateOscillator() {
		if (oscillator == null) {
			oscillator = new DefaultOscillator(template.getRule());
		}
	}

	public World getWorld() {
		return world;
	}

	protected Location getLocation() {
		return configPlaceableBalloonSet.getAbsolutePosition().toLocation(world);
	}

	protected void prepareBalloons(BalloonPurpose balloonPurpose) {
		for (ConfigPart myPart : template.getParts()) {
			if (myPart.isSuitableFor(balloonPurpose)) {
				balloons.put(myPart, null);
			}
		}
	}

	public void buildBalloons() throws BalloonException {
		chunkTicketManager.addChunkTicket(this, getLocation());
		for (Entry<ConfigPart, Balloon> myEntry : balloons.entrySet()) {
			ConfigPart myPart = myEntry.getKey();
			Balloon myBalloon = myEntry.getValue();
			if (myBalloon != null) {
				continue;
			}

			Balloon newBalloon = new PlacedBalloon(getLocation(), configPlaceableBalloonSet, myPart, oscillator);
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
		for (Entry<ConfigPart, Balloon> myEntry : balloons.entrySet()) {
			Balloon myBalloon = myEntry.getValue();
			if (myBalloon != null) {
				myBalloon.cancel();
				myEntry.setValue(null);
			}
		}
		chunkTicketManager.removeChunkTicket(this);
	}

}
