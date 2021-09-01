package de.polarwolf.heliumballoon.balloons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.logger.HeliumLogger;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifierManager;

public class BalloonManager extends BukkitRunnable {
	
	protected final HeliumLogger logger;
	protected final SpawnModifierManager spawnModifierManager;
	protected final int exceptionQuota; 
	protected int exceptionCount = 0;
	protected int exceptionCooldown = 0;
	protected List<Balloon> balloons = new ArrayList<>();
	
	
	public BalloonManager(HeliumBalloonOrchestrator orchestrator) {
		this.logger = orchestrator.getHeliumLogger();
		this.spawnModifierManager = orchestrator.getSpawnModifierManager();
		this.exceptionQuota = logger.getExceptionQuota();
		runTaskTimer(orchestrator.getPlugin(), 1, 1);
	}
	
	
	public void disable() {
		try {
			cancel(); // Bukkit Task
			cancelAllBalloons(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void addBalloon(Balloon balloon) throws BalloonException {
		if (this.isCancelled()) {
			balloon.cancel();
			throw new BalloonException(null, "Balloons are already disabled, cannot add new balloon", balloon.getName());
		}
		logger.printDebug(String.format("Adding new balloon: %s", balloon.getName()));
		balloon.prepare(spawnModifierManager);
		balloons.add(balloon);
	}
	
	
	protected void removeBalloon(Balloon balloon) {
		logger.printDebug (String.format("Removing Balloon: %s", balloon.getName()));
		balloons.remove(balloon);
	}
	
	
	public List<Balloon> findBalloonsByPlayer(Player player) {
		List<Balloon> playerBalloons = new ArrayList<>();
		for (Balloon myBalloon : balloons) {
			Player balloonPlayer = myBalloon.getPlayer();
			if ((balloonPlayer != null) && (balloonPlayer.equals(player))) {
				playerBalloons.add(myBalloon);
			}
		}
		return playerBalloons;
	}
	
	
	public Balloon findBalloonByEntity(Entity entity) {
		for (Balloon myBalloon : balloons) {
			if (myBalloon.hasEntity(entity)) {
				return myBalloon;
			}
		}
		return null;		
	}
	
	
	public List<Balloon> enumAllBalloons() {
		return new ArrayList<>(balloons);
	}
	
	
	public void cancelBalloonsByPlayer(Player player) {
		for (Balloon myBalloon : balloons) {
			Player balloonPlayer = myBalloon.getPlayer();
			if ((balloonPlayer != null) && (balloonPlayer.equals(player))) {
				myBalloon.cancel();
			}
		}
	}
	
	
	public void cancelBalloonsByWorld(World world) {
		for (Balloon myBalloon : balloons) {
			World myWorld = myBalloon.getWorld();
			if ((myWorld != null) && (myWorld.equals(world))) {
				myBalloon.cancel();
			}
		}
	}

	
	public void cancelAllBalloons(boolean forceRemove) {
		for (Balloon myBalloon : enumAllBalloons()) {
			myBalloon.cancel();
			if (forceRemove) {
				removeBalloon(myBalloon);							
			}
		}
		
	}

	
	protected void handleTick() throws BalloonException {
		List<Balloon> balloonList = new ArrayList<>(balloons);
		for (Balloon myBalloon : balloonList) {
			try {
				String resultMessage = myBalloon.move();
				if (resultMessage == null) {
					myBalloon.cancel();
					removeBalloon(myBalloon);				
				} else {
					if (!resultMessage.isEmpty()) {
						logger.printDebug(resultMessage);
					}
				}
			} catch (Exception e) {
				logger.printDebug("Exception during spawn or move");
				myBalloon.cancel();
				removeBalloon(myBalloon);
				Player myPlayer = myBalloon.getPlayer();
				if (myPlayer != null) {
					myPlayer.sendMessage(logger.getPetErrorMessage().findLocalizedforSender(myPlayer));
				}
				throw e;				
			}
		}
	}
	

	@Override
    public void run() {
		try {
			handleTick();
			if (exceptionCooldown > 0) {
				exceptionCooldown = exceptionCooldown -1;
			} else {
				exceptionCount = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (exceptionCount >= exceptionQuota) {
				logger.printWarning("Multiple exceptions caught, disabling Balloons. Please restart server");
				disable();
			} else {
				exceptionCount = exceptionCount +1;
				exceptionCooldown = 5*20;
				logger.printWarning(String.format("Exception caught in Balloon handling (%d/%d)", exceptionCount, exceptionQuota));
			}
		}
	}

}
