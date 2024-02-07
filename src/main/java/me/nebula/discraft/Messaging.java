package me.nebula.discraft;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.util.Objects;

public class Messaging extends ListenerAdapter implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        String message = "**" + event.getPlayer().getName() + ":** " + event.getMessage();
        String channel = Discraft.getChannel();
        JDA jda = Discraft.getJDA();

        Objects.requireNonNull(jda.getTextChannelById(channel)).sendMessage(message).queue();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String message = event.getPlayer().getName() + " has joined the game.";
        String skin = "https://mc-heads.net/avatar/" + event.getPlayer().getUniqueId();

        EmbedBuilder embed = new EmbedBuilder()
                .setFooter(message, skin)
                .setColor(Color.GREEN);

        Objects.requireNonNull(Discraft.getJDA().getTextChannelById(Discraft.getChannel())).sendMessageEmbeds(embed.build()).queue();

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String message = event.getPlayer().getName() + " has left the game.";
        String skin = "https://mc-heads.net/avatar/" + event.getPlayer().getUniqueId();

        EmbedBuilder embed = new EmbedBuilder()
                .setFooter(message, skin)
                .setColor(Color.RED);

        Objects.requireNonNull(Discraft.getJDA().getTextChannelById(Discraft.getChannel())).sendMessageEmbeds(embed.build()).queue();

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String channel = Discraft.getChannel();
        String message = event.getMessage().getContentRaw();
        if (event.getChannel().getId().equalsIgnoreCase(channel) && !event.getAuthor().isBot()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.BLUE + "[DISCORD] " + event.getAuthor().getEffectiveName() + ": " + ChatColor.WHITE + message);
            }
        }
    }
}
