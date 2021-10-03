package de.polarwolf.heliumballoon.balloons;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumName;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public interface Balloon extends HeliumName {
	
	// Prepare the Balloon by creating the underlying element.
	// This does only create the element, not the entity.
	// The spawn of the entity is done later by move().
	// Please do not call this directly,
	// this is done by the BalloonManager.
	public void prepare(SpawnModifier spawnModifier);
	
	// Get the associated player. Can be null.
	public Player getPlayer();
	
	// Get the world the Balloon is in.
	// A balloon cannot change worlds.
	public World getWorld();
	
	// Get the oscillator for swinging
	public Oscillator getOscillator();
	
	// Get the sleeping state of the Balloon.
	// A balloon went to sleep if he stand still (he has no movement).
	// This is for saving CPU-Usage.
	// A sleeping balloon perform only minimal checks, e.g. KeepAlive.
	// You must manually wake up the balloon.
	// An active oscillator prevents the balloon from going to sleep.
	public boolean isSleeping();

	// Wakeup a sleeping balloon, so he can move again.
	public void wakeup();

	// Check if the Balloon contains a given entity.
	public boolean hasEntity(Entity entity);
	
	// Check if a Balloon is cancelled.
	// A Balloon is cancelled if the move() detects an error
	// or if a minecraft event listener is cancelling it. 
	public boolean isCancelled();

	// Cancel the Balloon.
	// This destroys the unterlying Element.
	public void cancel();
	
	// Move the Balloon.
	// This function should be called by the Manager on every Minecraft Tick.
	// There is no need to call this function otherwise.
	public String move() throws BalloonException;

}
