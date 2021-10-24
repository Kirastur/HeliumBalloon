package de.polarwolf.heliumballoon.listener;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
	
	
	protected void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		Entity takerEntity = event.getEntity();
		Balloon balloonAsDamageTaker = balloonManager.findBalloonByEntity(takerEntity);
		if (balloonAsDamageTaker != null) {
			logger.printDebug("Cancelling EntityDamage for Balloon Element " + balloonAsDamageTaker.getName());
			event.setCancelled(true);
		}		
		Entity damagerEntity = event.getDamager();
		Balloon balloonAsDamager = balloonManager.findBalloonByEntity(damagerEntity);
		if (balloonAsDamager != null) {
			logger.printDebug("Cancelling EntityDamage for Balloon Element " + balloonAsDamager.getName());
			event.setCancelled(true);
		}		
	}
	
		
	protected void handleEntityEnterLoveModeEvent(EntityEnterLoveModeEvent event) {
		Entity entity = event.getEntity();
		Balloon balloon = balloonManager.findBalloonByEntity(entity);
		if (balloon != null) {
			logger.printDebug("Cancelling EntityEnterLoveMode for Balloon Element " + balloon.getName());
			event.setCancelled(true);
		}						
	}
	

	protected void handleEntityBreedEvent(EntityBreedEvent event) {
		Entity mother = event.getMother();
		Entity father = event.getFather();
		Balloon balloonMother = balloonManager.findBalloonByEntity(mother);
		Balloon balloonFather = balloonManager.findBalloonByEntity(father);
		if (balloonMother != null) {
			logger.printDebug("Cancelling EntityBreedEvent for Balloon Element " + balloonMother.getName());
			event.setCancelled(true);
		}						
		if (balloonFather != null) {
			logger.printDebug("Cancelling EntityBreedEvent for Balloon Element " + balloonFather.getName());
			event.setCancelled(true);
		}								
	}
	
	
	protected void handlePlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		Entity clickedEntity = event.getRightClicked();
		Balloon balloon = balloonManager.findBalloonByEntity(clickedEntity);
		if (balloon != null) {
			logger.printDebug("Cancelling PlayerInteractEntity for Balloon Element " + balloon.getName());
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
	

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityEnterLoveModeEvent(EntityEnterLoveModeEvent event) {
		try {
			handleEntityEnterLoveModeEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityBreedEvent(EntityBreedEvent event) {
		try {
			handleEntityBreedEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		try {
			handlePlayerInteractEntityEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
