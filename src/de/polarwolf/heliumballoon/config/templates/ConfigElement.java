package de.polarwolf.heliumballoon.config.templates;

import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.elements.ElementDefinition;
import de.polarwolf.heliumballoon.tools.helium.HeliumName;

public interface ConfigElement extends HeliumName {

	public ElementDefinition getElementDefinition();

	public Element createElement(Player player, ConfigRule rule, BehaviorDefinition behaviorDefinition);

	public double getMinYOffset();

	public double getMaxYOffset();

}
