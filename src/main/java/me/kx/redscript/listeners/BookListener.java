package me.kx.redscript.listeners;

import me.kx.redscript.RedScript;
import me.kx.redscript.utils.BulkCommandExecutor;
import me.kx.redscript.utils.ParserUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BookListener implements Listener {

	//anti-spam
	boolean PlayerInteractCancel = false;
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {

		if (PlayerInteractCancel) return;

		// if (!e.getPlayer().isSneaking()) return;

		if (e.getItem() == null) return;
		if ((e.getItem().getType() != Material.WRITABLE_BOOK) && (e.getItem().getType() != Material.WRITTEN_BOOK)) return;

		
		if ((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK)) {
			
			List<String> commands;
			try {
				commands = ParserUtil.parseCommandsFromPages(e.getItem().getItemMeta());
			} catch (Exception ignored) {
				return;
			}

			BulkCommandExecutor.executeCommandsAsync(e.getPlayer(), commands);
			e.setCancelled(true);
		}
		
		
		// anti-spam task
		PlayerInteractCancel = true;

		new BukkitRunnable() {
			public void run() {

				PlayerInteractCancel = false;

			}
		}.runTaskLater(RedScript.getInstance(), 15);
		
	}
}
