package de.polarwolf.heliumballoon.balloons.placeable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.World;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.behavior.observers.Observer;
import de.polarwolf.heliumballoon.behavior.observers.ObserverManager;
import de.polarwolf.heliumballoon.behavior.oscillators.Oscillator;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.config.templates.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.chunktickets.ChunkTicketManager;
import de.polarwolf.heliumballoon.tools.chunktickets.ChunkTicketOwner;

public abstract class Placeable implements ChunkTicketOwner {

	protected final ChunkTicketManager chunkTicketManager;
	protected final ObserverManager observerManager;
	protected final ConfigPlaceable configPlaceable;
	protected final ConfigTemplate template;
	protected final BalloonDefinition balloonDefinition;
	private Oscillator oscillator = null;
	private final World world;
	protected Map<ConfigElement, Observer> observers = new HashMap<>();

	protected Placeable(BalloonDefinition balloonDefinition, ChunkTicketManager chunkTicketManager,
			ObserverManager observerManager, ConfigPlaceable configPlaceable, World world) {
		this.balloonDefinition = balloonDefinition;
		this.chunkTicketManager = chunkTicketManager;
		this.observerManager = observerManager;
		this.configPlaceable = configPlaceable;
		this.world = world;
		this.template = configPlaceable.findTemplate(world);
	}

	protected Oscillator getOscillator() {
		return oscillator;
	}

	protected void generateOscillator() {
		if (oscillator == null) {
			oscillator = configPlaceable.getBehavior().createOscillator(template.getRule());
		}
	}

	public World getWorld() {
		return world;
	}

	protected Location getLocation() {
		return configPlaceable.getAbsolutePosition().toLocation(world);
	}

	public List<Observer> getObservers() {
		return new ArrayList<>(observers.values());
	}

	protected void prepareObservers() {
		for (ConfigElement myElement : template.getElements()) {
			if (observerManager.isCompatible(myElement.getElementDefinition(), configPlaceable.getBehavior(),
					balloonDefinition)) {
				observers.put(myElement, null);
			}
		}
	}

	public void buildObservers() throws BalloonException {
		chunkTicketManager.addChunkTicket(this, getLocation());
		for (Entry<ConfigElement, Observer> myEntry : observers.entrySet()) {
			ConfigElement myElement = myEntry.getKey();
			Observer myObserver = myEntry.getValue();
			if (myObserver != null) {
				continue;
			}
			Observer newObserver = configPlaceable.getBehavior().createObserver(configPlaceable, myElement, oscillator,
					getLocation());
			myEntry.setValue(newObserver);
		}
	}

	public void cancel() {
		for (Entry<ConfigElement, Observer> myEntry : observers.entrySet()) {
			Observer myObserver = myEntry.getValue();
			if (myObserver != null) {
				myObserver.cancel();
				myEntry.setValue(null);
			}
		}
		chunkTicketManager.removeChunkTicket(this);
	}

}
