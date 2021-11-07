package de.polarwolf.heliumballoon.helium;

import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;

public class HeliumLogger {

	private final int exceptionQuota;
	private boolean debug = true;
	private HeliumText petErrorMessage;
	protected final Plugin plugin;

	public HeliumLogger(HeliumBalloonOrchestrator orchestrator, boolean initialDebug, int exceptionQuota) {
		this.plugin = orchestrator.getPlugin();
		this.debug = initialDebug;
		this.exceptionQuota = exceptionQuota;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public int getExceptionQuota() {
		return exceptionQuota;
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
