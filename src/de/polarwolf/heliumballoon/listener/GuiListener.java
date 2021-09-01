package de.polarwolf.heliumballoon.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.gui.GuiManager;
import de.polarwolf.heliumballoon.pets.PetManager;

public class GuiListener implements Listener {
	
	protected final PetManager petManager;
	protected final GuiManager guiManager;
	
	
	public GuiListener(HeliumBalloonOrchestrator orchestrator) {
		Plugin plugin = orchestrator.getPlugin();
		this.petManager = orchestrator.getPetManager();
		this.guiManager = orchestrator.getGuiManager();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	public void unregisterListener() {
		HandlerList.unregisterAll(this);
	}
	

	protected void handleInventoryClickEvent(final InventoryClickEvent e) {
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) {
        	return;
        }
        
        String templateName = guiManager.findTemplateFromItemStack(clickedItem);
        if ((templateName == null) || (templateName.isEmpty())) {
        	return;
        }
        
        Player player = (Player) e.getWhoClicked();
        if (petManager.hasTemplatePermission(player, templateName)) {
        	petManager.assign(player, templateName);
        }
        
        player.closeInventory();
        e.setCancelled(true);
	}        


	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClick(InventoryClickEvent event) {
		try {
			handleInventoryClickEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
