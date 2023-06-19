package de.polarwolf.heliumballoon.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

import de.polarwolf.heliumballoon.compatibility.CompatibilityManager;
import de.polarwolf.heliumballoon.config.ConfigHelper;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;

public class EventManager {

	protected final HeliumLogger logger;
	protected final CompatibilityManager compatibilityManager;

	public EventManager(HeliumBalloonOrchestrator orchestrator) {
		this.logger = orchestrator.getHeliumLogger();
		this.compatibilityManager = orchestrator.getCompatibilityManager();
	}

	protected static List<ConfigSection> getRebuildConfigSections(BalloonRebuildConfigEvent event) {
		return event.sections;
	}

	protected static BalloonException getRebuildConfigCancelReason(BalloonRebuildConfigEvent event) {
		return event.cancelReason;
	}

	// The EventManager is loaded before the ConfigManager
	// so we cannot use a global variable here
	public void sendRefreshAllEvent(ConfigHelper configHelper) {
		BalloonRefreshAllEvent event = new BalloonRefreshAllEvent(configHelper);
		Bukkit.getPluginManager().callEvent(event);
	}

	// The EventManager is loaded before the ConfigManager
	// so we cannot use a global variable here
	public List<ConfigSection> sendRebuildConfigEvent(ConfigHelper configHelper, boolean initial)
			throws BalloonException {
		BalloonRebuildConfigEvent event = new BalloonRebuildConfigEvent(compatibilityManager, configHelper, initial);
		Bukkit.getPluginManager().callEvent(event);
		BalloonException cancelReason = getRebuildConfigCancelReason(event);
		if (cancelReason != null) {
			throw cancelReason;
		}
		if (event.isCancelled()) {
			throw new BalloonException(null, "Reload was cancelled for an unspecified reason", null);
		}
		return getRebuildConfigSections(event);
	}

	public void sendBlockDataCreateEvent(Element element, BlockData blockData) {
		BalloonBlockDataCreateEvent event = new BalloonBlockDataCreateEvent(element, blockData);
		Bukkit.getPluginManager().callEvent(event);
	}

	public void sendElementCreateEvent(Element element) {
		BalloonElementCreateEvent event = new BalloonElementCreateEvent(element);
		Bukkit.getPluginManager().callEvent(event);
	}

}
