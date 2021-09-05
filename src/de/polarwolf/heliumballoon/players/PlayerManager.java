package de.polarwolf.heliumballoon.players;

import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.helium.HeliumLogger;

public class PlayerManager extends BukkitRunnable {
	
	protected final HeliumLogger logger; 	
	protected final ConfigManager configManager;
	protected Instant lastAutoPurge = null;
	
	
	public PlayerManager(HeliumBalloonOrchestrator orchestrator) {
		this.logger = orchestrator.getHeliumLogger();
		this.configManager = orchestrator.getConfigManager();
		long checkIntervalMinutes = 30; 
		runTaskTimer(orchestrator.getPlugin(), 1200, 20*60*checkIntervalMinutes);
	}
	
	
	public void disable() {
		try {
			cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public String findPetForPlayer(OfflinePlayer offlinePlayer) {
		PlayerPersistentPet playerPersistentPet = configManager.findPlayerPersistentPet(offlinePlayer.getUniqueId());
		if (playerPersistentPet == null) {
			return null;
		} else {
			return playerPersistentPet.getTemplateName();
		}
	}

	
	public boolean setPetForPlayer(OfflinePlayer offlinePlayer, String templateName) {
		PlayerPersistentPet playerPersistentPet = new PlayerPersistentPet(offlinePlayer.getUniqueId());
		playerPersistentPet.setName(offlinePlayer.getName());
		playerPersistentPet.setLastSeen(Instant.now());
		playerPersistentPet.setTemplateName(templateName);
		return configManager.writePlayerPersistentPet(playerPersistentPet);
	}
	
	
	public boolean removePetForPlayer(OfflinePlayer offlinePlayer) {
		return configManager.removePlayerPersistentPet(offlinePlayer.getUniqueId());
	}
	
	
	public void touchPetForPlayer(OfflinePlayer offlinePlayer) {
		PlayerPersistentPet playerPersistentPet = configManager.findPlayerPersistentPet(offlinePlayer.getUniqueId());
		if (playerPersistentPet == null) {
			return;
		}
		playerPersistentPet.setName(offlinePlayer.getName());
		playerPersistentPet.setLastSeen(Instant.now());
		configManager.writePlayerPersistentPet(playerPersistentPet);		
	}
	
	
	public int purgeOldPlayers() {
		int count = 0;
		if (configManager.getKeepPlayerDays() == 0) {
			return 0;
		}
		Instant cutoverTime = Instant.now().minus(configManager.getKeepPlayerDays(), ChronoUnit.DAYS);
		for (UUID playerUUID : configManager.getAllPlayerPersistentPetUUIDs()) {
			try {
				PlayerPersistentPet playerPersistentPet = configManager.findPlayerPersistentPet(playerUUID);
				if ((playerPersistentPet == null) || (playerPersistentPet.getLastSeen().compareTo(cutoverTime) <= 0)) {
					configManager.removePlayerPersistentPet(playerUUID);
					count = count +1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.printInfo(String.format("AutoPurge has deleted %d entries.", count));
		return count;
	}
	
	
	protected void handleRun() {
		if (LocalTime.now().getHour() != configManager.getRunPurgeHour()) {
			return;
		}
		
		if ((lastAutoPurge == null) || (lastAutoPurge.compareTo(Instant.now().minus(4, ChronoUnit.HOURS)) <= 0)) {
			logger.printInfo("Starting AutoPurge");
			lastAutoPurge = Instant.now();
		}
	}
	
	
	@Override
    public void run() {
		try {
			handleRun();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
