package de.polarwolf.heliumballoon.balloons.placeable;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.behavior.observers.Observer;
import de.polarwolf.heliumballoon.behavior.observers.ObserverManager;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.tools.chunktickets.ChunkTicketManager;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public abstract class PlaceableManager implements BalloonDefinition {

	protected final Plugin plugin;
	protected final HeliumLogger logger;
	protected final ChunkTicketManager chunkTicketManager;
	protected final ConfigManager configManager;
	protected final ObserverManager observerManager;
	protected final int placingDelay;
	protected final String defaultBehaviorName;
	protected boolean disabled = false;
	protected List<Placeable> placeables = new ArrayList<>();
	protected List<PlaceableStarterScheduler> starters = new ArrayList<>();

	protected PlaceableManager(HeliumBalloonOrchestrator orchestrator, String defaultBehaviorName) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.chunkTicketManager = orchestrator.getChunkTicketManager();
		this.configManager = orchestrator.getConfigManager();
		this.observerManager = orchestrator.getObserverManager();
		this.placingDelay = orchestrator.getStartOptions().placingDelay();
		this.defaultBehaviorName = defaultBehaviorName;
	}

	protected abstract Placeable createPlaceable(ConfigPlaceable configPlaceable, World world);

	//
	// Implements the definition
	//

	@Override
	public boolean isType(HeliumParamType paramType) {
		return paramType == HeliumParamType.SECTION;
	}

	@Override
	public String getDefaultBehaviorName() {
		return defaultBehaviorName;
	}

	@Override
	public boolean isPlayerAssignable() {
		return false;
	}

	@Override
	public void refresh() {
		cancelAllStarter();
		for (World myWorld : plugin.getServer().getWorlds()) {
			int myCount = addPlaceables(myWorld);
			if (myCount > 0) {
				logger.printInfo(String.format("%d %s created in: %s", myCount, getAttributeName(), myWorld.getName()));
			}
		}
	}

	//
	// Now things valid for all placeables
	//

	public List<String> getRelevantBalloonNames() {
		return configManager.getBalloonNames(this);
	}

	public ConfigPlaceable findRelevantConfigBalloon(String balloonName) {
		ConfigBalloon configBalloon = configManager.findBalloon(balloonName);
		if (configBalloon instanceof ConfigPlaceable configPlaceable) {
			return configPlaceable;
		}
		return null;
	}

	public List<Placeable> getPlaceables() {
		return new ArrayList<>(placeables);
	}

	// Called from the StarterScheduler
	void registerStarter(PlaceableStarterScheduler starter) {
		starters.add(starter);
	}

	// Called from the StarterScheduler
	void unregisterStarter(PlaceableStarterScheduler oldStarter) {
		starters.remove(oldStarter);
	}

	protected void generateStarter(Observer newObserver) {
		new PlaceableStarterScheduler(plugin, observerManager, this, newObserver, placingDelay * 20);
	}

	protected void cancelAllStarter() {
		for (PlaceableStarterScheduler myStarter : new ArrayList<>(starters)) {
			myStarter.cancel();
		}
	}

	protected boolean hasObserver(Observer observer) {
		for (Observer myObserver : observerManager.getAllObservers()) {
			if (myObserver == observer) {
				return true;
			}
		}
		return false;
	}

	protected void registerPlaceable(Placeable newPlaceable) throws BalloonException {
		if (isDisabled()) {
			return;
		}
		placeables.add(newPlaceable);
		newPlaceable.buildObservers();
		for (Observer myObserver : newPlaceable.getObservers()) {
			if ((myObserver != null) && !myObserver.isCancelled() && !hasObserver(myObserver)) {
				generateStarter(myObserver);
			}
		}
	}

	protected void unregisterPlaceable(Placeable placeable) {
		placeable.cancel();
		placeables.remove(placeable);
	}

	public int removePlaceables(World world) {
		int count = 0;
		for (Placeable myPlaceable : new ArrayList<>(placeables)) {
			if (myPlaceable.getWorld().equals(world)) {
				unregisterPlaceable(myPlaceable);
				count = count + 1;
			}
		}
		return count;
	}

	public int addPlaceables(World world) {
		int count = 0;
		removePlaceables(world);
		try {
			for (String myPlacingName : getRelevantBalloonNames()) {
				ConfigPlaceable configPlaceable = findRelevantConfigBalloon(myPlacingName);
				if ((configPlaceable != null) && configPlaceable.isMatchingWorld(world)
						&& (configPlaceable.findTemplate(world) != null)) {
					String fullPlaceableName = configPlaceable.getName() + "." + world.getName();
					logger.printDebug(String.format("Creating PlacingObserver: %s", fullPlaceableName));
					Placeable newPlaceable = createPlaceable(configPlaceable, world);
					registerPlaceable(newPlaceable);
					count = count + 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			removePlaceables(world);
		}
		return count;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void disable() {
		disabled = true;
		for (Placeable myPlaceable : new ArrayList<>(placeables)) {
			unregisterPlaceable(myPlaceable);
		}
		cancelAllStarter();
	}

}
