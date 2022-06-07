package me.kx.redscript.listeners;

import me.kx.redscript.RedScript;
import me.kx.redscript.utils.SafeList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BookListener implements Listener {
	public void performInternCommand(String strippedCommand) throws InterruptedException {
		String[] parts = strippedCommand.split("=");
		String command = parts[0];
		String value = "";
		if (parts.length == 2) {
			value = parts[1];
		}
		if(command.equals("wait"))
		{
			try {
				Thread.sleep(Integer.parseInt(value));
			} catch (Exception ignored) {
				Thread.sleep(1000);
			}
		}
	}

	public void performCommand(Player player, String command) throws InterruptedException {
		if (command.startsWith("/")) {
			Bukkit.getScheduler().callSyncMethod(RedScript.getInstance(), () -> Bukkit.dispatchCommand(player, command.substring(1)));
		} else if (command.startsWith("[") && command.endsWith("]")) {
			performInternCommand(command.substring(1, command.length() - 1));
		} else if(command.startsWith("%")) {
			player.sendMessage("§7[§4Red§rScript§7] §r" + ChatColor.translateAlternateColorCodes('&', command.substring(1)));
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!e.getPlayer().isSneaking()) {
			return;
		} else if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		} else if (e.getItem() == null) {
			return;
		} else if (e.getItem().getType() != Material.WRITABLE_BOOK && e.getItem().getType() != Material.BOOK) {
			return;
		}
		e.setCancelled(true);
		BookMeta bookMeta = (BookMeta) e.getItem().getItemMeta();
		if (bookMeta == null) {
			return;
		}
		SafeList<String> pages = new SafeList<>(bookMeta.getPages());
		if (!pages.getOrDefault(0, "").split("\n")[0].equals("[RedScript]")) {
			e.getPlayer().sendMessage("§cThis book is not a RedScript book.");
			return;
		}
		e.getPlayer().sendMessage("§aRedScript: §fExecuting commands...");
		List<String> commands = new ArrayList<>();
		for (String page : pages) {
			commands.addAll(Arrays.stream(page.split("\n")).map(String::trim).collect(Collectors.toList()));
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				for (String command : commands) {
					try {
						performCommand(e.getPlayer(), command);
					} catch (InterruptedException ex) {
						e.getPlayer().sendMessage("§cAn error occurred while executing the command '" + command + "'.");
					}
				}
				e.getPlayer().sendMessage("§aRedScript: §fDone.");
			}
		}.runTaskAsynchronously(RedScript.getInstance());
	}
}
