package de.polarwolf.heliumballoon.config;

import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.helium.HeliumName;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public interface ConfigPart extends HeliumName, ConfigPurpose {

	public Element createElement(Player player, ConfigRule rule, SpawnModifier spawnModifier);

	public double getMinYOffset();

	public double getMaxYOffset();

}
