package de.polarwolf.heliumballoon.config;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.elements.ElementDefinition;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;

public interface ConfigHelper {

	public List<HeliumParam> getAdditionalRuleParams();

	public List<HeliumParam> getValidElementConfigParams();

	public List<ElementDefinition> listElementDefinitions();

	public boolean hasBehavior(String name);

	public BehaviorDefinition getBehaviorDefinition(String name) throws BalloonException;

	public List<BehaviorDefinition> listBehaviorDefinitions();

	public List<HeliumParam> getValidBalloonConfigParams();

	public List<BalloonDefinition> listBalloonDefinitions();

	public ConfigSection buildConfigSectionFromFileSection(String sectionName, ConfigurationSection fileSection)
			throws BalloonException;

	public ConfigSection buildConfigSectionFromConfigFile(Plugin fileOwnerPlugin) throws BalloonException;

}
