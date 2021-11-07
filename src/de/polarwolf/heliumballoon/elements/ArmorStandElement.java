package de.polarwolf.heliumballoon.elements;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigArmorStand;
import de.polarwolf.heliumballoon.config.ConfigElement;
import de.polarwolf.heliumballoon.config.ConfigRule;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public class ArmorStandElement extends SimpleElement {

	private final ConfigArmorStand config;
	protected ArmorStand armorStand = null;

	public ArmorStandElement(ConfigRule rule, ConfigArmorStand config, SpawnModifier spawnModifier) {
		super(null, rule, spawnModifier);
		this.config = config;
	}

	public ConfigArmorStand getConfig() {
		return config;
	}

	@Override
	public Vector getOffset() {
		return config.getOffset();
	}

	@Override
	public Entity getEntity() {
		return armorStand;
	}

	@Override
	public int getDelay() {
		return 0;
	}

	protected void spawnBaseEntity(Location targetLocation) {
		Entity entity = targetLocation.getWorld().spawnEntity(targetLocation, EntityType.ARMOR_STAND);

		armorStand = (ArmorStand) entity;
		armorStand.setPersistent(false);
		armorStand.setInvulnerable(true);
		armorStand.setGravity(false);
		armorStand.setSilent(true);
		armorStand.setVelocity(new Vector());

		armorStand.setCanPickupItems(false);
		armorStand.setCollidable(false);

		armorStand.setVisible(false);
		armorStand.setMarker(true);
		armorStand.setArms(false);
		armorStand.setBasePlate(false);
		for (EquipmentSlot myEquipmentSlot : EquipmentSlot.values()) {
			armorStand.addEquipmentLock(myEquipmentSlot, ArmorStand.LockType.REMOVING_OR_CHANGING);
		}

	}

	protected void modifySpawn() {
		// Nothing
	}

	protected void modifyLoad() {
		ConfigElement configLoadElement = config.getLoad();
		if (configLoadElement != null) {
			Material material = configLoadElement.getMaterial();
			ItemStack itemStack = new ItemStack(material, 1);
			EntityEquipment entityEquipment = armorStand.getEquipment();
			entityEquipment.setHelmet(itemStack, true);
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
		if (armorStand == null) {
			return;
		}
		armorStand.remove();
		armorStand = null;
	}

	@Override
	public void setSpin(double spin) {
		EulerAngle eulerAngle = new EulerAngle(0, Math.toRadians(spin), 0);
		armorStand.setHeadPose(eulerAngle);
	}

}
