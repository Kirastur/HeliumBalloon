package de.polarwolf.heliumballoon.system.reload;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ReloadScheduler extends BukkitRunnable {

	protected ReloadManager reloadManager;

	public ReloadScheduler(Plugin plugin, ReloadManager reloadManager) {
		this.reloadManager = reloadManager;
		runTask(plugin);
	}

	@Override
	public void run() {
		try {
			reloadManager.reload();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
