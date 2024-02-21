package me.nebula.discraft;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DiscordCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (Discraft.getDiscordCommandLink() != null && Discraft.getDiscordCommandMessage() != null) {

            Component msg = Component.text(Discraft.colorize(Discraft.getDiscordCommandMessage().replace("%link%", Discraft.getDiscordCommandLink())))
                .clickEvent(ClickEvent.openUrl(Discraft.getDiscordCommandLink()))
                .hoverEvent(HoverEvent.showText(Component.text("Click to open")));

            sender.sendMessage(msg);
            return true;
        }

        sender.sendMessage(ChatColor.RED + "This feature is disabled.");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) { return new ArrayList<>(); }
}
