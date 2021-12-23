package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumName;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumSection;

public class ConfigRule implements HeliumName {

	public static final double DEFAULT_HIGH_ABOVE_PLAYER = 3.0;
	public static final double DEFAULT_DISTANCE_FROM_PLAYER = 1.0;
	public static final double DEFAULT_ANGLE_FROM_PLAYER_DIRECTION = 120;
	public static final double DEFAULT_NORMAL_SPEED = 0.1;
	public static final double DEFAULT_SWITCH_TO_FAST_SPEED_AT_DISTANCE = 5.0;
	public static final double DEFAULT_START_MOVE_AT_DEVIATION = 0.5;
	public static final double DEFAULT_MAX_ALLOWED_DISTANCE = 9.9;
	public static final String DEFAULT_OSCILLATOR_HINT = "";
	public static final int DEFAULT_OSCILLATOR_PERIOD = 100;
	public static final double DEFAULT_OSCILLATOR_AMPLITUDE = 0.1;
	public static final int DEFAULT_ROTATOR_PERIOD = 100;
	public static final int DEFAULT_BLOCK_DELAY = 4;
	public static final int DEFAULT_LIVING_DELAY = 1;
	public static final int DEFAULT_MINECART_DELAY = 0;
	public static final double DEFAULT_ADJUST_ILLAGER_Y = 0.0;
	public static final boolean DEFAULT_ENABLE_RISING_Y_WORKAROUND = true;
	public static final boolean DEFAULT_ENABLE_WARN_Y64_WALLS = true;

	private final String name;
	private final String fullName;

	private double highAbovePlayer = DEFAULT_HIGH_ABOVE_PLAYER;
	private double distanceFromPlayer = DEFAULT_DISTANCE_FROM_PLAYER;
	private double angleFromPlayerDirection = DEFAULT_ANGLE_FROM_PLAYER_DIRECTION;

	private double normalSpeed = DEFAULT_NORMAL_SPEED;
	private double switchToFastSpeedAtDistance = DEFAULT_SWITCH_TO_FAST_SPEED_AT_DISTANCE;
	private double maxAllowedDistance = DEFAULT_MAX_ALLOWED_DISTANCE;
	private double startMoveAtDeviation = DEFAULT_START_MOVE_AT_DEVIATION;

	private String oscillatorHint = DEFAULT_OSCILLATOR_HINT;
	private int oscillatorPeriod = DEFAULT_OSCILLATOR_PERIOD;
	private double oscillatorAmplitude = DEFAULT_OSCILLATOR_AMPLITUDE;
	private int rotatorPeriod = DEFAULT_ROTATOR_PERIOD;
	private int blockDelay = DEFAULT_BLOCK_DELAY;
	private int livingDelay = DEFAULT_LIVING_DELAY;
	private int minecartDelay = DEFAULT_MINECART_DELAY;

	private double adjustIllagerY = DEFAULT_ADJUST_ILLAGER_Y;
	private boolean enableRisingYWorkaround = DEFAULT_ENABLE_RISING_Y_WORKAROUND;
	private boolean enableWarnY64Walls = DEFAULT_ENABLE_WARN_Y64_WALLS;

	private boolean defaultRule = false;

	public ConfigRule(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}

