package de.polarwolf.heliumballoon.balloons;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.rules.Rule;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public abstract class SimpleBalloon implements Balloon {
	
	private boolean cancelled = false;
	private final Player player;
	private final ConfigTemplate template;
	private final Oscillator oscillator;
	private Element element = null;
	protected boolean highSpeedMode = false;
	protected boolean sleeping = false;
	protected Vector lastPosition = null;
	protected int refreshCounter = 0;
	
	
	protected SimpleBalloon(Player player, ConfigTemplate template, Oscillator oscillator) {
		this.player = player;
		this.template=template;
		this.oscillator = oscillator;
	}
	
	
	protected abstract Element createElement(SpawnModifier spawnModifier);
	

	@Override
	public void prepare(SpawnModifier spawnModifier) {
		if (element == null) {
			element = createElement(spawnModifier);
		}
	}
			
	
	protected Element getElement() {
		return element;
	}


	public ConfigTemplate getTemplate() {
		return template;
	}


	public Rule getRule() {
		return  getTemplate().getRule();
	}


	@Override
	public Oscillator getOscillator() {
		return oscillator;
	}


	protected final void setCancel() {
		if (element != null) {
			element.hide();
		}
		cancelled = true;
	}
	
	
	@Override
	public final boolean isCancelled() {
		return cancelled;
	}
	
	
	@Override
	public void cancel() {
		setCancel();
	}
	

	@Override
	public Player getPlayer() {
		return player;
	}
	

	@Override
	public World getWorld() {
		Location centralLocation = element.getCurrentCentralLocation();
		if (centralLocation != null) {
			return centralLocation.getWorld();
		} else {
			return null;
		}
	}
	

	@Override
	public boolean isSleeping() {
		return sleeping;
	}
	
	
	@Override
	public void wakeup() {
		sleeping = false;
	}


	@Override
	public boolean hasEntity(Entity entity) {
		return element.hasEntity(entity);
	}
	
	
	protected Vector calculateVelocity(Vector currentPosition, Vector targetPosition) {
		Vector newPosition = targetPosition.clone();
		if (getOscillator() != null) {
			newPosition.add(getOscillator().getDeflection());
		}
		Double distance = newPosition.distance(currentPosition);
		
		// A leash has a maximum length of 10
		if (distance > getRule().getMaxAllowedDistance()) {
			// Teleport
			return null;			
		}	
		
		// Do not move until steps are at least 0.0001
		if (distance < 0.0001) {
			lastPosition = newPosition;
			if (!getTemplate().isOscillating()) {
				sleeping = true;
			}
			return new Vector();
		}

		double normalSpeed = getRule().getNormalSpeed();
		double highSpeed = normalSpeed; 
		if (lastPosition != null) {
			double lastDistance = newPosition.distance(lastPosition);
			if (lastDistance > normalSpeed) {
				highSpeed = lastDistance;
			}
		}
		lastPosition = newPosition;

		Vector movingDirection = newPosition.clone().subtract(currentPosition);
		movingDirection.normalize();
		
		// Check for slow speed
		if (distance < normalSpeed) {
			movingDirection.multiply(distance);
			return movingDirection;
		}

		// Avoid tremble between HighSpeed and NormalSpeed
		double switchToFastSpeedAtDistance = getRule().getSwitchToFastSpeedAtDistance();
		if (distance - switchToFastSpeedAtDistance > 0.25) {
			highSpeedMode = true;
		}
		if (distance - switchToFastSpeedAtDistance <= -0.25) {  
			highSpeedMode = false;
		}

		if (highSpeedMode) {
			movingDirection.multiply(highSpeed);
		} else {
			movingDirection.multiply(normalSpeed);
		}

		return movingDirection;	
	}


	// Return:
	//   null: Balloon is no longer needed, please cancel
	//   EmptyString: All is OK
	//   String: All is OK, please print String to Debug
	@Override
	public String move() throws BalloonException {
		if (isCancelled()) {
			return null;
		}
		
		String resultText = "";
		if (isSleeping()) {
			element.keepAlive();
			return resultText;
		}

		Vector newVelocity = null;
		boolean mustRefresh = false;

		Location targetLocation = getTargetLocation();
		if (targetLocation == null) {
			return null;
		}
		
		Location currentLocation = element.getCurrentCentralLocation();		
		if (currentLocation == null) {
			mustRefresh = true;
			resultText = "Refresh: Unknown current location: " + getName();			
		} else {

			if (!currentLocation.getWorld().equals(targetLocation.getWorld())) {
				// Wrong word. Remove Balloon
				return null;
			}
			
			newVelocity = calculateVelocity(currentLocation.toVector(), targetLocation.toVector());
			if (newVelocity == null) {
				mustRefresh = true;
				resultText = "Refresh: Balloon is too far away from target: " + getName();			
			}
		}
		
		if (!element.isValid()) {
			mustRefresh = true;
			resultText = "Refresh: At least one element is new or invalid: " + getName();									
		}
		
		if (mustRefresh) {
			if (refreshCounter > 10) {
				// too many errors
				cancel();
				return null;
			}
			refreshCounter = refreshCounter +1;
			element.show(targetLocation);
		} else {
			element.keepAlive();
			element.setVelocity(newVelocity);
			refreshCounter = 0;
		}
		return resultText;
	}

}
