package de.polarwolf.heliumballoon.pets;

import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.balloons.Balloon;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.balloons.PetBalloon;
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.oscillators.VerticalOscillator;

public class Pet {
	
	protected final Player player;
	protected final BalloonManager balloonManager;
	protected final ConfigTemplate template;
	protected final Oscillator oscillator;
	protected Balloon balloonLiving = null;
	protected Balloon balloonCompound = null;
	protected Balloon balloonMinecart = null;
	
	
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
		if (balloonLiving != null) {
			myCancel = balloonLiving.isCancelled();
		}
		if (balloonCompound != null) {
			myCancel = myCancel || balloonCompound.isCancelled();
		}
		if (balloonMinecart != null) {
			myCancel = balloonMinecart.isCancelled();
		}
		return myCancel;
	}
	
	
	protected void hideLiving() {
		if (balloonLiving != null) {
			balloonLiving.cancel();
			balloonLiving = null;
		}
	}


	protected void hideCompound() {
		if (balloonCompound != null) {
			balloonCompound.cancel();
			balloonCompound = null;
		}
	}

	
	protected void hideMinecart() {
		if (balloonMinecart != null) {
			balloonMinecart.cancel();
			balloonMinecart = null;
		}
	}


	public void hide() {
		hideLiving();
		hideCompound(); 
		hideMinecart();
	}
	
	
	public void show() throws BalloonException {
		try {
			if (template.hasLiving() && ((balloonLiving == null) || balloonLiving.isCancelled())) {
				balloonLiving = new PetBalloon(player, template, template.getLiving(), oscillator); 
				balloonManager.addBalloon(balloonLiving);
				}
			if (template.hasCompound() && ((balloonCompound == null) || balloonCompound.isCancelled())) {
				balloonCompound = new PetBalloon(player, template, template.getCompound(), oscillator);
				balloonManager.addBalloon(balloonCompound);
			}
			if (template.hasMinecart() && ((balloonMinecart == null) || balloonMinecart.isCancelled())) {
				balloonMinecart = new PetBalloon(player, template, template.getMinecart(), oscillator);
				balloonManager.addBalloon(balloonMinecart);
			}
		} catch (Exception e) {
			hide();
			throw e;
		}
	}

}
