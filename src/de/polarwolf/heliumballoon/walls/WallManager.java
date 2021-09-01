package de.polarwolf.heliumballoon.walls;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.config.ConfigWall;
import de.polarwolf.heliumballoon.logger.HeliumLogger;

public class WallManager {
	
	protected final Plugin plugin;
	protected final HeliumLogger logger;
	protected final ConfigManager configManager;
	protected final BalloonManager balloonManager;
	protected List<Wall> walls = new ArrayList<>();
	

	public WallManager(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.configManager = orchestrator.getConfigManager();
		this.balloonManager = orchestrator.getBalloonManager();
		logger.printDebug("Wallmanager startet");
	}
	
	
	public List<Wall> enumWalls() {
		return new ArrayList<>(walls);
	}
	
	
	public int removeWalls(World world) {
		int count = 0;
		List<Wall> wallList = new ArrayList <>(walls);
		for (Wall myWall : wallList) {
			if (myWall.getWorld().equals(world)) {
				myWall.cancel();
				walls.remove(myWall);
				count = count +1;
			}
		}
		world.removePluginChunkTickets(plugin);
		return count;
	}
	
	
	public int addWalls(World world) {
		int count = 0;
		removeWalls(world);
		try {
			for (String myWallName : configManager.getWallNames()) {
				ConfigWall myConfigWall = configManager.findWall(myWallName);
				if (myConfigWall.isMatchingWorld(world)) {
					String wallName = myConfigWall.getName()+"."+world.getName();
					logger.printDebug(String.format("Crating WallBalloon: %s", wallName));
					Wall newWall = new Wall(plugin, balloonManager, myConfigWall, world);
					newWall.buildBalloon();
					walls.add(newWall);
					count = count +1;
				}
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			removeWalls(world);
		}
		return count;
	}
	
	
	public int reload() {
		int count = 0;
		for (World myWorld : plugin.getServer().getWorlds()) {
			int myCount = addWalls(myWorld);
			if (myCount > 0) {
				logger.printInfo(String.format("%d walls created in world: %s", myCount, myWorld.getName()));
			}
			count = count + myCount;
		}
		return count;
	}
		
}