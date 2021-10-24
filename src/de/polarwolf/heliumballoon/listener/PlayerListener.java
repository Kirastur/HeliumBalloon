package de.polarwolf.heliumballoon.listener;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.helium.HeliumLogger;
import de.polarwolf.heliumballoon.pets.PetManager;
import de.polarwolf.heliumballoon.players.PlayerManager;

public class PlayerListener implements Listener {
	
	protected final HeliumLogger logger;
	protected final PlayerManager playerManager;
	protected final BalloonManager balloonManager;
	protected final PetManager petManager;
	

	public PlayerListener(HeliumBalloonOrchestrator orchestrator) {
		Plugin plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.playerManager = orchestrator.getPlayerManager();
		this.balloonManager = orchestrator.getBalloonManager();
		this.petManager = orchestrator.getPetManager();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	public void unregisterListener() {
		HandlerList.unregisterAll(this);
	}
	

	protected void resyncPlayer(Player player) {
		balloonManager.cancelBalloonsByPlayer(player);
	}
	
	
	protected void wakeupBalloons(Player player) {
		balloonManager.wakeupBalloonsByPlayer(player);
	}
	
	
	public void handlePlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		logger.printDebug("Init: PlayerJoin " + player.getName());
		resyncPlayer(player);
		playerManager.touchPetForPlayer(player);
	}
	
	
	public void handlePlayerSpawnLocationEvent(PlayerSpawnLocationEvent event) {
		Player player = event.getPlayer();
		logger.printDebug("Init: PlayerSpawnLocation " + player.getName());		
		resyncPlayer(player);
	}
	
	
	public void handlePlayerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location toLocation = event.getFrom();
		Location fromLocation = event.getTo();
		if (toLocation.getWorld().equals(fromLocation.getWorld())) {
			wakeupBalloons(player);
		} else {
			logger.printDebug("Resync: PlayerMove " + player.getName());
			resyncPlayer(player);
		}
	}
	
	
	public void handlePlayerTeleportEvent(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		Location toLocation = event.getFrom();
		Location fromLocation = event.getTo();
		if (!toLocation.getWorld().equals(fromLocation.getWorld())) {
			logger.printDebug("Resync: PlayerTeleport " + player.getName());
			resyncPlayer(player);				
		}
	}

	
	public void handlePlayerChangeWorldEvent(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		logger.printDebug("Resync: PlayerChangeWorld " + player.getName());
		resyncPlayer(player);
	}
	
	
	public void handlePlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		Player player = event.getPlayer();
		GameMode oldGameMode = event.getPlayer().getGameMode();
		GameMode newGameMode = event.getNewGameMode();
		// On Paper the old game mode can be null, documentation is wrong here 
		if (oldGameMode == null) { //NOSONAR
			return;
		}
		if (!newGameMode.equals(GameMode.SPECTATOR) && oldGameMode.equals(GameMode.SPECTATOR)) {
			logger.printDebug("Init: PlayerGameModeChange from Spectator " + player.getName());
			resyncPlayer(player);
		}
		if (newGameMode.equals(GameMode.SPECTATOR) && !oldGameMode.equals(GameMode.SPECTATOR)) {
			logger.printDebug("Done: PlayerGameModeChange to Spectator " + player.getName());
			resyncPlayer(player);
		}		
	}
	
	
	public void handlePlayerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();
		logger.printDebug("Done: PlayerRespawn " + player.getName());
		resyncPlayer(player);		
	}
	
	
	public void handlePlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		logger.printDebug("Done: PlayerRespawn " + player.getName());
		resyncPlayer(player);
	}
	
	
	public void handlePlayerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		logger.printDebug("Done: PlayerQuit " + player.getName());
		petManager.removePlayerCancelScore(player);
		resyncPlayer(player);
	}


	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		try {
			handlePlayerJoinEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerSpawnLocationEvent(PlayerSpawnLocationEvent event) {
		try {
			handlePlayerSpawnLocationEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		try {
			handlePlayerMoveEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
		
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
		try {
			handlePlayerTeleportEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerChangeWorldEvent(PlayerChangedWorldEvent event) {
		try {
			handlePlayerChangeWorldEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		try {
			handlePlayerGameModeChangeEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		try {
			handlePlayerDeathEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		try {
			handlePlayerRespawnEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		try {
			handlePlayerQuitEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
