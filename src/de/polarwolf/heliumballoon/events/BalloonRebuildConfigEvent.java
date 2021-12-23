package de.polarwolf.heliumballoon.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.config.ConfigPet;
import de.polarwolf.heliumballoon.config.ConfigRotator;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.config.ConfigWall;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;

public class BalloonRebuildConfigEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	private boolean isCancelled = false;
	protected final Plugin plugin;
	protected final HeliumLogger logger;
	protected final ConfigManager configManager;
	protected BalloonException cancelReason = null;
	protected List<ConfigSection> sections = new ArrayList<>();

	BalloonRebuildConfigEvent(Plugin plugin, HeliumLogger logger, ConfigManager configManager) {
		this.plugin = plugin;
		this.logger = logger;
		this.configManager = configManager;
	}

	List<ConfigSection> getSections() {
		return sections;
	}

	BalloonException getCancelReason() {
		return cancelReason;
	}

	protected void checkName(ConfigSection newSection) throws BalloonException {
		for (ConfigSection mySection : sections) {
			if (mySection.getName().equals(newSection.getName())) {
				throw new BalloonException(null, "Duplicate Section Name", newSection.getFullName());
			}
		}
	}

	protected void checkPets(ConfigSection newSection) throws BalloonException {
		for (String myPetName : newSection.getPetNames()) {
			for (ConfigSection mySection : sections) {
				if (mySection.findPet(myPetName) != null) {
					throw new BalloonException(newSection.getFullName(), "Duplicate Pet Name", myPetName);
				}
			}
		}
	}

	protected void checkWalls(ConfigSection newSection) throws BalloonException {
		for (String myWallName : newSection.getWallNames()) {
			for (ConfigSection mySection : sections) {
				if (mySection.findWall(myWallName) != null) {
					throw new BalloonException(newSection.getFullName(), "Duplicate Wall Name", myWallName);
				}
			}
		}
	}

	protected void checkRotators(ConfigSection newSection) throws BalloonException {
		for (String myRotatorName : newSection.getRotatorNames()) {
			for (ConfigSection mySection : sections) {
				if (mySection.findRotator(myRotatorName) != null) {
					throw new BalloonException(newSection.getFullName(), "Duplicate Rotator Name", myRotatorName);
				}
			}

		}
	}

	protected void checkForWrongPurpose(ConfigSection newSection) {
		if (!ConfigManager.getWarnOnWrongPurpose(plugin)) {
			return;
		}
		for (String myPetName : newSection.getPetNames()) {
			ConfigPet myConfigPet = newSection.findPet(myPetName);
			if (!myConfigPet.isSuitableFor(BalloonPurpose.PET)) {
				String s = String.format("Template is not completely suitable for pet %s", myConfigPet.getName());
				logger.printWarning(s);
			}
		}
		for (String myWallName : newSection.getWallNames()) {
			ConfigWall myConfigWall = newSection.findWall(myWallName);
			if (!myConfigWall.isSuitableFor(BalloonPurpose.WALL)) {
				String s = String.format("Template is not completely suitable for wall %s", myConfigWall.getName());
				logger.printWarning(s);
			}
		}
		for (String myRotatorName : newSection.getRotatorNames()) {
			ConfigRotator myConfigRotator = newSection.findRotator(myRotatorName);
			if (!myConfigRotator.isSuitableFor(BalloonPurpose.ROTATOR)) {
				String s = String.format("Template is not completely suitable as rotator in %s",
						myConfigRotator.getName());
				logger.printWarning(s);
			}
		}
	}

	protected void checkNewSection(ConfigSection newSection) throws BalloonException {
		checkName(newSection);
		checkPets(newSection);
		checkWalls(newSection);
		checkRotators(newSection);
		checkForWrongPurpose(newSection);
	}

	public void addSection(ConfigSection newSection) throws BalloonException {
		checkNewSection(newSection);
		sections.add(newSection);
	}

	public ConfigSection buildConfigSectionFromFileSection(String sectionName, ConfigurationSection fileSection)
			throws BalloonException {
		return configManager.buildConfigSectionFromFileSection(sectionName, fileSection);
	}

	public ConfigSection buildConfigSectionFromConfigFile(Plugin fileOwnerPlugin) throws BalloonException {
		return configManager.buildConfigSectionFromConfigFile(fileOwnerPlugin);
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		if (isCancelled) { // You cannot undo a cancel
			this.isCancelled = isCancelled;
		}
	}

	public void cancelWithReason(BalloonException newReason) {
		cancelReason = newReason;
		setCancelled(true);
	}

	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

}
