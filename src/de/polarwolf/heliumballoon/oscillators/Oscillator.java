package de.polarwolf.heliumballoon.oscillators;

import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.elements.Element;

public interface Oscillator {

	public boolean hasDeflection();

	public boolean hasSpin();

	public void setDeflectionState(boolean newDeflectionState);

	public void setSpinState(boolean newSpinState);

	public Vector getCurrentDeflection(Element element);

	public double getCurrentSpin(Element element);

	public void incrementCounters();

}
