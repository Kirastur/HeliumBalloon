package de.polarwolf.heliumballoon.elements.living;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.elements.ElementDefinition;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public class LivingDefinition implements ElementDefinition {

	public static final String ELEMENT_TYPE = "living";

	@Override
	public boolean isType(HeliumParamType paramType) {
		return paramType == HeliumParamType.SECTION;
	}

	@Override
	public String getAttributeName() {
		return ELEMENT_TYPE;
	}

	@Override
	public ConfigElement createConfigElement(ConfigurationSection fileSection) throws BalloonException {
		return new LivingConfig(this, fileSection);
	}
}
