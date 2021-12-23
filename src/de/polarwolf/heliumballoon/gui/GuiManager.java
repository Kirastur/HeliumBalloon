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
import de.polarwolf.heliumballoon.balloons.pets.PetManager;
import de.polarwolf.heliumballoon.config.ConfigGuiHelperItem;
import de.polarwolf.heliumballoon.config.ConfigGuiMenu;
import de.polarwolf.heliumballoon.config.ConfigManager;
import de.polarwolf.heliumballoon.config.ConfigPet;

public class GuiManager {

	public static final String ACTION_ASSIGN = "assign";
	public static final String ACTION_DEASSIGN = "deassign";
	public static final String ACTION_FILLER = "filler";

	private final NamespacedKey actionNamespace;
	private final NamespacedKey petNamespace;
	protected final ConfigManager configManager;
	protected final PetManager petManager;

	public GuiManager(HeliumBalloonOrchestrator orchestrator) {
		this.configManager = orchestrator.getConfigManager();
		this.petManager = orchestrator.getPetManager();
		actionNamespace = new NamespacedKey(orchestrator.getPlugin(), "heliumballoon-action");
		petNamespace = new NamespacedKey(orchestrator.getPlugin(), "heliumballoon-pet");
	}

	public NamespacedKey getActionNamespace() {
		return actionNamespace;
	}

	public NamespacedKey getPetNamespace() {
		return petNamespace;
	}

	public String findActionFromItemStack(ItemStack itemStack) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (itemMeta == null) {
			return null;
		}
		PersistentDataContainer container = itemMeta.getPersistentDataContainer();
		if (!container.has(getActionNamespace(), PersistentDataType.STRING)) {
			return null;
		}
		return container.get(getActionNamespace(), PersistentDataType.STRING);
	}

	public String findPetFromItemStack(ItemStack itemStack) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (itemMeta == null) {
			return null;
		}
		PersistentDataContainer container = itemMeta.getPersistentDataContainer();
		if (!container.has(getPetNamespace(), PersistentDataType.STRING)) {
			return null;
		}
		return container.get(getPetNamespace(), PersistentDataType.STRING);
	}

	protected ItemStack createItemStack(Material material, String actionName, String petName, String title,
			String description) {
		ItemStack itemStack = new ItemStack(material, 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.getPersistentDataContainer().set(getActionNamespace(), PersistentDataType.STRING, actionName);
		if ((petName != null) && !petName.isEmpty()) {
			itemMeta.getPersistentDataContainer().set(getPetNamespace(), PersistentDataType.STRING, petName);
		}
		itemMeta.setDisplayName(title);
		if ((description != null) && !description.isEmpty()) {
			List<String> lore = new ArrayList<>();
			lore.add(description);
			itemMeta.setLore(lore);
		}
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public ItemStack createPetItemStack(Player player, ConfigPet configPet) {
		Material material = configPet.getIcon();
		String petName = configPet.getName();
		String title = configPet.getTitle(player);
		if ((title == null) || title.isEmpty()) {
			title = configPet.getName();
		}
		String description = configPet.getDescription(player);
		return createItemStack(material, ACTION_ASSIGN, petName, title, description);
	}

	public ItemStack createDeassignItemStack(Player player) {
		ConfigGuiHelperItem configGuiDeassign = configManager.getGuiMenu().getGuiDeassign();
		if (configGuiDeassign == null) {
			return null;
		}
		Material material = configGuiDeassign.getIcon();
		String title = configGuiDeassign.getTitle(player);
		if ((title == null) || title.isEmpty()) {
			title = configGuiDeassign.getName();
		}
		String description = configGuiDeassign.getDescription(player);
		return createItemStack(material, ACTION_DEASSIGN, null, title, description);
	}

	public ItemStack createFillerItemStack(Player player) {
		ConfigGuiHelperItem configGuiFiller = configManager.getGuiMenu().getGuiFiller();
		if (configGuiFiller == null) {
			return null;
		}
		Material material = configGuiFiller.getIcon();
		String title = configGuiFiller.getTitle(player);
		if ((title == null) || title.isEmpty()) {
			title = " ";
		}
		String description = configGuiFiller.getDescription(player);
		return createItemStack(material, ACTION_FILLER, null, title, description);
	}

	public List<ItemStack> getPetsItemStacks(Player player) {
		List<ItemStack> newItemStacks = new ArrayList<>();
		for (ConfigPet myConfigPet : petManager.getConfigPetsForPlayer(player)) {
			ItemStack myItemStack = createPetItemStack(player, myConfigPet);
			newItemStacks.add(myItemStack);
		}
		return newItemStacks;
	}

	protected List<ItemStack> getAllItemStacks(Player player) {
		boolean hasDeassign = (configManager.getGuiMenu().getGuiDeassign() != null);
		List<ItemStack> newAllItemStacks = new ArrayList<>(getPetsItemStacks(player));
		int size = newAllItemStacks.size();
		if (hasDeassign) {
			size = size + 2;
		}
		while ((size % 9) > 0) {
			size = size + 1;
		}
		if (hasDeassign) {
			size = size - 1;
		}
		for (int i = newAllItemStacks.size(); i < size; i++) {
			newAllItemStacks.add(createFillerItemStack(player));
		}
		if (hasDeassign) {
			newAllItemStacks.add(createDeassignItemStack(player));
		}
		return newAllItemStacks;
	}

	public Inventory openGui(Player player) {
		ConfigGuiMenu configGuiMenu = configManager.getGuiMenu();
		if (configGuiMenu == null) {
			return null;
		}
		List<ItemStack> itemStackList = getAllItemStacks(player);
		if (itemStackList.isEmpty()) {
			return null;
		}

		Inventory newInventory = Bukkit.createInventory(null, itemStackList.size(), configGuiMenu.getGuiTitle(player));
		for (int i = 0; i < itemStackList.size(); i++) {
			ItemStack myItemStack = itemStackList.get(i);
			if (myItemStack != null) {
				newInventory.setItem(i, itemStackList.get(i));
			}
		}

		player.openInventory(newInventory);
		return newInventory;
	}

}
