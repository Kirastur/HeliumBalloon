package de.polarwolf.heliumballoon.system.listener;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.balloons.pets.PetManager;
import de.polarwolf.heliumballoon.gui.GuiManager;

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

	protected boolean containsOnlyBalloonActionItems(Inventory inventory) {
		boolean hasActionItems = false;
		boolean hasNonActionItems = false;
		for (ItemStack myItemStack : inventory.getContents()) {
			if (myItemStack != null) {
				String actionName = guiManager.findActionFromItemStack(myItemStack);
				if ((actionName == null) || actionName.isEmpty()) {
					hasNonActionItems = true;
				} else {
					hasActionItems = true;
				}
			}
		}
		return (hasActionItems && !hasNonActionItems);
	}

	protected void handleAssign(Player player, String petName) {
		petManager.assignPersistentPet(player, petName);
		player.closeInventory();
	}

	protected void handleDeassign(Player player) {
		petManager.deassignPersistentPet(player);
		player.closeInventory();
	}

	protected void handleInventoryClickEvent(InventoryClickEvent event) {
		if (containsOnlyBalloonActionItems(event.getInventory())) {
			event.setCancelled(true);
		}

		ItemStack clickedItem = event.getCurrentItem();
		if (clickedItem == null || clickedItem.getType().isAir()) {
			return;
		}

		String balloonActionName = guiManager.findActionFromItemStack(clickedItem);
		if ((balloonActionName == null) || (balloonActionName.isEmpty())) {
			return;
		}

		event.setCancelled(true);
		if (event.getAction() != InventoryAction.PICKUP_ALL) {
			return;
		}

		HumanEntity humanEntity = event.getWhoClicked();
		if (!(humanEntity instanceof Player)) {
			return;
		}
		Player player = (Player) humanEntity;

		if (balloonActionName.equals(GuiManager.ACTION_ASSIGN)) {
			String petName = guiManager.findPetFromItemStack(clickedItem);
			handleAssign(player, petName);
		}
		if (balloonActionName.equals(GuiManager.ACTION_DEASSIGN)) {
			handleDeassign(player);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClickEvent(InventoryClickEvent event) {
		try {
			handleInventoryClickEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
