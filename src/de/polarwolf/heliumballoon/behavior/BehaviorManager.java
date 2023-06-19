package de.polarwolf.heliumballoon.behavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.block.data.BlockData;

import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.events.EventManager;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;

public class BehaviorManager implements BehaviorHelper {

	protected final EventManager eventManager;

	protected Map<String, BehaviorDefinition> behaviorDefinitions = new HashMap<>();

	public BehaviorManager(HeliumBalloonOrchestrator orchestrator) {
		this.eventManager = orchestrator.getEventManager();
	}

	public BehaviorDefinition findBehaviorDefinition(String definitionName) {
		return behaviorDefinitions.get(definitionName);
	}

	public List<BehaviorDefinition> listBehaviorDefinitions() {
		return new ArrayList<>(behaviorDefinitions.values());
	}

	public void addDefinition(BehaviorDefinition behaviorDefinition) {
		String name = behaviorDefinition.getName();
		behaviorDefinitions.put(name, behaviorDefinition);
	}

	@Override
	public void modifyBlockDataDefault(Element element, BlockData blockData) {
		eventManager.sendBlockDataCreateEvent(element, blockData);
	}

	@Override
	public void modifyElementDefault(Element element) {
		eventManager.sendElementCreateEvent(element);
	}

	public List<HeliumParam> getAdditionalRuleParams() {
		List<HeliumParam> allParams = new ArrayList<>();
		for (BehaviorDefinition myBehaviorDefinition : behaviorDefinitions.values()) {
			List<HeliumParam> myParams = myBehaviorDefinition.getAdditionalRuleParams();
			if (myParams != null) {
				allParams.addAll(myParams);
			}
		}
		return allParams;
	}

	public void disable() {
		behaviorDefinitions.clear();
	}

}
