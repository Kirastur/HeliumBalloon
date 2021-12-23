package de.polarwolf.heliumballoon.system.listener;

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
import de.polarwolf.heliumballoon.observers.Observer;
import de.polarwolf.heliumballoon.observers.ObserverManager;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;

public class EntityListener implements Listener {

	protected final HeliumLogger logger;
	protected final ObserverManager observerManager;

	public EntityListener(HeliumBalloonOrchestrator orchestrator) {
		Plugin plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.observerManager = orchestrator.getObserverManager();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void unregisterListener() {
		HandlerList.unregisterAll(this);
	}

	protected void handleEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		Entity whatEntity = event.getEntity();
		Observer observer = observerManager.findObserverByEntity(whatEntity);
		if (observer != null) {
			logger.printDebug("Cancelling EntityChangeBlock for Balloon Element " + observer.getName());
			event.setCancelled(true);
		}
	}

	protected void handleEntityDropItemEvent(EntityDropItemEvent event) {
		Entity entity = event.getEntity();
		Observer observer = observerManager.findObserverByEntity(entity);
		if (observer != null) {
			logger.printDebug("Cancelling EntityDrop for Balloon Element " + observer.getName());
			event.setCancelled(true);
		}
	}

	protected void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		Entity takerEntity = event.getEntity();
		Observer observerAsDamageTaker = observerManager.findObserverByEntity(takerEntity);
		if (observerAsDamageTaker != null) {
			logger.printDebug("Cancelling EntityDamage for Balloon Element " + observerAsDamageTaker.getName());
			event.setCancelled(true);
		}
		Entity damagerEntity = event.getDamager();
		Observer observerAsDamager = observerManager.findObserverByEntity(damagerEntity);
		if (observerAsDamager != null) {
			logger.printDebug("Cancelling EntityDamage for Balloon Element " + observerAsDamager.getName());
			event.setCancelled(true);
		}
	}

	protected void handleEntityEnterLoveModeEvent(EntityEnterLoveModeEvent event) {
		Entity entity = event.getEntity();
		Observer observer = observerManager.findObserverByEntity(entity);
		if (observer != null) {
			logger.printDebug("Cancelling EntityEnterLoveMode for Balloon Element " + observer.getName());
			event.setCancelled(true);
		}
	}

	protected void handleEntityBreedEvent(EntityBreedEvent event) {
		Entity mother = event.getMother();
		Entity father = event.getFather();
		Observer observerMother = observerManager.findObserverByEntity(mother);
		Observer observerFather = observerManager.findObserverByEntity(father);
		if (observerMother != null) {
			logger.printDebug("Cancelling EntityBreedEvent for Balloon Element " + observerMother.getName());
			event.setCancelled(true);
		}
		if (observerFather != null) {
			logger.printDebug("Cancelling EntityBreedEvent for Balloon Element " + observerFather.getName());
			event.setCancelled(true);
		}
	}

	protected void handlePlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		Entity clickedEntity = event.getRightClicked();
		Observer observer = observerManager.findObserverByEntity(clickedEntity);
		if (observer != null) {
			logger.printDebug("Cancelling PlayerInteractEntity for Balloon Element " + observer.getName());
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
