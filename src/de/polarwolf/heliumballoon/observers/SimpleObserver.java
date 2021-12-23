package de.polarwolf.heliumballoon.observers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigBalloonSet;
import de.polarwolf.heliumballoon.config.ConfigPart;
import de.polarwolf.heliumballoon.config.ConfigRule;
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;
import de.polarwolf.heliumballoon.tools.helium.HeliumName;

public abstract class SimpleObserver implements Observer, HeliumName {

	private boolean cancelled = false;
	private final Player player;
	private final World world;
	private final ConfigBalloonSet configBalloonSet;
	private final ConfigPart configPart;
	private final ConfigTemplate template;
	private final Oscillator oscillator;
	private Element element = null;
	protected boolean sleeping = false;
	protected int refreshCounter = 0;
	protected List<Vector> positionQueue = new ArrayList<>();

	protected SimpleObserver(World world, ConfigBalloonSet configBalloonSet, ConfigPart configPart,
			Oscillator oscillator) throws BalloonException {
		this.player = null;
		this.world = world;
		this.configBalloonSet = configBalloonSet;
		this.configPart = configPart;
		this.template = getTemplateFromBalloonSet(configBalloonSet, world);
		this.oscillator = oscillator;
	}

	protected SimpleObserver(Player player, ConfigBalloonSet configBalloonSet, ConfigPart configPart,
			Oscillator oscillator) throws BalloonException {
		this.player = player;
		this.world = player.getWorld();
		this.configBalloonSet = configBalloonSet;
		this.configPart = configPart;
		this.template = getTemplateFromBalloonSet(configBalloonSet, world);
		this.oscillator = oscillator;
	}

	protected static ConfigTemplate getTemplateFromBalloonSet(ConfigBalloonSet configBalloonSet, World world)
			throws BalloonException {
		ConfigTemplate template = configBalloonSet.findTemplate(world);
		if (template == null) {
			throw new BalloonException(configBalloonSet.getFullName(), "Balloon has no template for this world",
					world.getName());
		}
		return template;
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
	public String getName() {
		return configBalloonSet.getName();
	}

	@Override
	public String getFullName() {
		return configBalloonSet.getFullName();
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	protected ConfigTemplate getTemplate() {
		return template;
	}

	protected ConfigRule getRule() {
		return template.getRule();
	}

	protected ConfigPart getPart() {
		return configPart;
	}

	@Override
	public Oscillator getOscillator() {
		return oscillator;
	}

	@Override
	public World getWorld() {
		return world;
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

	protected abstract Vector getTargetPosition();

	protected abstract Vector getVelocity(Vector currentPosition, Vector nextPosition);

	protected int getDelay() {
		return element.getDelay();
	}
	
	protected Vector getCurrentDeflection() {
		if ((getOscillator() != null) && getOscillator().hasDeflection()) {
			return getOscillator().getCurrentDeflection(element);
		} else {
			return new Vector();
		}
		
	}
	
	protected EulerAngle getCurrentSpin() {
		if ((getOscillator() != null) && getOscillator().hasSpin()) {
			return getOscillator().getCurrentSpin(element);
		} else {
			return new EulerAngle (0, 0, 0);
		}
		
	}

	protected Vector getNextPosition(Vector targetPosition) {
		Vector newPosition = targetPosition.clone();
		newPosition.add(getCurrentDeflection());

		positionQueue.add(newPosition);
		newPosition = positionQueue.get(0);
		if (positionQueue.size() > getDelay()) {
			positionQueue.remove(0);
		}

		return newPosition;
	}

	protected Vector calculateVelocity(Vector currentPosition, Vector targetPosition) {
		Vector nextPosition = getNextPosition(targetPosition);
		double nextDistance = nextPosition.distance(currentPosition);

		// A leash has a maximum length of 10
		if (nextDistance > getRule().getMaxAllowedDistance()) {
			// Teleport
			return null;
		}

		// Do not move until steps are at least 0.0001
		if (nextDistance < 0.0001) {
			if ((getOscillator() == null) && (positionQueue.size() == getDelay())) {
				sleeping = true;
			}
			return new Vector();
		}

		Vector velocity = getVelocity(currentPosition, nextPosition);
		if (Math.abs(velocity.getX()) < 0.0001) {
			velocity.setX(0);
		}
		if (Math.abs(velocity.getY()) < 0.0001) {
			velocity.setY(0);
		}
		if (Math.abs(velocity.getZ()) < 0.0001) {
			velocity.setZ(0);
		}

		return velocity;
	}

	protected void setSpin() {
		// Doublecheck for hasSpin to optimize Performance in setSpin
		if ((getOscillator() != null) && getOscillator().hasSpin()) {
			element.setSpin(getCurrentSpin());
		}
	}

	// Return:
	// null: Observer is no longer needed, please cancel
	// EmptyString: All is OK
	// String: All is OK, please print String to Debug
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

		Vector targetPosition = getTargetPosition();
		if (targetPosition == null) {
			return null;
		}

		Location currentLocation = element.getCurrentCentralLocation();
		if (currentLocation == null) {
			mustRefresh = true;
			resultText = "Refresh: Unknown current location: " + getName();
		} else {

			if (!currentLocation.getWorld().equals(world)) {
				// Wrong word. Remove Balloon
				return null;
			}

			newVelocity = calculateVelocity(currentLocation.toVector(), targetPosition);
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
			refreshCounter = refreshCounter + 1;
			element.show(targetPosition.toLocation(world));
		} else {
			element.keepAlive();
			element.setVelocity(newVelocity);
			refreshCounter = 0;
		}
		setSpin();
		return resultText;
	}

}
