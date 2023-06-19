package de.polarwolf.heliumballoon.config.balloons;

import java.util.List;

import org.bukkit.World;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.config.templates.ConfigTemplate;
import de.polarwolf.heliumballoon.tools.helium.HeliumName;

public interface ConfigBalloon extends HeliumName {

	public ConfigTemplate findTemplate(World world);

	public List<ConfigTemplate> listUsedTemplates();

	public BehaviorDefinition getBehavior();

	public BalloonDefinition getBalloonDefinition();

}
