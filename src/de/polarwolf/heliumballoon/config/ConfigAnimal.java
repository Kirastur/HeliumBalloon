package de.polarwolf.heliumballoon.config;

import java.util.Arrays;

import org.bukkit.DyeColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumSection;

public class ConfigAnimal {

	private final String name;
	private EntityType entityType = EntityType.CAT;
	private Vector offset = new Vector (0,0,0);
	private boolean hidden = false;
	private boolean leash = false;
	private DyeColor color = null;
	private Cat.Type catType = null;
	private String custom = null;
	
	
	public ConfigAnimal(String name) {
		this.name = name;
	}


	public ConfigAnimal(ConfigurationSection fileSection) throws BalloonException {
		this.name = fileSection.getName();
		loadConfig(fileSection);
	}

	
	public String getName() {
		return name;
	}

	
	public EntityType getEntityType() {
		return entityType;
	}


	protected void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}


	public Vector getOffset() {
		return offset;
	}


	protected void setOffset(Vector offset) {
		this.offset = offset;
	}
	
		
	public boolean isHidden() {
		return hidden;
	}


	protected void setHidden(boolean hidden) {
		this.hidden = hidden;
	}


	public boolean hasLeash() {
		return leash;
	}


	protected void setLeash(boolean leash) {
		this.leash = leash;
	}


	public DyeColor getColor() {
		return color;
	}


	protected void setColor(DyeColor color) {
		this.color = color;
	}


	public Cat.Type getCatType() {
		return catType;
	}


	protected void setCatType(Cat.Type catType) {
		this.catType = catType;
	}


	public String getCustom() {
		return custom;
	}


	protected void setCustom(String custom) {
		this.custom = custom;
	}


	protected void loadConfig(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, Arrays.asList(ParamAnimal.values()));

		String livingEntityName = heliumSection.getString(ParamAnimal.TYPE);
		setEntityType(ConfigUtils.getLivingEntityTypeFromName(getName(), livingEntityName));
		
		setHidden(heliumSection.getBoolean(ParamAnimal.HIDDEN, isHidden()));
		setLeash(heliumSection.getBoolean(ParamAnimal.LEASH, hasLeash()));
		
		setColor(ConfigUtils.getDyeColorFromName(getName(), heliumSection.getString(ParamAnimal.COLOR)));
		setCatType(ConfigUtils.getCatTypeFromName(getName(), heliumSection.getString(ParamAnimal.CATTYPE)));

		Double x = heliumSection.getDouble(ParamAnimal.X, getOffset().getX());
		Double y = heliumSection.getDouble(ParamAnimal.Y, getOffset().getY());
		Double z = heliumSection.getDouble(ParamAnimal.Z, getOffset().getZ());
		setOffset(new Vector(x, y, z));
		
		setCustom(heliumSection.getString(ParamAnimal.CUSTOM));
	}

}
