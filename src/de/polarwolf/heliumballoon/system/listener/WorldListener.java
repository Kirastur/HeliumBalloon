package de.polarwolf.heliumballoon.system.listener;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.balloons.rotators.RotatorManager;
import de.polarwolf.heliumballoon.balloons.walls.WallManager;
import de.polarwolf.heliumballoon.observers.ObserverManager;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;

public class WorldListener implements Listener {

	protected final HeliumLogger logger;
	protected final ObserverManager observerManager;
	protected final WallManager wallManager;
	protected final RotatorManager rotatorManager;

	public WorldListener(HeliumBalloonOrchestrator orchestrator) {
		Plugin plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.observerManager = orchestrator.getObserverManager();
		this.wallManager = orchestrator.getWallManager();
		this.rotatorManager = orchestrator.getRotatorManager();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
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
		wallManager.addPlacings(world);
		rotatorManager.addPlacings(world);
	}

	public void handleWorldUnloadEvent(WorldUnloadEvent event) {
		World world = event.getWorld();
		logger.printDebug("Resync: WorldUnload " + world.getName());
		resyncWorld(world);
		wallManager.removePlacings(world);
		rotatorManager.removePlacings(world);
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
