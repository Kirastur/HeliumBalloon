package de.polarwolf.heliumballoon.system.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.events.BalloonOscillatorCreateEvent;
import de.polarwolf.heliumballoon.oscillators.DefaultOscillator;
import de.polarwolf.heliumballoon.system.reload.ReloadManager;

public class BalloonOscillatorCreateListener implements Listener {

	protected final Plugin plugin;
	protected final ReloadManager reloadManager;

	public BalloonOscillatorCreateListener(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.reloadManager = orchestrator.getReloadManager();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void unregisterListener() {
		HandlerList.unregisterAll(this);
	}

	public void handleBalloonOscillatorCreateEvent(BalloonOscillatorCreateEvent event) {
		event.setOscillator(new DefaultOscillator(event.getConfigRule()));
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBalloonOscillatorReloadEvent(BalloonOscillatorCreateEvent event) {
		try {
			handleBalloonOscillatorCreateEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}