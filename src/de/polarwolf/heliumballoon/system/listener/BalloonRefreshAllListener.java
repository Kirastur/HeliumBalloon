package de.polarwolf.heliumballoon.system.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.events.BalloonRefreshAllEvent;
import de.polarwolf.heliumballoon.system.reload.ReloadManager;

public class BalloonRefreshAllListener implements Listener {

	protected final Plugin plugin;
	protected final ReloadManager reloadManager;

	public BalloonRefreshAllListener(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.reloadManager = orchestrator.getReloadManager();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void unregisterListener() {
		HandlerList.unregisterAll(this);
	}

	public void handleBalloonRefreshAllEvent(BalloonRefreshAllEvent event) { // NOSONAR
		reloadManager.refreshAllInternalManagers();
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBalloonRefreshAllEvent(BalloonRefreshAllEvent event) {
		try {
			handleBalloonRefreshAllEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
