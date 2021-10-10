package de.polarwolf.heliumballoon.api;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.balloons.Balloon;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.config.ConfigGuiItem;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.config.ConfigRule;
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.gui.GuiManager;
import de.polarwolf.heliumballoon.helium.HeliumLogger;
import de.polarwolf.heliumballoon.messages.Message;
import de.polarwolf.heliumballoon.messages.MessageManager;
import de.polarwolf.heliumballoon.pets.PetManager;
import de.polarwolf.heliumballoon.players.PlayerManager;
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
	protected final GuiManager guiManager;
	
	
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
		this.guiManager = orchestrator.getGuiManager();
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
	public void reload() throws BalloonException {
		plugin.reloadConfig();
		configManager.reload();
		petManager.reload();
		wallManager.reload();
	}
	
	public ConfigRule findRule(String ruleName) {
		return configManager.findRule(ruleName);
	}
	
	public Set<String> getRuleNames(){
		return configManager.getRuleNames();
	}
	
	public ConfigTemplate findTemplate(String templateName) {
		return configManager.findTemplate(templateName);
	}
	
	public Set<String> getTemplateNames() {
		return configManager.getTemplateNames();
	}
	
	public boolean hasWorld(String worldName) {
		return configManager.hasWorld(worldName);
	}
	
	public boolean hasPlayerPersistentPet(UUID playerUUID) {
		return configManager.hasPlayerPersistentPet(playerUUID);
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
	public ConfigTemplate findTemplateForPlayer(OfflinePlayer offlinePlayer) {
		return petManager.findTemplateForPlayer(offlinePlayer);
	}
	
	public boolean assign(OfflinePlayer offlinePlayer, String templateName) {
		return petManager.assign(offlinePlayer, templateName);
	}
	
	public boolean deassign(OfflinePlayer offlinePlayer) {
		return petManager.deassign(offlinePlayer);
	}
	
	public boolean hasPet(Player player) {
		return petManager.shouldHavePet(player);
	}

	public boolean hasTemplatePermission(Player player, String templateName) {
		return petManager.hasTemplatePermission(player, templateName);
	}	
	
	
	// WallManager
	public List<Wall> getAllWalls() {
		return wallManager.enumWalls();
	}
	
	
	// GuiManager
    public String findTemplateFromItemStack(ItemStack itemStack) {
    	return guiManager.findTemplateFromItemStack(itemStack);
    }
    
    public ItemStack createTemplateItemStack(Player player, ConfigGuiItem guiItem) {
    	return guiManager.createTemplateItemStack(player, guiItem);
    }
    
    public ItemStack createDeassignItemStack(Player player) {
    	return guiManager.createDeassignItemStack(player);
    }

    
	public List<ConfigGuiItem> getGuiItemConfigs(Player player) {
		return guiManager.getGuiItemConfigs(player);
	}

	public List<ItemStack> getGuiItemStacks(Player player) {
		return guiManager.getGuiItemStacks(player);
	}
	
	public Inventory openGui(Player player) {
		return guiManager.openGui(player);
	}


	// Global
	public boolean isDisabled() {
		return (balloonManager.isCancelled() || petManager.isCancelled());
	}
		
}
