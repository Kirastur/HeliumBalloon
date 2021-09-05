package de.polarwolf.heliumballoon.spawnmodifiers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.data.BlockData;
import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.elements.Element;

public class SpawnModifierManager implements SpawnModifier {
	
	protected List<SpawnModifier> modifiers = new ArrayList<>();
	
	
	public SpawnModifierManager(HeliumBalloonOrchestrator orchestrator) {
		// Nothing to do
	}
	

	public void registerModifier(SpawnModifier newSpawnModifier) {
		if (!modifiers.contains(newSpawnModifier)) {
			modifiers.add(newSpawnModifier);
		}
	}
	
	
	public void unregisterModifier(SpawnModifier oldSpawnModifier) {
		modifiers.remove(oldSpawnModifier);
	}
	
	
	@Override
	public void modifyEntity(Element element) {
		for (SpawnModifier myModifier : modifiers) {
			myModifier.modifyEntity(element);
		}
	}

	@Override
	public void modifyBlockData(Element element, BlockData blockData) {
		for (SpawnModifier myModifier : modifiers) {
			myModifier.modifyBlockData(element, blockData);
		}
	}
}
