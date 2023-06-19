package de.polarwolf.heliumballoon.behavior.observers;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.behavior.oscillators.Oscillator;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumName;

public interface Observer extends HeliumName {

	// Prepare the Observer by creating the underlying element.
	// This does only create the element, not the Minecraft entity.
	// The spawn of the entity is done later by move().
	// Please do not call this directly,
	// this is done by the ObserverManager.
	public void prepare();

	// Get the associated player. Can be null.
	// Needed to detect which Observers are effected
	// when a player logs out.
	public Player getPlayer();

	// Get the world the Observer is in.
	// An observer cannot change worlds.
	public World getWorld();

	// Get the oscillator for swinging
	public Oscillator getOscillator();

	// Get the Behavior
	public BehaviorDefinition getBehaviorDefinition();

	// Get the sleeping state of the Observer.
	// A balloon went to sleep if he stand still (he has no movement).
	// This is for saving CPU-Usage.
	// A sleeping balloon perform only minimal checks, e.g. KeepAlive.
	// You must manually wake up the balloon.
	// An active oscillator prevents the balloon from going to sleep.
	public boolean isSleeping();

	// Wakeup a sleeping observer, so the balloon can move again.
	public void wakeup();

	// Check if the Observer covers a given entity.
	public boolean hasEntity(Entity entity);

	// Check if an Observer is cancelled.
	// An Observer is cancelled if the move() detects an error
	// or if a minecraft event listener is cancelling it.
	public boolean isCancelled();

	// Cancel the Observer.
	// This destroys the unterlying Element.
	public void cancel();

	// Move the Observer (the element the observer covers).
	// This function should be called by the Manager on every Minecraft Tick.
	// There is no need to call this function otherwise.
	public String move() throws BalloonException;

}
