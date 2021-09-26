package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;

public enum ParamRule implements HeliumParam {

	IS_DEFAULT ("isDefault"),
	HIGH_ABOVE_PLAYER ("highAbovePlayer"),
	DISTANCE_FROM_PLAYER ("distanceFromPlayer"),
	ANGLE_FROM_PLAYER_DIRECTION ("angleFromPlayerDirection"),
	NORMAL_SPEED ("normalSpeed"),
	SWITCH_TO_FAST_SPEED_AT_DISTANCE ("switchToFastSpeedAtDistance"),
	MAX_ALLOWED_DISTANCE ("maxAllowedDistance"),
	OSCILLATOR_PERIOD ("oscillatorPeriod"),
	OSCILLATOR_AMPLITUDE ("oscillatorAmplitude"),
	BLOCK_DELAY ("blockDelay"),
	ADJUST_ILLAGER_Y ("adjustIllagerY"),
	ENABLE_RISING_Y_WORKAROUND("enableRisingYWorkaround"),
	ENABLE_WARN_Y64_WALLS("enableWarnY64Walls");
		
	private final String attributeName;
	

	private ParamRule(String attributeName) {
		this.attributeName = attributeName;
	}


	@Override
	public boolean isSection() {
		return false;
	}

	
	@Override
	public String getAttributeName() {
		return attributeName;
	}

}
