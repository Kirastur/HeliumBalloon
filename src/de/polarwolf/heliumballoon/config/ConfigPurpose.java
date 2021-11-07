package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.balloons.BalloonPurpose;

public interface ConfigPurpose {

	public boolean isSuitableFor(BalloonPurpose purpose);

}
