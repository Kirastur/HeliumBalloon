package de.polarwolf.heliumballoon.events;

import java.util.List;

import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumLogger;

public class EventManager {

	protected final Plugin plugin;
	protected final HeliumLogger logger;

	public EventManager(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
	}

	protected static List<ConfigSection> getReloadSections(BalloonReloadEvent event) {
		return event.sections;
	}

	protected static BalloonException getReloadCancelReason(BalloonReloadEvent event) {
		return event.cancelReason;
	}

	public List<ConfigSection> sendReloadEvent() throws BalloonException {
		BalloonReloadEvent event = new BalloonReloadEvent(plugin, logger);
		plugin.getServer().getPluginManager().callEvent(event);
		BalloonException cancelReason = getReloadCancelReason(event);
		if (cancelReason != null) {
			throw cancelReason;
		}
		if (event.isCancelled()) {
			throw new BalloonException(null, "Reload was cancelled for an unspecified reason", null);
		}
		return getReloadSections(event);
	}

}
