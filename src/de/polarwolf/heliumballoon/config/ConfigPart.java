package de.polarwolf.heliumballoon.config;

import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;
import de.polarwolf.heliumballoon.tools.helium.HeliumName;

public interface ConfigPart extends HeliumName, ConfigPurpose {

	public Element createElement(Player player, ConfigRule rule, SpawnModifier spawnModifier);

	public double getMinYOffset();

	public double getMaxYOffset();

}
