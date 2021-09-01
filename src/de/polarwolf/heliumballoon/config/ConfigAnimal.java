package de.polarwolf.heliumballoon.config;

import org.bukkit.DyeColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.exception.BalloonException;

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
		String livingEntityName = fileSection.getString(ParamAnimal.TYPE.getAttributeName());
		setEntityType(ConfigUtils.getLivingEntityTypeFromName(getName(), livingEntityName));
		
		setHidden(fileSection.getBoolean(ParamAnimal.HIDDEN.getAttributeName(), isHidden()));
		setLeash(fileSection.getBoolean(ParamAnimal.LEASH.getAttributeName(), hasLeash()));
		
		setColor(ConfigUtils.getDyeColorFromName(getName(), fileSection.getString(ParamAnimal.COLOR.getAttributeName())));
		setCatType(ConfigUtils.getCatTypeFromName(getName(), fileSection.getString(ParamAnimal.CATTYPE.getAttributeName())));

		Double x = fileSection.getDouble(ParamAnimal.X.getAttributeName());
		Double y = fileSection.getDouble(ParamAnimal.Y.getAttributeName());
		Double z = fileSection.getDouble(ParamAnimal.Z.getAttributeName());
		setOffset(new Vector(x, y, z));
		
		setCustom(fileSection.getString(ParamAnimal.CUSTOM.getAttributeName()));
	}

}
