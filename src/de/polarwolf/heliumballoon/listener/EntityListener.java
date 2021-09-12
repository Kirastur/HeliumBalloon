package de.polarwolf.heliumballoon.listener;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.balloons.Balloon;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.helium.HeliumLogger;

public class EntityListener implements Listener {
	
	protected final HeliumLogger logger;
	protected final BalloonManager balloonManager;
	
	
	public EntityListener(HeliumBalloonOrchestrator orchestrator) {
		Plugin plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.balloonManager = orchestrator.getBalloonManager();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	public void unregisterListener() {
		HandlerList.unregisterAll(this);
	}
	

	protected void handleEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		Entity whatEntity = event.getEntity();
		Balloon balloon = balloonManager.findBalloonByEntity(whatEntity);
		if (balloon != null) {
			logger.printDebug("Cancelling EntityChangeBlock for Balloon Element " + balloon.getName());
			event.setCancelled(true);
		}
	}
	
	
	protected void handleEntityDropItemEvent(EntityDropItemEvent event) {
		Entity entity=event.getEntity();
		Balloon balloon = balloonManager.findBalloonByEntity(entity);
		if (balloon != null) {
			logger.printDebug("Cancelling EntityDrop for Balloon Element " + balloon.getName());
			event.setCancelled(true);
		}
				
	}
	
	
	protected void handleEntityDamageByEntityEvent (EntityDamageByEntityEvent event) {
		Entity entity = event.getDamager();
		Balloon balloon = balloonManager.findBalloonByEntity(entity);
		if (balloon != null) {
			logger.printDebug("Cancelling EntityDrop for Balloon Element " + balloon.getName());
			event.setCancelled(true);
		}		
	}


	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		try {
			handleEntityChangeBlockEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDropItemEvent(EntityDropItemEvent event) {
		try {
			handleEntityDropItemEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		try {
			handleEntityDamageByEntityEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
