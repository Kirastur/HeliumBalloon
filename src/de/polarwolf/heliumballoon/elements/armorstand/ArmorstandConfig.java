package de.polarwolf.heliumballoon.elements.armorstand;

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

public class ArmorstandConfig implements ConfigElement {

	private final String name;
	private final String fullName;
	private final ElementDefinition elementDefinition;
	private Vector offset = new Vector(0, 0, 0);
	private BlocksPartConfig load = null;
	private String custom = null;

	public ArmorstandConfig(String name, String fullName, ElementDefinition elementDefinition) {
		this.name = name;
		this.fullName = fullName;
		this.elementDefinition = elementDefinition;
	}

	public ArmorstandConfig(ElementDefinition elementDefinition, ConfigurationSection fileSection)
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
		return new ArmorstandElement(rule, this, behaviorDefinition);
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
		return Arrays.asList(ArmorstandParam.values());
	}

	protected void loadLoadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		ConfigurationSection fileSectionLoad = fileSection
				.getConfigurationSection(ArmorstandParam.LOAD.getAttributeName());
		setLoad(new BlocksPartConfig(null, fileSectionLoad));
	}

	protected void importHeliumSection(HeliumSection heliumSection) throws BalloonException {
		Double x = heliumSection.getDouble(ArmorstandParam.X, getOffset().getX());
		Double y = heliumSection.getDouble(ArmorstandParam.Y, getOffset().getY());
		Double z = heliumSection.getDouble(ArmorstandParam.Z, getOffset().getZ());
		setOffset(new Vector(x, y, z));
		setCustom(heliumSection.getString(ArmorstandParam.CUSTOM));
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);

		if (heliumSection.isSection(ArmorstandParam.LOAD)) {
			loadLoadConfigFromFile(fileSection);
		}
	}

	protected List<String> paramListAsDump() {
		String fs = "%s: \"%s\"";
		String fb = "%s: %s";
		List<String> newParamListDump = new ArrayList<>();
		if (load != null)
			newParamListDump.add(load.toString());
		if (offset.getX() != 0)
			newParamListDump
					.add(String.format(fb, ArmorstandParam.X.getAttributeName(), Double.toString(offset.getX())));
		if (offset.getY() != 0)
			newParamListDump
					.add(String.format(fb, ArmorstandParam.Y.getAttributeName(), Double.toString(offset.getY())));
		if (offset.getZ() != 0)
			newParamListDump
					.add(String.format(fb, ArmorstandParam.Z.getAttributeName(), Double.toString(offset.getZ())));
		if ((custom != null) && !custom.isEmpty())
			newParamListDump.add(String.format(fs, ArmorstandParam.CUSTOM.getAttributeName(), custom));
		return newParamListDump;
	}

	@Override
	public String toString() {
		return String.format("%s: { %s }", getName(), String.join(", ", paramListAsDump()));
	}

}
