package de.polarwolf.heliumballoon.spawnmodifiers;

import org.bukkit.block.data.BlockData;
import de.polarwolf.heliumballoon.elements.Element;

public interface SpawnModifier {
	
	public void modifyEntity(Element element);

	public void modifyBlockData(Element element, BlockData blockData);

}
