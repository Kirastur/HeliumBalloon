package de.polarwolf.heliumballoon.tools.helium;

import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;

public class HeliumLogger {

	private boolean debug = true;
	private HeliumText petErrorMessage;
	protected final Plugin plugin;

	public HeliumLogger(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.debug = orchestrator.getStartOptions().initialDebug();
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void printDebug(String debugMessage) {
		if (isDebug()) {
			String s = String.format("DEBUG %s", debugMessage);
			plugin.getLogger().info(s);
		}
	}

	public void printInfo(String infoMessage) {
		plugin.getLogger().info(infoMessage);
	}

	public void printWarning(String warningMessage) {
		plugin.getLogger().warning(warningMessage);
	}

	public HeliumText getPetErrorMessage() {
		return petErrorMessage;
	}

	public void setPetErrorMessage(HeliumText petErrorMessage) {
		this.petErrorMessage = petErrorMessage;
	}

}
