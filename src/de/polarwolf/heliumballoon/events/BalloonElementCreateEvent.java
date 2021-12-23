package de.polarwolf.heliumballoon.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.polarwolf.heliumballoon.elements.Element;

public class BalloonElementCreateEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	private final Element element;

	BalloonElementCreateEvent(Element element) {
		this.element = element;
	}

	public Element getElement() {
		return element;
	}

	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

}
