package de.polarwolf.heliumballoon.system.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.behavior.observers.Observer;
import de.polarwolf.heliumballoon.behavior.observers.ObserverManager;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;

public class VehicleListener implements Listener {

	protected final Plugin plugin;
	protected final HeliumLogger logger;
	protected final ObserverManager observerManager;

	public VehicleListener(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.observerManager = orchestrator.getObserverManager();
	}

	public void startup() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public void unregisterListener() {
		HandlerList.unregisterAll(this);
	}

	public void handleVehicleEnterEvent(VehicleEnterEvent event) {
		Entity vehicleEntity = event.getVehicle();
		Observer observer = observerManager.findObserverByEntity(vehicleEntity);
		if (observer != null) {
			logger.printDebug("Cancelling EnterVehicle for Balloon Element " + observer.getName());
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onVehicleEnterEvent(VehicleEnterEvent event) {
		try {
			handleVehicleEnterEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
