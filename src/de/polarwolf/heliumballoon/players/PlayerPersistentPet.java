package de.polarwolf.heliumballoon.players;

import java.time.Instant;
import java.util.UUID;

public class PlayerPersistentPet {
	
	private final UUID playerUUID;
	private String name;
	private Instant lastSeen;
	private String templateName;
	
	
	public PlayerPersistentPet(UUID playerUUID) {
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


	public String getTemplateName() {
		return templateName;
	}


	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

}
