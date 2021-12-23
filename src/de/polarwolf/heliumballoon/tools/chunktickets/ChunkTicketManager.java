package de.polarwolf.heliumballoon.tools.chunktickets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;

public class ChunkTicketManager {

	protected final Plugin plugin;
	protected List<ChunkTicket> chunkTickets = new ArrayList<>();

	public ChunkTicketManager(HeliumBalloonOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
	}

	public ChunkTicket findChunkTicket(Location location) {
		for (ChunkTicket myChunkTicket : chunkTickets) {
			if (myChunkTicket.hasLocation(location)) {
				return myChunkTicket;
			}
		}
		return null;
	}

	public List<ChunkTicket> findChunkTicketsForOwner(ChunkTicketOwner owner) {
		List<ChunkTicket> resultList = new ArrayList<>();
		for (ChunkTicket myChunkTicket : chunkTickets) {
			if (myChunkTicket.hasOwner(owner)) {
				resultList.add(myChunkTicket);
			}
		}
		return resultList;
	}

	public void addChunkTicket(ChunkTicketOwner owner, Location location) {
		ChunkTicket myChunkTicket = findChunkTicket(location);
		if (myChunkTicket == null) {
			myChunkTicket = new ChunkTicket(plugin, location);
			chunkTickets.add(myChunkTicket);
		}
		myChunkTicket.addOwner(owner);
	}

	public void removeChunkTicket(ChunkTicketOwner owner, Location location) {
		ChunkTicket myChunkTicket = findChunkTicket(location);
		if (myChunkTicket == null) {
			return;
		}
		myChunkTicket.removeOwner(owner);
		if (myChunkTicket.isEmpty()) {
			chunkTickets.remove(myChunkTicket);
		}
	}

	public void removeChunkTicket(ChunkTicketOwner owner) {
		List<ChunkTicket> ownerChunkTickets = findChunkTicketsForOwner(owner);
		for (ChunkTicket myChunkTicket : ownerChunkTickets) {
			myChunkTicket.removeOwner(owner);
			if (myChunkTicket.isEmpty()) {
				chunkTickets.remove(myChunkTicket);
			}
		}
	}

}
