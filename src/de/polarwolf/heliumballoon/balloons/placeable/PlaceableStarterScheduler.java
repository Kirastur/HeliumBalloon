package de.polarwolf.heliumballoon.balloons.placeable;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.polarwolf.heliumballoon.behavior.observers.Observer;
import de.polarwolf.heliumballoon.behavior.observers.ObserverManager;

public class PlaceableStarterScheduler extends BukkitRunnable {

	protected final Plugin plugin;
	protected final ObserverManager observerManager;
	protected final PlaceableManager placeableManager;
	protected final Observer observer;

	public PlaceableStarterScheduler(Plugin plugin, ObserverManager observerManager, PlaceableManager placeableManager,
			Observer newObserver, int ticks) {
		this.plugin = plugin;
		this.observerManager = observerManager;
		this.placeableManager = placeableManager;
		this.observer = newObserver;
		this.runTaskLater(plugin, ticks);
		registerStarter();
	}

	public Observer getObserver() {
		return observer;
	}

	protected void registerStarter() {
		placeableManager.registerStarter(this);
	}

	protected void unregisterStarter() {
		placeableManager.unregisterStarter(this);
	}

	@Override
	public void run() {
		try {
			placeableManager.unregisterStarter(this);
			observerManager.addObserver(observer);
		} catch (Exception e) {
			e.printStackTrace();
			observer.cancel();
		}
	}

	@Override
	public synchronized void cancel() {
		placeableManager.unregisterStarter(this);
		super.cancel();
		observer.cancel();
	}

}
