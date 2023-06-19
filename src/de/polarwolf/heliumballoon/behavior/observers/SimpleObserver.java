package de.polarwolf.heliumballoon.behavior.observers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.behavior.oscillators.Oscillator;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.config.templates.ConfigTemplate;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumName;

public abstract class SimpleObserver implements Observer, HeliumName {

	private boolean cancelled = false;
	private final Player player;
	private final World world;
	private final ConfigBalloon configBalloon;
	private final ConfigElement configElement;
	private final ConfigTemplate template;
	private final Oscillator oscillator;
	private final BehaviorDefinition behaviorDefinition;
	private Element element = null;
	protected boolean sleeping = false;
	protected int refreshCounter = 0;
	protected List<Vector> positionQueue = new ArrayList<>();

	protected SimpleObserver(BehaviorDefinition behaviorDefinition, ConfigBalloon configBalloon,
			ConfigElement configElement, Oscillator oscillator, World world) throws BalloonException {
		this.behaviorDefinition = behaviorDefinition;
		this.configBalloon = configBalloon;
		this.configElement = configElement;
		this.oscillator = oscillator;
		this.player = null;
		this.world = world;
		this.template = getTemplateFromList(configBalloon, world);
	}

	protected SimpleObserver(BehaviorDefinition behaviorDefinition, ConfigBalloon configBalloon,
			ConfigElement configElement, Oscillator oscillator, Player player) throws BalloonException {
		this.behaviorDefinition = behaviorDefinition;
		this.configBalloon = configBalloon;
		this.configElement = configElement;
		this.oscillator = oscillator;
		this.player = player;
		this.world = player.getWorld();
		this.template = getTemplateFromList(configBalloon, world);
	}

	protected static ConfigTemplate getTemplateFromList(ConfigBalloon configBalloon, World world)
			throws BalloonException {
		ConfigTemplate template = configBalloon.findTemplate(world);
		if (template == null) {
			throw new BalloonException(configBalloon.getFullName(), "Balloon has no template for this world",
					world.getName());
		}
		return template;
	}

	protected Element createElement() {
		return getConfigElement().createElement(getPlayer(), getRule(), behaviorDefinition);
	}

	@Override
	public void prepare() {
		if (element == null) {
			element = createElement();
		}
	}

	protected Element getElement() {
		return element;
	}

	@Override
	public String getName() {
		return configBalloon.getName();
	}

	@Override
	public String getFullName() {
		return configBalloon.getFullName();
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public Oscillator getOscillator() {
		return oscillator;
	}

	@Override
	public BehaviorDefinition getBehaviorDefinition() {
		return behaviorDefinition;
	}

	protected ConfigRule getRule() {
		return template.getRule();
	}

	protected ConfigTemplate getTemplate() {
		return template;
	}

	protected ConfigElement getConfigElement() {
		return configElement;
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
			return new EulerAngle(0, 0, 0);
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
