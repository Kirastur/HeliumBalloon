package de.polarwolf.heliumballoon.elements;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigElement;
import de.polarwolf.heliumballoon.config.ConfigMinecart;
import de.polarwolf.heliumballoon.config.ConfigRule;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public class MinecartElement extends SimpleElement {

	protected static final double MIN_DISTANCE = 0.5;
	protected static final double MAX_SPEED = 10.0;
	
	private final ConfigMinecart config;
	protected Minecart minecart = null;
	

	public MinecartElement(Player player, ConfigRule rule, ConfigMinecart config, SpawnModifier spawnModifier) {
		super(player, rule, spawnModifier);
		this.config = config;
	}
	
	
	public ConfigMinecart getConfig() {
		return config;
	}


	@Override
	public Vector getOffset() {
		return config.getOffset();
	}

	
	@Override
	public Entity getEntity() {
		return minecart;
	}

	
	@Override
	public int getDelay() {
		return getRule().getMinecartDelay();
	}


	protected void spawnBaseEntity(Location targetLocation) {
		Entity entity = targetLocation.getWorld().spawnEntity(targetLocation, EntityType.MINECART);
		
		minecart = (Minecart) entity;		
		minecart.setPersistent(false);
		minecart.setInvulnerable(true);
		minecart.setGravity(false);
		minecart.setSilent(true);
		minecart.setDerailedVelocityMod(new Vector());
		minecart.setFlyingVelocityMod(new Vector());
		minecart.setSlowWhenEmpty(false);
		minecart.setMaxSpeed(MAX_SPEED);
		minecart.setVelocity(new Vector());
	}
	
	
	protected void modifySpawn() {
		// Nothing
	}


	protected void modifyLoad() {
		ConfigElement configLoadElement = config.getLoad();
		if (configLoadElement != null) {
			FallingBlockElement myElement = (FallingBlockElement)configLoadElement.createElement(null, getRule(), spawnModifier);
			BlockData myBlockData = myElement.createBlockData();
			minecart.setDisplayBlockData(myBlockData);
			minecart.setDisplayBlockOffset(config.getLoadOffset());
		}
	}
	
	
	@Override
	protected void spawn(Location targetLocation) throws BalloonException {
		spawnBaseEntity(targetLocation);
		modifySpawn();
		modifyLoad();
		spawnModifier.modifyEntity(this);
	}


	@Override
	public void hide() {
		if (minecart == null) {
			return;
		}
		minecart.remove();
		minecart = null;
	}
	
	
	protected void adjustMinecartDirection() {
		if (getPlayer() == null) {
			return;
		}
		Vector entityVector = minecart.getLocation().toVector();
		Vector playerVector = getPlayer().getLocation().toVector();
		
		entityVector.setY(0);
		playerVector.setY(0);
		Vector distanceVector = playerVector.subtract(entityVector);
		double distance = distanceVector.length();
		if (distance > MIN_DISTANCE) {
			distanceVector.normalize();
			double yaw = -Math.asin(distanceVector.getX());
			yaw = Math.toDegrees(yaw);
			if (distanceVector.getZ() < 0.0) {
				yaw = 180.0 - yaw;
			}
			yaw = yaw + 90;
			if (yaw >= 360) {
				yaw = yaw - 360;
			}

			minecart.setRotation((float) yaw, 0);
		}
	}
	
	
	@Override
	public void setVelocity(Vector newVelocity) {
		super.setVelocity(newVelocity);
		adjustMinecartDirection();
	}


	@Override
	public void setSpin (double spin) {
		minecart.setRotation((float)spin, 0);
	}
	
}
