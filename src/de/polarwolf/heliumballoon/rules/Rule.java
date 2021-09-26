package de.polarwolf.heliumballoon.rules;

public class Rule {

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

}
