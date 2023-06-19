package de.polarwolf.heliumballoon.elements.blocks;

import java.util.ArrayList;
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

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.config.ConfigUtils;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.elements.ElementDefinition;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumSection;

public class BlocksPartConfig implements ConfigElement {

	private final String name;
	private final String fullName;
	private final ElementDefinition elementDefinition;
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

	public BlocksPartConfig(String name, String fullName, ElementDefinition elementDefinition) {
		this.name = name;
		this.fullName = fullName;
		this.elementDefinition = elementDefinition;
	}

	public BlocksPartConfig(ElementDefinition elementDefinition, ConfigurationSection fileSection)
			throws BalloonException {
		this.name = fileSection.getName();
		this.fullName = fileSection.getCurrentPath();
		this.elementDefinition = elementDefinition;
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
	public ElementDefinition getElementDefinition() {
		return elementDefinition;
	}

	@Override
	public Element createElement(Player player, ConfigRule rule, BehaviorDefinition behaviorDefinition) {
		return new BlocksPartElement(player, rule, this, behaviorDefinition);
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
		return Arrays.asList(BlocksPartParam.values());
	}

	protected void importHeliumSection(HeliumSection heliumSection) throws BalloonException {
		setMaterial(
				ConfigUtils.getBlockMaterialFromName(getFullName(), heliumSection.getString(BlocksPartParam.MATERIAL)));
		setAxis(ConfigUtils.getAxisFromName(getFullName(), heliumSection.getString(BlocksPartParam.AXIS)));
		setBisectedHalf(
				ConfigUtils.getHalfFromName(getFullName(), heliumSection.getString(BlocksPartParam.BISECTED_HALF)));
		setBellAttachment(ConfigUtils.getBellAttachmentFromName(getFullName(),
				heliumSection.getString(BlocksPartParam.BELL_ATTACHMENT)));
		setBlockFace(
				ConfigUtils.getBlockFaceFromName(getFullName(), heliumSection.getString(BlocksPartParam.BLOCK_FACE)));
		setChestType(
				ConfigUtils.getChestTypeFromName(getFullName(), heliumSection.getString(BlocksPartParam.CHEST_TYPE)));
		setDoorHinge(
				ConfigUtils.getDoorHingeFromName(getFullName(), heliumSection.getString(BlocksPartParam.DOOR_HINGE)));
		setAttachedFace(ConfigUtils.getAttachedFaceFromName(getFullName(),
				heliumSection.getString(BlocksPartParam.ATTACHED_FACE)));
		setSlabType(ConfigUtils.getSlabTypeFromName(getFullName(), heliumSection.getString(BlocksPartParam.SLAB_TYPE)));
		setStairsShape(ConfigUtils.getStairsShapeFromName(getFullName(),
				heliumSection.getString(BlocksPartParam.STAIRS_SHAPE)));

		setOpen(heliumSection.getBoolean(BlocksPartParam.IS_OPEN, isOpen()));
		setLit(heliumSection.getBoolean(BlocksPartParam.IS_LIT, isLit()));
		setSignalFire(heliumSection.getBoolean(BlocksPartParam.IS_SIGNAL_FIRE, isSignalFire()));
		setHanging(heliumSection.getBoolean(BlocksPartParam.IS_HANGING, isHanging()));
		setEye(heliumSection.getBoolean(BlocksPartParam.HAS_EYE, hasEye()));

		Double x = heliumSection.getDouble(BlocksPartParam.X, getOffset().getX());
		Double y = heliumSection.getDouble(BlocksPartParam.Y, getOffset().getY());
		Double z = heliumSection.getDouble(BlocksPartParam.Z, getOffset().getZ());
		setOffset(new Vector(x, y, z));

		setCustom(heliumSection.getString(BlocksPartParam.CUSTOM));
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);
	}

	protected List<String> paramListAsDump() { // NOSONAR
		String fs = "%s: \"%s\"";
		String fb = "%s: %s";
		List<String> newParamListDump = new ArrayList<>();
		newParamListDump.add(String.format(fs, BlocksPartParam.MATERIAL.getAttributeName(), material.toString()));
		if (axis != null)
			newParamListDump.add(String.format(fs, BlocksPartParam.AXIS.getAttributeName(), axis.toString()));
		if (bisectedHalf != null)
			newParamListDump
					.add(String.format(fs, BlocksPartParam.BISECTED_HALF.getAttributeName(), bisectedHalf.toString()));
		if (bellAttachment != null)
			newParamListDump.add(
					String.format(fs, BlocksPartParam.BELL_ATTACHMENT.getAttributeName(), bellAttachment.toString()));
		if (blockFace != null)
			newParamListDump
					.add(String.format(fs, BlocksPartParam.BLOCK_FACE.getAttributeName(), blockFace.toString()));
		if (chestType != null)
			newParamListDump
					.add(String.format(fs, BlocksPartParam.CHEST_TYPE.getAttributeName(), chestType.toString()));
		if (doorHinge != null)
			newParamListDump
					.add(String.format(fs, BlocksPartParam.DOOR_HINGE.getAttributeName(), doorHinge.toString()));
		if (attachedFace != null)
			newParamListDump
					.add(String.format(fs, BlocksPartParam.ATTACHED_FACE.getAttributeName(), attachedFace.toString()));
		if (slabType != null)
			newParamListDump.add(String.format(fs, BlocksPartParam.SLAB_TYPE.getAttributeName(), slabType.toString()));
		if (stairsShape != null)
			newParamListDump
					.add(String.format(fs, BlocksPartParam.STAIRS_SHAPE.getAttributeName(), stairsShape.toString()));
		if (open)
			newParamListDump.add(String.format(fb, BlocksPartParam.IS_OPEN.getAttributeName(), Boolean.toString(true)));
		if (!lit)
			newParamListDump.add(String.format(fb, BlocksPartParam.IS_LIT.getAttributeName(), Boolean.toString(false)));
		if (signalFire)
			newParamListDump
					.add(String.format(fb, BlocksPartParam.IS_SIGNAL_FIRE.getAttributeName(), Boolean.toString(true)));
		if (hanging)
			newParamListDump
					.add(String.format(fb, BlocksPartParam.IS_HANGING.getAttributeName(), Boolean.toString(true)));
		if (eye)
			newParamListDump.add(String.format(fb, BlocksPartParam.HAS_EYE.getAttributeName(), Boolean.toString(true)));
		if (offset.getX() != 0)
			newParamListDump
					.add(String.format(fb, BlocksPartParam.X.getAttributeName(), Double.toString(offset.getX())));
		if (offset.getY() != 0)
			newParamListDump
					.add(String.format(fb, BlocksPartParam.Y.getAttributeName(), Double.toString(offset.getY())));
		if (offset.getZ() != 0)
			newParamListDump
					.add(String.format(fb, BlocksPartParam.Z.getAttributeName(), Double.toString(offset.getZ())));
		if ((custom != null) && !custom.isEmpty())
			newParamListDump.add(String.format(fs, BlocksPartParam.CUSTOM.getAttributeName(), custom));
		return newParamListDump;
	}

	@Override
	public String toString() {
		return String.format("%s: { %s }", getName(), String.join(", ", paramListAsDump()));
	}

}
