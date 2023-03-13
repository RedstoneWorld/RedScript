package me.kx.redscript.utils;

import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParserUtil {
	
	public static List<String> parseCommandsFromPages(ItemMeta bookItemMeta) {
		
		if (bookItemMeta == null) {
			throw new IllegalArgumentException("Book item meta is null");
		}
		
		if (!(bookItemMeta instanceof BookMeta)) {
			throw new IllegalArgumentException("ItemMeta is not a BookMeta");
		}
		
		BookMeta bookMeta = (BookMeta) bookItemMeta;
		ArrayList<String> commands = new ArrayList<>();
		SafeList<String> pages = new SafeList<>(bookMeta.getPages());
		if (!pages.getOrDefault(0, "").split("\n")[0].equals("[RedScript]")) {
			throw new IllegalArgumentException("Book is not a RedScript book");
		}
		
		pages.set(0, pages.get(0).replace("[RedScript]", ""));
		for (String page : pages) {
			commands.addAll(Arrays.stream(page.split("\n")).map(String::trim).collect(Collectors.toList()));
		}
		
		return commands;
	}
}
