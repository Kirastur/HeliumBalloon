package de.polarwolf.heliumballoon.main;

import org.bukkit.plugin.java.JavaPlugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.api.HeliumBalloonProvider;
import de.polarwolf.heliumballoon.bstats.Metrics;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.system.commands.BalloonCommand;

public final class Main extends JavaPlugin {

	public static final String BALLOON_COMMAND = "balloon";

	protected HeliumBalloonOrchestrator orchestrator = null;
	protected BalloonCommand balloonCommand = null;

	@Override
	public void onEnable() {

		// Prepare Configuration
		saveDefaultConfig();

		// Register commands
		balloonCommand = new BalloonCommand(this, BALLOON_COMMAND);

		// Enable bStats Metrics
		// Please download the bstats-code direct form their homepage
		// or disable the following instruction
		new Metrics(this, Metrics.PLUGINID_HELIUMBALLOON);

		// Check for passive mode
		if (ConfigManager.isPassiveMode(this)) {
			getLogger().info("HeliumBalloon is in passive mode. You must register your own orchestrator.");
			return;
		}

		// Startup Orchestrator and register as API
		if (HeliumBalloonProvider.getAPI() == null) {
			orchestrator = new HeliumBalloonOrchestrator(this);
			orchestrator.registerAPI();
		} else {
			getLogger().info("Another plugin has already set the API, so I do not need to create my own orchestrator.");
		}

		// Load Configuration
		HeliumBalloonProvider.getAPI().scheduleRedloadFoNextTick();

		// print final message
		getLogger().info("HeliumBalloon is ready");
	}

	@Override
	public void onDisable() {
		if (orchestrator != null) {
			orchestrator.disable(); // includes unregisterAPI()
			orchestrator = null;
			getLogger().info("All pets and balloons are removed");
		}
	}

}
