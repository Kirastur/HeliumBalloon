package de.polarwolf.heliumballoon.balloons.placings;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.config.ConfigPlaceableBalloonSet;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.observers.Observer;
import de.polarwolf.heliumballoon.observers.ObserverManager;
import de.polarwolf.heliumballoon.tools.chunktickets.ChunkTicketManager;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;

public abstract class PlacingManager {

	protected final Plugin plugin;
	protected final HeliumLogger logger;
	protected final ChunkTicketManager chunkTicketManager;
	protected final ConfigManager configManager;
	protected final ObserverManager observerManager;
	protected List<Placing> placings = new ArrayList<>();
	protected List<PlacingStarterScheduler> starters = new ArrayList<>();

	protected PlacingManager(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.chunkTicketManager = orchestrator.getChunkTicketManager();
		this.configManager = orchestrator.getConfigManager();
		this.observerManager = orchestrator.getObserverManager();
	}

	protected abstract Set<String> getBalloonSetNames();

	protected abstract ConfigPlaceableBalloonSet findBalloonSet(String placingName);

	protected abstract Placing createPlacing(ConfigPlaceableBalloonSet configPlaceableBalloonSet, World world);

	public List<Placing> getPlacings() {
		return new ArrayList<>(placings);
	}

	void registerStarter(PlacingStarterScheduler starter) {
		starters.add(starter);
	}

	void unregisterStarter(PlacingStarterScheduler oldStarter) {
		starters.remove(oldStarter);
	}

	protected void generateStarter(Observer newObserver) {
		int placingDelay = ConfigManager.getPlacingDelay(plugin) * 20;
		new PlacingStarterScheduler(plugin, observerManager, this, newObserver, placingDelay);
	}

	protected void cancelAllStarter() {
		for (PlacingStarterScheduler myStarter : new ArrayList<PlacingStarterScheduler>(starters)) {
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

	protected void registerPlacing(Placing newPlacing) throws BalloonException {
		placings.add(newPlacing);
		newPlacing.buildObservers();
		for (Observer myObserver : newPlacing.getObservers()) {
			if ((myObserver != null) && !myObserver.isCancelled() && !hasObserver(myObserver)) {
				generateStarter(myObserver);
			}
		}
	}

	protected void unregisterPlacing(Placing placing) {
		placing.cancel();
		placings.remove(placing);
	}

	public int removePlacings(World world) {
		int count = 0;
		List<Placing> placingList = new ArrayList<>(placings);
		for (Placing myPlacing : placingList) {
			if (myPlacing.getWorld().equals(world)) {
				unregisterPlacing(myPlacing);
				count = count + 1;
			}
		}
		return count;
	}

	public int addPlacings(World world) {
		int count = 0;
		removePlacings(world);
		try {
			for (String myPlacingName : getBalloonSetNames()) {
				ConfigPlaceableBalloonSet configPlaceableBalloonSet = findBalloonSet(myPlacingName);
				if (configPlaceableBalloonSet.isMatchingWorld(world)
						&& (configPlaceableBalloonSet.findTemplate(world) != null)) {
					String fullPlacingName = configPlaceableBalloonSet.getName() + "." + world.getName();
					logger.printDebug(String.format("Creating PlacingObserver: %s", fullPlacingName));
					Placing newPlacing = createPlacing(configPlaceableBalloonSet, world);
					registerPlacing(newPlacing);
					count = count + 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			removePlacings(world);
		}
		return count;
	}

	public int reload() {
		cancelAllStarter();
		int count = 0;
		for (World myWorld : plugin.getServer().getWorlds()) {
			int myCount = addPlacings(myWorld);
			if (myCount > 0) {
				logger.printInfo(String.format("%d placings created in world: %s", myCount, myWorld.getName()));
			}
			count = count + myCount;
		}
		return count;
	}

}
