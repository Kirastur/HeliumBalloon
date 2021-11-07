package de.polarwolf.heliumballoon.api;

import java.util.List;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.balloons.Balloon;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.config.ConfigPet;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.gui.GuiManager;
import de.polarwolf.heliumballoon.helium.HeliumLogger;
import de.polarwolf.heliumballoon.messages.Message;
import de.polarwolf.heliumballoon.messages.MessageManager;
import de.polarwolf.heliumballoon.pets.Pet;
import de.polarwolf.heliumballoon.pets.PetManager;
import de.polarwolf.heliumballoon.players.PlayerManager;
import de.polarwolf.heliumballoon.reload.ReloadManager;
import de.polarwolf.heliumballoon.rotators.Rotator;
import de.polarwolf.heliumballoon.rotators.RotatorManager;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifierManager;
import de.polarwolf.heliumballoon.walls.Wall;
import de.polarwolf.heliumballoon.walls.WallManager;

public class HeliumBalloonAPI {

	protected final Plugin plugin;
	protected final HeliumLogger logger;
	protected final SpawnModifierManager spawnModifierManager;
	protected final ConfigManager configManager;
	protected final MessageManager messageManager;
	protected final PlayerManager playerManager;
	protected final BalloonManager balloonManager;
	protected final PetManager petManager;
	protected final WallManager wallManager;
	protected final RotatorManager rotatorManager;
	protected final GuiManager guiManager;
	protected final ReloadManager reloadManager;

	public HeliumBalloonAPI(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.spawnModifierManager = orchestrator.getSpawnModifierManager();
		this.configManager = orchestrator.getConfigManager();
		this.messageManager = orchestrator.getMessageManager();
		this.playerManager = orchestrator.getPlayerManager();
		this.balloonManager = orchestrator.getBalloonManager();
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

	// SpawnModifyManager
	public void registerModifier(SpawnModifier newSpawnModifier) {
		spawnModifierManager.registerModifier(newSpawnModifier);
	}

	public void unregisterModifier(SpawnModifier oldSpawnModifier) {
		spawnModifierManager.unregisterModifier(oldSpawnModifier);
	}

	// ConfigManager
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

	// BalloonManager
	public Balloon findBalloonByEntity(Entity entity) {
		return balloonManager.findBalloonByEntity(entity);
	}

	public void addBalloon(Balloon balloon) throws BalloonException {
		balloonManager.addBalloon(balloon);
	}

	public List<Balloon> getAllBalloons() {
		return balloonManager.getAllBalloons();
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

	// RotationManager
	public List<Rotator> getAllRotators() {
		return rotatorManager.getRotators();
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
	public void reload() throws BalloonException {
		reloadManager.reload();
	}

	// Global
	public boolean isDisabled() {
		return (balloonManager.isCancelled() || petManager.isCancelled());
	}

}
