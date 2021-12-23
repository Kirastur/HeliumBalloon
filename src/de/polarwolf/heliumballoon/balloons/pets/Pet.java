package de.polarwolf.heliumballoon.balloons.pets;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.config.ConfigPart;
import de.polarwolf.heliumballoon.config.ConfigPet;
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.observers.Observer;
import de.polarwolf.heliumballoon.observers.ObserverManager;
import de.polarwolf.heliumballoon.observers.PetObserver;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.tools.helium.HeliumName;

public class Pet implements HeliumName {

	protected final Player player;
	protected final ObserverManager observerManager;
	protected final ConfigPet configPet;
	protected final Oscillator oscillator;
	protected final ConfigTemplate template;
	protected Map<ConfigPart, Observer> observers = new HashMap<>();

	public Pet(Player player, ObserverManager observerManager, ConfigPet configPet) {
		this.player = player;
		this.observerManager = observerManager;
		this.configPet = configPet;
		this.template = configPet.findTemplate(player.getWorld());
		if (template.isOscillating()) {
			oscillator = observerManager.getOscillator(template.getRule());
			oscillator.setDeflectionState(true);
		} else {
			oscillator = null;
		}
		for (ConfigPart configPart : template.getParts()) {
			if (configPart.isSuitableFor(BalloonPurpose.PET)) {
				observers.put(configPart, null);
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
		for (Entry<ConfigPart, Observer> myEntry : observers.entrySet()) {
			Observer myObserver = myEntry.getValue();
			if (myObserver != null) {
				myObserver.cancel();
				myEntry.setValue(null);
			}
		}
	}

	public void show() throws BalloonException {
		try {
			for (Entry<ConfigPart, Observer> myEntry : observers.entrySet()) {
				ConfigPart myConfigPart = myEntry.getKey();
				Observer myObserver = myEntry.getValue();
				if ((myObserver == null) || (myObserver.isCancelled())) {
					Observer newObserver = new PetObserver(player, template, myConfigPart, oscillator);
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
