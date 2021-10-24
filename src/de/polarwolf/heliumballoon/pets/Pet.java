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
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumName;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.oscillators.DefaultOscillator;

public class Pet implements HeliumName {
	
	protected final Player player;
	protected final BalloonManager balloonManager;
	protected final ConfigTemplate template;
	protected final Oscillator oscillator;
	protected Map<ConfigPart,Balloon> balloons = new HashMap<>();
	
	
	public Pet (Player player, BalloonManager balloonManager, ConfigTemplate template) {
		this.player = player;
		this.balloonManager = balloonManager;
		this.template = template;
		if (template.isOscillating()) {
			oscillator = new DefaultOscillator(template.getRule());
			oscillator.setDeflectionState(true);
		} else {
			oscillator = null;
		}
		for (ConfigPart part : template.getParts()) {
			if (part.isSuitableFor(BalloonPurpose.PET)) {
				balloons.put(part, null);
			}
		}
	}
	
	
	public Player getPlayer() {
		return player;
	}


	@Override
	public String getName() {
		return template.getName();
	}
	
	
	@Override
	public String getFullName() {
		return player.getName()+"."+template.getName();
	}
	
	
	public boolean isCancelled() {		
		boolean myCancel = false;
		for (Balloon myBalloon: balloons.values()) {
			if ((myBalloon != null) && myBalloon.isCancelled()) {
				myCancel = true;
			}
		}
		return myCancel; 
	}
		

	public void hide() {
		for (Entry<ConfigPart,Balloon> myEntry: balloons.entrySet()) {
			Balloon myBalloon = myEntry.getValue();
			if (myBalloon != null) {
				myBalloon.cancel();
				myEntry.setValue(null);
			}
		}
	}
	
	
	public void show() throws BalloonException {
		try {
			for (Entry<ConfigPart,Balloon> myEntry: balloons.entrySet()) {
				ConfigPart myPart = myEntry.getKey();
				Balloon myBalloon = myEntry.getValue();
				if ((myBalloon==null) || (myBalloon.isCancelled())) {
					Balloon newBalloon = new PetBalloon(player, template, myPart, oscillator);
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
