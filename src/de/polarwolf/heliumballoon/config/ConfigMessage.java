package de.polarwolf.heliumballoon.config;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.messages.Message;
import de.polarwolf.heliumballoon.system.IntlText;

public class ConfigMessage {
	
	public static final String MESSAGE_FILE_NAME = "messages.yml";

	protected File customMessageFile;
	protected FileConfiguration messageFileConfiguration;
	
	
	public ConfigMessage(Plugin plugin) {
		if (!new File(plugin.getDataFolder(), MESSAGE_FILE_NAME).exists()) {
			plugin.saveResource(MESSAGE_FILE_NAME, false);
		}

		customMessageFile = new File(plugin.getDataFolder(), MESSAGE_FILE_NAME);
		messageFileConfiguration = YamlConfiguration.loadConfiguration(customMessageFile);	
	}
	
	
	public IntlText getMessage(Message messageId) {
		String messageName = messageId.getMessageName();
		return new IntlText(messageFileConfiguration, messageName);
	}
	
}
