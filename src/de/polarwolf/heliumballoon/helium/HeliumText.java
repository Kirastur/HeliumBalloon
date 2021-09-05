package de.polarwolf.heliumballoon.helium;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class HeliumText {
	
	private final String name;
	protected Map<String,String> textMap = new HashMap<>();

	
	public HeliumText(String name) {
		this.name = name;
	}
		

	public HeliumText(ConfigurationSection fileSection, String paramName) {
		this.name = paramName;
		loadConfig(fileSection);
	}
	
	
	public String getName() {
		return name;
	}


	public void loadConfig(ConfigurationSection fileSection) {
		textMap.clear();
		for (String myKeyName : fileSection.getKeys(false)) {
			if (myKeyName.startsWith(getName()) && fileSection.contains(myKeyName, true) && fileSection.isString(myKeyName)) {
				String myValue = fileSection.getString(myKeyName);
				String locale = myKeyName.substring(getName().length(), myKeyName.length());
				textMap.put(locale, myValue);
			}
		}
	}
	
	
	public String findLocalizedText(String locale) {
		if (locale != null) {

			// 1st try: take the full language (e.g. "de_de")
			if (locale.length() >= 5) {
				String s = textMap.get("_" + locale.substring(0, 5));
				if (s != null) {
					return s;
				}
			}
		
			// 2nd try: take the group language (e.g. "de")
			if (locale.length() >= 2) {
				String s = textMap.get("_" + locale.substring(0, 2));
				if (s != null) {
					return s;
				}
			}
		}
		
		// No localized string found, return default
		return textMap.get("");
	}


	public String findLocalizedforSender(CommandSender sender) {
		String s;		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			String locale = player.getLocale();
			s = findLocalizedText(locale);
		} else {
			s = findLocalizedText(null);						
		}
		
		if (s == null) {
			s = "";
		}

		return s;
	}

}
