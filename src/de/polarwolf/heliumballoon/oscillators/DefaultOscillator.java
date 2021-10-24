package de.polarwolf.heliumballoon.oscillators;

import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigRule;
import de.polarwolf.heliumballoon.elements.ArmorStandElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.elements.MinecartElement;

public class DefaultOscillator implements Oscillator {
	
	protected final ConfigRule rule;

	protected boolean deflection = false;
	protected boolean spin = false;
	
	protected int countDeflection = 0;
	protected int countSpin = 0;
	
	protected Vector currentDeflection = new Vector();
	protected double currentMinecartSpin = 0.0;
	protected double currentArmorStandSpin = 0.0;
	

	
	public DefaultOscillator(ConfigRule rule) {
		this.rule = rule;
	}
	
	
	@Override
	public boolean hasDeflection() {
		return deflection;
	}


	@Override
	public void setDeflectionState(boolean newDeflectionState) {
		this.deflection = newDeflectionState;
	}


	@Override
	public boolean hasSpin() {
		return spin;
	}


	@Override
	public void setSpinState(boolean newSpinState) {
		this.spin = newSpinState;
	}


	protected void calculateEffects() {
		if (hasDeflection() && (rule.getOscillatorPeriod() != 0)) {
			int period = rule.getOscillatorPeriod();
			double amplitude = rule.getOscillatorAmplitude();
		
			double p = 2.0 * Math.PI / period;
			double x = countDeflection * p;
			double y = Math.sin(x) * amplitude;

			currentDeflection = new Vector(0.0, y, 0.0);
		}
		
		if (hasSpin() && (rule.getRotatorPeriod() != 0)) {
			double pa = 360.0 / Math.abs(rule.getRotatorPeriod());
			currentArmorStandSpin = countSpin * pa;
			if (rule.getRotatorPeriod() < 0) {
				currentArmorStandSpin = 360.0 - currentArmorStandSpin;
			}
			
			double pm = 2.0 * Math.PI / Math.abs(rule.getRotatorPeriod());
			double x = countSpin * pm;
			currentMinecartSpin = Math.sin(x) * 350.0;			
		}		
		
	}
	
	
	@Override
	public void incrementCounters() {
		countDeflection = (countDeflection +1) % rule.getOscillatorPeriod();
		countSpin = (countSpin +1) % Math.abs(rule.getRotatorPeriod());
		calculateEffects();
	}
	
	
	@Override
	public Vector getCurrentDeflection(Element element) {
		return currentDeflection;
	}
	
	
	@Override
	public double getCurrentSpin(Element element) {
		if (element instanceof MinecartElement) {
			return currentMinecartSpin;
		}
		if (element instanceof ArmorStandElement) {
			return currentArmorStandSpin;
		}
		return 0.0;
	}

}
