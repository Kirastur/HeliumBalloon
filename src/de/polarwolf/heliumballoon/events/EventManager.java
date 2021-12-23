package de.polarwolf.heliumballoon.events;

import java.util.List;

import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.config.ConfigRule;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;

public class EventManager {

	protected final Plugin plugin;
	protected final HeliumLogger logger;

	public EventManager(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
	}

	protected static List<ConfigSection> getRebuildConfigSections(BalloonRebuildConfigEvent event) {
		return event.sections;
	}

	protected static BalloonException getRebuildConfigCancelReason(BalloonRebuildConfigEvent event) {
		return event.cancelReason;
	}

	protected static Oscillator getOscillator(BalloonOscillatorCreateEvent event) {
		return event.oscillator;
	}

	// The EventManager is loaded before the ConfigManager
	// so we cannot use a global variable here
	public List<ConfigSection> sendRebuildConfigEvent(ConfigManager configManager) throws BalloonException {
		BalloonRebuildConfigEvent event = new BalloonRebuildConfigEvent(plugin, logger, configManager);
		plugin.getServer().getPluginManager().callEvent(event);
		BalloonException cancelReason = getRebuildConfigCancelReason(event);
		if (cancelReason != null) {
			throw cancelReason;
		}
		if (event.isCancelled()) {
			throw new BalloonException(null, "Reload was cancelled for an unspecified reason", null);
		}
		return getRebuildConfigSections(event);
	}

	public void sendRefreshAllEvent() {
		BalloonRefreshAllEvent event = new BalloonRefreshAllEvent();
		plugin.getServer().getPluginManager().callEvent(event);
	}

	public void sendBlockDataCreateEvent(Element element, BlockData blockData) {
		BalloonBlockDataCreateEvent event = new BalloonBlockDataCreateEvent(element, blockData);
		plugin.getServer().getPluginManager().callEvent(event);
	}

	public void sendElementCreateEvent(Element element) {
		BalloonElementCreateEvent event = new BalloonElementCreateEvent(element);
		plugin.getServer().getPluginManager().callEvent(event);
	}

	public Oscillator sendOscillatorCreateEvent(String oscillatorHint, ConfigRule configRule) {
		BalloonOscillatorCreateEvent event = new BalloonOscillatorCreateEvent(oscillatorHint, configRule);
		plugin.getServer().getPluginManager().callEvent(event);
		return getOscillator(event);
	}
}
