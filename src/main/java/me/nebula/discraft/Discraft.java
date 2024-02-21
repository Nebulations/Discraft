package me.nebula.discraft;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Discraft extends JavaPlugin {

    private static JDA jda;
    private static String channel;
    private static String commandLink;
    private static String commandMessage;

    @Override
    public void onEnable() {
        getLogger().info("Starting bot");

        saveDefaultConfig();

        FileConfiguration config = getConfig();
        String token = config.getString("settings.token");
        channel = config.getString("settings.channel");
        commandLink = config.getString("settings.command-link");
        commandMessage = config.getString("settings.command-message");

        if (token == null || channel == null) {
            getLogger().severe("No token and/or channel provided. Check if the correct token/channel has been given inside of the 'config.yml' file. Shutting down...");
            getServer().getPluginManager().disablePlugin(this);
        }

        if (commandLink == null || commandMessage == null) {
            getLogger().warning("No message/link provided. Check if you've correctly entered your link and message.");
        }

        Messaging messages = new Messaging();

        try { jda = JDABuilder.createLight(token)
                    .setEnabledIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(messages)
                    .build()
                    .awaitReady();
        } catch (InterruptedException e) { e.printStackTrace(); }

        getServer().getPluginManager().registerEvents(messages, this);

        Objects.requireNonNull(getCommand("discord")).setExecutor(new DiscordCommand());
        Objects.requireNonNull(getCommand("discord")).setTabCompleter(new DiscordCommand());

        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("**Server Startup**")
            .setDescription("The server is now online. Join now!")
            .setColor(Color.GREEN);

        Objects.requireNonNull(jda.getTextChannelById(channel)).sendMessageEmbeds(embed.build()).queue();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling...");

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("**Server Shutdown**")
                .setDescription("The server is now offline. Goodbye!")
                .setColor(Color.RED);

        Objects.requireNonNull(jda.getTextChannelById(channel)).sendMessageEmbeds(embed.build()).queue();

        getLogger().info("Successfully disabled");
    }

    public static String getChannel() { return channel; }
    public static JDA getJDA() { return jda; }
    public static String getDiscordCommandLink() { return commandLink; }
    public static String getDiscordCommandMessage() { return commandMessage; }
    public static String colorize(String message) {
        final Pattern hexPattern = Pattern.compile("<([A-Fa-f0-9]{6})>");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, ChatColor.COLOR_CHAR + "x"
                    + ChatColor.COLOR_CHAR + group.charAt(0) + ChatColor.COLOR_CHAR + group.charAt(1)
                    + ChatColor.COLOR_CHAR + group.charAt(2) + ChatColor.COLOR_CHAR + group.charAt(3)
                    + ChatColor.COLOR_CHAR + group.charAt(4) + ChatColor.COLOR_CHAR + group.charAt(5)
            );
        }
        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }
}
