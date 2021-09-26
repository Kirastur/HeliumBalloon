package de.polarwolf.heliumballoon.balloons;

import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.elements.CompoundElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public class CompoundPlayerBalloon extends PlayerBalloon {
	
	public static final String NAME_COMPOUND = "compound";


	public CompoundPlayerBalloon(Player player, ConfigTemplate template, Oscillator oscillator) {
		super(player, template, oscillator, template.getRule().getBlockDelay());
	}

	
	@Override
	protected Element createElement(SpawnModifier spawnModifier) {
		return new CompoundElement (getPlayer(), getTemplate().getRule(), getTemplate().getCompound(), spawnModifier);
	}
	
	
	@Override
	public String getName() {
		if (getPlayer() == null) {
			return getTemplate().getName()+"."+NAME_COMPOUND;
		} else {
			return getTemplate().getName()+"."+NAME_COMPOUND+"."+getPlayer().getName();
		}		
	}

}
