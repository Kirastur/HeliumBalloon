package de.polarwolf.heliumballoon.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.polarwolf.heliumballoon.config.ConfigRule;
import de.polarwolf.heliumballoon.oscillators.Oscillator;

public class BalloonOscillatorCreateEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	protected final String oscillatorHint;
	protected final ConfigRule configRule;
	Oscillator oscillator = null;

	BalloonOscillatorCreateEvent(String oscillatorHint, ConfigRule configRule) {
		this.oscillatorHint = oscillatorHint;
		this.configRule = configRule;
	}

	public String getHint() {
		return oscillatorHint;
	}

	public ConfigRule getConfigRule() {
		return configRule;
	}

	public void setOscillator(Oscillator oscillator) {
		this.oscillator = oscillator;
	}

	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
