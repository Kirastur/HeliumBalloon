package de.polarwolf.heliumballoon.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;

public class ElementManager {

	protected Map<String, ElementDefinition> elementDefinitions = new HashMap<>();

	public ElementManager(HeliumBalloonOrchestrator orchestrator) {
		// Nothing to do
	}

	public ElementDefinition findElementDefinition(String definitionName) {
		return elementDefinitions.get(definitionName);
	}

	public List<ElementDefinition> listElementDefinitions() {
		return new ArrayList<>(elementDefinitions.values());
	}

	public void addDefinition(ElementDefinition elementDefinition) {
		String name = elementDefinition.getAttributeName();
		elementDefinitions.put(name, elementDefinition);
	}

	public List<HeliumParam> getValidConfigParams() {
		return new ArrayList<>(elementDefinitions.values());
	}

	public void disable() {
		elementDefinitions.clear();
	}

}
