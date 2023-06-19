package de.polarwolf.heliumballoon.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.behavior.BehaviorManager;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.config.gui.ConfigGuiMenu;
import de.polarwolf.heliumballoon.elements.ElementDefinition;
import de.polarwolf.heliumballoon.elements.ElementManager;
import de.polarwolf.heliumballoon.events.EventManager;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonStartOptions;
import de.polarwolf.heliumballoon.system.players.PlayerPersistentData;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumText;
import de.polarwolf.heliumballoon.tools.messages.Message;

public class ConfigManager implements ConfigHelper {

	public static final String SECTION_STARTUP = "startup";
	public static final String SECTION_GDPR = "GDPR";
	public static final String PARAM_STARTUP_PASSIVEMODE = "passiveMode";
	public static final String PARAM_LOAD_LOCAL_CONFIG = "loadLocalConfig";
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
	public static final int DEFAULT_KEEP_PLAYER_DAYS = 30;
	public static final int DEFAULT_RUN_PURGE_HOUR = 5;

	private final int keepPlayerDays;
	private final int runPurgeHour;

	protected final Plugin plugin;
	protected final HeliumLogger logger;
	protected final EventManager eventManager;
	protected final ElementManager elementManager;
	protected final BehaviorManager behaviorManager;
	protected final BalloonManager balloonManager;
	protected final ConfigPlayer configPlayer;
	protected final ConfigMessage configMessage;
	protected List<ConfigSection> sections = new ArrayList<>();

	public ConfigManager(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.eventManager = orchestrator.getEventManager();
		this.elementManager = orchestrator.getElementManager();
		this.behaviorManager = orchestrator.getBehaviorManager();
		this.balloonManager = orchestrator.getBalloonManager();
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

	protected static boolean isLoadLocalConfig(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP).getBoolean(PARAM_LOAD_LOCAL_CONFIG,
				DEFAULT_LOAD_LOCAL_CONFIG);
	}

	protected static boolean isInitialDebug(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP).getBoolean(PARAM_STARTUP_DEBUG,
				DEFAULT_DEBUG);
	}

	protected static int getPlacingDelay(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP).getInt(PARAM_STARTUP_PLACING_DELAY,
				DEFAULT_PLACING_DELAY);
	}

	protected static int getExceptionQuota(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP).getInt(PARAM_STARTUP_EXCEPTION_QUOTA,
				DEFAULT_EXCEPTION_QUOTA);
	}

	public static HeliumBalloonStartOptions getStartOptions(Plugin startupPlugin) {
		boolean loadLocalConfig = isLoadLocalConfig(startupPlugin);
		boolean initialDebug = isInitialDebug(startupPlugin);
		int placingDelay = getPlacingDelay(startupPlugin);
		int exceptionQuota = getExceptionQuota(startupPlugin);
		return new HeliumBalloonStartOptions(loadLocalConfig, initialDebug, placingDelay, exceptionQuota);
	}

	public int getKeepPlayerDays() {
		return keepPlayerDays;
	}

	public int getRunPurgeHour() {
		return runPurgeHour;
	}

	public ConfigSection findSection(String sectionName) {
		for (ConfigSection mySection : sections) {
			if (mySection.getName().equals(sectionName)) {
				return mySection;
			}
		}
		return null;
	}

	public List<String> getSectionNames() {
		List<String> sectionNames = new ArrayList<>();
		for (ConfigSection mySection : sections) {
			sectionNames.add(mySection.getName());
		}
		return sectionNames;
	}

	public String dumpSection(String sectionName) {
		ConfigSection mySection = findSection(sectionName);
		if (mySection == null) {
			return null;
		}
		return mySection.toString();
	}

	public ConfigBalloon findBalloon(String balloonName) {
		for (ConfigSection mySection : sections) {
			ConfigBalloon myBalloon = mySection.findBalloon(balloonName);
			if (myBalloon != null) {
				return myBalloon;
			}
		}
		return null;
	}

	public List<String> getBalloonNames(BalloonDefinition balloonDefinition) {
		List<String> balloonNames = new ArrayList<>();
		for (ConfigSection mySection : sections) {
			balloonNames.addAll(mySection.getBalloonNames(balloonDefinition));
		}
		return balloonNames;
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

	@Override
	public ConfigSection buildConfigSectionFromFileSection(String sectionName, ConfigurationSection fileSection)
			throws BalloonException {
		return new ConfigSection(sectionName, this, fileSection);
	}

	@Override
	public ConfigSection buildConfigSectionFromConfigFile(Plugin fileOwnerPlugin) throws BalloonException {
		ConfigurationSection fileSection = fileOwnerPlugin.getConfig().getRoot();
		return buildConfigSectionFromFileSection(fileOwnerPlugin.getName(), fileSection);
	}

	public String reload(List<ConfigSection> newSections) {
		sections.clear();
		sections = newSections;
		int sectionCount = sections.size();
		String s;
		if (sectionCount == 1) {
			s = String.format("Loaded %d section with:", sectionCount);
		} else {
			s = String.format("Loaded %d sections with:", sectionCount);
		}
		for (BalloonDefinition myDefinition : balloonManager.listBalloonDefinitions()) {
			int balloonCount = getBalloonNames(myDefinition).size();
			if (balloonCount > 0) {
				s = String.format("%s %d %s,", s, balloonCount, myDefinition.getAttributeName());
			}
		}
		s = (s.substring(0, s.length() - 1));
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

	@Override
	public List<HeliumParam> getAdditionalRuleParams() {
		return behaviorManager.getAdditionalRuleParams();
	}

	@Override
	public List<HeliumParam> getValidElementConfigParams() {
		return elementManager.getValidConfigParams();
	}

	@Override
	public List<ElementDefinition> listElementDefinitions() {
		return elementManager.listElementDefinitions();
	}

	@Override
	public boolean hasBehavior(String name) {
		return (behaviorManager.findBehaviorDefinition(name) != null);
	}

	@Override
	public BehaviorDefinition getBehaviorDefinition(String name) throws BalloonException {
		BehaviorDefinition myBehaviorDefinition = behaviorManager.findBehaviorDefinition(name);
		if (myBehaviorDefinition == null) {
			throw new BalloonException(null, "Behavior not found", name);
		}
		return myBehaviorDefinition;
	}

	@Override
	public List<BehaviorDefinition> listBehaviorDefinitions() {
		return behaviorManager.listBehaviorDefinitions();
	}

	@Override
	public List<HeliumParam> getValidBalloonConfigParams() {
		return balloonManager.getValidConfigParams();
	}

	@Override
	public List<BalloonDefinition> listBalloonDefinitions() {
		return balloonManager.listBalloonDefinitions();
	}

}