package de.polarwolf.heliumballoon.elements;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigElement;
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.spawnmodifiers.HeliumModifier;
import de.polarwolf.heliumballoon.system.Rule;

public class CompoundElement implements Element {

	private final ConfigTemplate template;
	private final Player player;
	protected final HeliumModifier heliumModifier;
	protected List<Element> elements = new ArrayList<>();
	
	
	public CompoundElement(Player player, ConfigTemplate template, HeliumModifier heliumModifier) {
		this.player=player;
		this.template=template;
		this.heliumModifier = heliumModifier;
		buildElements();
	}
	
	
	protected void buildElements() {
		elements.clear();
		for(ConfigElement myConfigElement : getTemplate().enumElements()) {
			elements.add(new FallingBlockElement(player, getRule(), myConfigElement, heliumModifier));
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
	

	public ConfigTemplate getTemplate() {
		return template;
	}
	
	
	public Rule getRule() {
		return  getTemplate().getRule();
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
			if (distance > 9.9) {
				return null;
			}
		}
		return currentLocation;
	}

}
