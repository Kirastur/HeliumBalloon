package de.polarwolf.heliumballoon.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.polarwolf.heliumballoon.api.HeliumBalloonAPI;
import de.polarwolf.heliumballoon.api.HeliumBalloonProvider;
import de.polarwolf.heliumballoon.config.ConfigPet;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.main.Main;
import de.polarwolf.heliumballoon.messages.Message;

public class BalloonCommand implements CommandExecutor {

	public static final String COMMAND_PERMISSION_PREFIX = "balloon.command.";
	public static final String GUINOPARAM_PERMISSION = "balloon.guinoparam";

	protected final Main main;
	protected final String commandName;
	protected BalloonTabCompleter balloonTabCompleter;

	public BalloonCommand(Main main, String commandName) {
		this.main = main;
		this.commandName = commandName;
		main.getCommand(commandName).setExecutor(this);
		balloonTabCompleter = new BalloonTabCompleter(main, this);
	}

	public String getCommandName() {
		return commandName;
	}

	public boolean hasActionPermission(CommandSender sender, Action action) {
		return sender.hasPermission(COMMAND_PERMISSION_PREFIX + action.getCommand());
	}

	public List<String> listActions(CommandSender sender) {
		List<String> names = new ArrayList<>();
		for (Action myAction : Action.values()) {
			boolean isInclude = false;
			if (sender != null) {
				if ((sender instanceof Player) || !myAction.isOnlyForPlayer()) {
					isInclude = true;
				}
				if (!hasActionPermission(sender, myAction)) {
					isInclude = false;
				}
			} else {
				isInclude = true;
			}
			if (isInclude) {
				names.add(myAction.getCommand());
			}
		}
		return names;
	}

	public List<String> listPets(HeliumBalloonAPI api, CommandSender sender) {
		List<String> names = new ArrayList<>();
		for (String myPet : api.getConfigPetNames()) {
			if ((!(sender instanceof Player)) || api.hasPetPermission((Player) sender, myPet)) {
				names.add(myPet);
			}
		}
		return names;
	}

	protected void cmdHelp(HeliumBalloonAPI api, CommandSender sender) {
		String s = String.join(" ", listActions(sender));
		sender.sendMessage(api.getMessage(sender, Message.ALLOWED_ACTIONS, s));
	}

	protected void cmdDebugenable(HeliumBalloonAPI api) {
		api.setDebug(true);
	}

	protected void cmdDebugdisable(HeliumBalloonAPI api) {
		api.setDebug(false);
	}

	protected void cmdAssign(HeliumBalloonAPI api, CommandSender sender, ConfigPet configPet) {
		Player player = (Player) sender;
		if (!api.hasPetPermission(player, configPet.getName())) {
			sender.sendMessage(api.getMessage(sender, Message.MISSING_PET_PERMISSION, null));
			return;
		}
		if (configPet.findTemplate(player.getWorld()) == null) {
			sender.sendMessage(api.getMessage(sender, Message.WRONG_WORLD, null));
			return;
		}
		cmdAssignother(api, sender, player, configPet);
	}

	protected void cmdAssignother(HeliumBalloonAPI api, CommandSender sender, Player player, ConfigPet pet) {
		boolean success = api.assignPersistentPet(player, pet.getName());
		if (!success) {
			sender.sendMessage(api.getMessage(sender, Message.UNKNOWN_ERROR, null));
		}
	}

	protected void cmdDeassign(HeliumBalloonAPI api, CommandSender sender) {
		Player player = (Player) sender;
		cmdDeassignother(api, sender, player);
	}

	protected void cmdDeassignother(HeliumBalloonAPI api, CommandSender sender, Player player) {
		boolean success = api.deassignPersistentPet(player);
		if (!success) {
			sender.sendMessage(api.getMessage(sender, Message.UNKNOWN_ERROR, null));
		}
	}

	protected void cmdCheck(HeliumBalloonAPI api, CommandSender sender) {
		Player player = (Player) sender;
		cmdCheckother(api, sender, player);
	}

	protected void cmdCheckother(HeliumBalloonAPI api, CommandSender sender, Player player) {
		ConfigPet configPet = api.findPersistentPetForPlayer(player);
		if (configPet == null) {
			sender.sendMessage(api.getMessage(sender, Message.NO_PET_ASSIGNED, null));
		} else {
			sender.sendMessage(api.getMessage(sender, Message.ASSIGNED_PET, configPet.getName()));
		}
	}

	protected void cmdList(HeliumBalloonAPI api, CommandSender sender) {
		List<String> names = listPets(api, sender);
		String s = String.join(" ", names);
		sender.sendMessage(api.getMessage(sender, Message.ALLOWED_PETS_PLAYER, s));
	}

	protected void cmdListall(HeliumBalloonAPI api, CommandSender sender) {
		List<String> names = listPets(api, null);
		String s = String.join(" ", names);
		sender.sendMessage(api.getMessage(sender, Message.ALLOWED_PETS_OP, s));
	}

	protected void cmdInfo(HeliumBalloonAPI api, CommandSender sender) {
		if (api.isDisabled()) {
			sender.sendMessage(api.getMessage(sender, Message.ALREADY_DISABLED, null));
		} else {
			int count = api.getAllBalloons().size();
			sender.sendMessage(api.getMessage(sender, Message.ACTIVE_BALLOONS, Integer.toString(count)));
		}
	}

