package de.polarwolf.heliumballoon.pets;

import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.balloons.AnimalPlayerBalloon;
import de.polarwolf.heliumballoon.balloons.Balloon;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.balloons.CompoundPlayerBalloon;
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.oscillators.VerticalOscillator;

public class Pet {
	
	protected final Player player;
	protected final BalloonManager balloonManager;
	protected final ConfigTemplate template;
	protected final Oscillator oscillator;
	protected Balloon balloonAnimal = null;
	protected Balloon balloonCompound = null;
	
	
	public Pet (Player player, BalloonManager balloonManager, ConfigTemplate template) {
		this.player = player;
		this.balloonManager = balloonManager;
		this.template = template;
		if (template.isOscillating()) {
			oscillator = new VerticalOscillator(template.getRule());
		} else {
			oscillator = null;
		}		
	}
	
	
	public Player getPlayer() {
		return player;
	}


	public String getName() {
		return player.getName()+"."+template.getName();
	}
	
	
	public boolean isCancelled() {
		boolean myCancel = false;
		if (balloonAnimal != null) {
			myCancel = balloonAnimal.isCancelled();
		}
		if (balloonCompound != null) {
			myCancel = myCancel || balloonCompound.isCancelled();
		}
		return myCancel;
	}
	
	
	protected void hideAnimal() {
		if (balloonAnimal != null) {
			balloonAnimal.cancel();
			balloonAnimal = null;
		}
	}


	protected void hideCompound() {
		if (balloonCompound != null) {
			balloonCompound.cancel();
			balloonCompound = null;
		}
	}

	public void hide() {
		hideAnimal();
		hideCompound(); 
	}
	
	
	public void show() throws BalloonException {
		try {
			if (template.hasAnimal() && ((balloonAnimal == null) || balloonAnimal.isCancelled())) {
				balloonAnimal = new AnimalPlayerBalloon(player, template, oscillator);
				balloonManager.addBalloon(balloonAnimal);
				}
			if (template.hasCompound() && ((balloonCompound == null) || balloonCompound.isCancelled())) {
				balloonCompound = new CompoundPlayerBalloon(player, template, oscillator);
				balloonManager.addBalloon(balloonCompound);
			}
		} catch (Exception e) {
			hide();
			throw e;
		}
	}

}
