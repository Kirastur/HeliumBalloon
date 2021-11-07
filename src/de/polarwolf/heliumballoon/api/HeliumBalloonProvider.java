package de.polarwolf.heliumballoon.api;

public final class HeliumBalloonProvider {

	private static HeliumBalloonOrchestrator heliumBalloonOrchestrator = null;

	private HeliumBalloonProvider() {
	}

	protected static boolean setOrchestrator(HeliumBalloonOrchestrator newOrchestrator) {
		if (heliumBalloonOrchestrator == null) {
			heliumBalloonOrchestrator = newOrchestrator;
			return true;
		} else {
			return false;
		}
	}

	protected static boolean clearOrchestrator(HeliumBalloonOrchestrator oldOrchestrator) {
		if (heliumBalloonOrchestrator == null) {
			return true;
		}
		if (heliumBalloonOrchestrator == oldOrchestrator) {
			heliumBalloonOrchestrator = null;
			return true;
		} else {
			return false;
		}
	}

	public static HeliumBalloonAPI getAPI() {
		if (heliumBalloonOrchestrator != null) {
			return heliumBalloonOrchestrator.getApi();
		} else {
			return null;
		}
	}

}
