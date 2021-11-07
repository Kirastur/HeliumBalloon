package de.polarwolf.heliumballoon.config;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.type.Bell;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.elements.FallingBlockElement;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumSection;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public class ConfigElement implements ConfigPart {

	private final String name;
	private final String fullName;
	private Material material = Material.STONE;
	private Vector offset = new Vector(0, 0, 0);
	private Axis axis = null;
	private Bisected.Half bisectedHalf = null;
	private Bell.Attachment bellAttachment = null;
	private BlockFace blockFace = null;
	private Chest.Type chestType = null;
	private Door.Hinge doorHinge = null;
	private FaceAttachable.AttachedFace attachedFace = null;
	private Slab.Type slabType = null;
	private Stairs.Shape stairsShape = null;
	private boolean open = false;
	private boolean lit = true;
	private boolean signalFire = false;
	private boolean hanging = false;
	private boolean eye = false;
	private String custom = null;

	public ConfigElement(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}

	public ConfigElement(ConfigurationSection fileSection) throws BalloonException {
		this.name = fileSection.getName();
		this.fullName = fileSection.getCurrentPath();
		loadConfigFromFile(fileSection);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getFullName() {
		return fullName;
	}

	@Override
	public boolean isSuitableFor(BalloonPurpose purpose) {
		switch (purpose) {
		case PET:
			return true;
		case WALL:
			return true;
		case ROTATOR:
			return false;
		default:
			return false;
		}
	}

	@Override
	public Element createElement(Player player, ConfigRule rule, SpawnModifier spawnModifier) {
		return new FallingBlockElement(player, rule, this, spawnModifier);
	}

	@Override
	public double getMinYOffset() {
		return offset.getY();
	}

	@Override
	public double getMaxYOffset() {
		return offset.getY();
	}

	public Material getMaterial() {
		return material;
	}

	protected void setMaterial(Material material) {
		this.material = material;
	}

	public Vector getOffset() {
		return offset;
	}

	protected void setOffset(Vector offset) {
		this.offset = offset;
	}

	public Axis getAxis() {
		return axis;
	}

	protected void setAxis(Axis axis) {
		this.axis = axis;
	}

	public Bisected.Half getBisectedHalf() {
		return bisectedHalf;
	}

	protected void setBisectedHalf(Bisected.Half bisectedHalf) {
		this.bisectedHalf = bisectedHalf;
	}

	public Bell.Attachment getBellAttachment() {
		return bellAttachment;
	}

	protected void setBellAttachment(Bell.Attachment bellAttachment) {
		this.bellAttachment = bellAttachment;
	}

	public BlockFace getBlockFace() {
		return blockFace;
	}

	protected void setBlockFace(BlockFace blockFace) {
		this.blockFace = blockFace;
	}

	public Chest.Type getChestType() {
		return chestType;
	}

	protected void setChestType(Chest.Type chestType) {
		this.chestType = chestType;
	}

	public Door.Hinge getDoorHinge() {
		return doorHinge;
	}

	protected void setDoorHinge(Door.Hinge doorHinge) {
		this.doorHinge = doorHinge;
	}

	public FaceAttachable.AttachedFace getAttachedFace() {
		return attachedFace;
	}

	protected void setAttachedFace(FaceAttachable.AttachedFace attachedFace) {
		this.attachedFace = attachedFace;
	}

	public Slab.Type getSlabType() {
		return slabType;
	}

	protected void setSlabType(Slab.Type slabType) {
		this.slabType = slabType;
	}

	public Stairs.Shape getStairsShape() {
		return stairsShape;
	}

	protected void setStairsShape(Stairs.Shape stairsShape) {
		this.stairsShape = stairsShape;
	}

	public boolean isOpen() {
		return open;
	}

	protected void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isLit() {
		return lit;
	}

	protected void setLit(boolean lit) {
		this.lit = lit;
	}

	public boolean isSignalFire() {
		return signalFire;
	}

	protected void setSignalFire(boolean signalFire) {
		this.signalFire = signalFire;
	}

	public boolean isHanging() {
		return hanging;
	}

	protected void setHanging(boolean hanging) {
		this.hanging = hanging;
	}

	public boolean hasEye() {
		return eye;
	}

	public void setEye(boolean eye) {
		this.eye = eye;
	}

	public String getCustom() {
		return custom;
	}

	protected void setCustom(String custom) {
		this.custom = custom;
	}

	protected List<HeliumParam> getValidParams() {
		return Arrays.asList(ParamElement.values());
	}

	protected void importHeliumSection(HeliumSection heliumSection) throws BalloonException {
		setMaterial(
				ConfigUtils.getBlockMaterialFromName(getFullName(), heliumSection.getString(ParamElement.MATERIAL)));
		setAxis(ConfigUtils.getAxisFromName(getFullName(), heliumSection.getString(ParamElement.AXIS)));
		setBisectedHalf(
				ConfigUtils.getHalfFromName(getFullName(), heliumSection.getString(ParamElement.BISECTED_HALF)));
		setBellAttachment(ConfigUtils.getBellAttachmentFromName(getFullName(),
				heliumSection.getString(ParamElement.BELL_ATTACHMENT)));
		setBlockFace(ConfigUtils.getBlockFaceFromName(getFullName(), heliumSection.getString(ParamElement.BLOCK_FACE)));
		setChestType(ConfigUtils.getChestTypeFromName(getFullName(), heliumSection.getString(ParamElement.CHEST_TYPE)));
		setDoorHinge(ConfigUtils.getDoorHingeFromName(getFullName(), heliumSection.getString(ParamElement.DOOR_HINGE)));
		setAttachedFace(ConfigUtils.getAttachedFaceFromName(getFullName(),
				heliumSection.getString(ParamElement.ATTACHED_FACE)));
		setSlabType(ConfigUtils.getSlabTypeFromName(getFullName(), heliumSection.getString(ParamElement.SLAB_TYPE)));
		setStairsShape(
				ConfigUtils.getStairsShapeFromName(getFullName(), heliumSection.getString(ParamElement.STAIRS_SHAPE)));

		setOpen(heliumSection.getBoolean(ParamElement.IS_OPEN, isOpen()));
		setLit(heliumSection.getBoolean(ParamElement.IS_LIT, isLit()));
		setSignalFire(heliumSection.getBoolean(ParamElement.IS_SIGNAL_FIRE, isSignalFire()));
		setHanging(heliumSection.getBoolean(ParamElement.IS_HANGING, isHanging()));
		setEye(heliumSection.getBoolean(ParamElement.HAS_EYE, hasEye()));

		Double x = heliumSection.getDouble(ParamElement.X, getOffset().getX());
		Double y = heliumSection.getDouble(ParamElement.Y, getOffset().getY());
		Double z = heliumSection.getDouble(ParamElement.Z, getOffset().getZ());
		setOffset(new Vector(x, y, z));

		setCustom(heliumSection.getString(ParamElement.CUSTOM));
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);
	}

}
