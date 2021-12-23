package de.polarwolf.heliumballoon.tools.messages;

public enum Message {

	ERROR("ERROR %s"),
	JAVA_EXCEPTION("Java exception"),
	PET_ERROR("Pet Error"),
	UNKNOWN_ERROR("Something went wrong. Please check ther server logs."),
	MISSING_PARAMETER("Missing parameter"),
	TOO_MANY_PARAMETERS("Too many parameters"),
	UNKNOWN_PLAYER("Unkown player"),
	UNKNOWN_PET("Unknown pet"),
	UNKNOWN_ACTION("Unknown action"),
	ALREADY_DISABLED("The plugin or one of its components are disabled"),
	PASSIVE_MODE("Missing Orchestrator. Are you using passive mode?"),
	MISSING_ACTION_PERMISSION("You don't have the permission to execute this action"),
	MISSING_PET_PERMISSION("You do not have the permission for this pet"),
	WRONG_WORLD("You cannot summon pets in this world"),
	NO_PET_ASSIGNED("No pet assigned"),
	NO_PETS_AVAIL("You don't have access to any pets"),
	PLAYER_ONLY_COMMAND("Sorry, only player can execute this command. Try \"%sother\" instead."),
	CONSOLE_ONLY_COMMAND("Sorry, this command can only be executed from a console or rcon."),
	ALLOWED_ACTIONS("Valid actions are:  %s"),
	ALLOWED_PETS_PLAYER("You can use the following pets: %s"),
	ALLOWED_PETS_OP("The following pets are avail: %s"),
	ALLOWED_WORLDS("Allowed worlds for pets are: %s"),
	ASSIGNED_PET("Assigned pet: %s"),
	PURGE_RESULT("%s assigns purged"),
	ACTIVE_BALLOONS("Active balloons: %s");

	private final String messageText;

	private Message(String messageText) {
		this.messageText = messageText;
	}

	public String getMessageText() {
		return messageText;
	}

	public String getMessageName() {
		String s = this.toString();
		s = s.toLowerCase();
		return s;
	}

}