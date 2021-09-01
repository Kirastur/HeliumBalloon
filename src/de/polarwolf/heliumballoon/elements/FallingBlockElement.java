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
import de.polarwolf.heliumballoon.spawnmodifiers.HeliumModifier;
import de.polarwolf.heliumballoon.system.Rule;

public class FallingBlockElement extends SimpleElement {
	
	private final ConfigElement config;
	protected FallingBlock fallingBlock = null;
	

	public FallingBlockElement(Player player, Rule rule, ConfigElement config, HeliumModifier heliumModifier) {
		super(player, rule, heliumModifier);
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
	
	
	protected BlockData createBlockData() {
		BlockData blockData = Bukkit.createBlockData(config.getMaterial());
		modifyBlockDataBisected(blockData);
		modifyBlockDataSlab(blockData);
		modifyBlockDataDirectional(blockData);
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
	protected void spawn(Location targetLocation) throws BalloonException {
		fallingBlock = targetLocation.getWorld().spawnFallingBlock(targetLocation, createBlockData());
		fallingBlock.setPersistent(false);
		fallingBlock.setHurtEntities(false);
		fallingBlock.setGravity(false);
		fallingBlock.setDropItem(false);
		fallingBlock.setSilent(true);
		fallingBlock.setVelocity(new Vector());
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
