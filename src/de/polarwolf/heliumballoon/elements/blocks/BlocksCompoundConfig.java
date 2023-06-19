package de.polarwolf.heliumballoon.elements.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.elements.ElementDefinition;
import de.polarwolf.heliumballoon.exception.BalloonException;

public class BlocksCompoundConfig implements ConfigElement {

	private final String name;
	private final String fullName;
	private final ElementDefinition elementDefinition;
	private Map<String, BlocksPartConfig> elements = new TreeMap<>();

	public BlocksCompoundConfig(String name, String fullName, ElementDefinition elementDefinition) {
		this.name = name;
		this.fullName = fullName;
		this.elementDefinition = elementDefinition;
	}

	public BlocksCompoundConfig(ElementDefinition elementDefinition, ConfigurationSection fileSection)
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

	@Override
	public Element createElement(Player player, ConfigRule rule, BehaviorDefinition behaviorDefinition) {
		return new BlocksCompoundElement(player, rule, this, behaviorDefinition);
	}

	@Override
	public double getMinYOffset() {
		double minYOffset = 0;
		for (BlocksPartConfig myElement : elements.values()) {
			if (myElement.getMinYOffset() < minYOffset) {
				minYOffset = myElement.getMinYOffset();
			}
		}
		return minYOffset;
	}

	@Override
	public double getMaxYOffset() {
		double maxYOffset = 0;
		for (BlocksPartConfig myElement : elements.values()) {
			if (myElement.getMaxYOffset() > maxYOffset) {
				maxYOffset = myElement.getMinYOffset();
			}
		}
		return maxYOffset;
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public List<BlocksPartConfig> getElements() {
		return new ArrayList<>(elements.values());
	}

	protected void addElement(BlocksPartConfig newElement) {
		elements.put(newElement.getName(), newElement);
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		for (String myElementName : fileSection.getKeys(false)) {
			if (!fileSection.contains(myElementName, true)) { // ignore default from jar
				continue;
			}
			if (!fileSection.isConfigurationSection(myElementName)) {
				throw new BalloonException(getFullName(), "Illegal elements section", myElementName);
			}
			addElement(new BlocksPartConfig(elementDefinition, fileSection.getConfigurationSection(myElementName)));
		}
	}

	protected List<String> elementListAsDump() {
		List<String> newElementListDump = new ArrayList<>();
		for (BlocksPartConfig myElement : elements.values())
			newElementListDump.add(myElement.toString());
		return newElementListDump;
	}

	@Override
	public String toString() {
		return String.format("%s: { %s }", getName(), String.join(", ", elementListAsDump()));
	}

}
