package de.polarwolf.heliumballoon.config;

import java.util.Arrays;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumSection;
import de.polarwolf.heliumballoon.rules.Rule;

public class ConfigRule extends Rule {
	
	private final String name;
	private boolean defaultRule = false;
	

	public ConfigRule(String name) {
		this.name = name;
	}


	public ConfigRule(ConfigurationSection fileSection) throws BalloonException {
		this.name = fileSection.getName();
		loadConfig(fileSection);
	}

	
	public String getName() {
		return name;
	}


	public boolean isDefaultRule() {
		return defaultRule;
	}


	protected void setDefaultRule(boolean defaultRule) {
		this.defaultRule = defaultRule;
	}
	
	
	protected void validateConfig() throws BalloonException {
		if ((getAngleFromPlayerDirection() < -180.0) || (getAngleFromPlayerDirection() >= 360.0)) {
			throw new BalloonException (getName(), "AngleFromPlayerDirection out of range", Double.toString(getAngleFromPlayerDirection()));
		}
	}
	
	
	protected void loadConfig(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, Arrays.asList(ParamRule.values()));
		
		setDefaultRule(heliumSection.getBoolean(ParamRule.IS_DEFAULT, isDefaultRule()));
		setHighAbovePlayer(heliumSection.getDouble(ParamRule.HIGH_ABOVE_PLAYER, getHighAbovePlayer()));
		setDistanceFromPlayer(heliumSection.getDouble(ParamRule.DISTANCE_FROM_PLAYER, getDistanceFromPlayer()));
		setAngleFromPlayerDirection(heliumSection.getDouble(ParamRule.ANGLE_FROM_PLAYER_DIRECTION, getAngleFromPlayerDirection()));
		setNormalSpeed(heliumSection.getDouble(ParamRule.NORMAL_SPEED, getNormalSpeed()));
		setSwitchToFastSpeedAtDistance(heliumSection.getDouble(ParamRule.SWITCH_TO_FAST_SPEED_AT_DISTANCE, getSwitchToFastSpeedAtDistance()));
		setMaxAllowedDistance(heliumSection.getDouble(ParamRule.MAX_ALLOWED_DISTANCE, getMaxAllowedDistance()));
		setOscillatorPeriod(heliumSection.getInt(ParamRule.OSCILLATOR_PERIOD, getOscillatorPeriod()));
		setOscillatorAmplitude(heliumSection.getDouble(ParamRule.OSCILLATOR_AMPLITUDE, getOscillatorAmplitude()));
		setAdjustIllagerY(heliumSection.getDouble(ParamRule.ADJUST_ILLAGER_Y, getAdjustIllagerY()));
		setEnableRisingYWorkaround(heliumSection.getBoolean(ParamRule.ENABLE_RISING_Y_WORKAROUND, isEnableRisingYWorkaround()));
		setEnableWarnY64Walls(heliumSection.getBoolean(ParamRule.ENABLE_WARN_Y64_WALLS, isEnableWarnY64Walls()));
		
		validateConfig();	
	}

}
