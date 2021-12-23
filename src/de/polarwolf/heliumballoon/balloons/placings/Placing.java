package de.polarwolf.heliumballoon.balloons.placings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.World;

import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.config.ConfigPart;
import de.polarwolf.heliumballoon.config.ConfigPlaceableBalloonSet;
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.observers.Observer;
import de.polarwolf.heliumballoon.observers.ObserverManager;
import de.polarwolf.heliumballoon.observers.PlacedObserver;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.tools.chunktickets.ChunkTicketManager;
import de.polarwolf.heliumballoon.tools.chunktickets.ChunkTicketOwner;

public abstract class Placing implements ChunkTicketOwner {

	protected final ChunkTicketManager chunkTicketManager;
	protected final ObserverManager observerManager;
	protected final ConfigPlaceableBalloonSet configPlaceableBalloonSet;
	protected final ConfigTemplate template;
	private Oscillator oscillator = null;
	private final World world;
	protected Map<ConfigPart, Observer> observers = new HashMap<>();

	protected Placing(ChunkTicketManager chunkTicketManager, ObserverManager observerManager,
			ConfigPlaceableBalloonSet configPlaceableBalloonSet, World world) {
		this.chunkTicketManager = chunkTicketManager;
		this.observerManager = observerManager;
		this.configPlaceableBalloonSet = configPlaceableBalloonSet;
		this.template = configPlaceableBalloonSet.findTemplate(world);
		this.world = world;
	}

	protected Oscillator getOscillator() {
		return oscillator;
	}

	protected void generateOscillator() {
		if (oscillator == null) {
			oscillator = observerManager.getOscillator(template.getRule());
		}
	}

	public World getWorld() {
		return world;
	}

	protected Location getLocation() {
		return configPlaceableBalloonSet.getAbsolutePosition().toLocation(world);
	}
	
	public List<Observer> getObservers() {
		return new ArrayList<>(observers.values());
	}

	protected void prepareObservers(BalloonPurpose balloonPurpose) {
		for (ConfigPart myPart : template.getParts()) {
			if (myPart.isSuitableFor(balloonPurpose)) {
				observers.put(myPart, null);
			}
		}
	}

	public void buildObservers() throws BalloonException {
		chunkTicketManager.addChunkTicket(this, getLocation());
		for (Entry<ConfigPart, Observer> myEntry : observers.entrySet()) {
			ConfigPart myPart = myEntry.getKey();
			Observer myObserver = myEntry.getValue();
			if (myObserver != null) {
				continue;
			}
			Observer newObserver = new PlacedObserver(getLocation(), configPlaceableBalloonSet, myPart, oscillator);
			myEntry.setValue(newObserver);
		}
	}

	public void cancel() {
		for (Entry<ConfigPart, Observer> myEntry : observers.entrySet()) {
			Observer myObserver = myEntry.getValue();
			if (myObserver != null) {
				myObserver.cancel();
				myEntry.setValue(null);
			}
		}
		chunkTicketManager.removeChunkTicket(this);
	}

}
