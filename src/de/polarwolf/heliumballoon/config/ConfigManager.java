package de.polarwolf.heliumballoon.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.events.EventManager;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.system.players.PlayerPersistentData;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;
import de.polarwolf.heliumballoon.tools.helium.HeliumText;
import de.polarwolf.heliumballoon.tools.messages.Message;

public class ConfigManager {

	public static final String SECTION_STARTUP = "startup";
	public static final String SECTION_GDPR = "GDPR";
	public static final String PARAM_STARTUP_PASSIVEMODE = "passiveMode";
	public static final String PARAM_LOAD_LOCAL_CONFIG = "loadLocalConfig";
	public static final String PARAM_STARTUP_WARN_ON_WRONG_PURPOSE = "warnOnWrongPurpose";
	public static final String PARAM_STARTUP_DEBUG = "debug";
	public static final String PARAM_STARTUP_PLACING_DELAY = "placingDelay";
	public static final String PARAM_STARTUP_EXCEPTION_QUOTA = "exceptionQuota";
	public static final String PARAM_GDPR_KEEP_PLAYER_DAYS = "keepPlayerDays";
	public static final String PARAM_GDPR_RUN_PURGE_HOUR = "runPurgeHour";

	public static final boolean DEFAULT_PASSIVEMODE = false;
	public static final boolean DEFAULT_LOAD_LOCAL_CONFIG = true;
	public static final boolean DEFAULT_DEBUG = false;
	public static final int DEFAULT_PLACING_DELAY = 3;
	public static final int DEFAULT_EXCEPTION_QUOTA = 10;
	public static final boolean DEFAULT_WARN_ON_WRONG_PURPOSE = true;
	public static final int DEFAULT_KEEP_PLAYER_DAYS = 30;
	public static final int DEFAULT_RUN_PURGE_HOUR = 5;

	private final int keepPlayerDays;
	private final int runPurgeHour;

	protected final Plugin plugin;
	protected final HeliumLogger logger;
	protected final EventManager eventManager;
	protected final ConfigPlayer configPlayer;
	protected final ConfigMessage configMessage;
	protected List<ConfigSection> sections = new ArrayList<>();

	public ConfigManager(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.eventManager = orchestrator.getEventManager();
		configPlayer = buildConfigPlayer();
		configMessage = buildConfigMessage();
		keepPlayerDays = plugin.getConfig().getConfigurationSection(SECTION_GDPR).getInt(PARAM_GDPR_KEEP_PLAYER_DAYS,
				DEFAULT_KEEP_PLAYER_DAYS);
		runPurgeHour = plugin.getConfig().getConfigurationSection(SECTION_GDPR).getInt(PARAM_GDPR_RUN_PURGE_HOUR,
				DEFAULT_RUN_PURGE_HOUR);
	}

