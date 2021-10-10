package de.polarwolf.heliumballoon.config;

import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.helium.HeliumName;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public interface ConfigPart extends HeliumName {
	
	public boolean isSuitableFor(BalloonPurpose purpose);
	
	public Element createElement(Player player, ConfigRule rule, SpawnModifier spawnModifier);
	
	public double getMinYOffset();
	public double getMaxYOffset();
	
}
