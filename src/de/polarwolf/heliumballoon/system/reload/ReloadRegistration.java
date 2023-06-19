package de.polarwolf.heliumballoon.system.reload;

import org.bukkit.plugin.Plugin;

public record ReloadRegistration(Plugin plugin, String fileName, String fileSection) {

}
