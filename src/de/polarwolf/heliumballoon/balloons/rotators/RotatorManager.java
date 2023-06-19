package de.polarwolf.heliumballoon.balloons.rotators;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.balloons.placeable.ConfigPlaceable;
import de.polarwolf.heliumballoon.balloons.placeable.Placeable;
import de.polarwolf.heliumballoon.balloons.placeable.PlaceableManager;
import de.polarwolf.heliumballoon.config.ConfigHelper;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;

public class RotatorManager extends PlaceableManager {

	public static final String BALLOON_TYPE = "rotators";

	public RotatorManager(HeliumBalloonOrchestrator orchestrator, String defaultBehaviorName) {
		super(orchestrator, defaultBehaviorName);
		logger.printDebug("Rotatormanager started");
	}

	//
	// Implement the Definition
	//

	@Override
	public String getAttributeName() {
		return BALLOON_TYPE;
	}

	@Override
	public ConfigBalloon createConfigBalloon(ConfigHelper configHelper, ConfigurationSection fileSection,
			ConfigSection balloonSection) throws BalloonException {
		return new ConfigRotator(this, configHelper, fileSection, balloonSection);
	}

	//
	// Implements the placeable
	//

	@Override
	protected Rotator createPlaceable(ConfigPlaceable configPlaceable, World world) {
		return new Rotator(this, chunkTicketManager, observerManager, configPlaceable, world);
	}

	//
	// Now the things specific for rotators
	//

	public ConfigRotator findConfigRotator(String rotatorName) {
		ConfigPlaceable configPlaceable = findRelevantConfigBalloon(rotatorName);
		if (configPlaceable instanceof ConfigRotator configRotator) {
			return configRotator;
		}
		return null;
	}

	public List<Rotator> getRotators() {
		List<Rotator> rotatorList = new ArrayList<>();
		for (Placeable myPlaceable : getPlaceables()) {
			rotatorList.add((Rotator) myPlaceable);
		}
		return rotatorList;
	}

	public Rotator createRotator(ConfigRotator configRotator, World world) throws BalloonException {
		Rotator rotator = createPlaceable(configRotator, world);
		registerPlaceable(rotator);
		return rotator;
	}

	public void destroyRotator(Rotator rotator) {
		unregisterPlaceable(rotator);
	}

}