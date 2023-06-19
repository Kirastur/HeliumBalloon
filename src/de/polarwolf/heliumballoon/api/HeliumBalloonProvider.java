package de.polarwolf.heliumballoon.api;

public final class HeliumBalloonProvider {

	private static HeliumBalloonAPI heliumBalloonAPI = null;

	private HeliumBalloonProvider() {
	}

	public static HeliumBalloonAPI getAPI() {
		return heliumBalloonAPI;
	}

	public static boolean setAPI(HeliumBalloonAPI newHeliumBalloonAPI) {
		if ((heliumBalloonAPI == null) || heliumBalloonAPI.isDisabled()) {
			heliumBalloonAPI = newHeliumBalloonAPI;
			return true;
		} else {
			return false;
		}
	}

}
