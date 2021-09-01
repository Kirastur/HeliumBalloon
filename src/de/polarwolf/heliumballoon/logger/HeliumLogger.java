package de.polarwolf.heliumballoon.logger;

import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.system.IntlText;

public class HeliumLogger {
	
	private final int exceptionQuota;
	private boolean debug = true;
	private IntlText petErrorMessage;
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


	public void printDebug(String debugMessage) {
		if (debug) {
			plugin.getLogger().info("DEBUG " + debugMessage);
		}
	}
	
	
	public int getExceptionQuota() {
		return exceptionQuota;
	}


	public void printInfo(String infoMessage) {
		plugin.getLogger().info(infoMessage);
	}
	
	
	public void printWarning(String warningMessage) {
		plugin.getLogger().warning(warningMessage);
	}


	public IntlText getPetErrorMessage() {
		return petErrorMessage;
	}


	public void setPetErrorMessage(IntlText petErrorMessage) {
		this.petErrorMessage = petErrorMessage;
	}
	
}
