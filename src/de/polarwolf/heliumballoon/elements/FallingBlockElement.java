package de.polarwolf.heliumballoon.elements;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.Bell;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigElement;
import de.polarwolf.heliumballoon.config.ConfigRule;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public class FallingBlockElement extends SimpleElement {
	
	private final ConfigElement config;
	protected FallingBlock fallingBlock = null;
	

	public FallingBlockElement(Player player, ConfigRule rule, ConfigElement config, SpawnModifier spawnModifier) {
		super(player, rule, spawnModifier);
		this.config = config;
	}
	
	
	protected void modifyBlockDataAxis(BlockData blockData) {
		if ((config.getAxis() == null) || !(blockData instanceof Orientable)) {
			return;
		}
		Orientable orientable = (Orientable) blockData;
		orientable.setAxis(config.getAxis());
	}
	
	
	protected void modifyBlockDataBisected(BlockData blockData) {
		if ((config.getBisectedHalf() == null) || !(blockData instanceof Bisected)) {
			return;
		}
		Bisected bisected = (Bisected) blockData;
		bisected.setHalf(config.getBisectedHalf());
	}
	
	
	protected void modifyBlockDataBell(BlockData blockData) {
		if ((config.getBellAttachment() == null) || !(blockData instanceof Bell)) {
			return;
		}
		Bell bell = (Bell) blockData;
		bell.setAttachment(config.getBellAttachment());
	}

	
	protected void modifyBlockDataDirectional(BlockData blockData) {
		if ((config.getBlockFace() == null) || !(blockData instanceof Directional)) {
			return;
		}
		Directional directional = (Directional) blockData;
		directional.setFacing(config.getBlockFace());			
	}
	
	
	protected void modifyBlockDataChest(BlockData blockData) {
		if ((config.getChestType() == null) || !(blockData instanceof Chest)) {
			return;
		}
		Chest chest = (Chest) blockData;
		chest.setType(config.getChestType());
	}

	
	protected void modifyBlockDataDoor(BlockData blockData) {
		if ((config.getDoorHinge() == null) || !(blockData instanceof Door)) {
			return;
		}
		Door door = (Door) blockData;
		door.setHinge(config.getDoorHinge());
	}

	
	protected void modifyBlockDataFaceAttachable(BlockData blockData) {
		if ((config.getAttachedFace() == null) || !(blockData instanceof FaceAttachable)) {
			return;
		}
		FaceAttachable faceAttachable = (FaceAttachable) blockData;
		faceAttachable.setAttachedFace(config.getAttachedFace());
	}

	
	protected void modifyBlockDataSlab(BlockData blockData) {
		if ((config.getSlabType() == null) || !(blockData instanceof Slab)) {
			return;
		}
		Slab slab = (Slab) blockData;
		slab.setType(config.getSlabType());
	}
	
	
	protected void modifyBlockDataStairs(BlockData blockData) {
		if ((config.getStairsShape() == null) || !(blockData instanceof Stairs)) {
			return;
		}
		Stairs stairs = (Stairs) blockData;
		stairs.setShape(config.getStairsShape());
	}

	
	protected void modifyBlockDataOpenable(BlockData blockData) {
		if (!(blockData instanceof Openable)) {
			return;
		}
		Openable openable = (Openable) blockData;
		openable.setOpen(config.isOpen());
	}

	
	protected void modifyBlockDataLightable(BlockData blockData) {
		if (!(blockData instanceof Lightable)) {
			return;
		}
		Lightable lightable = (Lightable) blockData;
		lightable.setLit(config.isLit());
	}

	
	protected void modifyBlockDataCampfire(BlockData blockData) {
		if (!(blockData instanceof Campfire)) {
			return;
		}
		Campfire campfire = (Campfire) blockData;
		campfire.setSignalFire(config.isSignalFire());
	}

	
	protected void modifyBlockDataLantern(BlockData blockData) {
		if (!(blockData instanceof Lantern)) {
			return;
		}
		Lantern lantern = (Lantern) blockData;
		lantern.setHanging(config.isHanging());
	}

	
	protected void modifyBlockDataEndPortalFrame(BlockData blockData) {
		if (!(blockData instanceof EndPortalFrame)) {
			return;
		}
		EndPortalFrame endPortalFrame = (EndPortalFrame) blockData;
		endPortalFrame.setEye(config.hasEye());
	}

	
	protected BlockData createBaseBlockData() {
		return Bukkit.createBlockData(config.getMaterial());
	}
	
	
	protected void modifyBlockData(BlockData blockData) {
		modifyBlockDataAxis(blockData);
		modifyBlockDataBisected(blockData);
		modifyBlockDataBell(blockData);
		modifyBlockDataDirectional(blockData);
		modifyBlockDataChest(blockData);
		modifyBlockDataDoor(blockData);
		modifyBlockDataFaceAttachable(blockData);
		modifyBlockDataSlab(blockData);
		modifyBlockDataStairs(blockData);
		modifyBlockDataOpenable(blockData);
		modifyBlockDataLightable(blockData);
		modifyBlockDataCampfire(blockData);
		modifyBlockDataLantern(blockData);
		modifyBlockDataEndPortalFrame(blockData);
	}
	

	public BlockData createBlockData() {
		BlockData blockData = createBaseBlockData();
		modifyBlockData(blockData);
		spawnModifier.modifyBlockData(this, blockData);
		return blockData;	
	}
	
	
	public ConfigElement getConfig() {
		return config;
	}


	@Override
	public Vector getOffset() {
		return config.getOffset();
	}
	
	
	@Override
	public Entity getEntity() {
		return fallingBlock;
	}
	
	
	@Override
	public boolean needDelay() {
		return true;
	}


	protected void spawnBaseFallingBlock(Location targetLocation) {
		fallingBlock = targetLocation.getWorld().spawnFallingBlock(targetLocation, createBlockData());
		fallingBlock.setPersistent(false);
		fallingBlock.setHurtEntities(false);
		fallingBlock.setGravity(false);
		fallingBlock.setDropItem(false);
		fallingBlock.setSilent(true);
		fallingBlock.setVelocity(new Vector());
	}
	
	
	protected void modifySpawn() {
		// Nothing to to
	}

	
	@Override
	protected void spawn(Location targetLocation) throws BalloonException {
		spawnBaseFallingBlock(targetLocation);
		modifySpawn();		
		spawnModifier.modifyEntity(this);
	}
	
	
	@Override
	public void hide() {
		if (fallingBlock == null) {
			return;
		}
		fallingBlock.remove();
		fallingBlock = null;
	}

}
