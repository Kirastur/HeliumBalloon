package de.polarwolf.heliumballoon.tools.chunktickets;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class ChunkTicket {

	private final int chunkX;
	private final int chunkZ;
	private final World world;
	protected final Plugin plugin;
	protected Set<ChunkTicketOwner> owners = new HashSet<>();

	public ChunkTicket(Plugin plugin, Location location) {
		this.chunkX = cor2chunk(location.getBlockX());
		this.chunkZ = cor2chunk(location.getBlockZ());
		this.world = location.getWorld();
		this.plugin = plugin;
	}

	public static int cor2chunk(int cor) {
		return Math.floorDiv(cor, 16);
	}

	public int getChunkX() {
		return chunkX;
	}

	public int getChunkZ() {
		return chunkZ;
	}

	public World getWorld() {
		return world;
	}

	public boolean hasLocation(Location testLocation) {
		int clX = cor2chunk(testLocation.getBlockX());
		int clZ = cor2chunk(testLocation.getBlockZ());
		return ((clX == getChunkX()) && (clZ == getChunkZ()) && testLocation.getWorld().equals(world));
	}

	public boolean hasOwner(ChunkTicketOwner testOwner) {
		return owners.contains(testOwner);
	}

	public boolean isEmpty() {
		return owners.isEmpty();
	}

	protected void addMinecraftChunkTicket() {
		world.addPluginChunkTicket(chunkX, chunkZ, plugin);
	}

	protected void removeMinecraftChunkTicket() {
		world.removePluginChunkTicket(chunkX, chunkZ, plugin);
	}

	public void addOwner(ChunkTicketOwner newOwner) {
		boolean oldIsEmpty = isEmpty();
		owners.add(newOwner);
		boolean newIsEmpty = isEmpty();
		if (oldIsEmpty && !newIsEmpty) {
			addMinecraftChunkTicket();
		}
	}

	public void removeOwner(ChunkTicketOwner oldOwner) {
		boolean oldIsEmpty = isEmpty();
		owners.remove(oldOwner);
		boolean newIsEmpty = isEmpty();
		if (newIsEmpty && !oldIsEmpty) {
			removeMinecraftChunkTicket();
		}
	}

}
