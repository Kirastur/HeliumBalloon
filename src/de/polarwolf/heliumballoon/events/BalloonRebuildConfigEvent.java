package de.polarwolf.heliumballoon.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.compatibility.CompatibilityManager;
import de.polarwolf.heliumballoon.config.ConfigHelper;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.config.templates.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;

public class BalloonRebuildConfigEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	private boolean isCancelled = false;
	protected final CompatibilityManager compatibilityManager;
	protected final ConfigHelper configHelper;
	protected final boolean initial;
	protected BalloonException cancelReason = null;
	protected List<ConfigSection> sections = new ArrayList<>();

	BalloonRebuildConfigEvent(CompatibilityManager compatibilityManager, ConfigHelper configHelper, boolean initial) {
		this.compatibilityManager = compatibilityManager;
		this.configHelper = configHelper;
		this.initial = initial;
	}

	public ConfigHelper getConfigHelper() {
		return configHelper;
	}

	public boolean isInitial() {
		return initial;
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

	protected void checkBalloons(ConfigSection newSection) throws BalloonException {
		for (BalloonDefinition myBalloonDefinition : configHelper.listBalloonDefinitions()) {
			for (String myBalloonName : newSection.getBalloonNames(myBalloonDefinition)) {
				for (ConfigSection mySection : sections) {
					if (mySection.findBalloon(myBalloonName) != null) {
						throw new BalloonException(newSection.getFullName(), "Duplicate Balloon Name", myBalloonName);
					}
				}
			}
		}
	}

	protected void checkCompatibility(ConfigSection newSection) throws BalloonException {
		for (BalloonDefinition myBalloonDefinition : configHelper.listBalloonDefinitions()) {
			for (String myBalloonName : newSection.getBalloonNames(myBalloonDefinition)) {
				ConfigBalloon myConfigBalloon = newSection.findBalloon(myBalloonName);
				for (ConfigTemplate myTemplate : myConfigBalloon.listUsedTemplates()) {
					for (ConfigElement myElement : myTemplate.getElements()) {
						if (!compatibilityManager.isCompatible(myElement.getElementDefinition(),
								myConfigBalloon.getBehavior(), myBalloonDefinition)) {
							String s = String.format("(%s => %s => %s)", myElement.getName(),
									myConfigBalloon.getBehavior().getName(), myBalloonDefinition.getAttributeName());
							throw new BalloonException(newSection.getFullName(),
									"Balloon uses inadequate behavior or element", s);
						}
					}
				}
			}
		}
	}

	protected void checkNewSection(ConfigSection newSection) throws BalloonException {
		checkName(newSection);
		checkBalloons(newSection);
		checkCompatibility(newSection);
	}

	public void addSection(ConfigSection newSection) throws BalloonException {
		checkNewSection(newSection);
		sections.add(newSection);
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
