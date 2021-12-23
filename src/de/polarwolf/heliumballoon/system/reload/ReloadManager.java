package de.polarwolf.heliumballoon.system.reload;

import java.util.List;

import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.balloons.pets.PetManager;
import de.polarwolf.heliumballoon.balloons.rotators.RotatorManager;
import de.polarwolf.heliumballoon.balloons.walls.WallManager;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.events.EventManager;
import de.polarwolf.heliumballoon.exception.BalloonException;

public class ReloadManager {

	protected final Plugin plugin;
	protected final EventManager eventManager;
	protected final ConfigManager configManager;
	protected final PetManager petManager;
	protected final WallManager wallManager;
	protected final RotatorManager rotatorManager;

	protected ReloadScheduler reloadScheduler = null;

	public ReloadManager(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.eventManager = orchestrator.getEventManager();
		this.configManager = orchestrator.getConfigManager();
		this.petManager = orchestrator.getPetManager();
		this.wallManager = orchestrator.getWallManager();
		this.rotatorManager = orchestrator.getRotatorManager();
	}

	public void disable() {
		if (reloadScheduler != null) {
			reloadScheduler.cancel();
		}
	}

	public void refreshAllInternalManagers() {
		petManager.reload();
		wallManager.reload();
		rotatorManager.reload();
	}

	public String reload() throws BalloonException {
		reloadScheduler = null;
		plugin.reloadConfig();
		List<ConfigSection> newSections = eventManager.sendRebuildConfigEvent(configManager);
		String s = configManager.reload(newSections);
		eventManager.sendRefreshAllEvent();
		return s;
	}

	public void scheduleRedloadFoNextTick() {
		if (reloadScheduler == null) {
			reloadScheduler = new ReloadScheduler(plugin, this);
		}
	}

}
