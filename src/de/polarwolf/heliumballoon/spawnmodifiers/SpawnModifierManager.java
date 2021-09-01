package de.polarwolf.heliumballoon.spawnmodifiers;

import java.util.ArrayList;
import java.util.List;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.elements.Element;

public class SpawnModifierManager implements HeliumModifier {
	
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
	public void modify(Element element) {
		for (SpawnModifier myModifier : modifiers) {
			myModifier.modify(element);
		}
	}

}
