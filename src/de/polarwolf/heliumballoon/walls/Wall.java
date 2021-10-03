package de.polarwolf.heliumballoon.walls;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.balloons.BalloonManager;
import de.polarwolf.heliumballoon.balloons.WallBalloon;
import de.polarwolf.heliumballoon.config.ConfigWall;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.oscillators.VerticalOscillator;

public class Wall {
	
	protected final Plugin plugin;
	protected final BalloonManager balloonManager;
	protected final ConfigWall config;
	protected final Oscillator oscillator;
	protected final World world;
	protected WallBalloon balloon = null;
	
	
	public Wall(Plugin plugin, BalloonManager balloonManager, ConfigWall config, World world) {
		this.plugin = plugin;
		this.balloonManager = balloonManager;
		this.config = config;
		this.world = world;
		if (config.getTemplate().isOscillating()) {
			oscillator = new VerticalOscillator(config.getTemplate().getRule());
		} else {
			oscillator = null;
		}		
	}
	
	
	public World getWorld() {
		return world;
	}


	public void buildBalloon() throws BalloonException {
		if (balloon != null) {
			return;
		}
		Location location = config.getAbsolutePosition().toLocation(world);
		int chunkX = location.getBlockX() / 16;
		int chunkZ = location.getBlockZ() / 16;
		world.addPluginChunkTicket(chunkX, chunkZ, plugin);
		balloon = new WallBalloon(config, location, oscillator);
		try {
			balloonManager.addBalloon(balloon);
		} catch (Exception e) {
			balloon.cancel();
			throw e;
		}
	}
	
	
	public void cancel() {
		if (balloon != null) {
			balloon.cancel();
		}
	}

}
