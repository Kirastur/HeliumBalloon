package de.polarwolf.heliumballoon.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.balloons.pets.PetManager;
import de.polarwolf.heliumballoon.balloons.rotators.RotatorManager;
import de.polarwolf.heliumballoon.balloons.walls.WallManager;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.events.EventManager;
import de.polarwolf.heliumballoon.gui.GuiManager;
import de.polarwolf.heliumballoon.observers.ObserverManager;
import de.polarwolf.heliumballoon.system.reload.ReloadManager;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifierManager;
import de.polarwolf.heliumballoon.system.listener.ListenManager;
import de.polarwolf.heliumballoon.system.players.PlayerManager;
import de.polarwolf.heliumballoon.tools.chunktickets.ChunkTicketManager;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;
import de.polarwolf.heliumballoon.tools.messages.MessageManager;

public class HeliumBalloonOrchestrator {

	public static final String PLUGIN_NAME = "HeliumBalloon";

	private final Plugin plugin;
	private final HeliumLogger heliumLogger;
	private final ChunkTicketManager chunkTicketManager;
	private final EventManager eventManager;
	private final ConfigManager configManager;
	private final MessageManager messageManager;
	private final PlayerManager playerManager;
	private final SpawnModifierManager spawnModifierManager;
	private final ObserverManager observerManager;
	private final PetManager petManager;
	private final WallManager wallManager;
	private final RotatorManager rotatorManager;
	private final GuiManager guiManager;
	private final ReloadManager reloadManager;
	private final ListenManager listenManager;
	private HeliumBalloonAPI api = null;

	public HeliumBalloonOrchestrator(Plugin plugin) {
		if (plugin != null) {
			this.plugin = plugin;
		} else {
			this.plugin = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
		}
		heliumLogger = createHeliumLogger();
		chunkTicketManager = createChunkTicketManager();
		eventManager = createEventManager();
		configManager = createConfigManager();
		messageManager = createMessageManager();
		playerManager = createPlayerManager();
		spawnModifierManager = createSpawnModifierManager();
		observerManager = createObserverManager();
		petManager = createPetManager();
		wallManager = createWallManager();
		rotatorManager = createRotatorManager();
		guiManager = createGuiManager();
		reloadManager = createReloadManager();
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

	public ChunkTicketManager getChunkTicketManager() {
		return chunkTicketManager;
	}

	public EventManager getEventManager() {
		return eventManager;
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

	public ObserverManager getObserverManager() {
		return observerManager;
	}

	public PetManager getPetManager() {
		return petManager;
	}

	public WallManager getWallManager() {
		return wallManager;
	}

	public RotatorManager getRotatorManager() {
		return rotatorManager;
	}

	public GuiManager getGuiManager() {
		return guiManager;
	}

	public ReloadManager getReloadManager() {
		return reloadManager;
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

	protected ChunkTicketManager createChunkTicketManager() {
		return new ChunkTicketManager(this);
	}

	protected EventManager createEventManager() {
		return new EventManager(this);
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

	protected ObserverManager createObserverManager() {
		return new ObserverManager(this);
	}

	protected PetManager createPetManager() {
		return new PetManager(this);
	}

	protected WallManager createWallManager() {
		return new WallManager(this);
	}

	protected RotatorManager createRotatorManager() {
		return new RotatorManager(this);
	}

	protected GuiManager createGuiManager() {
		return new GuiManager(this);
	}

	protected ReloadManager createReloadManager() {
		return new ReloadManager(this);
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
		reloadManager.disable();
		listenManager.disable();
		petManager.disable();
		observerManager.disable();
		playerManager.disable();
	}

	public void disable() {
		unregisterAPI();
		disableManagers();
	}

}
