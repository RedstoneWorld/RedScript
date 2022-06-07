package me.kx.redscript.utils;

import me.kx.redscript.RedScript;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class BulkCommandExecutor {

	private static void performInternCommand(Player player, String strippedCommand) throws InterruptedException {
		String[] parts = strippedCommand.split("=");
		String command = parts[0];
		String value = "";
		if (parts.length == 2) {
			value = parts[1];
		}
		if (command.equals("wait")) {
			try {
				Thread.sleep(Integer.parseInt(value));
			} catch (Exception ignored) {
				Thread.sleep(1000);
			}
		}
	}

	private static void performCommand(Player player, String command) throws InterruptedException {
		if (command.startsWith("/")) {
			Bukkit.getScheduler().callSyncMethod(RedScript.getInstance(), () -> Bukkit.dispatchCommand(player, command.substring(1)));
		} else if (command.startsWith("[") && command.endsWith("]")) {
			performInternCommand(player, command.substring(1, command.length() - 1));
		} else if (command.startsWith("%")) {
			player.sendMessage(ConfigUtil.getPrefix() + ChatColor.translateAlternateColorCodes('&', command.substring(1)));
		}
	}

	public static void executeCommands(Player player, List<String> commands) throws InterruptedException {
		player.sendMessage(ConfigUtil.getPrefix() + ConfigUtil.getMessage("commandExecution.starting"));
		for (String command : commands) {
			performCommand(player, command);
		}
		player.sendMessage(ConfigUtil.getPrefix() + ConfigUtil.getMessage("commandExecution.done"));
	}

	public static BukkitTask executeCommandsAsync(Player player, List<String> commands) throws InterruptedException {
		return new BukkitRunnable() {
			@Override
			public void run() {
				try {
					executeCommands(player, commands);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(RedScript.getInstance());
	}
}
