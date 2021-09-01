package de.polarwolf.heliumballoon.config;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.system.Rule;

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
		setDefaultRule(fileSection.getBoolean(ParamRule.IS_DEFAULT.getAttributeName(), isDefaultRule()));
		setHighAbovePlayer(fileSection.getDouble(ParamRule.HIGH_ABOVE_PLAYER.getAttributeName(), getHighAbovePlayer()));
		setDistanceFromPlayer(fileSection.getDouble(ParamRule.DISTANCE_FROM_PLAYER.getAttributeName(), getDistanceFromPlayer()));
		setAngleFromPlayerDirection(fileSection.getDouble(ParamRule.ANGLE_FROM_PLAYER_DIRECTION.getAttributeName(), getAngleFromPlayerDirection()));
		setNormalSpeed(fileSection.getDouble(ParamRule.NORMAL_SPEED.getAttributeName(), getNormalSpeed()));
		setSwitchToFastSpeedAtDistance(fileSection.getDouble(ParamRule.SWITCH_TO_FAST_SPEED_AT_DISTANCE.getAttributeName(), getSwitchToFastSpeedAtDistance()));
		setEnableRisingYWorkaround(fileSection.getBoolean(ParamRule.ENABLE_RISING_Y_WORKAROUND.getAttributeName(), isEnableRisingYWorkaround()));
		
		validateConfig();	
	}

}
