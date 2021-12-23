package de.polarwolf.heliumballoon.system.players;

import java.time.Instant;
import java.util.UUID;

public class PlayerPersistentData {

	private final UUID playerUUID;
	private String name;
	private Instant lastSeen;
	private String petName;

	public PlayerPersistentData(UUID playerUUID) {
		this.playerUUID = playerUUID;
	}

	public UUID getplayerUUID() {
		return playerUUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(Instant lastSeen) {
		this.lastSeen = lastSeen;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

}
