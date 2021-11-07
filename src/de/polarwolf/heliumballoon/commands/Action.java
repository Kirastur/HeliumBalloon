package de.polarwolf.heliumballoon.commands;

public enum Action {

	HELP(false, "help", 0, 0),
	DEBUGENABLE(false, "debugenable", 0, 0),
	DEBUGDISABLE(false, "debugdisable", 0, 0),
	ASSIGN(true, "assign", 2, 0),
	DEASSIGN(true, "deassign", 0, 0),
	ASSIGNOTHER(false, "assignother", 1, 2),
	DEASSIGNOTHER(false, "deassignother", 1, 0),
	CHECK(false, "check", 0, 0),
	CHECKOTHER(false, "checkother", 1, 0),
	LIST(true, "list", 0, 0),
	LISTALL(false, "listall", 0, 0),
	INFO(false, "info", 0, 0),
	RELOAD(false, "reload", 0, 0),
	PURGE(false, "purge", 0, 0),
	GUI(true, "gui", 0, 0);

	private final boolean onlyForPlayer;
	private final String command;
	private final int param1;
	private final int param2;

	private Action(boolean onlyForPlayer, String command, int param1, int param2) {
		this.onlyForPlayer = onlyForPlayer;
		this.command = command;
		this.param1 = param1;
		this.param2 = param2;
	}

	public boolean isOnlyForPlayer() {
		return onlyForPlayer;
	}

	public String getCommand() {
		return command;
	}

	public int getParam1() {
		return param1;
	}

	public int getParam2() {
		return param2;
	}

	public int getParamCount() {
		if (param2 > 0) {
			return 2;
		}
		if (param1 > 0) {
			return 1;
		}
		return 0;
	}

	public int findPlayerPosition() {
		if (param1 == 1) {
			return 1;
		}
		if (param2 == 1) {
			return 2;
		}
		return 0;
	}

	public int findPetPosition() {
		if (param1 == 2) {
			return 1;
		}
		if (param2 == 2) {
			return 2;
		}
		return 0;
	}
}
