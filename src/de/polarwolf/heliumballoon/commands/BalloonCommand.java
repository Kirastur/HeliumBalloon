package de.polarwolf.heliumballoon.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import de.polarwolf.heliumballoon.api.HeliumBalloonAPI;
import de.polarwolf.heliumballoon.api.HeliumBalloonProvider;
import de.polarwolf.heliumballoon.config.ConfigTemplate;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.main.Main;
import de.polarwolf.heliumballoon.messages.Message;

public class BalloonCommand implements CommandExecutor {
	
	protected final Main main;
	
	
	public BalloonCommand(Main main) {
		this.main = main;
	}
	
	
	public boolean hasActionPermission (CommandSender sender, Action action) {
		return sender.hasPermission("balloon.command."+action.getCommand());
	}
	

	public List<String> listActions(CommandSender sender) {
		List<String> names = new ArrayList<>();
		for (Action myAction : Action.values()) {
			boolean isInclude = false;
			if (sender != null) {
				if  ((sender instanceof Player) || !myAction.isOnlyForPlayer()) {
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
	
	
	public List<String> listTemplates(HeliumBalloonAPI api, CommandSender sender) {
		List<String> names = new ArrayList<>();
		for (String myTemplate : api.getTemplateNames()) {
			if ((!(sender instanceof Player)) || api.hasTemplatePermission((Player) sender, myTemplate)) {
				names.add(myTemplate);
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
	
	
	protected void cmdAssign(HeliumBalloonAPI api, CommandSender sender, ConfigTemplate template) {
		Player player = (Player) sender;
		
		if (!api.hasTemplatePermission(player, template.getName())) {
			sender.sendMessage (api.getMessage(sender, Message.MISSING_TEMPLATE_PERMISSION, null));
			return;
		}
		
		if (!api.hasWorld(player.getWorld().getName())) {
			sender.sendMessage (api.getMessage(sender, Message.WRONG_WORLD, null));
			return;
		}
		
		cmdAssignother(api, sender, player, template);
	}
	
	
	protected void cmdAssignother(HeliumBalloonAPI api, CommandSender sender, Player player, ConfigTemplate template) {
		boolean success = api.assign(player, template.getName());
		if (!success) {
			sender.sendMessage(api.getMessage(sender, Message.UNKNOWN_ERROR, null));
		}		
	}
	
	
	protected void cmdDeassign(HeliumBalloonAPI api, CommandSender sender) {
		Player player = (Player) sender;
		cmdDeassignother(api, sender, player);
	}
	
	
	protected void cmdDeassignother(HeliumBalloonAPI api, CommandSender sender, Player player) {
		boolean success = api.deassign(player);
		if (!success) {
			sender.sendMessage(api.getMessage(sender, Message.UNKNOWN_ERROR, null));
		}		
	}
	
	
	protected void cmdCheck(HeliumBalloonAPI api, CommandSender sender) {
		Player player = (Player) sender;
		cmdCheckother(api, sender, player);
	}
	
	
	protected void cmdCheckother(HeliumBalloonAPI api, CommandSender sender, Player player) {
		ConfigTemplate template = api.findTemplateForPlayer(player);
		if (template == null) {
			sender.sendMessage(api.getMessage(sender, Message.NO_PET_ASSIGNED, null));
		} else {
			sender.sendMessage(api.getMessage(sender, Message.ASSIGNED_PET, template.getName()));
		}
	}
	
	
	protected void cmdList(HeliumBalloonAPI api, CommandSender sender) {
		List<String> names = listTemplates(api, sender);
		String s = String.join(" ", names);
		sender.sendMessage(api.getMessage(sender, Message.ALLOWED_PETS_PLAYER, s));
	}
	
	
	protected void cmdListall(HeliumBalloonAPI api, CommandSender sender) {
		List<String> names = listTemplates(api, null);
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
	
	
	protected void cmdWorlds(HeliumBalloonAPI api, CommandSender sender) {
		List<World> worlds = main.getServer().getWorlds();
		Set<String> names = new TreeSet<>(); 
		for (World myWorld : worlds) {
			String myName = myWorld.getName();
			if (api.hasWorld(myName)) {
				names.add(myName);
			}			
		}
		String s = String.join(" ", names);
		sender.sendMessage(api.getMessage(sender, Message.ALLOWED_WORLDS, s));
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
	
	
	protected void dispatchCommand(HeliumBalloonAPI api, CommandSender sender, Action action, Player player, ConfigTemplate template) {
		try {
			switch (action) {
				case HELP:			cmdHelp(api, sender);
									break;
				case DEBUGENABLE:	cmdDebugenable(api);
									break;
				case DEBUGDISABLE:	cmdDebugdisable(api);
									break;
				case ASSIGN:		cmdAssign(api, sender, template);
									break;
				case ASSIGNOTHER:	cmdAssignother(api, sender, player, template);
									break;
				case DEASSIGN:		cmdDeassign(api, sender);
									break;
				case DEASSIGNOTHER:	cmdDeassignother(api, sender, player);
									break;
				case CHECK:			cmdCheck(api, sender);
									break;
				case CHECKOTHER:	cmdCheckother(api, sender, player);
									break;
				case LIST:			cmdList(api, sender);
									break;
				case LISTALL:		cmdListall(api, sender);
									break;
				case INFO:			cmdInfo(api, sender);
									break;
				case WORLDS:		cmdWorlds(api, sender);
									break;
				case RELOAD:		cmdReload(api, sender);
									break;
				case PURGE:			cmdPurge(api, sender);
									break;
				case GUI:			cmdGUI(api, sender);
									break;
				default: sender.sendMessage(api.getMessage(sender, Message.ERROR, ""));
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
	
	
	protected boolean handleCommand(CommandSender sender, String[] args) {
		if (args.length==0) {
			return false;
		}
		
		HeliumBalloonAPI api = HeliumBalloonProvider.getAPI();
		if (api == null) {
			sender.sendMessage(Message.ALREADY_DISABLED.getMessageText());
			return true;
		}

		String actionName=args[0];
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
			sender.sendMessage (api.getMessage(sender, Message.PLAYER_ONLY_COMMAND, action.getCommand()));
			return true;
		}

		ConfigTemplate template = null;
		Player player = null;
		
		int count = action.getParamCount() +1;
		
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
		
		int templatePosition = action.findTemplatePosition();
		if (templatePosition > 0) {
			String templateName = args[templatePosition];
			template = api.findTemplate(templateName);
			if (template == null) {
				sender.sendMessage (api.getMessage(sender, Message.UNKNOWN_TEMPLATE, null));
				return true;			
			}
		}
						
		dispatchCommand (api, sender, action, player, template);
			
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
