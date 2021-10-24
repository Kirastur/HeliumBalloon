package de.polarwolf.heliumballoon.placings;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.chunktickets.ChunkTicketManager;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.config.ConfigPlaceableBalloonSet;
import de.polarwolf.heliumballoon.helium.HeliumLogger;

public abstract class PlacingManager {

	protected final Plugin plugin;
	protected final HeliumLogger logger;
	protected final ChunkTicketManager chunkTicketManager;
	protected final ConfigManager configManager;
	protected final BalloonManager balloonManager;
	protected List<Placing> placings = new ArrayList<>();
	

	protected PlacingManager(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.logger = orchestrator.getHeliumLogger();
		this.chunkTicketManager = orchestrator.getChunkTicketManager();
		this.configManager = orchestrator.getConfigManager();
		this.balloonManager = orchestrator.getBalloonManager();
	}
	
	
	protected abstract Set<String> getBalloonSetNames();
	
	protected abstract ConfigPlaceableBalloonSet findBalloonSet(String placingName);
	
	protected abstract Placing createPlacing(ConfigPlaceableBalloonSet configPlaceableBalloonSet, World world);
	
	
	public List<Placing> getPlacings() {
		return new ArrayList<>(placings);
	}
	
	
	public int removePlacings(World world) {
		int count = 0;
		List<Placing> placingList = new ArrayList <>(placings);
		for (Placing myPlacing : placingList) {
			if (myPlacing.getWorld().equals(world)) {
				myPlacing.cancel();
				placings.remove(myPlacing);
				count = count +1;
			}
		}
		return count;
	}
	
	
	public int addPlacings(World world) {
		int count = 0;
		removePlacings(world);
		try {
			for (String myPlacingName : getBalloonSetNames()) {
				ConfigPlaceableBalloonSet configPlaceableBalloonSet = findBalloonSet(myPlacingName);
				if (configPlaceableBalloonSet.isMatchingWorld(world)) {
					String fullPlacingName = configPlaceableBalloonSet.getName()+"."+world.getName();
					logger.printDebug(String.format("Crating PlacingBalloon: %s", fullPlacingName));
					Placing newPlacing = createPlacing(configPlaceableBalloonSet, world);
					placings.add(newPlacing);
					newPlacing.buildBalloons();
					count = count +1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			removePlacings(world);
		}
		return count;
	}
	
	
	public int reload() {
		int count = 0;
		for (World myWorld : plugin.getServer().getWorlds()) {
			int myCount = addPlacings(myWorld);
			if (myCount > 0) {
				logger.printInfo(String.format("%d placings created in world: %s", myCount, myWorld.getName()));
			}
			count = count + myCount;
		}
		return count;
	}
		
}
