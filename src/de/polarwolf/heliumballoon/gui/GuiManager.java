package de.polarwolf.heliumballoon.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.config.ConfigGuiItem;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.pets.PetManager;

public class GuiManager {

	private final NamespacedKey templateNamespace;
	protected final ConfigManager configManager;
	protected final PetManager petManager;
	
	
	public GuiManager(HeliumBalloonOrchestrator orchestrator) {
		this.configManager = orchestrator.getConfigManager();
		this.petManager = orchestrator.getPetManager();
		templateNamespace = new NamespacedKey(orchestrator.getPlugin(), "guiitem-template");
	}
	
	
	public NamespacedKey getTemplateNamespace() {
		return templateNamespace;
	}


    public String findTemplateFromItemStack(ItemStack itemStack) {
    	ItemMeta itemMeta = itemStack.getItemMeta();
    	PersistentDataContainer container = itemMeta.getPersistentDataContainer();
    	if (!container.has(getTemplateNamespace() , PersistentDataType.STRING)) {
    		return null;
    	}
       	return container.get(getTemplateNamespace(), PersistentDataType.STRING);
    }
    

    public ItemStack createItemStack(Player player, ConfigGuiItem guiItem) {
		ItemStack itemStack = new ItemStack(guiItem.getIcon(), 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		String templateName = guiItem.getTemplate().getName();
		itemMeta.getPersistentDataContainer().set(getTemplateNamespace(), PersistentDataType.STRING, templateName);
		itemMeta.setDisplayName(guiItem.getTitle(player));
		List<String> lore = new ArrayList<>();
		lore.add(guiItem.getDescription(player));
		itemMeta.setLore(lore);								
		itemStack.setItemMeta(itemMeta);
		return itemStack;		
	}
	
	
	public List<ConfigGuiItem> enumGuiItemConfigs(Player player) {
		List<ConfigGuiItem> newGuiItems = new ArrayList<>();
		for (ConfigGuiItem myGuiItem : configManager.enumGuiItems()) {
			if ((player == null) || petManager.hasTemplatePermission(player, myGuiItem.getTemplate().getName())) {
				newGuiItems.add(myGuiItem);
			}
		}
		return newGuiItems;
	}

	
	public List<ItemStack> enumGuiItemStacks(Player player) {
		List<ItemStack> newItemStacks = new ArrayList<>();
		for (ConfigGuiItem myGuiItem : enumGuiItemConfigs(player)) {
			ItemStack myItemStack = createItemStack(player, myGuiItem);
			newItemStacks.add(myItemStack);
		}
		return newItemStacks;
	}
	
	
	public Inventory openGui(Player player) { 
		List<ItemStack> itemStackList = enumGuiItemStacks(player);
		if (itemStackList.isEmpty()) {
			return null;
		}

		int size = itemStackList.size();
		while ((size % 9) > 0) {
			size = size +1;
		}
		
		Inventory newInventory = Bukkit.createInventory(null, size, configManager.getGuiTitle(player));
		for (ItemStack myItemStack : enumGuiItemStacks(player)) {
			newInventory.addItem(myItemStack);
		}
		player.openInventory(newInventory);
		return newInventory;
	}

}
