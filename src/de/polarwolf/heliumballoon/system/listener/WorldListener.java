package de.polarwolf.heliumballoon.system.listener;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.balloons.rotators.RotatorManager;
import de.polarwolf.heliumballoon.balloons.walls.WallManager;
import de.polarwolf.heliumballoon.behavior.observers.ObserverManager;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;

public class WorldListener implements Listener {

	protected final Plugin plugin;
	protected final HeliumLogger logger;
	protected final ObserverManager observerManager;
	protected final WallManager wallManager;
	protected final RotatorManager rotatorManager;

	public WorldListener(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.observerManager = orchestrator.getObserverManager();
		this.wallManager = orchestrator.getWallManager();
		this.rotatorManager = orchestrator.getRotatorManager();
	}

	public void startup() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public void unregisterListener() {
		HandlerList.unregisterAll(this);
	}

	protected void resyncWorld(World world) {
		observerManager.cancelObserversByWorld(world);
	}

	public void handleWorldLoadEvent(WorldLoadEvent event) {
		World world = event.getWorld();
		logger.printDebug("Resync: WorldLoad " + world.getName());
		wallManager.addPlaceables(world);
		rotatorManager.addPlaceables(world);
	}

	public void handleWorldUnloadEvent(WorldUnloadEvent event) {
		World world = event.getWorld();
		logger.printDebug("Resync: WorldUnload " + world.getName());
		resyncWorld(world);
		wallManager.removePlaceables(world);
		rotatorManager.removePlaceables(world);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onWorldLoadEvent(WorldLoadEvent event) {
		try {
			handleWorldLoadEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onWorldUnloadEvent(WorldUnloadEvent event) {
		try {
			handleWorldUnloadEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
