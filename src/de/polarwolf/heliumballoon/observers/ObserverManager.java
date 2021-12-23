package de.polarwolf.heliumballoon.observers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.config.ConfigRule;
import de.polarwolf.heliumballoon.events.EventManager;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.oscillators.Oscillator;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifierManager;
import de.polarwolf.heliumballoon.tools.helium.HeliumLogger;

public class ObserverManager extends BukkitRunnable {

	protected final HeliumLogger logger;
	protected final EventManager eventManager;
	protected final SpawnModifierManager spawnModifierManager;
	protected final int exceptionQuota;
	protected int exceptionCount = 0;
	protected int exceptionCooldown = 0;
	protected List<Observer> observers = new ArrayList<>();

	public ObserverManager(HeliumBalloonOrchestrator orchestrator) {
		this.logger = orchestrator.getHeliumLogger();
		this.eventManager = orchestrator.getEventManager();
		this.spawnModifierManager = orchestrator.getSpawnModifierManager();
		this.exceptionQuota = logger.getExceptionQuota();
		runTaskTimer(orchestrator.getPlugin(), 1, 1);
	}

	public void disable() {
		try {
			cancel(); // Bukkit Task
			cancelAllObservers(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addObserver(Observer observer) throws BalloonException {
		if (this.isCancelled()) {
			observer.cancel();
			throw new BalloonException(null, "Balloons are already disabled, cannot add new observer",
					observer.getName());
		}
		if (observer.isCancelled()) {
			logger.printDebug(String.format("Can't add Observer, because it's already cancelled: %s", observer.getName()));
			return;
		}
		if (observers.contains(observer)) {
			logger.printDebug(String.format("Can't add Observer, because it already exists: %s", observer.getName()));
			return;
		}
		logger.printDebug(String.format("Adding new observer: %s", observer.getName()));
		observer.prepare(spawnModifierManager.getSpawnModifier());
		observers.add(observer);
	}

	protected void removeObserver(Observer observer) {
		logger.printDebug(String.format("Removing observer: %s", observer.getName()));
		observers.remove(observer);
	}

	public List<Observer> findObserversByPlayer(Player player) {
		List<Observer> playerObservers = new ArrayList<>();
		for (Observer myObserver : observers) {
			Player observerPlayer = myObserver.getPlayer();
			if ((observerPlayer != null) && (observerPlayer.equals(player))) {
				playerObservers.add(myObserver);
			}
		}
		return playerObservers;
	}

	public Observer findObserverByEntity(Entity entity) {
		for (Observer myObserver : observers) {
			if (myObserver.hasEntity(entity)) {
				return myObserver;
			}
		}
		return null;
	}

	public List<Observer> getAllObservers() {
		return new ArrayList<>(observers);
	}

	public void wakeupObserversByPlayer(Player player) {
		for (Observer myObserver : observers) {
			Player observerPlayer = myObserver.getPlayer();
			if ((observerPlayer != null) && (observerPlayer.equals(player))) {
				myObserver.wakeup();
			}
		}

	}

	public void cancelObserversByPlayer(Player player) {
		for (Observer myObserver : observers) {
			Player observerPlayer = myObserver.getPlayer();
			if ((observerPlayer != null) && (observerPlayer.equals(player))) {
				myObserver.cancel();
			}
		}
	}

	public void cancelObserversByWorld(World world) {
		for (Observer myObserver : observers) {
			World myWorld = myObserver.getWorld();
			if ((myWorld != null) && (myWorld.equals(world))) {
				myObserver.cancel();
			}
		}
	}

	public void cancelAllObservers(boolean forceRemove) {
		for (Observer myObserver : getAllObservers()) {
			myObserver.cancel();
			if (forceRemove) {
				removeObserver(myObserver);
			}
		}

	}

	public Oscillator getOscillator(ConfigRule configRule) {
		return eventManager.sendOscillatorCreateEvent(configRule.getOscillatorHint(), configRule);
	}

	protected void incrementOscillators() {
		Set<Oscillator> oscillatorSet = new HashSet<>();
		for (Observer myObserver : observers) {
			Oscillator oscillator = myObserver.getOscillator();
			if (oscillator != null) {
				oscillatorSet.add(oscillator);
			}
		}
		for (Oscillator myOscillator : oscillatorSet) {
			myOscillator.incrementCounters();
		}
	}

	protected void moveObservers() throws BalloonException {
		for (Observer myObserver : getAllObservers()) {
			try {
				String resultMessage = myObserver.move();
				if (resultMessage == null) {
					myObserver.cancel();
					removeObserver(myObserver);
				} else {
					if (!resultMessage.isEmpty()) {
						logger.printDebug(resultMessage);
					}
				}
			} catch (Exception e) {
				myObserver.cancel();
				removeObserver(myObserver);
				Player myPlayer = myObserver.getPlayer();
				String playerName = null;
				if (myPlayer != null) {
					myPlayer.sendMessage(logger.getPetErrorMessage().findLocalizedforSender(myPlayer));
					playerName = myPlayer.getName();
				}
				throw new BalloonException(myObserver.getFullName(), "Exception during spawn or move", playerName, e);
			}
		}
	}

	protected void handleTick() throws BalloonException {
		incrementOscillators();
		moveObservers();
	}

	@Override
	public void run() {
		try {
			handleTick();
			if (exceptionCooldown > 0) {
				exceptionCooldown = exceptionCooldown - 1;
			} else {
				exceptionCount = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (exceptionCount >= exceptionQuota) {
				logger.printWarning("Multiple exceptions caught, disabling Observers. Please restart server");
				disable();
			} else {
				exceptionCount = exceptionCount + 1;
				exceptionCooldown = 5 * 20;
				logger.printWarning(
						String.format("Exception caught in Observer handling (%d/%d)", exceptionCount, exceptionQuota));
			}
		}
	}

}
