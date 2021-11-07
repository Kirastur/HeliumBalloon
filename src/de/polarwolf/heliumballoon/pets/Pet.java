package de.polarwolf.heliumballoon.pets;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.balloons.Balloon;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.balloons.PetBalloon;
import de.polarwolf.heliumballoon.config.ConfigPart;
import de.polarwolf.heliumballoon.config.ConfigPet;
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumName;
import de.polarwolf.heliumballoon.oscillators.DefaultOscillator;
import de.polarwolf.heliumballoon.oscillators.Oscillator;

public class Pet implements HeliumName {

	protected final Player player;
	protected final BalloonManager balloonManager;
	protected final ConfigPet configPet;
	protected final Oscillator oscillator;
	protected final ConfigTemplate template;
	protected Map<ConfigPart, Balloon> balloons = new HashMap<>();

	public Pet(Player player, BalloonManager balloonManager, ConfigPet configPet) {
		this.player = player;
		this.balloonManager = balloonManager;
		this.configPet = configPet;
		this.template = configPet.findTemplate(player.getWorld());
		if (template.isOscillating()) {
			oscillator = new DefaultOscillator(template.getRule());
			oscillator.setDeflectionState(true);
		} else {
			oscillator = null;
		}
		for (ConfigPart configPart : template.getParts()) {
			if (configPart.isSuitableFor(BalloonPurpose.PET)) {
				balloons.put(configPart, null);
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
		for (Balloon myBalloon : balloons.values()) {
			if ((myBalloon != null) && myBalloon.isCancelled()) {
				myCancel = true;
			}
		}
		return myCancel;
	}

	public void hide() {
		for (Entry<ConfigPart, Balloon> myEntry : balloons.entrySet()) {
			Balloon myBalloon = myEntry.getValue();
			if (myBalloon != null) {
				myBalloon.cancel();
				myEntry.setValue(null);
			}
		}
	}

	public void show() throws BalloonException {
		try {
			for (Entry<ConfigPart, Balloon> myEntry : balloons.entrySet()) {
				ConfigPart myConfigPart = myEntry.getKey();
				Balloon myBalloon = myEntry.getValue();
				if ((myBalloon == null) || (myBalloon.isCancelled())) {
					Balloon newBalloon = new PetBalloon(player, template, myConfigPart, oscillator);
					myEntry.setValue(newBalloon);
					balloonManager.addBalloon(newBalloon);
				}
			}
		} catch (Exception e) {
			hide();
			throw e;
		}
	}

}
