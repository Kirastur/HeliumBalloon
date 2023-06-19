package de.polarwolf.heliumballoon.behavior;

import java.util.List;

import org.bukkit.block.data.BlockData;

import de.polarwolf.heliumballoon.behavior.observers.Observer;
import de.polarwolf.heliumballoon.behavior.oscillators.Oscillator;
import de.polarwolf.heliumballoon.config.balloons.ConfigBalloon;
import de.polarwolf.heliumballoon.config.rules.ConfigRule;
import de.polarwolf.heliumballoon.config.templates.ConfigElement;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.tools.helium.HeliumName;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;

public interface BehaviorDefinition extends HeliumName {

	public List<HeliumParam> getAdditionalRuleParams();

	public void modifyElement(Element element);

	public void modifyBlockData(Element element, BlockData blockData);

	public Oscillator createOscillator(ConfigRule rule);

	public Observer createObserver(ConfigBalloon configBalloon, ConfigElement configElement, Oscillator oscillator,
			Object target) throws BalloonException;

}
