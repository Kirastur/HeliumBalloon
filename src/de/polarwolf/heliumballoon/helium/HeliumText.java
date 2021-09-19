package de.polarwolf.heliumballoon.helium;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class HeliumText {
	
	private final String name;
	protected Map<String,String> textMap = new HashMap<>();

	
	public HeliumText(String name) {
		this.name = name;
	}
	
	
	public HeliumText(String name, String value) {
		this.name = name;
		addText("", value);
	}
	
	
	public HeliumText(String name, Map<String,String> params) {
		this.name = name;
		loadFromMap(params);		
	}
		

	public HeliumText(String name, ConfigurationSection fileSection) {
		this.name = name;
		loadFromConfig(fileSection);
	}
	
	
	public String getName() {
		return name;
	}
	
	
	protected void addText(String locale, String text) {
		textMap.put(locale, text);		
	}
	
	
	protected boolean matchKey(String key) {
		return (key.equals(getName()) || key.startsWith(getName()+"_")); 
	}
	
	
	protected String getLocaleFromKey(String key) {
		if (key.length() > getName().length()) {
			return key.substring(getName().length()+1, key.length());
		} else {
			return "";
		}
	}
	

	protected void loadFromMap(Map<String,String>params) {
		for (Entry<String,String> myEntry : params.entrySet()) {
			String myKey = myEntry.getKey();
			if (matchKey(myKey)) {
				String myValue = myEntry.getValue();
				String myLocale = getLocaleFromKey(myKey);
				addText(myLocale, myValue);
			}
		}
	}


	protected void loadFromConfig(ConfigurationSection fileSection) {
		for (String myKey : fileSection.getKeys(false)) {
			if (matchKey(myKey) && fileSection.contains(myKey, true) && fileSection.isString(myKey)) {
				String myValue = fileSection.getString(myKey);
				String mylocale = getLocaleFromKey(myKey);
				addText(mylocale, myValue);
			}
		}
	}
	
	
	public String findText() {
		return textMap.get("");	
	}
	
	
	public String findLocalizedText(String locale) {
		if (locale != null) {

			// 1st try: take the full language (e.g. "de_de")
			if (locale.length() >= 5) {
				String s = textMap.get(locale.substring(0, 5));
				if (s != null) {
					return s;
				}
			}
		
			// 2nd try: take the group language (e.g. "de")
			if (locale.length() >= 2) {
				String s = textMap.get(locale.substring(0, 2));
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
			s = findText();						
		}
		
		if (s == null) {
			s = "";
		}

		return s;
	}

}
