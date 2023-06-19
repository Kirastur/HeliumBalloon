package de.polarwolf.heliumballoon.system.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.config.ConfigHelper;
import de.polarwolf.heliumballoon.events.BalloonRebuildConfigEvent;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.system.reload.ReloadManager;

public class BalloonRebuildConfigListener implements Listener {

	protected final Plugin plugin;
	protected final ConfigHelper configHelper;
	protected final ReloadManager reloadManager;

	public BalloonRebuildConfigListener(HeliumBalloonOrchestrator orchestrator) {
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

	@EventHandler(priority = EventPriority.LOW)
	public void onBalloonRebuildConfigEvent(BalloonRebuildConfigEvent event) {
		try {
			if (configHelper.equals(event.getConfigHelper())) {
				reloadManager.reloadAllRegistrations(event);
			}
		} catch (BalloonException be) {
			event.cancelWithReason(be);
		} catch (Exception e) {
			event.cancelWithReason(new BalloonException(null, BalloonException.JAVA_EXCEPTION, null, e));
		}
	}
}
