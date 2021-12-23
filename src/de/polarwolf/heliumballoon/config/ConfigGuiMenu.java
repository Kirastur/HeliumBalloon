package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumName;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumSection;
import de.polarwolf.heliumballoon.tools.helium.HeliumText;

public class ConfigGuiMenu implements HeliumName {

	private final String name;
	private final String fullName;
	private HeliumText guiTitle = new HeliumText("title");
	private ConfigGuiHelperItem guiDeassign = null;
	private ConfigGuiHelperItem guiFiller = null;

	public ConfigGuiMenu(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}

	public ConfigGuiMenu(ConfigurationSection fileSection) throws BalloonException {
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

	public String getGuiTitle(CommandSender sender) {
		return guiTitle.findLocalizedforSender(sender);
	}

	protected void setGuiTitle(HeliumText guiTitle) {
		this.guiTitle = guiTitle;
	}

	public ConfigGuiHelperItem getGuiDeassign() {
		return guiDeassign;
	}

	protected void setGuiDeassign(ConfigGuiHelperItem guiDeassign) {
		this.guiDeassign = guiDeassign;
	}

	public ConfigGuiHelperItem getGuiFiller() {
		return guiFiller;
	}

	protected void setGuiFiller(ConfigGuiHelperItem guiFiller) {
		this.guiFiller = guiFiller;
	}

	protected List<HeliumParam> getValidParams() {
		return Arrays.asList(ParamGuiMenu.values());
	}

	protected void importHeliumSection(HeliumSection heliumSection) {
		setGuiTitle(heliumSection.getHeliumText(ParamGuiMenu.TITLE));
	}

	protected void loadDeassignFromFile(ConfigurationSection fileSection) throws BalloonException {
		ConfigurationSection fileSectionDeassign = fileSection
				.getConfigurationSection(ParamGuiMenu.DEASSIGN.getAttributeName());
		setGuiDeassign(new ConfigGuiHelperItem(fileSectionDeassign));
	}

	protected void loadFillerFromFile(ConfigurationSection fileSection) throws BalloonException {
		ConfigurationSection fileSectionFiller = fileSection
				.getConfigurationSection(ParamGuiMenu.FILLER.getAttributeName());
		setGuiFiller(new ConfigGuiHelperItem(fileSectionFiller));
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);

		if (heliumSection.isSection(ParamGuiMenu.DEASSIGN)) {
			loadDeassignFromFile(fileSection);
		}
		if (heliumSection.isSection(ParamGuiMenu.FILLER)) {
			loadFillerFromFile(fileSection);
		}
	}

	protected List<String> paramListAsDump() {
		String fs = "%s: \"%s\"";
		List<String> newParamListDump = new ArrayList<>();
		if (guiTitle != null)
			for (Entry<String, String> myEntry : guiTitle.dump().entrySet())
				newParamListDump.add(String.format(fs, myEntry.getKey(), myEntry.getValue()));
		if (guiDeassign != null)
			newParamListDump.add(guiDeassign.toString());
		if (guiFiller != null)
			newParamListDump.add(guiFiller.toString());
		return newParamListDump;
	}

	@Override
	public String toString() {
		return String.format("%s: { %s }", ParamSection.GUI.getAttributeName(), String.join(", ", paramListAsDump()));
	}
}