	protected void cmdReload(HeliumBalloonAPI api, CommandSender sender) throws BalloonException {
		api.reload();
		sender.sendMessage(api.getMessage(sender, Message.RELOAD_STATUS, null));
	}

	protected void cmdPurge(HeliumBalloonAPI api, CommandSender sender) {
		int count = api.purgeOldPlayers();
		sender.sendMessage(api.getMessage(sender, Message.PURGE_RESULT, Integer.toString(count)));
	}

	protected void cmdGUI(HeliumBalloonAPI api, CommandSender sender) {
		Player player = (Player) sender;
		Inventory inventory = api.openGui(player);
		if (inventory == null) {
			sender.sendMessage(api.getMessage(sender, Message.NO_PETS_AVAIL, null));
		}
	}

	protected void dispatchCommand(HeliumBalloonAPI api, CommandSender sender, Action action, Player player,
			ConfigPet configPet) {
		try {
			switch (action) {
			case HELP:
				cmdHelp(api, sender);
				break;
			case DEBUGENABLE:
				cmdDebugenable(api);
				break;
			case DEBUGDISABLE:
				cmdDebugdisable(api);
				break;
			case ASSIGN:
				cmdAssign(api, sender, configPet);
				break;
			case ASSIGNOTHER:
				cmdAssignother(api, sender, player, configPet);
				break;
			case DEASSIGN:
				cmdDeassign(api, sender);
				break;
			case DEASSIGNOTHER:
				cmdDeassignother(api, sender, player);
				break;
			case CHECK:
				cmdCheck(api, sender);
				break;
			case CHECKOTHER:
				cmdCheckother(api, sender, player);
				break;
			case LIST:
				cmdList(api, sender);
				break;
			case LISTALL:
				cmdListall(api, sender);
				break;
			case INFO:
				cmdInfo(api, sender);
				break;
			case RELOAD:
				cmdReload(api, sender);
				break;
			case PURGE:
				cmdPurge(api, sender);
				break;
			case GUI:
				cmdGUI(api, sender);
				break;
			default:
				sender.sendMessage(api.getMessage(sender, Message.ERROR, ""));
			}
		} catch (BalloonException e) {
			main.getLogger().warning(api.getMessage(sender, Message.ERROR, e.getMessage()));
			sender.sendMessage(e.getMessage());
		}
	}

	public Action findAction(String actionName) {
		for (Action myAction : Action.values()) {
			if (myAction.getCommand().equalsIgnoreCase(actionName)) {
				return myAction;
			}
		}
		return null;
	}

	protected boolean handleGuiNoParam(HeliumBalloonAPI api, CommandSender sender) {
		if (!(sender instanceof Player) || !sender.hasPermission(GUINOPARAM_PERMISSION)) {
			return false;
		}
		cmdGUI(api, sender);
		return true;

	}

	protected boolean handleCommand(CommandSender sender, String[] args) {
		HeliumBalloonAPI api = HeliumBalloonProvider.getAPI();
		if (api == null) {
			sender.sendMessage(Message.ALREADY_DISABLED.getMessageText());
			return true;
		}

		if (args.length == 0) {
			return handleGuiNoParam(api, sender);
		}

		String actionName = args[0];
		Action action = findAction(actionName);
		if (action == null) {
			sender.sendMessage(api.getMessage(sender, Message.UNKNOWN_ACTION, null));
			return true;
		}

		if (!hasActionPermission(sender, action)) {
			sender.sendMessage(api.getMessage(sender, Message.MISSING_ACTION_PERMISSION, null));
			return true;
		}

		if (action.isOnlyForPlayer() && !(sender instanceof Player)) {
			sender.sendMessage(api.getMessage(sender, Message.PLAYER_ONLY_COMMAND, action.getCommand()));
			return true;
		}

		ConfigPet configPet = null;
		Player player = null;

		int count = action.getParamCount() + 1;

		if (args.length < count) {
			sender.sendMessage(api.getMessage(sender, Message.MISSING_PARAMETER, null));
			return true;
		}

		if (args.length > count) {
			sender.sendMessage(api.getMessage(sender, Message.TOO_MANY_PARAMETERS, null));
			return true;
		}

		int playerPosition = action.findPlayerPosition();
		if (playerPosition > 0) {
			String playerName = args[playerPosition];
			player = main.getServer().getPlayer(playerName);
			if (player == null) {
				sender.sendMessage(api.getMessage(sender, Message.UNKNOWN_PLAYER, null));
				return true;
			}

		}

		int petPosition = action.findPetPosition();
		if (petPosition > 0) {
			String petName = args[petPosition];
			configPet = api.findConfigPet(petName);
			if (configPet == null) {
				sender.sendMessage(api.getMessage(sender, Message.UNKNOWN_PET, null));
				return true;
			}
		}

		dispatchCommand(api, sender, action, player, configPet);

		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			return handleCommand(sender, args);
		} catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage(Message.JAVA_EXCEPTION.getMessageText());
		}
		return true;
	}

}