	public static boolean isPassiveMode(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP).getBoolean(PARAM_STARTUP_PASSIVEMODE,
				DEFAULT_PASSIVEMODE);
	}

	public static boolean isLoadLocalConfig(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP).getBoolean(PARAM_LOAD_LOCAL_CONFIG,
				DEFAULT_LOAD_LOCAL_CONFIG);
	}

	public static boolean isInitialDebug(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP).getBoolean(PARAM_STARTUP_DEBUG,
				DEFAULT_DEBUG);
	}

	public static int getPlacingDelay(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP).getInt(PARAM_STARTUP_PLACING_DELAY,
				DEFAULT_PLACING_DELAY);
	}

	public static int getExceptionQuota(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP).getInt(PARAM_STARTUP_EXCEPTION_QUOTA,
				DEFAULT_EXCEPTION_QUOTA);
	}

	public static boolean getWarnOnWrongPurpose(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP)
				.getBoolean(PARAM_STARTUP_WARN_ON_WRONG_PURPOSE, DEFAULT_WARN_ON_WRONG_PURPOSE);
	}

	public int getKeepPlayerDays() {
		return keepPlayerDays;
	}

	public int getRunPurgeHour() {
		return runPurgeHour;
	}

	protected ConfigSection findSection(String sectionName) {
		for (ConfigSection mySection : sections) {
			if (mySection.getName().equals(sectionName)) {
				return mySection;
			}
		}
		return null;
	}

	public Set<String> getSectionNames() {
		Set<String> allSectionNames = new TreeSet<>();
		for (ConfigSection mySection : sections) {
			allSectionNames.add(mySection.getName());
		}
		return allSectionNames;
	}

	public String dumpSection(String sectionName) {
		for (ConfigSection mySection : sections) {
			if (mySection.getName().equals(sectionName)) {
				return mySection.toString();
			}
		}
		return null;
	}

	public ConfigWorldset findWorldsetInSection(String sectionName, String worldsetName) {
		ConfigSection mySection = findSection(sectionName);
		if (mySection == null) {
			return null;
		}
		return mySection.findWorldset(worldsetName);
	}

	public ConfigRule findRuleInSection(String sectionName, String ruleName) {
		ConfigSection mySection = findSection(sectionName);
		if (mySection == null) {
			return null;
		}
		return mySection.findRule(ruleName);
	}

	public ConfigTemplate findTemplateInSection(String sectionName, String templateName) {
		ConfigSection mySection = findSection(sectionName);
		if (mySection == null) {
			return null;
		}
		return mySection.findTemplate(templateName);
	}

	public ConfigPet findPet(String petName) {
		for (ConfigSection mySection : sections) {
			ConfigPet myPet = mySection.findPet(petName);
			if (myPet != null) {
				return myPet;
			}
		}
		return null;
	}

	public Set<String> getPetNames() {
		Set<String> allPetNames = new TreeSet<>();
		for (ConfigSection mySection : sections) {
			allPetNames.addAll(mySection.getPetNames());
		}
		return allPetNames;
	}

	public ConfigWall findWall(String wallName) {
		for (ConfigSection mySection : sections) {
			ConfigWall myWall = mySection.findWall(wallName);
			if (myWall != null) {
				return myWall;
			}
		}
		return null;
	}

	public Set<String> getWallNames() {
		Set<String> allWallNames = new TreeSet<>();
		for (ConfigSection mySection : sections) {
			allWallNames.addAll(mySection.getWallNames());
		}
		return allWallNames;
	}

	public ConfigRotator findRotator(String rotatorName) {
		for (ConfigSection mySection : sections) {
			ConfigRotator myRotator = mySection.findRotator(rotatorName);
			if (myRotator != null) {
				return myRotator;
			}
		}
		return null;
	}

	public Set<String> getRotatorNames() {
		Set<String> allRotatorNames = new TreeSet<>();
		for (ConfigSection mySection : sections) {
			allRotatorNames.addAll(mySection.getRotatorNames());
		}
		return allRotatorNames;
	}

	public ConfigGuiMenu getGuiMenu() {
		ConfigGuiMenu lastGuiMenu = null;
		for (ConfigSection mySection : sections) {
			ConfigGuiMenu myGuiMenu = mySection.getGuiMenu();
			if (myGuiMenu != null) {
				lastGuiMenu = myGuiMenu;
			}
		}
		return lastGuiMenu;
	}

	public ConfigSection buildConfigSectionFromFileSection(String sectionName, ConfigurationSection fileSection)
			throws BalloonException {
		return new ConfigSection(sectionName, fileSection);
	}

	public ConfigSection buildConfigSectionFromConfigFile(Plugin fileOwnerPlugin) throws BalloonException {
		ConfigurationSection fileSection = fileOwnerPlugin.getConfig().getRoot();
		return buildConfigSectionFromFileSection(fileOwnerPlugin.getName(), fileSection);
	}

	public String reload(List<ConfigSection> newSections) {
		sections = newSections;
		int sectionCount = getSectionNames().size();
		int petCount = getPetNames().size();
		int wallCount = getWallNames().size();
		int rotatorCount = getRotatorNames().size();
		String s = String.format("%d sections, %d pets, %d walls, and %d rotators loaded", sectionCount, petCount,
				wallCount, rotatorCount);
		plugin.getLogger().info(s);
		return s;
	}

	public ConfigPlayer buildConfigPlayer() {
		try {
			return new ConfigPlayer(plugin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public PlayerPersistentData findPlayerPersistentData(UUID playerUUID) {
		try {
			if (configPlayer != null) {
				return configPlayer.findPlayerPersistentData(playerUUID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean hasPlayerPersistentData(UUID playerUUID) {
		return findPlayerPersistentData(playerUUID) != null;
	}

	public boolean writePlayerPersistentData(PlayerPersistentData playerPersistentData) {
		try {
			if (configPlayer != null) {
				configPlayer.writePlayerPersistentData(playerPersistentData);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean removePlayerPersistentData(UUID playerUUID) {
		try {
			if (configPlayer != null) {
				configPlayer.removePlayerPersistentData(playerUUID);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<UUID> getAllPlayerPersistentDataUUIDs() {
		if (configPlayer != null) {
			return configPlayer.getAllPlayerPersistentDataUUIDs();
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

	public HeliumText getMessage(Message messageId) {
		if (configMessage != null) {
			return configMessage.getMessage(messageId);
		}
		return new HeliumText("");
	}

}