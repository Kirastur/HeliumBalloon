package de.polarwolf.heliumballoon.balloons.placings;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import de.polarwolf.heliumballoon.observers.Observer;
import de.polarwolf.heliumballoon.observers.ObserverManager;

public class PlacingStarterScheduler extends BukkitRunnable {

	protected final Plugin plugin;
	protected final ObserverManager observerManager;
	protected final PlacingManager placingManager;
	protected final Observer observer;

	public PlacingStarterScheduler(Plugin plugin, ObserverManager observerManager, PlacingManager placingManager,
			Observer newObserver, int ticks) {
		this.plugin = plugin;
		this.observerManager = observerManager;
		this.placingManager = placingManager;
		this.observer = newObserver;
		this.runTaskLater(plugin, ticks);
		registerStarter();
	}

	public Observer getObserver() {
		return observer;
	}

	protected void registerStarter() {
		placingManager.registerStarter(this);
	}

	protected void unregisterStarter() {
		placingManager.unregisterStarter(this);
	}

	@Override
	public void run() {
		try {
			placingManager.unregisterStarter(this);
			observerManager.addObserver(observer);
		} catch (Exception e) {
			e.printStackTrace();
			observer.cancel();
		}
	}

	@Override
	public synchronized void cancel() {
		placingManager.unregisterStarter(this);
		super.cancel();
		observer.cancel();
	}

}
