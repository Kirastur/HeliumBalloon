package de.polarwolf.heliumballoon.spawnmodifiers;

import org.bukkit.block.data.BlockData;

import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.events.EventManager;

public class DefaultSpawnModifier implements SpawnModifier {

	protected final EventManager eventManager;

	public DefaultSpawnModifier(EventManager eventManager) {
		this.eventManager = eventManager;
	}

	@Override
	public void modifyEntity(Element element) {
		eventManager.sendElementCreateEvent(element);
	}

	@Override
	public void modifyBlockData(Element element, BlockData blockData) {
		eventManager.sendBlockDataCreateEvent(element, blockData);
	}

}
