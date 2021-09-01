package de.polarwolf.heliumballoon.config;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Slab;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.exception.BalloonException;

public class ConfigElement {
	
	private final String name;
	private Material material = Material.STONE;
	private Vector offset = new Vector (0,0,0);
	private Bisected.Half bisectedHalf = null;
	private Slab.Type slabType = null;
	private BlockFace blockFace = null;
	private String custom = null;
	
	
	public ConfigElement(String name) {
		this.name = name;
	}


	public ConfigElement(ConfigurationSection fileSection) throws BalloonException {
		this.name = fileSection.getName();
		loadConfig(fileSection);
	}

	
	public String getName() {
		return name;
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
	
	
	public Bisected.Half getBisectedHalf() {
		return bisectedHalf;
	}


	protected void setBisectedHalf(Bisected.Half bisectedHalf) {
		this.bisectedHalf = bisectedHalf;
	}


	public Slab.Type getSlabType() {
		return slabType;
	}


	protected void setSlabType(Slab.Type slabType) {
		this.slabType = slabType;
	}


	public BlockFace getBlockFace() {
		return blockFace;
	}


	protected void setBlockFace(BlockFace blockFace) {
		this.blockFace = blockFace;
	}


	public String getCustom() {
		return custom;
	}


	protected void setCustom(String custom) {
		this.custom = custom;
	}


	protected void loadConfig(ConfigurationSection fileSection) throws BalloonException {
		setMaterial(ConfigUtils.getMaterialFromName(getName(), fileSection.getString(ParamElement.MATERIAL.getAttributeName())));
		setBisectedHalf(ConfigUtils.getHalfFromName(getName(), fileSection.getString(ParamElement.HALF.getAttributeName())));
		setSlabType(ConfigUtils.getSlabTypeFromName(getName(), fileSection.getString(ParamElement.SLAB.getAttributeName())));
		setBlockFace(ConfigUtils.getBlockFaceFromName(getName(), fileSection.getString(ParamElement.FACE.getAttributeName())));

		Double x = fileSection.getDouble(ParamElement.X.getAttributeName());
		Double y = fileSection.getDouble(ParamElement.Y.getAttributeName());
		Double z = fileSection.getDouble(ParamElement.Z.getAttributeName());
		setOffset(new Vector(x, y, z));

		setCustom(fileSection.getString(ParamElement.CUSTOM.getAttributeName()));
	}

}
