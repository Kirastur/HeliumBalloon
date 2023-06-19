package de.polarwolf.heliumballoon.behavior.oscillators;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.elements.armorstand.ArmorstandElement;
import de.polarwolf.heliumballoon.elements.minecart.MinecartElement;

public abstract class SimpleOscillator implements Oscillator {

	private boolean prepared = false;

	protected boolean deflection = false;
	protected boolean spin = false;

	protected List<Vector> deflections = new ArrayList<>();
	protected List<EulerAngle> minecartSpins = new ArrayList<>();
	protected List<EulerAngle> armorStandSpins = new ArrayList<>();

	protected int countDeflection = 0;
	protected int countMinecartSpin = 0;
	protected int countArmorStandSpin = 0;

	protected boolean isPrepared() {
		return prepared;
	}

	protected void setPrepared() {
		prepared = true;
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

	protected void addDeflection(Vector newDeflection) {
		deflections.add(newDeflection);
	}

	protected void addMinecartSpin(EulerAngle newMinecartSpin) {
		minecartSpins.add(newMinecartSpin);
	}

	protected void addArmorStandSpin(EulerAngle newArmorStandSpin) {
		armorStandSpins.add(newArmorStandSpin);
	}

	protected abstract void prepare();

	@Override
	public void incrementCounters() {
		if (!isPrepared()) {
			prepare();
			return;
		}
		if (!deflections.isEmpty()) {
			countDeflection = (countDeflection + 1) % deflections.size();
		}
		if (!minecartSpins.isEmpty()) {
			countMinecartSpin = (countMinecartSpin + 1) % minecartSpins.size();
		}
		if (!armorStandSpins.isEmpty()) {
			countArmorStandSpin = (countArmorStandSpin + 1) % armorStandSpins.size();
		}
	}

	@Override
	public Vector getCurrentDeflection(Element element) {
		if (!hasDeflection() || deflections.isEmpty()) {
			return null;
		}
		return deflections.get(countDeflection).clone();
	}

	protected static EulerAngle cloneEulerAngle(EulerAngle eulerAngle) {
		return new EulerAngle(eulerAngle.getX(), eulerAngle.getY(), eulerAngle.getZ());
	}

	@Override
	public EulerAngle getCurrentSpin(Element element) {
		if (!hasSpin()) {
			return null;
		}
		if ((element instanceof MinecartElement) && !minecartSpins.isEmpty()) {
			return cloneEulerAngle(minecartSpins.get(countMinecartSpin));
		}
		if ((element instanceof ArmorstandElement) && !minecartSpins.isEmpty()) {
			return cloneEulerAngle(armorStandSpins.get(countArmorStandSpin));
		}
		return new EulerAngle(0, 0, 0);
	}

}
