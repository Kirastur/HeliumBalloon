package de.polarwolf.heliumballoon.config;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.players.PlayerPersistentData;

public class ConfigPlayer {

	public static final String PLAYER_FILE_NAME = "player.yml";
	public static final String PARAM_NAME = "Name";
	public static final String PARAM_LASTSEEN = "LastSeen";
	public static final String PARAM_PET = "Pet";

	protected File customPlayerFile;
	protected FileConfiguration playerFileConfiguration;

	public ConfigPlayer(Plugin plugin) throws BalloonException, IOException {
		customPlayerFile = new File(plugin.getDataFolder(), PLAYER_FILE_NAME);
		if (!customPlayerFile.exists() && !customPlayerFile.createNewFile()) {
			throw new BalloonException(null, "Cannot create config file", PLAYER_FILE_NAME);
		}
		playerFileConfiguration = YamlConfiguration.loadConfiguration(customPlayerFile);
	}

	public PlayerPersistentData findPlayerPersistentData(UUID playerUUID) {
		ConfigurationSection section = playerFileConfiguration.getConfigurationSection(playerUUID.toString());
		if (section == null) {
			return null;
		}

		PlayerPersistentData playerPersistentData = new PlayerPersistentData(playerUUID);
		playerPersistentData.setName(section.getString(PARAM_NAME));
		playerPersistentData.setLastSeen(Instant.parse(section.getString(PARAM_LASTSEEN)));
		playerPersistentData.setPetName(section.getString(PARAM_PET));
		return playerPersistentData;
	}

	public void writePlayerPersistentData(PlayerPersistentData playerPersistentData) throws IOException {
		String uuidString = playerPersistentData.getplayerUUID().toString();
		if (!playerFileConfiguration.isConfigurationSection(uuidString)) {
			playerFileConfiguration.createSection(uuidString);
		}
		ConfigurationSection section = playerFileConfiguration.getConfigurationSection(uuidString);
		section.set(PARAM_NAME, playerPersistentData.getName());
		section.set(PARAM_LASTSEEN, playerPersistentData.getLastSeen().toString());
		section.set(PARAM_PET, playerPersistentData.getPetName());
		playerFileConfiguration.save(customPlayerFile);

	}

	public void removePlayerPersistentData(UUID playerUUID) throws IOException {
		playerFileConfiguration.set(playerUUID.toString(), null);
		playerFileConfiguration.save(customPlayerFile);
	}

	public List<UUID> getAllPlayerPersistentDataUUIDs() {
		List<UUID> playerUUIDList = new ArrayList<>();
		for (String myString : playerFileConfiguration.getKeys(false)) {
			try {
				UUID myUUID = UUID.fromString(myString);
				playerUUIDList.add(myUUID);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return playerUUIDList;
	}

}
