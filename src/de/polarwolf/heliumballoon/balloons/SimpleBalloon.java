package de.polarwolf.heliumballoon.balloons;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.spawnmodifiers.HeliumModifier;
import de.polarwolf.heliumballoon.system.Rule;

public abstract class SimpleBalloon implements Balloon {
	
	private boolean cancelled = false;
	private final Player player;
	private final ConfigTemplate template;
	private Element element = null;
	protected boolean highSpeedMode = false;
	protected Vector lastTargetPosition = null;
	protected int refreshCounter = 0;
	
	
	protected SimpleBalloon(Player player, ConfigTemplate template) {
		this.player = player;
		this.template=template;
	}
	
	
	protected abstract Element createElement(HeliumModifier heliumModifier);
	

	@Override
	public void prepare(HeliumModifier heliumModifier) {
		if (element == null) {
			element = createElement(heliumModifier);
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
	public boolean hasEntity(Entity entity) {
		return element.hasEntity(entity);
	}
	
	
	protected Vector calculateVelocity(Vector currentPosition, Vector targetPosition) {
		Double distance = targetPosition.distance(currentPosition);
		
		// A leash has a maximum length of 10
		if (distance > 9.9) {
			// Teleport
			return null;			
		}	
		
		// Do not move until steps are at least 0.01
		if (distance < 0.01) {
			lastTargetPosition = targetPosition;
			return new Vector();
		}
		
		double normalSpeed = getRule().getNormalSpeed();
		double highSpeed = normalSpeed; 
		if (lastTargetPosition != null) {
			double lastTargetDistance = targetPosition.distance(lastTargetPosition);
			if (lastTargetDistance > normalSpeed) {
				highSpeed = lastTargetDistance;
			}
		}
		lastTargetPosition = targetPosition;

		Vector movingDirection = targetPosition.clone().subtract(currentPosition);
		movingDirection.normalize();
		
		// Check for slow speed
		if (distance < normalSpeed) {
			movingDirection.multiply(distance);
			return movingDirection;
		}

		// Avoid tremble between HighSpeed and NormalSpeed
		if (distance - getRule().getSwitchToFastSpeedAtDistance() > 0.25) {
			highSpeedMode = true;
		}
		if (distance - getRule().getSwitchToFastSpeedAtDistance() <= -0.25) {  
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
