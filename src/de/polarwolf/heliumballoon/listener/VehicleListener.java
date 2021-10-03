package de.polarwolf.heliumballoon.listener;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.balloons.Balloon;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.helium.HeliumLogger;

public class VehicleListener implements Listener {

	protected final HeliumLogger logger;
	protected final BalloonManager balloonManager;
	

	public VehicleListener(HeliumBalloonOrchestrator orchestrator) {
		Plugin plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.balloonManager = orchestrator.getBalloonManager();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	public void unregisterListener() {
		HandlerList.unregisterAll(this);
	}
	

	public void handleVehicleEnterEvent(VehicleEnterEvent event) {
		Entity vehicleEntity = event.getVehicle();
		Balloon balloon = balloonManager.findBalloonByEntity(vehicleEntity);
		if (balloon != null) {
			logger.printDebug("Cancelling EnterVehicle for Balloon Element " + balloon.getName());
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
