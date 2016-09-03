package com.aaomidi.nodamage.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Lang {
	@Getter
	@Setter
	private static Logger logger = Bukkit.getLogger();
	@Getter
	@Setter
	private static String PREFIX = "NoDamage ";


	public static String colorize(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static void log(Level level, Object o, Object... format) {
		logger.log(level, o.toString(), format);
	}

	public static void sendMessage(CommandSender commandSender, Object o, Object... format) {
        commandSender.sendMessage(colorize(String.format(getPREFIX() + o.toString(), format)));
	}

}
