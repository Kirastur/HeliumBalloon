package de.polarwolf.heliumballoon.system.reload;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.events.BalloonRebuildConfigEvent;
import de.polarwolf.heliumballoon.events.EventManager;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;

public class ReloadManager extends BukkitRunnable {

	protected final Plugin plugin;
	protected final EventManager eventManager;
	protected final BalloonManager balloonManager;
	protected final ConfigManager configManager;

	protected BukkitTask bukkitTask = null;
	protected Set<ReloadRegistration> reloadRegistrations = new HashSet<>();

	public ReloadManager(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.eventManager = orchestrator.getEventManager();
		this.balloonManager = orchestrator.getBalloonManager();
		this.configManager = orchestrator.getConfigManager();
	}

	public void startup() {
		bukkitTask = runTask(plugin);
	}

	public void disable() {
		if (bukkitTask != null)
			try {
				bukkitTask = null;
				cancel(); // Bukkit Task
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public void addReloadRegistration(Plugin filePlugin, String fileName, String fileSection) {
		reloadRegistrations.add(new ReloadRegistration(filePlugin, fileName, fileSection));
	}

	public void removeReloadRegistrations(Plugin filePlugin) {
		for (ReloadRegistration myRegistration : new ArrayList<>(reloadRegistrations)) {
			if (myRegistration.plugin().equals(filePlugin)) {
				reloadRegistrations.remove(myRegistration);
			}
		}
	}

	public void reloadAllRegistrations(BalloonRebuildConfigEvent event) throws BalloonException { // NOSONAR
		for (ReloadRegistration myRegistration : reloadRegistrations) {
			Plugin reloadPlugin = myRegistration.plugin();
			String reloadFileName = myRegistration.fileName();
			String reloadFileSection = myRegistration.fileSection();

			String myName = reloadPlugin.getName();
			ConfigurationSection mySection;
			if ((reloadFileName == null) || reloadFileName.isEmpty()) {
				if (!event.isInitial()) {
					reloadPlugin.reloadConfig();
				}
				mySection = reloadPlugin.getConfig().getRoot();
			} else {
				File dataFile = new File(reloadPlugin.getDataFolder(), reloadFileName);
				if (!dataFile.exists()) {
					throw new BalloonException(reloadPlugin.getName(), "Config file not found", reloadFileName);
				}
				mySection = YamlConfiguration.loadConfiguration(dataFile);
				myName = myName + ":" + reloadFileName;
			}

			if ((reloadFileSection != null) && !reloadFileSection.isEmpty()) {
				if (!mySection.contains(reloadFileSection, true)) {
					throw new BalloonException(reloadPlugin.getName(), "Section not found in file", reloadFileSection);
				}
				mySection = mySection.getConfigurationSection(reloadFileSection);
				myName = myName + ":" + reloadFileSection;
			}

			ConfigSection newConfigSection = event.getConfigHelper().buildConfigSectionFromFileSection(myName,
					mySection);
			new ConfigSection(myName, event.getConfigHelper(), mySection);
			event.addSection(newConfigSection);
		}
	}

	public void refreshBalloons() {
		for (BalloonDefinition myBalloonDefinition : balloonManager.listBalloonDefinitions()) {
			myBalloonDefinition.refresh();
		}
	}

	public void refreshAll() {
		eventManager.sendRefreshAllEvent(configManager);
	}

	public String reload() throws BalloonException {
		plugin.reloadConfig();
		List<ConfigSection> newSections = eventManager.sendRebuildConfigEvent(configManager, false);
		String s = configManager.reload(newSections);
		refreshAll();
		return s;
	}

	@Override
	public void run() {
		try {
			bukkitTask = null;
			List<ConfigSection> newSections = eventManager.sendRebuildConfigEvent(configManager, true);
			configManager.reload(newSections);
			refreshAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
