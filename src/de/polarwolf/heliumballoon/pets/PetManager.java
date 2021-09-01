package de.polarwolf.heliumballoon.pets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.logger.HeliumLogger;
import de.polarwolf.heliumballoon.players.PlayerManager;

public class PetManager extends BukkitRunnable {
	
	public static final String PERMISSION_PREFIX = "balloon.template.";
	
	protected final Plugin plugin;
	protected final HeliumLogger logger;
	protected final ConfigManager configManager;
	protected final PlayerManager playerManager;
	protected final BalloonManager balloonManager;
	protected final int exceptionQuota; 
	protected int exceptionCount = 0;
	protected int exceptionCooldown = 0;
	protected List<Pet> pets = new ArrayList<>();
	protected Map <UUID, Integer> scorePlayerDelay = new HashMap<>();
	protected Map <UUID, Double> scorePlayerCancel = new HashMap<>();
		
	
	public PetManager(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.configManager = orchestrator.getConfigManager();
		this.playerManager = orchestrator.getPlayerManager();
		this.balloonManager = orchestrator.getBalloonManager();
		this.exceptionQuota = logger.getExceptionQuota();
		runTaskTimer(plugin, 20, 20);
	}
	
	
	public void disable() {
		try {
			cancel(); // Bukkit Task
			validateAllPlayers(); // Remove all pets
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//
	// Handle the association between player and pet
	// 
	
	public ConfigTemplate findTemplateForPlayer(OfflinePlayer offlinePlayer) {
		String templateName = playerManager.findPetForPlayer(offlinePlayer); 
		if ((templateName == null) || templateName.isEmpty()) {
			return null;
		}
		return configManager.findTemplate(templateName);
	}
	

	public boolean assign(OfflinePlayer offlinePlayer, String templateName) {
		ConfigTemplate template = configManager.findTemplate(templateName);
		if (template == null) {
			return false;
		}

		if (offlinePlayer instanceof Player) {
			Player player = (Player) offlinePlayer;
			hideNow(player);
			removePlayerDelay(player);
			removePlayerCancelScore(player);
		}

		return playerManager.setPetForPlayer(offlinePlayer, templateName);
	}
	

	public boolean deassign(OfflinePlayer offlinePlayer) {
		if (offlinePlayer instanceof Player) {
			Player player = (Player) offlinePlayer;
			hideNow(player);
		}
		return playerManager.removePetForPlayer(offlinePlayer);
	}
	
	
	//
	// Handle permissions
	//
	
	public boolean hasTemplatePermission(Player player, String templateName) {
		return player.hasPermission(PERMISSION_PREFIX + templateName);
	}
	
	
	public boolean hasWorld(World world) {
		return configManager.hasWorld(world.getName());
	}
	
	
	//
	// Handle Player Delay Score
	//
	
	public boolean isPlayerDelayed(Player player) {
		Integer delay = scorePlayerDelay.get(player.getUniqueId());
		return !((delay == null) || (delay == 0)); 
	}
	

	protected void setPlayerDelay(Player player) {
		Integer delay = 3;
		scorePlayerDelay.put(player.getUniqueId(), delay);
		logger.printDebug(String.format("Setting PlayerDelay: %s (%d)", player.getName(), delay));
	}
	
	
	protected void cooldownPlayerDelay(Player player) {
		Integer delay = scorePlayerDelay.get(player.getUniqueId());
		if (delay == null) {
			return;
		}
		delay = delay -1;
		if (delay > 0) {
			scorePlayerDelay.put(player.getUniqueId(), delay);
			logger.printDebug(String.format("Decrementing PlayerDelay: %s (%d)", player.getName(), delay));
		} else {
			scorePlayerDelay.remove(player.getUniqueId());
			logger.printDebug(String.format("Deleting PlayerDelay: %s (0)", player.getName()));
		}
	}
	

	public void removePlayerDelay(Player player) {
		scorePlayerCancel.remove(player.getUniqueId());
		logger.printDebug(String.format("Removing PlayerDelay: %s", player.getName()));
	}
	
	
	//
	// Handle Player Cancel Score
	//
	
	public boolean isPlayerAboveCancelScoreLimit(Player player) {
		Double score = scorePlayerCancel.get(player.getUniqueId());
		if (score == null) {
			return false;
		}
		return score >= 2.0;
	}
	

	protected void increasePlayerCancelScore(Player player) {
		Double score = scorePlayerCancel.get(player.getUniqueId());
		if (score == null) {
			score = 0.0;
		}
		if (score < 2.0) {
			score = score +1.0;
		}
		scorePlayerCancel.put(player.getUniqueId(), score);
		logger.printDebug(String.format("Incrementing PlayerCancelScore: %s (%.2f)", player.getName(), score));
		if (score >= 2.0) {
			logger.printInfo(String.format("Pet is now disabled because of too mutch errors: %s", player.getName()));
		}
	}
	
	
	protected void cooldownPlayerCancelScore(Player player) {
		Double score = scorePlayerCancel.get(player.getUniqueId());
		if (score == null) {
			return;
		}
		score = score -0.4;
		if (score > 0.0) {
			scorePlayerCancel.put(player.getUniqueId(), score);
			logger.printDebug(String.format("Decrementing PlayerCancelScore: %s (%.2f)", player.getName(), score));
		} else {
			scorePlayerCancel.remove(player.getUniqueId());
			logger.printDebug(String.format("Deleting PlayerCancelScore: %s (0)", player.getName()));
		}
	}
	

	public void removePlayerCancelScore(Player player) {
		scorePlayerCancel.remove(player.getUniqueId());
		logger.printDebug(String.format("Removing PlayerCancelScore: %s", player.getName()));
	}
	
	
	//
	// Manage pet persistence
	//
	
	public boolean shouldHavePet(Player player) {
		return	player.isValid() && 
				player.isOnline() &&
				!player.isDead() &&
				!player.getGameMode().equals(GameMode.SPECTATOR) &&
				hasWorld(player.getWorld()) &&
				(findTemplateForPlayer(player) != null) &&
				!isPlayerAboveCancelScoreLimit(player);
	}
	
	
	public Pet findPetForPlayer(Player player) {
		for (Pet myPet : pets) {
			if (myPet.getPlayer().equals(player)) {
				return myPet;
			}
		}
		return null;
	}
	
	
	protected void hideNow(Player player) {
		Pet pet = findPetForPlayer(player);
		if (pet == null) {
			return;
		}

		boolean wasPetCancelled = pet.isCancelled(); 
		pet.hide();
		pets.remove(pet);
		if (wasPetCancelled) {
			logger.printDebug(String.format("Deleting cancelled pet: %s", pet.getName()));
			setPlayerDelay(player);
			increasePlayerCancelScore(player);
		} else {
			logger.printDebug(String.format("Removing pet: %s", pet.getName()));
		}
	}
	

	protected void showNow(Player player) throws BalloonException {		
		if (isPlayerDelayed(player)) {
				return;
		}
		
	    if (findPetForPlayer(player) == null) { 
	    	ConfigTemplate template = findTemplateForPlayer(player);
	    	logger.printDebug(String.format("Creating pet %s for player %s", template.getName(), player.getName()));		
	    	Pet pet = new Pet(player, balloonManager, template);
	    	pet.show();		
	    	pets.add(pet);
	    }

	    cooldownPlayerCancelScore(player);
	}
	
	
	//
	// Do scheduled tasks
	//
	
	
	protected void validateAllPets() {
		List<Pet> petList = new ArrayList<>(pets);
		for (Pet myPet : petList) {
			Player myPlayer = myPet.getPlayer();
			if (!shouldHavePet(myPlayer) || myPet.isCancelled()) {
				hideNow(myPlayer);
			}
		}
	}
	
	
	protected void validateAllPlayers() {
		for (Player myPlayer : plugin.getServer().getOnlinePlayers()) {
			if (shouldHavePet(myPlayer) && !isCancelled()) {
					try {
						showNow(myPlayer);
						cooldownPlayerDelay(myPlayer);
					} catch (Exception e) { 
						// Only Element creation error.
						// Spawn is done later in Balloon tick/move
						e.printStackTrace();
						hideNow(myPlayer);
						increasePlayerCancelScore(myPlayer);
					}
			} else {
				hideNow(myPlayer);
			}
		}
	}
	
	
	protected void doTimer() {
		if (balloonManager.isCancelled()) {
			logger.printWarning("Ballons are disabled, so we will disable pets now");
			disable();
		} else {
			validateAllPets();
			validateAllPlayers();
		}
	}
		

	@Override
    public void run() {
		try {
			doTimer();
			if (exceptionCooldown > 0) {
				exceptionCooldown = exceptionCooldown -1;
			} else {
				exceptionCount = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (exceptionCount >= exceptionQuota) {
				logger.printWarning("Multiple exceptions caught, disabling Pets. Please restart server");
				disable();				
			} else {
				exceptionCount = exceptionCount +1;
				exceptionCooldown = 10;
				logger.printWarning(String.format("Exception caught in Pet handling (%d/%d)", exceptionCount, exceptionQuota));			
			}
		}
	}

}
