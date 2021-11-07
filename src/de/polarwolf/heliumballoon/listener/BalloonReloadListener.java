package de.polarwolf.heliumballoon.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.events.BalloonReloadEvent;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumLogger;

public class BalloonReloadListener implements Listener {

	protected final Plugin plugin;
	protected final HeliumLogger logger;
	protected final ConfigManager configManager;

	public BalloonReloadListener(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.configManager = orchestrator.getConfigManager();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void unregisterListener() {
		HandlerList.unregisterAll(this);
	}

	public void handleBalloonReloadEvent(BalloonReloadEvent event) throws BalloonException {
		// Plugin Configfile reload is done in ReloadManager
		if (event.isCancelled() || !ConfigManager.isLoadLocalConfig(plugin)) {
			return;
		}
		logger.printInfo("Loading configuration from file");
		ConfigSection newSection = configManager.buildConfigSectionFromConfigFile(plugin);
		event.addSection(newSection);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBalloonReloadEvent(BalloonReloadEvent event) {
		try {
			handleBalloonReloadEvent(event);
		} catch (BalloonException be) {
			be.printStackTrace();
			event.cancelWithReason(be);
		} catch (Exception e) {
			e.printStackTrace();
			event.cancelWithReason(new BalloonException(null, BalloonException.JAVA_EXCEPTION, null, e));
		}
	}
}
