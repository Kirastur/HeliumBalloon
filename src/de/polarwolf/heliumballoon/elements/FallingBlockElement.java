package de.polarwolf.heliumballoon.elements;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.config.ConfigElement;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.rules.Rule;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public class FallingBlockElement extends SimpleElement {
	
	private final ConfigElement config;
	protected FallingBlock fallingBlock = null;
	

	public FallingBlockElement(Player player, Rule rule, ConfigElement config, SpawnModifier spawnModifier) {
		super(player, rule, spawnModifier);
		this.config = config;
	}
	
	
	protected void modifyBlockDataBisected(BlockData blockData) {
		if ((config.getBisectedHalf() == null) || !(blockData instanceof Bisected)) {
			return;
		}
		Bisected bisected = (Bisected) blockData;
		bisected.setHalf(config.getBisectedHalf());
	}
	
	
	protected void modifyBlockDataSlab(BlockData blockData) {
		if ((config.getSlabType() == null) || !(blockData instanceof Slab)) {
			return;
		}
		Slab slab = (Slab) blockData;
		slab.setType(config.getSlabType());
	}
	
	
	protected void modifyBlockDataDirectional(BlockData blockData) {
		if ((config.getBlockFace() == null) || !(blockData instanceof Directional)) {
			return;
		}
		Directional directional = (Directional) blockData;
		directional.setFacing(config.getBlockFace());			
	}
	
	
	protected BlockData createBaseBlockData() {
		return Bukkit.createBlockData(config.getMaterial());
	}
	
	
	protected void modifyBlockData(BlockData blockData) {
		modifyBlockDataBisected(blockData);
		modifyBlockDataSlab(blockData);
		modifyBlockDataDirectional(blockData);
	}
	
	protected BlockData createBlockData() {
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
