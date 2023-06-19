package de.polarwolf.heliumballoon.main;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonAPI;
import de.polarwolf.heliumballoon.api.HeliumBalloonProvider;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonStartOptions;
import de.polarwolf.heliumballoon.system.commands.BalloonCommand;

public final class Main extends JavaPlugin {

	public static final int PLUGINID_HELIUMBALLOON = 12597;
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
		new Metrics(this, PLUGINID_HELIUMBALLOON);

		// Check for passive mode
		if (ConfigManager.isPassiveMode(this)) {
			getLogger().info("HeliumBalloon is in passive mode. You must register your own orchestrator.");
			return;
		}

		// Startup Orchestrator and register as API
		HeliumBalloonStartOptions startOptions = ConfigManager.getStartOptions(this);
		orchestrator = new HeliumBalloonOrchestrator(this, startOptions);
		orchestrator.startup();
		HeliumBalloonAPI api = new HeliumBalloonAPI(orchestrator);
		if (!HeliumBalloonProvider.setAPI(api)) {
			orchestrator.disable();
			getLogger().info("Another plugin has already set the API, so I do not need to create my own orchestrator.");
		}

		// print final message
		getLogger().info("The helium tank is well filled to inflate balloons");
	}

	@Override
	public void onDisable() {
		if (orchestrator != null) {
			orchestrator.disable();
			orchestrator = null;
			HeliumBalloonProvider.setAPI(null);
			getLogger().info("All pets and balloons are removed");
		}
	}

}
