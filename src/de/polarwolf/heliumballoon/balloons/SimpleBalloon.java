package de.polarwolf.heliumballoon.balloons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigPart;
import de.polarwolf.heliumballoon.config.ConfigRule;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public abstract class SimpleBalloon implements Balloon {
	
	private boolean cancelled = false;
	private final Player player;
	private final ConfigRule rule;
	private final ConfigPart part;
	private final Oscillator oscillator;
	private Element element = null;
	protected boolean highSpeedMode = false;
	protected boolean sleeping = false;
	protected Vector lastPosition = null;
	protected int refreshCounter = 0;
	protected List<Vector> positionQueue = new ArrayList<>();
	
	
	protected SimpleBalloon(Player player, ConfigRule rule, ConfigPart part, Oscillator oscillator) {
		this.player = player;
		this.rule = rule;
		this.part = part;
		this.oscillator = oscillator;
	}
	
	
	protected Element createElement(SpawnModifier spawnModifier) {
		return getPart().createElement(getPlayer(), getRule(), spawnModifier);
	}
	

	@Override
	public void prepare(SpawnModifier spawnModifier) {
		if (element == null) {
			element = createElement(spawnModifier);
		}
	}
	

	protected Element getElement() {
		return element;
	}


	@Override
	public Player getPlayer() {
		return player;
	}
	

	protected ConfigRule getRule() {
		return  rule;
	}


	protected ConfigPart getPart() {
		return part;
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
	
	
	protected abstract Location getTargetLocation();
	
	
	protected int getDelay() {
		return element.getDelay();
	}
	
	
	protected Vector getNextPosition(Vector targetPosition) {
		Vector newPosition = targetPosition.clone();

		if (getOscillator() != null) {
			newPosition.add(getOscillator().getDeflection());
		}
		
		positionQueue.add(newPosition);
		newPosition = positionQueue.get(0);
		if (positionQueue.size() > getDelay()) {
			positionQueue.remove(0);
		}
		
		return newPosition;			
	}
	
	
	protected Vector calculateVelocity(Vector currentPosition, Vector targetPosition) {
		Vector newPosition = getNextPosition(targetPosition);
		Double distance = newPosition.distance(currentPosition);
		
		// A leash has a maximum length of 10
		if (distance > getRule().getMaxAllowedDistance()) {
			// Teleport
			return null;			
		}	
		
		// Do not move until steps are at least 0.0001
		if (distance < 0.0001) {
			lastPosition = newPosition;
			if ((getOscillator() == null)  && (positionQueue.size() == getDelay())) {
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
