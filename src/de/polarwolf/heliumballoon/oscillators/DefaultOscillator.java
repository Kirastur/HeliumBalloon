package de.polarwolf.heliumballoon.oscillators;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigRule;

public class DefaultOscillator extends SimpleOscillator {

	protected final ConfigRule rule;

	public DefaultOscillator(ConfigRule rule) {
		this.rule = rule;
	}

	protected List<Double> calculateDeflectionY() {
		List<Double> deflections = new ArrayList<>();
		double amplitude = rule.getOscillatorAmplitude();
		int period = rule.getOscillatorPeriod();
		double step = 2.0 * Math.PI / period;
		for (int i = 0; i < period; i++) {
			double y = Math.sin(i * step) * amplitude;
			deflections.add(Double.valueOf(y));
		}
		return deflections;
	}

	protected List<Double> calculateMinecartSpin() {
		List<Double> spins = new ArrayList<>();
		int period = rule.getRotatorPeriod();
		double step = 2.0 * Math.PI / period;
		for (int i = 0; i < period; i++) {
			double mySpin = Math.sin(i * step) * (2 * Math.PI - 0.17);
			spins.add(Double.valueOf(mySpin));
		}
		return spins;
	}

	protected List<Double> calculateArmorStandSpin() {
		List<Double> spins = new ArrayList<>();
		int period = rule.getRotatorPeriod();
		double step = 2.0 * Math.PI / period;
		for (int i = 0; i < period; i++) {
			double myAngle = i * step;
			if (period < 0) {
				myAngle = 2 * Math.PI - myAngle;
			}
			spins.add(Double.valueOf(myAngle));
		}
		return spins;
	}

	@Override
	protected void prepare() {
		for (double y : calculateDeflectionY()) {
			addDeflection(new Vector(0, y, 0));
		}
		for (double d : calculateMinecartSpin()) {
			addMinecartSpin(new EulerAngle(0, d, 0));
		}
		for (double d : calculateArmorStandSpin()) {
			addArmorStandSpin(new EulerAngle(0, d, 0));
		}
		setPrepared();
	}

}
