package de.polarwolf.heliumballoon.config;

import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumName;
import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumSection;

public class ConfigRule implements HeliumName {
	
	private final String name;
	private final String fullName;

	private double highAbovePlayer = 3.0;
	private double distanceFromPlayer = 1.0;
	private double angleFromPlayerDirection = 120;

	private double normalSpeed = 0.1;
	private double switchToFastSpeedAtDistance = 5.0;
	private double maxAllowedDistance = 9.9;
	
	private int oscillatorPeriod = 100;
	private double oscillatorAmplitude = 0.1;
	private int blockDelay = 4;
	
	private double adjustIllagerY = 0.0;
	private boolean enableRisingYWorkaround = true;
	private boolean enableWarnY64Walls = true;

	private boolean defaultRule = false;
	

	public ConfigRule(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}


	public ConfigRule(ConfigurationSection fileSection) throws BalloonException {
		this.name = fileSection.getName();
		this.fullName = fileSection.getCurrentPath();
		loadConfigFromFile(fileSection);
	}


	@Override
	public String getName() {
		return name;
	}


	@Override
	public String getFullName() {
		return fullName;
	}


	public double getHighAbovePlayer() {
		return highAbovePlayer;
	}

	
	protected void setHighAbovePlayer(double highAbovePlayer) {
		this.highAbovePlayer = highAbovePlayer;
	}

	
	public double getDistanceFromPlayer() {
		return distanceFromPlayer;
	}

	
	protected void setDistanceFromPlayer(double distanceFromPlayer) {
		this.distanceFromPlayer = distanceFromPlayer;
	}

	
	public double getAngleFromPlayerDirection() {
		return angleFromPlayerDirection;
	}

	
	protected void setAngleFromPlayerDirection(double angleFromPlayerDirection) {
		this.angleFromPlayerDirection = angleFromPlayerDirection;
	}

	
	public double getNormalSpeed() {
		return normalSpeed;
	}

	
	protected void setNormalSpeed(double normalSpeed) {
		this.normalSpeed = normalSpeed;
	}

	
	public double getSwitchToFastSpeedAtDistance() {
		return switchToFastSpeedAtDistance;
	}

	
	protected void setSwitchToFastSpeedAtDistance(double switchToFastSpeedAtDistance) {
		this.switchToFastSpeedAtDistance = switchToFastSpeedAtDistance;
	}


	public double getMaxAllowedDistance() {
		return maxAllowedDistance;
	}


	protected void setMaxAllowedDistance(double maxAllowedDistance) {
		this.maxAllowedDistance = maxAllowedDistance;
	}


	public int getOscillatorPeriod() {
		return oscillatorPeriod;
	}


	protected void setOscillatorPeriod(int oscillatorPeriod) {
		this.oscillatorPeriod = oscillatorPeriod;
	}


	public double getOscillatorAmplitude() {
		return oscillatorAmplitude;
	}


	protected void setOscillatorAmplitude(double oscillatorAmplitude) {
		this.oscillatorAmplitude = oscillatorAmplitude;
	}


	public int getBlockDelay() {
		return blockDelay;
	}


	protected void setBlockDelay(int blockDelay) {
		this.blockDelay = blockDelay;
	}


	public double getAdjustIllagerY() {
		return adjustIllagerY;
	}


	protected void setAdjustIllagerY(double adjustIllagerY) {
		this.adjustIllagerY = adjustIllagerY;
	}


	public boolean isEnableRisingYWorkaround() {
		return enableRisingYWorkaround;
	}


	protected void setEnableRisingYWorkaround(boolean enableRisingYWorkaround) {
		this.enableRisingYWorkaround = enableRisingYWorkaround;
	}


	public boolean isEnableWarnY64Walls() {
		return enableWarnY64Walls;
	}


	protected void setEnableWarnY64Walls(boolean enableWarnY64Walls) {
		this.enableWarnY64Walls = enableWarnY64Walls;
	}

	public boolean isDefaultRule() {
		return defaultRule;
	}


	protected void setDefaultRule(boolean defaultRule) {
		this.defaultRule = defaultRule;
	}
	
	
	protected List<HeliumParam> getValidParams() {
		return  Arrays.asList(ParamRule.values());
	}
	
	
	protected void importHeliumSection(HeliumSection heliumSection) throws BalloonException {
		setDefaultRule(heliumSection.getBoolean(ParamRule.IS_DEFAULT, isDefaultRule()));
		setHighAbovePlayer(heliumSection.getDouble(ParamRule.HIGH_ABOVE_PLAYER, getHighAbovePlayer()));
		setDistanceFromPlayer(heliumSection.getDouble(ParamRule.DISTANCE_FROM_PLAYER, getDistanceFromPlayer()));
		setAngleFromPlayerDirection(heliumSection.getDouble(ParamRule.ANGLE_FROM_PLAYER_DIRECTION, getAngleFromPlayerDirection()));
		setNormalSpeed(heliumSection.getDouble(ParamRule.NORMAL_SPEED, getNormalSpeed()));
		setSwitchToFastSpeedAtDistance(heliumSection.getDouble(ParamRule.SWITCH_TO_FAST_SPEED_AT_DISTANCE, getSwitchToFastSpeedAtDistance()));
		setMaxAllowedDistance(heliumSection.getDouble(ParamRule.MAX_ALLOWED_DISTANCE, getMaxAllowedDistance()));
		setOscillatorPeriod(heliumSection.getInt(ParamRule.OSCILLATOR_PERIOD, getOscillatorPeriod()));
		setOscillatorAmplitude(heliumSection.getDouble(ParamRule.OSCILLATOR_AMPLITUDE, getOscillatorAmplitude()));
		setBlockDelay(heliumSection.getInt(ParamRule.BLOCK_DELAY, getBlockDelay()));
		setAdjustIllagerY(heliumSection.getDouble(ParamRule.ADJUST_ILLAGER_Y, getAdjustIllagerY()));
		setEnableRisingYWorkaround(heliumSection.getBoolean(ParamRule.ENABLE_RISING_Y_WORKAROUND, isEnableRisingYWorkaround()));
		setEnableWarnY64Walls(heliumSection.getBoolean(ParamRule.ENABLE_WARN_Y64_WALLS, isEnableWarnY64Walls()));		
	}
	
	
	protected void validateConfig() throws BalloonException {
		if ((getAngleFromPlayerDirection() < -180.0) || (getAngleFromPlayerDirection() >= 360.0)) {
			throw new BalloonException (getFullName(), "AngleFromPlayerDirection out of range", Double.toString(getAngleFromPlayerDirection()));
		}
	}
	
	
	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);		
		validateConfig();	
	}

}
