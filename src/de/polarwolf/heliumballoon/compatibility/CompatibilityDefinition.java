package de.polarwolf.heliumballoon.compatibility;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.elements.ElementDefinition;

public record CompatibilityDefinition(ElementDefinition elementDefinition, BehaviorDefinition behaviorDefinition,
		BalloonDefinition balloonDefinition) {

}
