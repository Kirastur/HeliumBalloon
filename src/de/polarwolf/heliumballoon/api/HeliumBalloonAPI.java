package de.polarwolf.heliumballoon.api;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.balloons.BalloonDefinition;
import de.polarwolf.heliumballoon.balloons.pets.ConfigPet;
import de.polarwolf.heliumballoon.balloons.pets.Pet;
import de.polarwolf.heliumballoon.balloons.rotators.ConfigRotator;
import de.polarwolf.heliumballoon.balloons.rotators.Rotator;
import de.polarwolf.heliumballoon.balloons.walls.ConfigWall;
import de.polarwolf.heliumballoon.balloons.walls.Wall;
import de.polarwolf.heliumballoon.behavior.BehaviorDefinition;
import de.polarwolf.heliumballoon.behavior.BehaviorHelper;
import de.polarwolf.heliumballoon.behavior.observers.Observer;
import de.polarwolf.heliumballoon.config.ConfigHelper;
import de.polarwolf.heliumballoon.config.ConfigSection;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.elements.ElementDefinition;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.orchestrator.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.tools.messages.Message;

public class HeliumBalloonAPI {

	protected final HeliumBalloonOrchestrator orchestrator;

	public HeliumBalloonAPI(HeliumBalloonOrchestrator orchestrator) {
		this.orchestrator = orchestrator;
	}

	// Logger
	public boolean isDebug() {
		return orchestrator.getHeliumLogger().isDebug();
	}

	public void setDebug(boolean debug) {
		orchestrator.getHeliumLogger().setDebug(debug);
	}

	// CompatibilityManager
	public boolean isCompatible(ElementDefinition elementDefinition, BehaviorDefinition behaviorDefinition,
			BalloonDefinition balloonDefinition) {
		return orchestrator.getCompatibilityManager().isCompatible(elementDefinition, behaviorDefinition,
				balloonDefinition);
	}

	public void addCompatibility(ElementDefinition elementDefinition, BehaviorDefinition behaviorDefinition,
			BalloonDefinition balloonDefinition) {
		orchestrator.getCompatibilityManager().addCompatibility(elementDefinition, behaviorDefinition,
				balloonDefinition);
	}

	// ElementManager
	public ElementDefinition findElementDefinition(String definitionName) {
		return orchestrator.getElementManager().findElementDefinition(definitionName);
	}

	public List<ElementDefinition> listElementDefinitions() {
		return orchestrator.getElementManager().listElementDefinitions();
	}

	public void addElementDefinition(ElementDefinition elementDefinition) {
		orchestrator.getElementManager().addDefinition(elementDefinition);
	}

	// BehaviorManager
	public BehaviorDefinition findBehaviorDefinition(String definitionName) {
		return orchestrator.getBehaviorManager().findBehaviorDefinition(definitionName);
	}

	public List<BehaviorDefinition> listBehaviorDefinitions() {
		return orchestrator.getBehaviorManager().listBehaviorDefinitions();
	}

	public void addBehaviorDefinition(BehaviorDefinition behaviorDefinition) {
		orchestrator.getBehaviorManager().addDefinition(behaviorDefinition);
	}

	public BehaviorHelper getBehaviorHelper() {
		return orchestrator.getBehaviorManager();
	}

	// BalloonManager
	public BalloonDefinition findBalloonDefinition(String definitionName) {
		return orchestrator.getBalloonManager().findBalloonDefinition(definitionName);
	}

	public List<BalloonDefinition> listBalloonDefinitions() {
		return orchestrator.getBalloonManager().listBalloonDefinitions();
	}

	public void addBalloonDefinition(BalloonDefinition balloonDefinition) {
		orchestrator.getBalloonManager().addDefinition(balloonDefinition);
	}

	// ConfigManager
	public List<String> getConfigSectionNames() {
		return orchestrator.getConfigManager().getSectionNames();
	}

	public ConfigSection findSection(String sectionName) {
		return orchestrator.getConfigManager().findSection(sectionName);
	}

	public String dumpConfigSection(String sectionName) {
		return orchestrator.getConfigManager().dumpSection(sectionName);
	}

	public List<String> getBalloonNames(BalloonDefinition balloonDefinition) {
		return orchestrator.getConfigManager().getBalloonNames(balloonDefinition);
	}

	public ConfigBalloon findBalloon(String balloonName) {
		return orchestrator.getConfigManager().findBalloon(balloonName);
	}

	public ConfigHelper getConfigHelper() {
		return orchestrator.getConfigManager();
	}

	// MessageManager
	public String getMessage(CommandSender sender, Message messageId, String replacementString) {
		return orchestrator.getMessageManager().getMessage(sender, messageId, replacementString);
	}

