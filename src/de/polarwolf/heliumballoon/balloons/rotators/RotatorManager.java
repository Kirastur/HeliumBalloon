package de.polarwolf.heliumballoon.balloons.rotators;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.World;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.balloons.placings.Placing;
import de.polarwolf.heliumballoon.balloons.placings.PlacingManager;
import de.polarwolf.heliumballoon.config.ConfigPlaceableBalloonSet;
import de.polarwolf.heliumballoon.config.ConfigRotator;
import de.polarwolf.heliumballoon.exception.BalloonException;

public class RotatorManager extends PlacingManager {

	public RotatorManager(HeliumBalloonOrchestrator orchestrator) {
		super(orchestrator);
		logger.printDebug("Rotatormanager started");
	}

	@Override
	protected Set<String> getBalloonSetNames() {
		return configManager.getRotatorNames();
	}

	@Override
	protected ConfigPlaceableBalloonSet findBalloonSet(String placingName) {
		return configManager.findRotator(placingName);
	}

	@Override
	protected Rotator createPlacing(ConfigPlaceableBalloonSet configPlaceableBalloonSet, World world) {
		return new Rotator(chunkTicketManager, observerManager, configPlaceableBalloonSet, world);
	}

	@Override
	public int reload() {
		int count = super.reload();
		if (count > 0) {
			logger.printInfo(String.format("%d rotators created in total.", count));
		}
		return count;
	}

	public List<Rotator> getRotators() {
		List<Rotator> rotatorList = new ArrayList<>();
		for (Placing myPlacing : getPlacings()) {
			if (myPlacing instanceof Rotator) {
				Rotator myRotator = (Rotator) myPlacing;
				rotatorList.add(myRotator);
			}
		}
		return rotatorList;
	}
	
	public Rotator createRotator (ConfigRotator configRotator, World world) throws BalloonException {
		Rotator rotator = createPlacing(configRotator, world);
		registerPlacing(rotator);
		return rotator;
	}
	
	public void destroyRotator(Rotator rotator) {
		unregisterPlacing(rotator);
	}

}