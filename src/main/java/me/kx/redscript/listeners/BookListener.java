package me.kx.redscript.listeners;

import me.kx.redscript.utils.BulkCommandExecutor;
import me.kx.redscript.utils.ParserUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class BookListener implements Listener {

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

		List<String> commands;
		try {
			commands = ParserUtil.parseCommandsFromPages(e.getItem().getItemMeta());
		} catch (Exception ignored) {
			return;
		}
		e.setCancelled(true);
		BulkCommandExecutor.executeCommandsAsync(e.getPlayer(), commands);
	}
}
