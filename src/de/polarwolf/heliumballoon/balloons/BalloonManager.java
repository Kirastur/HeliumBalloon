package de.polarwolf.heliumballoon.balloons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;

public class BalloonManager {

	protected final HeliumLogger heliumLogger;
	protected Map<String, BalloonDefinition> balloonDefinitions = new HashMap<>();

	public BalloonManager(HeliumBalloonOrchestrator orchestrator) {
		this.heliumLogger = orchestrator.getHeliumLogger();
	}

	public BalloonDefinition findBalloonDefinition(String definitionName) {
		return balloonDefinitions.get(definitionName);
	}

	public List<BalloonDefinition> listBalloonDefinitions() {
		return new ArrayList<>(balloonDefinitions.values());
	}

	public void addDefinition(BalloonDefinition balloonDefinition) {
		String name = balloonDefinition.getAttributeName();
		balloonDefinitions.put(name, balloonDefinition);
	}

	public List<HeliumParam> getValidConfigParams() {
		return new ArrayList<>(balloonDefinitions.values());
	}

	public void disable() {
		balloonDefinitions.clear();
	}

}
