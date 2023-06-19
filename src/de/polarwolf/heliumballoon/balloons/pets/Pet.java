package de.polarwolf.heliumballoon.balloons.pets;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.behavior.observers.Observer;
import de.polarwolf.heliumballoon.behavior.observers.ObserverManager;
import de.polarwolf.heliumballoon.behavior.oscillators.Oscillator;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.config.templates.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumName;

public class Pet implements HeliumName {

	protected final Player player;
	protected final ObserverManager observerManager;
	protected final ConfigPet configPet;
	protected final Oscillator oscillator;
	protected final ConfigTemplate template;
	protected final BalloonDefinition balloonDefinition;
	protected Map<ConfigElement, Observer> observers = new HashMap<>();

	public Pet(BalloonDefinition balloonDefinition, ObserverManager observerManager, ConfigPet configPet,
			Player player) {
		this.balloonDefinition = balloonDefinition;
		this.observerManager = observerManager;
		this.configPet = configPet;
		this.player = player;
		this.template = configPet.findTemplate(player.getWorld());
		if (template.isOscillating()) {
			oscillator = configPet.getBehavior().createOscillator(template.getRule());
			oscillator.setDeflectionState(true);
		} else {
			oscillator = null;
		}
		for (ConfigElement configElement : template.getElements()) {
			if (observerManager.isCompatible(configElement.getElementDefinition(), configPet.getBehavior(),
					balloonDefinition)) {
				observers.put(configElement, null);
			}
		}
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public String getName() {
		return configPet.getName();
	}

	@Override
	public String getFullName() {
		return player.getName() + "." + configPet.getName();
	}

	public boolean isCancelled() {
		boolean myCancel = false;
		for (Observer myObserver : observers.values()) {
			if ((myObserver != null) && myObserver.isCancelled()) {
				myCancel = true;
			}
		}
		return myCancel;
	}

	public void hide() {
		for (Entry<ConfigElement, Observer> myEntry : observers.entrySet()) {
			Observer myObserver = myEntry.getValue();
			if (myObserver != null) {
				myObserver.cancel();
				myEntry.setValue(null);
			}
		}
	}

	public void show() throws BalloonException {
		try {
			for (Entry<ConfigElement, Observer> myEntry : observers.entrySet()) {
				ConfigElement myConfigElement = myEntry.getKey();
				Observer myObserver = myEntry.getValue();
				if ((myObserver == null) || (myObserver.isCancelled())) {
					Observer newObserver = configPet.getBehavior().createObserver(configPet, myConfigElement,
							oscillator, player);
					myEntry.setValue(newObserver);
					observerManager.addObserver(newObserver);
				}
			}
		} catch (Exception e) {
			hide();
			throw e;
		}
	}

}
