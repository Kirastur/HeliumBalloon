package de.polarwolf.heliumballoon.compatibility;

import java.util.ArrayList;
import java.util.List;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.elements.ElementDefinition;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;

public class CompatibilityManager {

	protected List<CompatibilityDefinition> compatibilityDefinitions = new ArrayList<>();

	public CompatibilityManager(HeliumBalloonOrchestrator orchestrator) {
		// Do Nothing
	}

	public void addCompatibility(ElementDefinition elementDefinition, BehaviorDefinition behaviorDefinition,
			BalloonDefinition balloonDefinition) {
		CompatibilityDefinition newCompatibility = new CompatibilityDefinition(elementDefinition, behaviorDefinition,
				balloonDefinition);
		if (!compatibilityDefinitions.contains(newCompatibility)) {
			compatibilityDefinitions.add(newCompatibility);
		}
	}

	public boolean isCompatible(ElementDefinition elementDefinition, BehaviorDefinition behaviorDefinition,
			BalloonDefinition balloonDefinition) {
		CompatibilityDefinition testCompatibility = new CompatibilityDefinition(elementDefinition, behaviorDefinition,
				balloonDefinition);
		return compatibilityDefinitions.contains(testCompatibility);
	}

	public void disable() {
		compatibilityDefinitions.clear();
	}

}
