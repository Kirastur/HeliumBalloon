package de.polarwolf.heliumballoon.elements;

import org.bukkit.Location;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigAnimal;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.rules.Rule;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public class AnimalElement extends SimpleElement {

	private final ConfigAnimal config;
	protected LivingEntity livingEntity = null;
	

	public AnimalElement(Player player, Rule rule, ConfigAnimal config, SpawnModifier spawnModifier) {
		super(player, rule, spawnModifier);
		this.config = config;
	}
	
	
	public ConfigAnimal getConfig() {
		return config;
	}


	@Override
	public Vector getOffset() {
		return config.getOffset();
	}

	
	@Override
	public Entity getEntity() {
		return livingEntity;
	}

	
	@Override
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}
		
		if (config.hasLeash()) {
			return (livingEntity.isLeashed() && livingEntity.getLeashHolder().equals(getPlayer()));
		} else {
			return true;
		}
	}
	
	
	protected void modifySpawnMonster() {
		if (!(livingEntity instanceof Monster)) {
			return;
		}
		Monster monster = (Monster) livingEntity;
		monster.setAware(false);
	}


	protected void modifySpawnCat() {
		if (!(livingEntity instanceof Cat)) {
			return;
		}
		Cat cat = (Cat) livingEntity;
		if (config.getColor() != null) {
			cat.setCollarColor(config.getColor());
		}
		if (config.getCatType() != null) {
			cat.setCatType(config.getCatType());
		}
	}


	@Override
	protected void spawn(Location targetLocation) throws BalloonException {
		Entity entity = targetLocation.getWorld().spawnEntity(targetLocation, config.getEntityType());
		if (!(entity instanceof LivingEntity)) {
			entity.remove();
			throw new BalloonException (config.getName(), "Wrong entity type", config.getEntityType().toString());
		}
		livingEntity = (LivingEntity) entity;
		
		livingEntity.setPersistent(false);
		livingEntity.setCanPickupItems(false);
		livingEntity.setCollidable(false);
		livingEntity.setInvulnerable(true);
		livingEntity.setGravity(false);
		livingEntity.setSilent(true);
		livingEntity.setRemoveWhenFarAway(false);
		livingEntity.setInvisible(config.isHidden());
		if (config.hasLeash()) {
			livingEntity.setLeashHolder(getPlayer());
		}
		livingEntity.setVelocity(new Vector());
		
		modifySpawnMonster();
		modifySpawnCat();
		
	}


	@Override
	public void hide() {
		if (livingEntity == null) {
			return;
		}
		livingEntity.remove();
		livingEntity = null;
	}
	
	
	protected void adjustMonsterDirection() {
		if ((getPlayer() == null) || !(livingEntity instanceof Monster)) {
			return;
		}
		Monster monster = (Monster) livingEntity;
		Vector mv = monster.getLocation().toVector();
		Vector pv = getPlayer().getLocation().toVector();
		mv.setY(0);
		pv.setY(0);
		pv.subtract(mv);
		if (pv.length() > 0.1) {
			pv.normalize();
			double angle = Math.asin(pv.getZ());
			angle = Math.toDegrees(angle);
			if (pv.getX() < 0) {
				angle = 180 - angle;
			}
			angle = angle + 270.0;
			if (angle >= 360.0) {
				angle = angle - 360.0;
			}
			monster.setRotation((float) angle, 0);
		}
	}
	
	
	@Override
	public void setVelocity(Vector newVelocity) {
		super.setVelocity(newVelocity);
		adjustMonsterDirection();
	}
		
}
