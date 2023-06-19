package de.polarwolf.heliumballoon.elements.blocks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.exception.BalloonException;

public class BlocksCompoundElement implements Element {

	private final ConfigRule rule;
	private final BlocksCompoundConfig compound;
	private final Player player;
	protected final BehaviorDefinition behaviorDefinition;
	protected List<Element> elements = new ArrayList<>();

	protected BlocksCompoundElement(Player player, ConfigRule rule, BlocksCompoundConfig compound,
			BehaviorDefinition behaviorDefinition) {
		this.player = player;
		this.rule = rule;
		this.compound = compound;
		this.behaviorDefinition = behaviorDefinition;
		buildElements();
	}

	protected void buildElements() {
		elements.clear();
		for (BlocksPartConfig myConfigElement : getCompound().getElements()) {
			elements.add(myConfigElement.createElement(player, getRule(), behaviorDefinition));
		}
	}

	@Override
	public Vector getOffset() {
		return new Vector();
	}

	@Override
	public Entity getEntity() {
		return null;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public boolean hasEntity(Entity entityToCheck) {
		for (Element myElement : elements) {
			if (myElement.hasEntity(entityToCheck)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getDelay() {
		return getRule().getBlockDelay();
	}

	protected void spawn(Location targetLocation) throws BalloonException {
		try {
			for (Element myElement : elements) {
				myElement.show(targetLocation);
			}
		} catch (Exception e) {
			hide();
			throw e;
		}
	}

	public ConfigRule getRule() {
		return rule;
	}

	public BlocksCompoundConfig getCompound() {
		return compound;
	}

	@Override
	public void hide() {
		for (Element myElement : elements) {
			myElement.hide();
		}
	}

	@Override
	public void show(Location centralLocation) throws BalloonException {
		hide();
		spawn(centralLocation);
	}

	@Override
	public void setVelocity(Vector newVelocity) {
		Vector expectedLocation = elements.get(0).getCurrentCentralLocation().toVector();
		for (Element myElement : elements) {
			Vector myVelocity = newVelocity.clone();
			Vector myElementLocation = myElement.getCurrentCentralLocation().toVector();
			Vector myCorrection = expectedLocation.clone().subtract(myElementLocation);
			myVelocity.add(myCorrection);
			myElement.setVelocity(myVelocity);
		}
	}

	@Override
	public void setSpin(EulerAngle spin) {
		// Nothing to do
	}

	@Override
	public void keepAlive() {
		for (Element myElement : elements) {
			myElement.keepAlive();
		}
	}

	@Override
	public boolean isValid() {
		boolean newValid = true;
		for (Element myElement : elements) {
			newValid = newValid && myElement.isValid();
		}
		return newValid;
	}

	@Override
	public Location getCurrentCentralLocation() {
		List<Location> centralLocations = new ArrayList<>();
		for (Element myElement : elements) {
			Location elementLocation = myElement.getCurrentCentralLocation();
			if (elementLocation == null) {
				return null;
			}
			centralLocations.add(elementLocation);
		}

		if (centralLocations.isEmpty()) {
			return null;
		}

		Location currentLocation = centralLocations.get(0).clone();
		for (Location myLocation : centralLocations) {
			if (!currentLocation.getWorld().equals(myLocation.getWorld())) {
				return null;
			}

			// A correction of the element position is done in setVelocity
			// so this is only a fail safe here.
			Double distance = currentLocation.distance(myLocation);
			if (distance > getRule().getMaxAllowedDistance()) {
				return null;
			}
		}
		return currentLocation;
	}

}
