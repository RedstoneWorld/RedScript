package me.kx.redscript;

import me.kx.redscript.listeners.BookListener;
import me.kx.redscript.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
		saveDefaultConfig();
		ConfigUtil.Init(new File("plugins/RedScript/config.yml"));
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
