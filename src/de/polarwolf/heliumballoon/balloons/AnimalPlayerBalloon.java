package de.polarwolf.heliumballoon.balloons;

import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.elements.AnimalElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.spawnmodifiers.HeliumModifier;

public class AnimalPlayerBalloon extends PlayerBalloon {

	public static final String NAME_ANIMAL = "animal";

	
	public AnimalPlayerBalloon(Player player, ConfigTemplate template) {
		super(player, template);
	}

	
	@Override
	protected Element createElement(HeliumModifier heliumModifier) {
		return new AnimalElement(getPlayer(), getTemplate().getRule(), getTemplate().getAnimal(), heliumModifier);
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