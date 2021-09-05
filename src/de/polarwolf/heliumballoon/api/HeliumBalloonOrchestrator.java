package de.polarwolf.heliumballoon.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.gui.GuiManager;
import de.polarwolf.heliumballoon.helium.HeliumLogger;
import de.polarwolf.heliumballoon.listener.ListenManager;
import de.polarwolf.heliumballoon.messages.MessageManager;
import de.polarwolf.heliumballoon.pets.PetManager;
import de.polarwolf.heliumballoon.players.PlayerManager;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifierManager;
import de.polarwolf.heliumballoon.walls.WallManager;

public class HeliumBalloonOrchestrator {
	
	public static final String PLUGIN_NAME = "HeliumBalloon";

	private final Plugin plugin;
	private final HeliumLogger heliumLogger;
	private final SpawnModifierManager spawnModifierManager;
	private final ConfigManager configManager;
	private final PlayerManager playerManager;
	private final BalloonManager balloonManager;
	private final MessageManager messageManager;
	private final PetManager petManager;
	private final WallManager wallManager;
	private final GuiManager guiManager;
	private final ListenManager listenManager;
	private HeliumBalloonAPI api = null; 
	
	
	public HeliumBalloonOrchestrator(Plugin plugin) {
		if (plugin != null) {
			this.plugin = plugin;
		} else {
			this.plugin = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
		}
		heliumLogger = createHeliumLogger();
		spawnModifierManager = createSpawnModifierManager();
		configManager = createConfigManager();
		messageManager = createMessageManager();
		playerManager = createPlayerManager();
		balloonManager = createBalloonManager();
		petManager = createPetManager();
		wallManager = createWallManager();
		guiManager = createGuiManager();
		listenManager = createListenManager();
	}

	
	// Getter
	public Plugin getPlugin() {
		return plugin;
	}

	public HeliumLogger getHeliumLogger() {
		return heliumLogger;
	}
	
	public SpawnModifierManager getSpawnModifierManager() {
		return spawnModifierManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}
	
	public MessageManager getMessageManager() {
		return messageManager;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public BalloonManager getBalloonManager() {
		return balloonManager;
	}

	public PetManager getPetManager() {
		return petManager;
	}

	public WallManager getWallManager() {
		return wallManager;
	}
	
	public GuiManager getGuiManager() {
		return guiManager;
	}
	
	public ListenManager getListenManager() {
		return listenManager;
	}
		
	public HeliumBalloonAPI getApi() {
		return api;
	}


	// Creator
	protected HeliumLogger createHeliumLogger() {
		return new HeliumLogger(this, ConfigManager.isInitialDebug(plugin), ConfigManager.getExceptionQuota(plugin));
	}
	
	protected SpawnModifierManager createSpawnModifierManager() {
		return new SpawnModifierManager(this);
	}
	
	protected ConfigManager createConfigManager() {
		return new ConfigManager(this);
	}
	
	protected MessageManager createMessageManager() {
		return new MessageManager(this);
	}
	
	protected PlayerManager createPlayerManager() {
		return new PlayerManager(this);
	}
	
	protected BalloonManager createBalloonManager() {
		return new BalloonManager(this);
	}
	
	protected PetManager createPetManager() {
		return new PetManager(this);
	}
	
	protected WallManager createWallManager() {
		return new WallManager(this);
	}
	
	protected GuiManager createGuiManager() {
		return new GuiManager(this);
	}
	
	protected ListenManager createListenManager() {
		return new ListenManager(this);
	}
		
	protected HeliumBalloonAPI createAPI() {
		return new HeliumBalloonAPI(this);
	}
	
	
	// Enable and Disable
	public final boolean registerAPI() {
		if (api == null) {
			api = createAPI();
		}
		return HeliumBalloonProvider.setOrchestrator(this);
	}
	
	public final boolean unregisterAPI() {
		return HeliumBalloonProvider.clearOrchestrator(this);
	}
	
	protected void disableManagers() {
		listenManager.disable();
		petManager.disable();
		balloonManager.disable();	
		playerManager.disable();
	}
	
	public void disable() {
		unregisterAPI();
		disableManagers();		
	}

}
