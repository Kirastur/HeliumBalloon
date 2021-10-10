package de.polarwolf.heliumballoon.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import de.polarwolf.heliumballoon.api.HeliumBalloonOrchestrator;
import de.polarwolf.heliumballoon.config.ConfigGuiDeassign;
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
    
    
    protected ItemStack createItemStack(Material material, String actionName, String title, String description) {
		ItemStack itemStack = new ItemStack(material, 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.getPersistentDataContainer().set(getTemplateNamespace(), PersistentDataType.STRING, actionName);
		itemMeta.setDisplayName(title);
		if ((description != null) && !description.isEmpty()) {
			List<String> lore = new ArrayList<>();
			lore.add(description);
			itemMeta.setLore(lore);
		}
		itemStack.setItemMeta(itemMeta);
		return itemStack;		    	
    }
    

    public ItemStack createTemplateItemStack(Player player, ConfigGuiItem guiItem) {
    	Material material = guiItem.getIcon();
    	String templateName = guiItem.getTemplate().getName();
    	String title = guiItem.getTitle(player);
    	if ((title == null) || title.isEmpty()) {
    		title = guiItem.getName();
    	}
    	String description = guiItem.getDescription(player);
    	return createItemStack(material, templateName, title, description);
    }
    
    
    public ItemStack createDeassignItemStack(Player player) {
    	ConfigGuiDeassign configGuiDeassign= configManager.getGuiDeassign();
    	if (configGuiDeassign == null) {
    		return null;
    	}
    	Material material = Material.BARRIER;
    	String actionName = "!";
    	String title = configGuiDeassign.getTitle(player);
    	if ((title == null) || title.isEmpty()) {
    		title = configGuiDeassign.getName();
    	}
    	String description = configGuiDeassign.getDescription(player);
    	return createItemStack(material, actionName, title, description);
    }


	public List<ConfigGuiItem> getGuiItemConfigs(Player player) {
		List<ConfigGuiItem> newGuiItems = new ArrayList<>();
		for (ConfigGuiItem myGuiItem : configManager.getGuiItems()) {
			if ((player == null) || petManager.hasTemplatePermission(player, myGuiItem.getTemplate().getName())) {
				newGuiItems.add(myGuiItem);
			}
		}
		return newGuiItems;
	}

	
	public List<ItemStack> getGuiItemStacks(Player player) {
		List<ItemStack> newItemStacks = new ArrayList<>();
		for (ConfigGuiItem myGuiItem : getGuiItemConfigs(player)) {
			ItemStack myItemStack = createTemplateItemStack(player, myGuiItem);
			newItemStacks.add(myItemStack);
		}
		return newItemStacks;
	}
	
	
	public Inventory openGui(Player player) { 
		List<ItemStack> itemStackList = getGuiItemStacks(player);
		if (itemStackList.isEmpty()) {
			return null;
		}

		int size = itemStackList.size();
		if (configManager.getGuiDeassign() != null) {
			size = size +2;
		}
		while ((size % 9) > 0) {
			size = size +1;
		}
		
		Inventory newInventory = Bukkit.createInventory(null, size, configManager.getGuiTitle(player));
		for (ItemStack myItemStack : getGuiItemStacks(player)) {
			newInventory.addItem(myItemStack);
		}
		if (configManager.getGuiDeassign() != null) {
			ItemStack deassignItemStack = createDeassignItemStack(player);
			newInventory.setItem(size-1, deassignItemStack);
		}
		player.openInventory(newInventory);
		return newInventory;
	}

}
