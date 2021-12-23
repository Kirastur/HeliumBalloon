package de.polarwolf.heliumballoon.config;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum ParamRule implements HeliumParam {

	IS_DEFAULT(STRING, "isDefault"),
	HIGH_ABOVE_PLAYER(STRING, "highAbovePlayer"),
	DISTANCE_FROM_PLAYER(STRING, "distanceFromPlayer"),
	ANGLE_FROM_PLAYER_DIRECTION(STRING, "angleFromPlayerDirection"),
	NORMAL_SPEED(STRING, "normalSpeed"),
	SWITCH_TO_FAST_SPEED_AT_DISTANCE(STRING, "switchToFastSpeedAtDistance"),
	MAX_ALLOWED_DISTANCE(STRING, "maxAllowedDistance"),
	START_MOVE_AT_DEVIATION(STRING, "startMoveAtDeviation"),
	OSCILLATOR_HINT(STRING, "oscillatorHint"),
	OSCILLATOR_PERIOD(STRING, "oscillatorPeriod"),
	OSCILLATOR_AMPLITUDE(STRING, "oscillatorAmplitude"),
	ROTATOR_PERIOD(STRING, "rotatorPeriod"),
	BLOCK_DELAY(STRING, "blockDelay"),
	LIVING_DELAY(STRING, "livingDelay"),
	MINECART_DELAY(STRING, "minecartDelay"),
	ADJUST_ILLAGER_Y(STRING, "adjustIllagerY"),
	ENABLE_RISING_Y_WORKAROUND(STRING, "enableRisingYWorkaround"),
	ENABLE_WARN_Y64_WALLS(STRING, "enableWarnY64Walls");

	private final HeliumParamType paramType;
	private final String attributeName;

	private ParamRule(HeliumParamType paramType, String attributeName) {
		this.paramType = paramType;
		this.attributeName = attributeName;
	}

	@Override
	public boolean isType(HeliumParamType testParamType) {
		return testParamType == paramType;
	}

	@Override
	public String getAttributeName() {
		return attributeName;
	}

}
