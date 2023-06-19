package de.polarwolf.heliumballoon.orchestrator;

import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonAPI;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.balloons.pets.PetManager;
import de.polarwolf.heliumballoon.balloons.rotators.RotatorManager;
import de.polarwolf.heliumballoon.balloons.walls.WallManager;
import de.polarwolf.heliumballoon.behavior.BehaviorManager;
import de.polarwolf.heliumballoon.behavior.FixedBehavior;
import de.polarwolf.heliumballoon.behavior.FollowingBehavior;
import de.polarwolf.heliumballoon.behavior.observers.ObserverManager;
import de.polarwolf.heliumballoon.compatibility.CompatibilityManager;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.elements.ElementManager;
import de.polarwolf.heliumballoon.elements.armorstand.ArmorstandDefinition;
import de.polarwolf.heliumballoon.elements.blocks.BlocksDefinition;
import de.polarwolf.heliumballoon.elements.living.LivingDefinition;
import de.polarwolf.heliumballoon.elements.minecart.MinecartDefinition;
import de.polarwolf.heliumballoon.events.EventManager;
import de.polarwolf.heliumballoon.gui.GuiManager;
import de.polarwolf.heliumballoon.system.listener.ListenManager;
import de.polarwolf.heliumballoon.system.players.PlayerManager;
import de.polarwolf.heliumballoon.system.reload.ReloadManager;
import de.polarwolf.heliumballoon.tools.chunktickets.ChunkTicketManager;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;
import de.polarwolf.heliumballoon.tools.messages.MessageManager;

public class HeliumBalloonOrchestrator {

	public static final String PLUGIN_NAME = "HeliumBalloon";

	private final Plugin plugin;
	private final HeliumBalloonStartOptions startOptions;
	private final HeliumLogger heliumLogger;
	private final ChunkTicketManager chunkTicketManager;
	private final CompatibilityManager compatibilityManager;
	private final EventManager eventManager;
	private final ElementManager elementManager;
	private final BehaviorManager behaviorManager;
	private final BalloonManager balloonManager;
	private final ConfigManager configManager;
	private final MessageManager messageManager;
	private final PlayerManager playerManager;
	private final ObserverManager observerManager;
	private final PetManager petManager;
	private final WallManager wallManager;
	private final RotatorManager rotatorManager;
	private final GuiManager guiManager;
	private final ReloadManager reloadManager;
	private final ListenManager listenManager;

	protected final BlocksDefinition blocksDefinition;
	protected final LivingDefinition livingDefinition;
	protected final MinecartDefinition minecartDefinition;
	protected final ArmorstandDefinition armorstandDefinition;
	protected final FollowingBehavior followingBehavior;
	protected final FixedBehavior fixedBehavior;

	public HeliumBalloonOrchestrator(Plugin plugin, HeliumBalloonStartOptions startOptions) {
		this.plugin = plugin;
		this.startOptions = startOptions;
		heliumLogger = createHeliumLogger();
		chunkTicketManager = createChunkTicketManager();
		compatibilityManager = createCompatibilityManager();
		eventManager = createEventManager();
		elementManager = createElementManager();
		behaviorManager = createBehaviorManager();
		balloonManager = createBalloonManager();

		blocksDefinition = createBlocksDefinition();
		livingDefinition = createLivingDefinition();
		minecartDefinition = createMinecartDefinition();
		armorstandDefinition = createArmorstandDefinition();
		followingBehavior = createFollowingBehavior();
		fixedBehavior = createFixedBehavior();

		configManager = createConfigManager();
		messageManager = createMessageManager();
		playerManager = createPlayerManager();
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

	public HeliumBalloonStartOptions getStartOptions() {
		return startOptions;
	}

	public HeliumLogger getHeliumLogger() {
		return heliumLogger;
	}

	public ChunkTicketManager getChunkTicketManager() {
		return chunkTicketManager;
	}

	public CompatibilityManager getCompatibilityManager() {
		return compatibilityManager;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public ElementManager getElementManager() {
		return elementManager;
	}

	public BehaviorManager getBehaviorManager() {
		return behaviorManager;
	}

	public BalloonManager getBalloonManager() {
		return balloonManager;
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

	// Creator
	protected HeliumLogger createHeliumLogger() {
		return new HeliumLogger(this);
	}

	protected ChunkTicketManager createChunkTicketManager() {
		return new ChunkTicketManager(this);
	}

	protected CompatibilityManager createCompatibilityManager() {
		return new CompatibilityManager(this);
	}

	protected EventManager createEventManager() {
		return new EventManager(this);
	}

	protected ElementManager createElementManager() {
		return new ElementManager(this);
	}

	protected BehaviorManager createBehaviorManager() {
		return new BehaviorManager(this);
	}

	protected BalloonManager createBalloonManager() {
		return new BalloonManager(this);
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
		return new PetManager(this, FollowingBehavior.BEHAVIOR_NAME);
	}

	protected WallManager createWallManager() {
		return new WallManager(this, FixedBehavior.BEHAVIOR_NAME);
	}

	protected RotatorManager createRotatorManager() {
		return new RotatorManager(this, FixedBehavior.BEHAVIOR_NAME);
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

	protected BlocksDefinition createBlocksDefinition() {
		return new BlocksDefinition();
	}

	protected LivingDefinition createLivingDefinition() {
		return new LivingDefinition();
	}

	protected MinecartDefinition createMinecartDefinition() {
		return new MinecartDefinition();
	}

	protected ArmorstandDefinition createArmorstandDefinition() {
		return new ArmorstandDefinition();
	}

	protected FollowingBehavior createFollowingBehavior() {
		return new FollowingBehavior(behaviorManager);
	}

	protected FixedBehavior createFixedBehavior() {
		return new FixedBehavior(behaviorManager);
	}

	public void startup() {
		elementManager.addDefinition(blocksDefinition);
		elementManager.addDefinition(livingDefinition);
		elementManager.addDefinition(minecartDefinition);
		elementManager.addDefinition(armorstandDefinition);
		behaviorManager.addDefinition(followingBehavior);
		behaviorManager.addDefinition(fixedBehavior);
		balloonManager.addDefinition(petManager);
		balloonManager.addDefinition(wallManager);
		balloonManager.addDefinition(rotatorManager);

		compatibilityManager.addCompatibility(blocksDefinition, followingBehavior, petManager);
		compatibilityManager.addCompatibility(livingDefinition, followingBehavior, petManager);
		compatibilityManager.addCompatibility(minecartDefinition, followingBehavior, petManager);
		compatibilityManager.addCompatibility(blocksDefinition, fixedBehavior, wallManager);
		compatibilityManager.addCompatibility(minecartDefinition, fixedBehavior, rotatorManager);
		compatibilityManager.addCompatibility(armorstandDefinition, fixedBehavior, rotatorManager);

		playerManager.startup();
		observerManager.startup();
		petManager.startup();
		reloadManager.startup();
		listenManager.startup();

		if (startOptions.loadLocalConfig()) {
			reloadManager.addReloadRegistration(plugin, null, null);
		}
	}

	public boolean isDisabled() {
		return (observerManager.isDisabled() || petManager.isDisabled());
	}

	public void disable() {
		listenManager.disable();
		reloadManager.disable();
		rotatorManager.disable();
		wallManager.disable();
		petManager.disable();
		observerManager.disable();
		playerManager.disable();

		balloonManager.disable();
		behaviorManager.disable();
		elementManager.disable();
		compatibilityManager.disable();
	}

}
