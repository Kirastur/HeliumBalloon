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
		runTaskTimer(orchestrator.getPlugin(), 1200, 20 * 60 * checkIntervalMinutes);
	}

	public void disable() {
		try {
			cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String findPersistentPetForPlayer(OfflinePlayer offlinePlayer) {
		PlayerPersistentData playerPersistentData = configManager.findPlayerPersistentData(offlinePlayer.getUniqueId());
		if (playerPersistentData == null) {
			return null;
		} else {
			return playerPersistentData.getPetName();
		}
	}

	public boolean setPersistentPetForPlayer(OfflinePlayer offlinePlayer, String petName) {
		PlayerPersistentData playerPersistentData = new PlayerPersistentData(offlinePlayer.getUniqueId());
		playerPersistentData.setName(offlinePlayer.getName());
		playerPersistentData.setLastSeen(Instant.now());
		playerPersistentData.setPetName(petName);
		return configManager.writePlayerPersistentData(playerPersistentData);
	}

	public boolean removePersistentPetFromPlayer(OfflinePlayer offlinePlayer) {
		return configManager.removePlayerPersistentData(offlinePlayer.getUniqueId());
	}

	public void touchPersistentPetForPlayer(OfflinePlayer offlinePlayer) {
		PlayerPersistentData playerPersistentData = configManager.findPlayerPersistentData(offlinePlayer.getUniqueId());
		if (playerPersistentData == null) {
			return;
		}
		playerPersistentData.setName(offlinePlayer.getName());
		playerPersistentData.setLastSeen(Instant.now());
		configManager.writePlayerPersistentData(playerPersistentData);
	}

	public int purgeOldPlayers() {
		int count = 0;
		if (configManager.getKeepPlayerDays() == 0) {
			return 0;
		}
		Instant cutoverTime = Instant.now().minus(configManager.getKeepPlayerDays(), ChronoUnit.DAYS);
		for (UUID playerUUID : configManager.getAllPlayerPersistentDataUUIDs()) {
			try {
				PlayerPersistentData playerPersistentData = configManager.findPlayerPersistentData(playerUUID);
				if ((playerPersistentData == null)
						|| (playerPersistentData.getLastSeen().compareTo(cutoverTime) <= 0)) {
					configManager.removePlayerPersistentData(playerUUID);
					count = count + 1;
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
