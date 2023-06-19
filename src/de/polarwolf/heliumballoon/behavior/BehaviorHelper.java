package de.polarwolf.heliumballoon.behavior;

import org.bukkit.block.data.BlockData;

import de.polarwolf.heliumballoon.elements.Element;

public interface BehaviorHelper {

	public void modifyBlockDataDefault(Element element, BlockData blockData);

	public void modifyElementDefault(Element element);

}
