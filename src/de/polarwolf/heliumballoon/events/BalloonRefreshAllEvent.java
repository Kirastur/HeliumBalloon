package de.polarwolf.heliumballoon.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.polarwolf.heliumballoon.config.ConfigHelper;

public class BalloonRefreshAllEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	protected final ConfigHelper configHelper;

	BalloonRefreshAllEvent(ConfigHelper configHelper) {
		this.configHelper = configHelper;
	}

	public ConfigHelper getConfigHelper() {
		return configHelper;
	}

	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

}
