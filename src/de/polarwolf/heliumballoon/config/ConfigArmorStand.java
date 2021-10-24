package de.polarwolf.heliumballoon.config;

import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.elements.ArmorStandElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumParam;
import de.polarwolf.heliumballoon.helium.HeliumSection;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public class ConfigArmorStand implements ConfigPart {

	private final String name;
	private final String fullName;
	private Vector offset = new Vector (0,0,0);
	private ConfigElement load = null;
	private String custom = null;
	
	
	public ConfigArmorStand(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}


	public ConfigArmorStand(ConfigurationSection fileSection) throws BalloonException {
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

	
	public ConfigElement getLoad() {
		return load;
	}


	@Override
	public boolean isSuitableFor(BalloonPurpose purpose) {
		switch(purpose) {
			case PET: return false;
			case WALL: return false;
			case ROTATOR: return true;
			default: return false;
		
		}
	}

	
	@Override
	public Element createElement(Player player, ConfigRule rule, SpawnModifier spawnModifier) {
		return new ArmorStandElement(rule, this, spawnModifier);
	}

	
	@Override
	public double getMinYOffset() {
		return offset.getY();		
	}

	
	@Override
	public double getMaxYOffset() {
		return offset.getY();		
	}

	
	protected void setLoad(ConfigElement load) {
		this.load = load;
	}


	public Vector getOffset() {
		return offset;
	}


	protected void setOffset(Vector offset) {
		this.offset = offset;
	}
	
		
	public String getCustom() {
		return custom;
	}


	protected void setCustom(String custom) {
		this.custom = custom;
	}
	
	
	protected List<HeliumParam> getValidParams() {
		return  Arrays.asList(ParamArmorStand.values());
	}
	
	
	protected void loadLoadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		ConfigurationSection fileSectionLoad = fileSection.getConfigurationSection(ParamArmorStand.LOAD.getAttributeName());
		setLoad(new ConfigElement (fileSectionLoad));
	}
	
	
	protected void importHeliumSection(HeliumSection heliumSection) throws BalloonException { 
		Double x = heliumSection.getDouble(ParamArmorStand.X, getOffset().getX());
		Double y = heliumSection.getDouble(ParamArmorStand.Y, getOffset().getY());
		Double z = heliumSection.getDouble(ParamArmorStand.Z, getOffset().getZ());
		setOffset(new Vector(x, y, z));
		
		setCustom(heliumSection.getString(ParamArmorStand.CUSTOM));
	}
	
	
	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);

		if (heliumSection.isSection(ParamArmorStand.LOAD)) {
			loadLoadConfigFromFile(fileSection);
		}						
	}
	
}
