package de.polarwolf.heliumballoon.system.listener;

import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;

public class ListenManager {

	protected final EntityListener entityListener;
	protected final PlayerListener playerListener;
	protected final VehicleListener vehicleListener;
	protected final WorldListener worldListener;
	protected final GuiListener guiListener;
	protected final BalloonRebuildConfigListener balloonRebuildConfigListener;
	protected final BalloonRefreshAllListener balloonRefreshAllListener;

	public ListenManager(HeliumBalloonOrchestrator orchestrator) {
		entityListener = new EntityListener(orchestrator);
		playerListener = new PlayerListener(orchestrator);
		vehicleListener = new VehicleListener(orchestrator);
		worldListener = new WorldListener(orchestrator);
		guiListener = new GuiListener(orchestrator);
		balloonRebuildConfigListener = new BalloonRebuildConfigListener(orchestrator);
		balloonRefreshAllListener = new BalloonRefreshAllListener(orchestrator);
	}

	public void startup() {
		entityListener.startup();
		playerListener.startup();
		vehicleListener.startup();
		worldListener.startup();
		guiListener.startup();
		balloonRebuildConfigListener.startup();
		balloonRefreshAllListener.startup();
	}

	public void disable() {
		entityListener.unregisterListener();
		playerListener.unregisterListener();
		vehicleListener.unregisterListener();
		worldListener.unregisterListener();
		guiListener.unregisterListener();
		balloonRebuildConfigListener.unregisterListener();
		balloonRefreshAllListener.unregisterListener();
	}

}
