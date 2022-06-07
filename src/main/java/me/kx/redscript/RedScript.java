package me.kx.redscript;

import me.kx.redscript.listeners.BookListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class RedScript extends JavaPlugin {

	// region INSTANCING
	private static RedScript INSTANCE = null;
	public static RedScript getInstance() {
		return INSTANCE;
	}
	public RedScript() {
		INSTANCE = this;
	}
	// endregion

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new BookListener(), this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
