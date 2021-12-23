package de.polarwolf.heliumballoon.spawnmodifiers;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.events.EventManager;

public class SpawnModifierManager {

	protected final EventManager eventManager;

	public SpawnModifierManager(HeliumBalloonOrchestrator orchestrator) {
		this.eventManager = orchestrator.getEventManager();
	}

	public SpawnModifier getSpawnModifier() {
		return new DefaultSpawnModifier(eventManager);
	}

}
