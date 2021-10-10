package de.polarwolf.heliumballoon.oscillators;

import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigRule;

public class VerticalSinusOscillator implements Oscillator {
	
	protected final ConfigRule rule;
	protected int count = 0;
	protected Vector deflection = new Vector();

	
	public VerticalSinusOscillator(ConfigRule rule) {
		this.rule = rule;
	}
	
	
	protected void calculateDeflection() {
		int period = rule.getOscillatorPeriod();
		double amplitude = rule.getOscillatorAmplitude();
		
		double p = 2*Math.PI / period;
		double x = count * p;
		double y = Math.sin(x) * amplitude;

		deflection = new Vector(0.0, y, 0.0);
	}
	
	
	@Override
	public void increment() {
		count = (count +1) % rule.getOscillatorPeriod();
		calculateDeflection();
	}
	
	
	@Override
	public Vector getDeflection() {
		return deflection;
	}

}
