package de.polarwolf.heliumballoon.balloons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.elements.CompoundElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;

public class CompoundPlayerBalloon extends PlayerBalloon {
	
	public static final String NAME_COMPOUND = "compound";
	protected List<Location> targetLocationQueue = new ArrayList<>();


	public CompoundPlayerBalloon(Player player, ConfigTemplate template) {
		super(player, template);
	}

	
	@Override
	protected Element createElement(SpawnModifier spawnModifier) {
		return new CompoundElement (getPlayer(), getTemplate(), spawnModifier);
	}
	
	
	@Override
	public String getName() {
		if (getPlayer() == null) {
			return getTemplate().getName()+"."+NAME_COMPOUND;
		} else {
			return getTemplate().getName()+"."+NAME_COMPOUND+"."+getPlayer().getName();
		}		
	}


	@Override
	public Location getTargetLocation() {
		if (!targetLocationQueue.isEmpty()) {
			return targetLocationQueue.get(0);
		} else {
			return super.getTargetLocation();
		}
	}

	
	@Override
	public String move() throws BalloonException {
		targetLocationQueue.add(super.getTargetLocation());
		if (targetLocationQueue.size() > 4) {
			targetLocationQueue.remove(0);
		}
		return super.move();
	}
}
