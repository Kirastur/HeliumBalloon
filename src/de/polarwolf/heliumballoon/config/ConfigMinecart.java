package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.elements.MinecartElement;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumSection;

public class ConfigMinecart implements ConfigPart {

	public static final int DEFAULT_LOAD_OFFSET = 6;

	private final String name;
	private final String fullName;
	private Vector offset = new Vector(0, 0, 0);
	private ConfigElement load = null;
	private int loadOffset = DEFAULT_LOAD_OFFSET;
	private String custom = null;

	public ConfigMinecart(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}

	public ConfigMinecart(ConfigurationSection fileSection) throws BalloonException {
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
		switch (purpose) {
		case PET:
			return true;
		case WALL:
			return false;
		case ROTATOR:
			return true;
		default:
			return false;
		}
	}

	@Override
	public Element createElement(Player player, ConfigRule rule, SpawnModifier spawnModifier) {
		return new MinecartElement(player, rule, this, spawnModifier);
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

	public int getLoadOffset() {
		return loadOffset;
	}

	protected void setLoadOffset(int loadOffset) {
		this.loadOffset = loadOffset;
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
		return Arrays.asList(ParamMinecart.values());
	}

	protected void loadLoadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		ConfigurationSection fileSectionLoad = fileSection
				.getConfigurationSection(ParamMinecart.LOAD.getAttributeName());
		setLoad(new ConfigElement(fileSectionLoad));
	}

	protected void importHeliumSection(HeliumSection heliumSection) throws BalloonException {
		setLoadOffset(heliumSection.getInt(ParamMinecart.LOAD_OFFSET, getLoadOffset()));

		Double x = heliumSection.getDouble(ParamMinecart.X, getOffset().getX());
		Double y = heliumSection.getDouble(ParamMinecart.Y, getOffset().getY());
		Double z = heliumSection.getDouble(ParamMinecart.Z, getOffset().getZ());
		setOffset(new Vector(x, y, z));

		setCustom(heliumSection.getString(ParamMinecart.CUSTOM));
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);

		if (heliumSection.isSection(ParamMinecart.LOAD)) {
			loadLoadConfigFromFile(fileSection);
		}
	}

	protected List<String> paramListAsDump() {
		String fs = "%s: \"%s\"";
		String fb = "%s: %s";
		List<String> newParamListDump = new ArrayList<>();
		if (load != null)
			newParamListDump.add(load.toString());
		if (loadOffset != DEFAULT_LOAD_OFFSET)
			newParamListDump
					.add(String.format(fb, ParamMinecart.LOAD_OFFSET.getAttributeName(), Integer.toString(loadOffset)));
		if (offset.getX() != 0)
			newParamListDump.add(String.format(fb, ParamMinecart.X.getAttributeName(), Double.toString(offset.getX())));
		if (offset.getY() != 0)
			newParamListDump.add(String.format(fb, ParamMinecart.Y.getAttributeName(), Double.toString(offset.getY())));
		if (offset.getZ() != 0)
			newParamListDump.add(String.format(fb, ParamMinecart.Z.getAttributeName(), Double.toString(offset.getZ())));
		if ((custom != null) && !custom.isEmpty())
			newParamListDump.add(String.format(fs, ParamMinecart.CUSTOM.getAttributeName(), custom));
		return newParamListDump;
	}

	@Override
	public String toString() {
		return String.format("%s: { %s }", getName(), String.join(", ", paramListAsDump()));
	}

}
