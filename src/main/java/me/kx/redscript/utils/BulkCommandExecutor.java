package me.kx.redscript.utils;

import me.kx.redscript.RedScript;
import me.kx.redscript.types.VariableStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BulkCommandExecutor {
	private static final VariableStorage variableStorage = new VariableStorage();

	public static void executeCommandsAsync(Player player, List<String> commands) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					executeCommands(player, commands);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}.runTaskAsynchronously(RedScript.getInstance());
	}

	public static void executeCommands(Player player, List<String> commands) throws InterruptedException {
		long startTime = System.currentTimeMillis();
		player.sendMessage(ConfigUtil.getPrefix() + ConfigUtil.getMessage("commandExecution.starting"));
		
		for (String command : commands) {
			if (System.currentTimeMillis() - startTime > 30000) {
				player.sendMessage(ConfigUtil.getPrefix() + ConfigUtil.getMessage("commandExecution.timeout"));
				break;
			}
			String filledCommand = variableStorage.replaceVariables(player.getUniqueId(), command);
			performCommand(player, filledCommand);
		}
		
		variableStorage.clearVariables(player.getUniqueId());
		player.sendMessage(ConfigUtil.getPrefix() + ConfigUtil.getMessage("commandExecution.done"));
	}

	private static void performCommand(Player player, String command) throws InterruptedException {
		
		// Command: "/" ...
		// Variable: "[" ... "]"
		// Message: ">>" ...
		
		if (command.startsWith("/")) {
			Bukkit.getScheduler().callSyncMethod(RedScript.getInstance(), () -> Bukkit.dispatchCommand(player, command.substring(1)));
		} else if (command.startsWith("[") && command.endsWith("]")) {
			performInternalCommand(player, command.substring(1, command.length() - 1));
		} else if (command.startsWith(">>")) {
			player.sendMessage(ConfigUtil.getPrefix() + ChatColor.translateAlternateColorCodes('&', command.substring(2)));
		}
	}

	private static void performInternalCommand(Player player, String strippedCommand) throws InterruptedException {
		String[] parts = strippedCommand.split("=");
		String command = parts[0].trim();
		String value = "";
		if (parts.length == 2) {
			value = parts[1].trim();
		}
		
		if (command.equals("wait")) {
			try {
				Thread.sleep(Math.min(Integer.parseInt(value), 10000));
			} catch (Exception ignored) {
				Thread.sleep(1000);
			}
		} else {
			if (value.equals("")) {
				variableStorage.clearVariable(player.getUniqueId(), command);
			} else {
				variableStorage.setVariable(player.getUniqueId(), command, value);
			}
		}
	}
}
