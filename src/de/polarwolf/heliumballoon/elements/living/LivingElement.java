package de.polarwolf.heliumballoon.elements.living;

import org.bukkit.Location;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Illager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;
import de.polarwolf.heliumballoon.elements.SimpleElement;
import de.polarwolf.heliumballoon.exception.BalloonException;

public class LivingElement extends SimpleElement {

	protected static final double MIN_DISTANCE = 0.5;
	protected static final double MAX_PITCH_PER_STEP = 5;

	private final LivingConfig config;
	protected boolean isAware = true;
	protected LivingEntity livingEntity = null;
	protected double lastPitch = 0;

	protected LivingElement(Player player, ConfigRule rule, LivingConfig config,
			BehaviorDefinition behaviorDefinition) {
		super(player, rule, behaviorDefinition);
		this.config = config;
	}

	public LivingConfig getConfig() {
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
	public int getDelay() {
		return getRule().getLivingDelay();
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

	public void disableAware() {
		isAware = false;
		if (livingEntity instanceof Mob mob) {
			mob.setAware(false);
		}
	}

	protected void modifySpawnMonster() {
		if (livingEntity instanceof Monster) {
			disableAware();
		}
	}

	protected void modifySpawnTamed() {
		if ((livingEntity instanceof Tameable tameable) && getConfig().isTamed()) {
			tameable.setTamed(true);
			if (getPlayer() != null) {
				tameable.setOwner(getPlayer());
			}
		}
	}

	protected void modifySpawnCat() {
		if (livingEntity instanceof Cat cat) {
			if (config.getCollarColor() != null) {
				cat.setCollarColor(config.getCollarColor());
			}
			if (config.getCatType() != null) {
				cat.setCatType(config.getCatType());
			}
		}
	}

	protected void modifySpawnFox() {
		if ((livingEntity instanceof Fox fox) && (config.getFoxType() != null)) {
			fox.setFoxType(config.getFoxType());
		}
	}

	protected void modifySpawnFrog() {
		if ((livingEntity instanceof Frog frog) && (config.getFrogVariant() != null)) {
			frog.setVariant(config.getFrogVariant());
		}
	}

	protected void modifySpawnHorse() {
		if (livingEntity instanceof Horse horse) {
			if (config.getHorseColor() != null) {
				horse.setColor(config.getHorseColor());
			}
			if (config.getHorseStyle() != null) {
				horse.setStyle(config.getHorseStyle());
			}
		}
	}

	protected void modifySpawnLlama() {
		if ((livingEntity instanceof Llama llama) && (config.getLlamaColor() != null)) {
			llama.setColor(config.getLlamaColor());
		}
	}

	protected void modifySpawnMushroomCow() {
		if ((livingEntity instanceof MushroomCow mushroomCow) && (config.getMushroomCowVariant() != null)) {
			mushroomCow.setVariant(config.getMushroomCowVariant());
		}
	}

	protected void modifySpawnPanda() {
		if (livingEntity instanceof Panda panda) {
			if (config.getPandaMainGene() != null) {
				panda.setMainGene(config.getPandaMainGene());
			}
			if (config.getPandaHiddenGene() != null) {
				panda.setHiddenGene(config.getPandaHiddenGene());
			}
		}
	}

	protected void modifySpawnParrot() {
		if ((livingEntity instanceof Parrot parrot) && (config.getParrotVariant() != null)) {
			parrot.setVariant(config.getParrotVariant());
		}
	}

	protected void modifySpawnRabbit() {
		if ((livingEntity instanceof Rabbit rabbit) && (config.getRabbitType() != null)) {
			rabbit.setRabbitType(config.getRabbitType());
		}
	}

	protected void modifySpawnSheep() {
		if ((livingEntity instanceof Sheep sheep) && (config.getSheepColor() != null)) {
			sheep.setColor(config.getSheepColor());
		}
	}

	protected void modifySpawnTropicalFish() {
		if (livingEntity instanceof TropicalFish tropicalFish) {
			if (config.getTropicalFishBodyColor() != null) {
				tropicalFish.setBodyColor(config.getTropicalFishBodyColor());
			}
			if (config.getTropicalFishPatternColor() != null) {
				tropicalFish.setPatternColor(config.getTropicalFishPatternColor());
			}
			if (config.getTropicalFishPattern() != null) {
				tropicalFish.setPattern(config.getTropicalFishPattern());
			}
		}
	}

	protected void modifySpawnWolf() {
		if ((livingEntity instanceof Wolf wolf) && (config.getCollarColor() != null)) {
			wolf.setCollarColor(config.getCollarColor());
		}
	}

	protected void modifySpawnVillager() {
		if (livingEntity instanceof Villager villager) {
			if (config.getVillagerType() != null) {
				villager.setVillagerType(config.getVillagerType());
			}
			if (config.getVillagerProfession() != null) {
				villager.setProfession(config.getVillagerProfession());
			}
			if ((config.getVillagerLevel() >= 1) && (config.getVillagerLevel() <= 5)) {
				villager.setVillagerLevel(config.getVillagerLevel());
			}
		}
	}

	protected void spawnBaseEntity(Location targetLocation) throws BalloonException {
		Entity entity = targetLocation.getWorld().spawnEntity(targetLocation, config.getEntityType());
		if (!(entity instanceof LivingEntity)) {
			entity.remove();
			throw new BalloonException(config.getName(), "Wrong entity type", config.getEntityType().toString());
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
		if (config.hasLeash() && !(entity instanceof Villager)) {
			livingEntity.setLeashHolder(getPlayer());
		}
		livingEntity.setVelocity(new Vector());
	}

	protected void modifySpawn() {
		modifySpawnMonster();
		modifySpawnTamed();
		modifySpawnCat();
		modifySpawnFox();
		modifySpawnFrog();
		modifySpawnHorse();
		modifySpawnLlama();
		modifySpawnMushroomCow();
		modifySpawnPanda();
		modifySpawnParrot();
		modifySpawnRabbit();
		modifySpawnSheep();
		modifySpawnTropicalFish();
		modifySpawnWolf();
		modifySpawnVillager();
	}

	@Override
	protected void spawn(Location targetLocation) throws BalloonException {
		spawnBaseEntity(targetLocation);
		modifySpawn();
		if (getBehaviorDefinition() != null) {
			getBehaviorDefinition().modifyElement(this);
		}
	}

	@Override
	public void hide() {
		if (livingEntity == null) {
			return;
		}
		livingEntity.remove();
		livingEntity = null;
	}

	protected boolean isPitchableEntity() {
		return (livingEntity instanceof Illager);
	}

	protected void adjustMonsterDirection() {
		if ((getPlayer() == null) || isAware) {
			return;
		}
		Vector entityVector = livingEntity.getEyeLocation().toVector();
		Vector playerVector = getPlayer().getEyeLocation().toVector();

		double high = entityVector.getY() - playerVector.getY();
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

			double pitch = 0.0;
			if (isPitchableEntity()) {
				high = high + getRule().getAdjustIllagerY();
				pitch = Math.atan(Math.abs(high) / distance);
				pitch = Math.toDegrees(pitch);
				pitch = pitch * Math.signum(high);
			}

			if ((pitch - lastPitch) > MAX_PITCH_PER_STEP) {
				pitch = lastPitch + MAX_PITCH_PER_STEP;
			}
			if ((pitch - lastPitch) < -MAX_PITCH_PER_STEP) {
				pitch = lastPitch - MAX_PITCH_PER_STEP;
			}
			lastPitch = pitch;

			livingEntity.setRotation((float) yaw, (float) pitch);
		}
	}

	@Override
	public void setVelocity(Vector newVelocity) {
		super.setVelocity(newVelocity);
		adjustMonsterDirection();
	}

}
