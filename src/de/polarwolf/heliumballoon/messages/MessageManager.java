package de.polarwolf.heliumballoon.messages;

import org.bukkit.command.CommandSender;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.helium.HeliumLogger;
import de.polarwolf.heliumballoon.helium.HeliumText;

public class MessageManager {

	protected final ConfigManager configManager;

	public MessageManager(HeliumBalloonOrchestrator orchestrator) {
		this.configManager = orchestrator.getConfigManager();
		HeliumLogger logger = orchestrator.getHeliumLogger();
		logger.setPetErrorMessage(configManager.getMessage(Message.PET_ERROR));
	}

	public String getMessage(CommandSender sender, Message messageId, String replacementString) {
		HeliumText heliumMessage = configManager.getMessage(messageId);
		String messageText = heliumMessage.findLocalizedforSender(sender);
		if ((messageText == null) || messageText.isEmpty()) {
			messageText = messageId.getMessageText();
		}
		if (replacementString != null) {
			messageText = messageText.replace("%s", replacementString);
		}
		return messageText;
	}

}