	public ConfigRule(String name, String fullName, boolean defaultRule) {
		this.name = name;
		this.fullName = fullName;
		this.defaultRule = defaultRule;
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

	public double getStartMoveAtDeviation() {
		return startMoveAtDeviation;
	}

	protected void setStartMoveAtDeviation(double startMoveAtDeviation) {
		this.startMoveAtDeviation = startMoveAtDeviation;
	}

	public String getOscillatorHint() {
		return oscillatorHint;
	}

	protected void setOscillatorHint(String oscillatorHint) {
		this.oscillatorHint = oscillatorHint;
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

	public int getRotatorPeriod() {
		return rotatorPeriod;
	}

	protected void setRotatorPeriod(int rotatorPeriod) {
		this.rotatorPeriod = rotatorPeriod;
	}

	public int getBlockDelay() {
		return blockDelay;
	}

	protected void setBlockDelay(int blockDelay) {
		this.blockDelay = blockDelay;
	}

	public int getLivingDelay() {
		return livingDelay;
	}

	protected void setLivingDelay(int livingDelay) {
		this.livingDelay = livingDelay;
	}

	public int getMinecartDelay() {
		return minecartDelay;
	}

	protected void setMinecartDelay(int minecartDelay) {
		this.minecartDelay = minecartDelay;
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
		return Arrays.asList(ParamRule.values());
	}

	protected void importHeliumSection(HeliumSection heliumSection) throws BalloonException {
		setDefaultRule(heliumSection.getBoolean(ParamRule.IS_DEFAULT, isDefaultRule()));
		setHighAbovePlayer(heliumSection.getDouble(ParamRule.HIGH_ABOVE_PLAYER, getHighAbovePlayer()));
		setDistanceFromPlayer(heliumSection.getDouble(ParamRule.DISTANCE_FROM_PLAYER, getDistanceFromPlayer()));
		setAngleFromPlayerDirection(
				heliumSection.getDouble(ParamRule.ANGLE_FROM_PLAYER_DIRECTION, getAngleFromPlayerDirection()));
		setNormalSpeed(heliumSection.getDouble(ParamRule.NORMAL_SPEED, getNormalSpeed()));
		setSwitchToFastSpeedAtDistance(
				heliumSection.getDouble(ParamRule.SWITCH_TO_FAST_SPEED_AT_DISTANCE, getSwitchToFastSpeedAtDistance()));
		setMaxAllowedDistance(heliumSection.getDouble(ParamRule.MAX_ALLOWED_DISTANCE, getMaxAllowedDistance()));
		setStartMoveAtDeviation(heliumSection.getDouble(ParamRule.START_MOVE_AT_DEVIATION, getStartMoveAtDeviation()));
		setOscillatorHint(heliumSection.getString(ParamRule.OSCILLATOR_HINT, getOscillatorHint()));
		setOscillatorPeriod(heliumSection.getInt(ParamRule.OSCILLATOR_PERIOD, getOscillatorPeriod()));
		setOscillatorAmplitude(heliumSection.getDouble(ParamRule.OSCILLATOR_AMPLITUDE, getOscillatorAmplitude()));
		setRotatorPeriod(heliumSection.getInt(ParamRule.ROTATOR_PERIOD, getRotatorPeriod()));
		setBlockDelay(heliumSection.getInt(ParamRule.BLOCK_DELAY, getBlockDelay()));
		setLivingDelay(heliumSection.getInt(ParamRule.LIVING_DELAY, getLivingDelay()));
		setMinecartDelay(heliumSection.getInt(ParamRule.MINECART_DELAY, getMinecartDelay()));
		setAdjustIllagerY(heliumSection.getDouble(ParamRule.ADJUST_ILLAGER_Y, getAdjustIllagerY()));
		setEnableRisingYWorkaround(
				heliumSection.getBoolean(ParamRule.ENABLE_RISING_Y_WORKAROUND, isEnableRisingYWorkaround()));
		setEnableWarnY64Walls(heliumSection.getBoolean(ParamRule.ENABLE_WARN_Y64_WALLS, isEnableWarnY64Walls()));
	}

	protected void validateConfig() throws BalloonException {
		if ((getAngleFromPlayerDirection() < -180.0) || (getAngleFromPlayerDirection() >= 360.0)) {
			throw new BalloonException(getFullName(), "AngleFromPlayerDirection out of range",
					Double.toString(getAngleFromPlayerDirection()));
		}
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);
		validateConfig();
	}

	protected Map<String, String> buildMathParamMap() { // NOSONAR
		Map<String, String> newParamMap = new HashMap<>();
		if (defaultRule)
			newParamMap.put(ParamRule.IS_DEFAULT.getAttributeName(), Boolean.toString(true));
		if (highAbovePlayer != DEFAULT_HIGH_ABOVE_PLAYER)
			newParamMap.put(ParamRule.HIGH_ABOVE_PLAYER.getAttributeName(), Double.toString(highAbovePlayer));
		if (distanceFromPlayer != DEFAULT_DISTANCE_FROM_PLAYER)
			newParamMap.put(ParamRule.DISTANCE_FROM_PLAYER.getAttributeName(), Double.toString(distanceFromPlayer));
		if (angleFromPlayerDirection != DEFAULT_ANGLE_FROM_PLAYER_DIRECTION)
			newParamMap.put(ParamRule.ANGLE_FROM_PLAYER_DIRECTION.getAttributeName(),
					Double.toString(angleFromPlayerDirection));
		if (normalSpeed != DEFAULT_NORMAL_SPEED)
			newParamMap.put(ParamRule.NORMAL_SPEED.getAttributeName(), Double.toString(normalSpeed));
		if (switchToFastSpeedAtDistance != DEFAULT_SWITCH_TO_FAST_SPEED_AT_DISTANCE)
			newParamMap.put(ParamRule.SWITCH_TO_FAST_SPEED_AT_DISTANCE.getAttributeName(),
					Double.toString(switchToFastSpeedAtDistance));
		if (maxAllowedDistance != DEFAULT_MAX_ALLOWED_DISTANCE)
			newParamMap.put(ParamRule.MAX_ALLOWED_DISTANCE.getAttributeName(), Double.toString(maxAllowedDistance));
		if (startMoveAtDeviation != DEFAULT_START_MOVE_AT_DEVIATION)
			newParamMap.put(ParamRule.START_MOVE_AT_DEVIATION.getAttributeName(),
					Double.toString(startMoveAtDeviation));
		if (oscillatorPeriod != DEFAULT_OSCILLATOR_PERIOD)
			newParamMap.put(ParamRule.OSCILLATOR_PERIOD.getAttributeName(), Integer.toString(oscillatorPeriod));
		if (oscillatorAmplitude != DEFAULT_OSCILLATOR_AMPLITUDE)
			newParamMap.put(ParamRule.OSCILLATOR_AMPLITUDE.getAttributeName(), Double.toString(oscillatorAmplitude));
		if (rotatorPeriod != DEFAULT_ROTATOR_PERIOD)
			newParamMap.put(ParamRule.ROTATOR_PERIOD.getAttributeName(), Integer.toString(rotatorPeriod));
		if (blockDelay != DEFAULT_BLOCK_DELAY)
			newParamMap.put(ParamRule.BLOCK_DELAY.getAttributeName(), Integer.toString(blockDelay));
		if (livingDelay != DEFAULT_LIVING_DELAY)
			newParamMap.put(ParamRule.LIVING_DELAY.getAttributeName(), Integer.toString(livingDelay));
		if (minecartDelay != DEFAULT_MINECART_DELAY)
			newParamMap.put(ParamRule.MINECART_DELAY.getAttributeName(), Integer.toString(minecartDelay));
		if (adjustIllagerY != DEFAULT_ADJUST_ILLAGER_Y)
			newParamMap.put(ParamRule.ADJUST_ILLAGER_Y.getAttributeName(), Double.toString(adjustIllagerY));
		if (enableRisingYWorkaround != DEFAULT_ENABLE_RISING_Y_WORKAROUND)
			newParamMap.put(ParamRule.ENABLE_RISING_Y_WORKAROUND.getAttributeName(),
					Boolean.toString(enableRisingYWorkaround));
		if (enableWarnY64Walls != DEFAULT_ENABLE_WARN_Y64_WALLS)
			newParamMap.put(ParamRule.ENABLE_WARN_Y64_WALLS.getAttributeName(), Boolean.toString(enableWarnY64Walls));
		return newParamMap;
	}

	protected List<String> paramListAsDump() {
		List<String> newParamListDump = new ArrayList<>();
		for (Entry<String, String> myEntry : buildMathParamMap().entrySet())
			newParamListDump.add(String.format("%s: %s", myEntry.getKey(), myEntry.getValue()));
		if (!oscillatorHint.isEmpty()) {
			newParamListDump
					.add(String.format("%s: \"%s\"", ParamRule.OSCILLATOR_HINT.getAttributeName(), oscillatorHint));
		}
		return newParamListDump;
	}

	@Override
	public String toString() {
		return String.format("%s: { %s }", getName(), String.join(", ", paramListAsDump()));
	}

}
