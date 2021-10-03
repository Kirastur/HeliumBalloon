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
import de.polarwolf.heliumballoon.players.PlayerPersistentPet;

public class ConfigPlayer {

	public static final String PLAYER_FILE_NAME = "player.yml";
	public static final String PARAM_NAME = "Name";
	public static final String PARAM_LASTSEEN = "LastSeen";
	public static final String PARAM_TEMPLATE = "Template";

	protected File customPlayerFile;
	protected FileConfiguration playerFileConfiguration;
	
	
	public ConfigPlayer(Plugin plugin) throws BalloonException, IOException {
		customPlayerFile = new File(plugin.getDataFolder(), PLAYER_FILE_NAME);
		if (!customPlayerFile.exists() && !customPlayerFile.createNewFile()) {
			throw new BalloonException (null, "Cannot create config file", PLAYER_FILE_NAME);
		}
		playerFileConfiguration = YamlConfiguration.loadConfiguration(customPlayerFile);	
	}
	
	
	public PlayerPersistentPet findPlayerPersistentPet(UUID playerUUID) {
		ConfigurationSection section = playerFileConfiguration.getConfigurationSection(playerUUID.toString());
		if (section == null) {
			return null;
		}
		
		PlayerPersistentPet playerPersistentPet = new PlayerPersistentPet(playerUUID);
		playerPersistentPet.setName(section.getString(PARAM_NAME));
		playerPersistentPet.setLastSeen(Instant.parse(section.getString(PARAM_LASTSEEN)));
		playerPersistentPet.setTemplateName(section.getString(PARAM_TEMPLATE));
		return playerPersistentPet;
	}
	
	
	public void writePlayerPersistentPet(PlayerPersistentPet playerPersistentPet) throws IOException {
		String uuidString = playerPersistentPet.getplayerUUID().toString();
		if (!playerFileConfiguration.isConfigurationSection(uuidString)) {
			playerFileConfiguration.createSection(uuidString);
		}
		ConfigurationSection section = playerFileConfiguration.getConfigurationSection(uuidString);
		section.set(PARAM_NAME, playerPersistentPet.getName());
		section.set(PARAM_LASTSEEN, playerPersistentPet.getLastSeen().toString());
		section.set(PARAM_TEMPLATE, playerPersistentPet.getTemplateName());
		playerFileConfiguration.save(customPlayerFile);				
		
	}
	
	
	public void removePlayerPersistentPet(UUID playerUUID) throws IOException {
		playerFileConfiguration.set(playerUUID.toString(), null);
		playerFileConfiguration.save(customPlayerFile);				
	}
	
		
	public List<UUID> getAllPlayerPersistentPetUUIDs() {
		List<UUID> playerUUIDList = new ArrayList<>();
		for (String myString : playerFileConfiguration.getKeys(false)) {
			try { 
				UUID myUUID = UUID.fromString(myString);
				playerUUIDList.add(myUUID);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return playerUUIDList;
	}

}
