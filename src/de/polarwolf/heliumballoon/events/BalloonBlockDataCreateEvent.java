package de.polarwolf.heliumballoon.events;

import org.bukkit.block.data.BlockData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.polarwolf.heliumballoon.elements.Element;

public class BalloonBlockDataCreateEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	private final Element element;
	private final BlockData blockData;

	BalloonBlockDataCreateEvent(Element element, BlockData blockData) {
		this.element = element;
		this.blockData = blockData;
	}

	public Element getElement() {
		return element;
	}

	public BlockData getBlockData() {
		return blockData;
	}

	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}