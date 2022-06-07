package me.kx.redscript.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ConfigUtil {
	private static YamlConfiguration config;

	public static void Init(File configFile) {
		if(!configFile.exists())
			throw new IllegalArgumentException("Config file does not exist");
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	public static YamlConfiguration getConfig() {
		return config;
	}

	public static String getMessage(String path) {
		String message = config.getString(path);
		if(message == null || message.isEmpty())
			return "§4§l[RedScript] §c§lError: Message §4§l" + path + "§c§l is not set";
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static String getPrefix() {
		return getMessage("prefix");
	}
}
