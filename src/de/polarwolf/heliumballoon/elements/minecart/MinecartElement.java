package de.polarwolf.heliumballoon.elements.minecart;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;
import de.polarwolf.heliumballoon.elements.SimpleElement;
import de.polarwolf.heliumballoon.elements.blocks.BlocksPartConfig;
import de.polarwolf.heliumballoon.elements.blocks.BlocksPartElement;
import de.polarwolf.heliumballoon.exception.BalloonException;

public class MinecartElement extends SimpleElement {

	protected static final double MIN_DISTANCE = 0.5;
	protected static final double MAX_SPEED = 10.0;

	private final MinecartConfig config;
	protected Minecart minecart = null;

	protected MinecartElement(Player player, ConfigRule rule, MinecartConfig config,
			BehaviorDefinition behaviorDefinition) {
		super(player, rule, behaviorDefinition);
		this.config = config;
	}

	public MinecartConfig getConfig() {
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
		BlocksPartConfig configLoadElement = config.getLoad();
		if (configLoadElement != null) {
			BlocksPartElement myElement = (BlocksPartElement) configLoadElement.createElement(null, getRule(),
					getBehaviorDefinition());
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
		if (getBehaviorDefinition() != null) {
			getBehaviorDefinition().modifyElement(this);
		}
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
	public void setSpin(EulerAngle spin) {
		double yaw = Math.toDegrees(spin.getY());
		double pitch = Math.toDegrees(spin.getZ());
		minecart.setRotation((float) yaw, (float) pitch);
	}

}
