package de.polarwolf.heliumballoon.api;

import java.util.List;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.balloons.pets.Pet;
import de.polarwolf.heliumballoon.balloons.pets.PetManager;
import de.polarwolf.heliumballoon.balloons.rotators.Rotator;
import de.polarwolf.heliumballoon.balloons.rotators.RotatorManager;
import de.polarwolf.heliumballoon.balloons.walls.Wall;
import de.polarwolf.heliumballoon.balloons.walls.WallManager;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.config.ConfigPet;
import de.polarwolf.heliumballoon.config.ConfigRotator;
import de.polarwolf.heliumballoon.config.ConfigRule;
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.config.ConfigWall;
import de.polarwolf.heliumballoon.config.ConfigWorldset;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.gui.GuiManager;
import de.polarwolf.heliumballoon.observers.Observer;
import de.polarwolf.heliumballoon.observers.ObserverManager;
import de.polarwolf.heliumballoon.system.reload.ReloadManager;
import de.polarwolf.heliumballoon.system.players.PlayerManager;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;
import de.polarwolf.heliumballoon.tools.messages.Message;
import de.polarwolf.heliumballoon.tools.messages.MessageManager;

public class HeliumBalloonAPI {

	protected final Plugin plugin;
	protected final HeliumLogger logger;
	protected final ConfigManager configManager;
	protected final MessageManager messageManager;
	protected final PlayerManager playerManager;
	protected final ObserverManager observerManager;
	protected final PetManager petManager;
	protected final WallManager wallManager;
	protected final RotatorManager rotatorManager;
	protected final GuiManager guiManager;
	protected final ReloadManager reloadManager;

	public HeliumBalloonAPI(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.configManager = orchestrator.getConfigManager();
		this.messageManager = orchestrator.getMessageManager();
		this.playerManager = orchestrator.getPlayerManager();
		this.observerManager = orchestrator.getObserverManager();
		this.petManager = orchestrator.getPetManager();
		this.wallManager = orchestrator.getWallManager();
		this.rotatorManager = orchestrator.getRotatorManager();
		this.guiManager = orchestrator.getGuiManager();
		this.reloadManager = orchestrator.getReloadManager();
	}

	// Logger
	public boolean isDebug() {
		return logger.isDebug();
	}

	public void setDebug(boolean debug) {
		logger.setDebug(debug);
	}

	// ConfigManager
	public Set<String> getConfigSectionNames() {
		return configManager.getSectionNames();
	}

	public String dumpConfigSection(String sectionName) {
		return configManager.dumpSection(sectionName);
	}

	public ConfigWorldset findConfigWorldsetInSection(String sectionName, String worldsetName) {
		return configManager.findWorldsetInSection(sectionName, worldsetName);
	}

	public ConfigRule findConfigRuleInSection(String sectionName, String ruleName) {
		return configManager.findRuleInSection(sectionName, ruleName);
	}

	public ConfigTemplate findConfigTemplateInSection(String sectionName, String templateName) {
		return configManager.findTemplateInSection(sectionName, templateName);
	}

	public ConfigPet findConfigPet(String petName) {
		return configManager.findPet(petName);
	}

	public Set<String> getConfigPetNames() {
		return configManager.getPetNames();
	}

	// MessageManager
	public String getMessage(CommandSender sender, Message messageId, String replacementString) {
		return messageManager.getMessage(sender, messageId, replacementString);
	}

	// PlayerManager
	public int purgeOldPlayers() {
		return playerManager.purgeOldPlayers();
	}

	// ObserverManager
	public Observer findObserverByEntity(Entity entity) {
		return observerManager.findObserverByEntity(entity);
	}

	public void addObserver(Observer observer) throws BalloonException {
		observerManager.addObserver(observer);
	}

	public List<Observer> getAllObservers() {
		return observerManager.getAllObservers();
	}

	// PetManager
	public ConfigPet findPersistentPetForPlayer(OfflinePlayer offlinePlayer) {
		return petManager.findPersistentPetForPlayer(offlinePlayer);
	}

	public boolean assignPersistentPet(OfflinePlayer offlinePlayer, String petName) {
		return petManager.assignPersistentPet(offlinePlayer, petName);
	}

	public boolean deassignPersistentPet(OfflinePlayer offlinePlayer) {
		return petManager.deassignPersistentPet(offlinePlayer);
	}

	public boolean hasPet(Player player) {
		return petManager.shouldHavePet(player);
	}

	public boolean hasPetPermission(Player player, String petName) {
		return petManager.hasPetPermission(player, petName);
	}

	public List<ConfigPet> getConfigPetsForPlayer(Player player) {
		return petManager.getConfigPetsForPlayer(player);
	}

	public List<Pet> getAllPets() {
		return petManager.getPets();
	}

	// WallManager
	public List<Wall> getAllWalls() {
		return wallManager.getWalls();
	}

	public Wall createWall(ConfigWall configWall, World world) throws BalloonException {
		return wallManager.createWall(configWall, world);
	}

	public void destroyWall(Wall wall) {
		wallManager.destroyWall(wall);
	}

	// RotationManager
	public List<Rotator> getAllRotators() {
		return rotatorManager.getRotators();
	}

	public Rotator createRotator(ConfigRotator configRotator, World world) throws BalloonException {
		return rotatorManager.createRotator(configRotator, world);
	}

	public void destroyRotator(Rotator rotator) {
		rotatorManager.destroyRotator(rotator);
	}

	// GuiManager
	public String findActionFromItemStack(ItemStack itemStack) {
		return guiManager.findActionFromItemStack(itemStack);
	}

	public String findPetFromItemStack(ItemStack itemStack) {
		return guiManager.findPetFromItemStack(itemStack);
	}

	public ItemStack createPetItemStack(Player player, ConfigPet configPet) {
		return guiManager.createPetItemStack(player, configPet);
	}

	public ItemStack createDeassignItemStack(Player player) {
		return guiManager.createDeassignItemStack(player);
	}

	public ItemStack createFillerItemStack(Player player) {
		return guiManager.createFillerItemStack(player);
	}

	public List<ItemStack> getPetsItemStacks(Player player) {
		return guiManager.getPetsItemStacks(player);
	}

	public Inventory openGui(Player player) {
		return guiManager.openGui(player);
	}

	// ReloadManager
	public String reload() throws BalloonException {
		return reloadManager.reload();
	}

	public void scheduleRedloadFoNextTick() {
		reloadManager.scheduleRedloadFoNextTick();
	}

	// Global
	public boolean isDisabled() {
		return (observerManager.isCancelled() || petManager.isCancelled());
	}

}
