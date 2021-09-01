package de.polarwolf.heliumballoon.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.logger.HeliumLogger;
import de.polarwolf.heliumballoon.messages.Message;
import de.polarwolf.heliumballoon.players.PlayerPersistentPet;
import de.polarwolf.heliumballoon.system.IntlText;

public class ConfigManager {

	public static final String SECTION_STARTUP = "startup";
	public static final String SECTION_GDPR = "GDPR";
	public static final String PARAM_STARTUP_PASSIVEMODE = "passiveMode";
	public static final String PARAM_STARTUP_DEBUG = "debug";
	public static final String PARAM_STARTUP_EXCEPTION_QUOTA = "exceptionQuota";
	public static final String PARAM_GDPR_KEEP_PLAYER_DAYS = "keepPlayerDays";
	public static final String PARAM_GDPR_RUN_PURGE_HOUR = "runPurgeHour";
	
	public static final boolean DEFAULT_PASSIVEMODE = false;
	public static final boolean DEFAULT_DEBUG = false;
	public static final int DEFAULT_EXCEPTION_QUOTA = 10;
	public static final int DEFAULT_KEEP_PLAYER_DAYS = 30;
	public static final int DEFAULT_RUN_PURGE_HOUR = 5;


	protected final Plugin plugin;
	protected final HeliumLogger logger;
	protected final ConfigPlayer configPlayer;
	protected final ConfigMessage configMessage;
	protected ConfigSection section;
	

	public ConfigManager(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		section = new ConfigSection();
		configPlayer = buildConfigPlayer();
		configMessage = buildConfigMessage();		
	}
	
	
	public static boolean isPassiveMode(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP).getBoolean(PARAM_STARTUP_PASSIVEMODE, DEFAULT_PASSIVEMODE);
	}
		

	public static boolean isInitialDebug(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP).getBoolean(PARAM_STARTUP_DEBUG, DEFAULT_DEBUG);
	}

	
	public static int getExceptionQuota(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP).getInt(PARAM_STARTUP_EXCEPTION_QUOTA, DEFAULT_EXCEPTION_QUOTA);
	}

	
	public int getKeepPayerDays() {
		return plugin.getConfig().getConfigurationSection(SECTION_GDPR).getInt(PARAM_GDPR_KEEP_PLAYER_DAYS, DEFAULT_KEEP_PLAYER_DAYS);
	}

	
	public int getRunPurgeHour() {
		return plugin.getConfig().getConfigurationSection(SECTION_GDPR).getInt(PARAM_GDPR_RUN_PURGE_HOUR, DEFAULT_RUN_PURGE_HOUR);
	}

	
	public ConfigRule findRule(String ruleName) {
		return section.findRule(ruleName);
	}
	
	
	public Set<String> getRuleNames() {
		return section.getRuleNames();
	}


	public ConfigTemplate findTemplate(String templateName) {
		return section.findTemplate(templateName);
	}
	
	
	public Set<String> getTemplateNames() {
		return section.getTemplateNames();
	}
	
	
	public ConfigWall findWall(String wallName) {
		return section.findWall(wallName);
	}
	
	
	public Set<String> getWallNames() {
		return section.getWallNames();
	}

	
	public boolean hasWorld(String worldName) {
		return section.hasWorld(worldName);
	}
	
	
	public String getGuiTitle(CommandSender sender) {
		return section.getGuiTitle(sender);
	}
	
	
	public List<ConfigGuiItem> enumGuiItems() {
		return section.enumGuiItems();
	}
	

	public void reload() throws BalloonException {
		ConfigurationSection fileSection = plugin.getConfig().getRoot();
		ConfigSection newSection = new ConfigSection();
		newSection.loadConfig(fileSection);
		section = newSection;
		
		int ruleCount = section.getRuleNames().size();
		int templateCount = section.getTemplateNames().size();
		int wallCount = section.getWallNames().size();
		int guiItemCount = section.enumGuiItems().size();
		logger.printInfo(String.format("%d rules, %d templates, %d walls and %d GUI items loaded", ruleCount, templateCount, wallCount, guiItemCount));
	}


	public ConfigPlayer buildConfigPlayer() {
		try {
			return new ConfigPlayer(plugin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public PlayerPersistentPet findPlayerPersistentPet(UUID playerUUID) {
		try {
			if (configPlayer != null) {
				return configPlayer.findPlayerPersistentPet(playerUUID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public boolean hasPlayerPersistentPet(UUID playerUUID) {
		return findPlayerPersistentPet(playerUUID) != null;
	}


	public boolean writePlayerPersistentPet(PlayerPersistentPet playerPersistentPet) {
		try {
			if (configPlayer != null) {
				configPlayer.writePlayerPersistentPet(playerPersistentPet);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}


	public boolean removePlayerPersistentPet(UUID playerUUID) {
		try {
			if (configPlayer != null) {
				configPlayer.removePlayerPersistentPet(playerUUID);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public List<UUID> getAllPlayerPersistentPetUUIDs() {
		if (configPlayer != null) {
			return configPlayer.getAllPlayerPersistentPetUUIDs();
		} else {
			return new ArrayList<>();
		}
	}
	

	protected ConfigMessage buildConfigMessage() {
		try {
			return new ConfigMessage(plugin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public IntlText getMessage(Message messageId) {
		if (configMessage != null) {
			return configMessage.getMessage(messageId);
		}
		return new IntlText("");
	}


}