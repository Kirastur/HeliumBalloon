package de.polarwolf.heliumballoon.rules;

public class Rule {

	private double highAbovePlayer = 3.0;
	private double distanceFromPlayer = 1.0;
	private double angleFromPlayerDirection = 120;

	private double normalSpeed = 0.1;
	private double switchToFastSpeedAtDistance = 5.0;
	
	private boolean enableRisingYWorkaround = true;
	

	
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


	public boolean isEnableRisingYWorkaround() {
		return enableRisingYWorkaround;
	}


	protected void setEnableRisingYWorkaround(boolean enableRisingYWorkaround) {
		this.enableRisingYWorkaround = enableRisingYWorkaround;
	}

}
