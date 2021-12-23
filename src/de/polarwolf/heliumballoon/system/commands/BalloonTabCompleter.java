package de.polarwolf.heliumballoon.system.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.polarwolf.heliumballoon.api.HeliumBalloonAPI;
import de.polarwolf.heliumballoon.api.HeliumBalloonProvider;
import de.polarwolf.heliumballoon.main.Main;

public class BalloonTabCompleter implements TabCompleter {

	protected final BalloonCommand balloonCommand;

	public BalloonTabCompleter(Main main, BalloonCommand balloonCommand) {
		this.balloonCommand = balloonCommand;
		main.getCommand(balloonCommand.getCommandName()).setTabCompleter(this);
	}

	protected List<String> handleTabComplete(CommandSender sender, String[] args) {
		if (args.length == 0) {
			return new ArrayList<>();
		}

		HeliumBalloonAPI api = HeliumBalloonProvider.getAPI();
		if (api == null) {
			return new ArrayList<>();
		}

		if (args.length == 1) {
			return balloonCommand.listActions(sender);
		}

		String actionName = args[0];
		Action action = balloonCommand.findAction(actionName);
		if (action == null) {
			return new ArrayList<>();
		}

		if (args.length == action.findPlayerPosition() + 1) {
			return null; // NOSONAR
		}

		if (args.length == action.findPetPosition() + 1) {
			return balloonCommand.listPets(api, sender);
		}

		return new ArrayList<>();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			return handleTabComplete(sender, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

}
