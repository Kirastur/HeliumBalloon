package de.polarwolf.heliumballoon.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BalloonRefreshAllEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

}
