package de.polarwolf.heliumballoon.balloons;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.config.ConfigHelper;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;

public interface BalloonDefinition extends HeliumParam {

	public String getDefaultBehaviorName();

	public boolean isPlayerAssignable();

	public ConfigBalloon createConfigBalloon(ConfigHelper configHelper, ConfigurationSection fileSection,
			ConfigSection balloonSection) throws BalloonException;

	public void refresh();

}