	// PlayerManager
	public int purgeOldPlayers() {
		return orchestrator.getPlayerManager().purgeOldPlayers();
	}

	// ObserverManager
	public Observer findObserverByEntity(Entity entity) {
		return orchestrator.getObserverManager().findObserverByEntity(entity);
	}

	public void addObserver(Observer observer) throws BalloonException {
		orchestrator.getObserverManager().addObserver(observer);
	}

	public List<Observer> getAllObservers() {
		return orchestrator.getObserverManager().getAllObservers();
	}

	// PetManager
	public List<String> getConfigPetNames() {
		return orchestrator.getPetManager().getPetNames();
	}

	public ConfigPet findConfigPet(String petName) {
		return orchestrator.getPetManager().findConfigPet(petName);
	}

	public ConfigPet findPersistentPetForPlayer(OfflinePlayer offlinePlayer) {
		return orchestrator.getPetManager().findPersistentPetForPlayer(offlinePlayer);
	}

	public boolean assignPersistentPet(OfflinePlayer offlinePlayer, String petName) {
		return orchestrator.getPetManager().assignPersistentPet(offlinePlayer, petName);
	}

	public boolean deassignPersistentPet(OfflinePlayer offlinePlayer) {
		return orchestrator.getPetManager().deassignPersistentPet(offlinePlayer);
	}

	public boolean hasPet(Player player) {
		return orchestrator.getPetManager().shouldHavePet(player);
	}

	public boolean hasPetPermission(Player player, String petName) {
		return orchestrator.getPetManager().hasPetPermission(player, petName);
	}

	public List<ConfigPet> getConfigPetsForPlayer(Player player) {
		return orchestrator.getPetManager().getConfigPetsForPlayer(player);
	}

	public List<Pet> getAllPets() {
		return orchestrator.getPetManager().getPets();
	}

	// WallManager
	public List<String> getConfigWallNames() {
		return orchestrator.getWallManager().getRelevantBalloonNames();
	}

	public ConfigWall findConfigWall(String wallName) {
		return orchestrator.getWallManager().findConfigWall(wallName);
	}

	public List<Wall> getAllWalls() {
		return orchestrator.getWallManager().getWalls();
	}

	public Wall createWall(ConfigWall configWall, World world) throws BalloonException {
		return orchestrator.getWallManager().createWall(configWall, world);
	}

	public void destroyWall(Wall wall) {
		orchestrator.getWallManager().destroyWall(wall);
	}

	// RotationManager
	public List<String> getConfigRotatorlNames() {
		return orchestrator.getRotatorManager().getRelevantBalloonNames();
	}

	public ConfigRotator findConfigRotator(String wallName) {
		return orchestrator.getRotatorManager().findConfigRotator(wallName);
	}

	public List<Rotator> getAllRotators() {
		return orchestrator.getRotatorManager().getRotators();
	}

	public Rotator createRotator(ConfigRotator configRotator, World world) throws BalloonException {
		return orchestrator.getRotatorManager().createRotator(configRotator, world);
	}

	public void destroyRotator(Rotator rotator) {
		orchestrator.getRotatorManager().destroyRotator(rotator);
	}

	// GuiManager
	public String findActionFromItemStack(ItemStack itemStack) {
		return orchestrator.getGuiManager().findActionFromItemStack(itemStack);
	}

	public String findPetFromItemStack(ItemStack itemStack) {
		return orchestrator.getGuiManager().findPetFromItemStack(itemStack);
	}

	public ItemStack createPetItemStack(Player player, ConfigPet configPet) {
		return orchestrator.getGuiManager().createPetItemStack(player, configPet);
	}

	public ItemStack createDeassignItemStack(Player player) {
		return orchestrator.getGuiManager().createDeassignItemStack(player);
	}

	public ItemStack createFillerItemStack(Player player) {
		return orchestrator.getGuiManager().createFillerItemStack(player);
	}

	public List<ItemStack> getPetsItemStacks(Player player) {
		return orchestrator.getGuiManager().getPetsItemStacks(player);
	}

	public Inventory openGui(Player player) {
		return orchestrator.getGuiManager().openGui(player);
	}

	// ReloadManager
	public void refreshAll() {
		orchestrator.getReloadManager().refreshAll();
	}

	public String reload() throws BalloonException {
		return orchestrator.getReloadManager().reload();
	}

	public void addReloadRegistration(Plugin plugin, String fileName, String fileSection) {
		orchestrator.getReloadManager().addReloadRegistration(plugin, fileName, fileSection);
	}

	public void removeReloadRegistrations(Plugin plugin) {
		orchestrator.getReloadManager().removeReloadRegistrations(plugin);
	}

	// Global
	public boolean isDisabled() {
		return orchestrator.isDisabled();
	}

}
