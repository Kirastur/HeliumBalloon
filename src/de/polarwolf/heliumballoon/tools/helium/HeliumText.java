package de.polarwolf.heliumballoon.tools.helium;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class HeliumText {

	private final String name;
	protected Map<String, String> textMap = new HashMap<>();

	public HeliumText(String name) {
		this.name = name;
	}

	public HeliumText(String name, String value) {
		this.name = name;
		addText("", value);
	}

	public HeliumText(String name, Map<String, String> params) {
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
		return (key.equals(getName()) || key.startsWith(getName() + "_"));
	}

	protected String getLocaleFromKey(String key) {
		if (key.length() > getName().length()) {
			return key.substring(getName().length() + 1, key.length());
		} else {
			return "";
		}
	}

	protected void loadFromMap(Map<String, String> params) {
		for (Entry<String, String> myEntry : params.entrySet()) {
			String myKey = myEntry.getKey();
			if (matchKey(myKey)) {
				String myValue = myEntry.getValue();
				String myLocale = getLocaleFromKey(myKey);
				addText(myLocale, myValue);
			}
		}
	}

	protected boolean isValidDataType(String key, ConfigurationSection fileSection) {
		return (fileSection.isBoolean(key) || fileSection.isInt(key) || fileSection.isDouble(key)
				|| fileSection.isString(key));
	}

	protected void loadFromConfig(ConfigurationSection fileSection) {
		for (String myKey : fileSection.getKeys(false)) {
			if (matchKey(myKey) && fileSection.contains(myKey, true) && isValidDataType(myKey, fileSection)) {
				String myValue = fileSection.getString(myKey);
				String myLocale = getLocaleFromKey(myKey);
				addText(myLocale, myValue);
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
		if (sender instanceof Player player) {
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

	public static String escapeUTF8(String intlString) {
		StringBuilder b = new StringBuilder();
		for (char c : intlString.toCharArray()) {
			if (c >= 128)
				b.append("\\u").append(String.format("%04X", (int) c));
			else {
				if ((c == '\\') || (c == '\"'))
					b.append('\\');
				b.append(c);
			}
		}
		return b.toString();
	}

	public Map<String, String> dump() {
		Map<String, String> newMapDump = new TreeMap<>();
		for (Entry<String, String> myEntry : textMap.entrySet()) {
			String myKey = myEntry.getKey();
			if (!myKey.isEmpty())
				myKey = "_" + myKey;
			myKey = name + myKey;
			String myValue = escapeUTF8(myEntry.getValue());
			newMapDump.put(myKey, myValue);
		}
		return newMapDump;
	}

}
