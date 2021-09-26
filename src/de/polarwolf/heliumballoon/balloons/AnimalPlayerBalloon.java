package de.polarwolf.heliumballoon.balloons;

import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.elements.AnimalElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public class AnimalPlayerBalloon extends PlayerBalloon {

	public static final String NAME_ANIMAL = "animal";

	
	public AnimalPlayerBalloon(Player player, ConfigTemplate template, Oscillator oscillator) {
		super(player, template, oscillator, 0);
	}

	
	@Override
	protected Element createElement(SpawnModifier spawnModifier) {
		return new AnimalElement(getPlayer(), getTemplate().getRule(), getTemplate().getAnimal(), spawnModifier);
	}
	

	@Override
	public String getName() {
		if (getPlayer() == null) {
			return getTemplate().getName()+"."+NAME_ANIMAL;
		} else {
			return getTemplate().getName()+"."+NAME_ANIMAL+"."+getPlayer().getName();
		}		
	}

}
