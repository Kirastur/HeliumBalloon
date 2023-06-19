package de.polarwolf.heliumballoon.elements.minecart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.elements.ElementDefinition;
import de.polarwolf.heliumballoon.elements.blocks.BlocksPartConfig;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumSection;

public class MinecartConfig implements ConfigElement {

	public static final int DEFAULT_LOAD_OFFSET = 6;

	private final String name;
	private final String fullName;
	private final ElementDefinition elementDefinition;
	private Vector offset = new Vector(0, 0, 0);
	private BlocksPartConfig load = null;
	private int loadOffset = DEFAULT_LOAD_OFFSET;
	private String custom = null;

	public MinecartConfig(String name, String fullName, ElementDefinition elementDefinition) {
		this.name = name;
		this.fullName = fullName;
		this.elementDefinition = elementDefinition;
	}

	public MinecartConfig(ElementDefinition elementDefinition, ConfigurationSection fileSection)
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

	public BlocksPartConfig getLoad() {
		return load;
	}

	@Override
	public Element createElement(Player player, ConfigRule rule, BehaviorDefinition behaviorDefinition) {
		return new MinecartElement(player, rule, this, behaviorDefinition);
	}

	@Override
	public double getMinYOffset() {
		return offset.getY();
	}

	@Override
	public double getMaxYOffset() {
		return offset.getY();
	}

	protected void setLoad(BlocksPartConfig load) {
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
		return Arrays.asList(MinecartParam.values());
	}

	protected void loadLoadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		ConfigurationSection fileSectionLoad = fileSection
				.getConfigurationSection(MinecartParam.LOAD.getAttributeName());
		setLoad(new BlocksPartConfig(null, fileSectionLoad));
	}

	protected void importHeliumSection(HeliumSection heliumSection) throws BalloonException {
		setLoadOffset(heliumSection.getInt(MinecartParam.LOAD_OFFSET, getLoadOffset()));

		Double x = heliumSection.getDouble(MinecartParam.X, getOffset().getX());
		Double y = heliumSection.getDouble(MinecartParam.Y, getOffset().getY());
		Double z = heliumSection.getDouble(MinecartParam.Z, getOffset().getZ());
		setOffset(new Vector(x, y, z));

		setCustom(heliumSection.getString(MinecartParam.CUSTOM));
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);

		if (heliumSection.isSection(MinecartParam.LOAD)) {
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
					.add(String.format(fb, MinecartParam.LOAD_OFFSET.getAttributeName(), Integer.toString(loadOffset)));
		if (offset.getX() != 0)
			newParamListDump.add(String.format(fb, MinecartParam.X.getAttributeName(), Double.toString(offset.getX())));
		if (offset.getY() != 0)
			newParamListDump.add(String.format(fb, MinecartParam.Y.getAttributeName(), Double.toString(offset.getY())));
		if (offset.getZ() != 0)
			newParamListDump.add(String.format(fb, MinecartParam.Z.getAttributeName(), Double.toString(offset.getZ())));
		if ((custom != null) && !custom.isEmpty())
			newParamListDump.add(String.format(fs, MinecartParam.CUSTOM.getAttributeName(), custom));
		return newParamListDump;
	}

	@Override
	public String toString() {
		return String.format("%s: { %s }", getName(), String.join(", ", paramListAsDump()));
	}

}
