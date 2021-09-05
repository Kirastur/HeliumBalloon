package de.polarwolf.heliumballoon.balloons;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public interface Balloon {
	
	public abstract void prepare(SpawnModifier spawnModifier);
	
	public abstract String getName();
	
	public abstract Location getTargetLocation();
	
	public abstract Player getPlayer();
	
	public abstract World getWorld();
	
	public abstract boolean hasEntity(Entity entity);
	
	public abstract boolean isCancelled();

	public abstract void cancel();
	
	public abstract String move() throws BalloonException;

}
