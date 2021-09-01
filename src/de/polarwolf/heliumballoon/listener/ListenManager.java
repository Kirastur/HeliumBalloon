package de.polarwolf.heliumballoon.listener;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;

public class ListenManager {
	
	protected final EntityListener entityListener;
	protected final PlayerListener playerListener;
	protected final WorldListener worldListener;
	protected final GuiListener guiListener;
	
	
	public ListenManager(HeliumBalloonOrchestrator orchestrator) {
		entityListener = new EntityListener (orchestrator);
		playerListener = new PlayerListener (orchestrator);
		worldListener = new WorldListener (orchestrator);
		guiListener = new GuiListener(orchestrator);
	}
	
	
	public void disable() {
		entityListener.unregisterListener();
		playerListener.unregisterListener();
		worldListener.unregisterListener();
		guiListener.unregisterListener();
	}

}
