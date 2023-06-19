package de.polarwolf.heliumballoon.system.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.config.ConfigHelper;
import de.polarwolf.heliumballoon.events.BalloonRefreshAllEvent;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.system.reload.ReloadManager;

public class BalloonRefreshAllListener implements Listener {

	protected final Plugin plugin;
	protected final ConfigHelper configHelper;
	protected final ReloadManager reloadManager;

	public BalloonRefreshAllListener(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.configHelper = orchestrator.getConfigManager();
		this.reloadManager = orchestrator.getReloadManager();
	}

	public void startup() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public void unregisterListener() {
		HandlerList.unregisterAll(this);
	}

	public void handleBalloonRefreshAllEvent(BalloonRefreshAllEvent event) { // NOSONAR
		reloadManager.refreshBalloons();
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBalloonRefreshAllEvent(BalloonRefreshAllEvent event) {
		try {
			if (configHelper.equals(event.getConfigHelper())) {
				handleBalloonRefreshAllEvent(event);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
