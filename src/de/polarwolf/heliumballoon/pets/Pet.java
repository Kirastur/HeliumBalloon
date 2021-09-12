package de.polarwolf.heliumballoon.pets;

import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.balloons.AnimalPlayerBalloon;
import de.polarwolf.heliumballoon.balloons.Balloon;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.balloons.CompoundPlayerBalloon;
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;

public class Pet {
	
	private final Player player;
	private final ConfigTemplate template;
	protected Balloon balloonAnimal = null;
	protected Balloon balloonCompound = null;
	protected final BalloonManager balloonManager;
	
	
	public Pet (Player player, BalloonManager balloonManager, ConfigTemplate template) {
		this.player = player;
		this.balloonManager = balloonManager;
		this.template = template;		
	}
	
	
	public Player getPlayer() {
		return player;
	}


	public ConfigTemplate getTemplate() {
		return template;
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
				balloonAnimal = new AnimalPlayerBalloon(player, template);
				balloonManager.addBalloon(balloonAnimal);
				}
			if (template.hasCompound() && ((balloonCompound == null) || balloonCompound.isCancelled())) {
				balloonCompound = new CompoundPlayerBalloon(player, template);
				balloonManager.addBalloon(balloonCompound);
			}
		} catch (Exception e) {
			hide();
			throw e;
		}
	}

}
