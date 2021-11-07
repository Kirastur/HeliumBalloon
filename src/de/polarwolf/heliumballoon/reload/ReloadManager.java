package de.polarwolf.heliumballoon.reload;

import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.pets.PetManager;
import de.polarwolf.heliumballoon.rotators.RotatorManager;
import de.polarwolf.heliumballoon.walls.WallManager;

public class ReloadManager {

	protected final Plugin plugin;
	protected final ConfigManager configManager;
	protected final PetManager petManager;
	protected final WallManager wallManager;
	protected final RotatorManager rotatorManager;

	public ReloadManager(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.configManager = orchestrator.getConfigManager();
		this.petManager = orchestrator.getPetManager();
		this.wallManager = orchestrator.getWallManager();
		this.rotatorManager = orchestrator.getRotatorManager();
	}

	public void reload() throws BalloonException {
		plugin.reloadConfig();
		configManager.reload();
		petManager.reload();
		wallManager.reload();
		rotatorManager.reload();
	}

}
