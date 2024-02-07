package me.nebula.discraft;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.Objects;

public final class Discraft extends JavaPlugin {

    private static JDA jda;
    private static String channel;

    @Override
    public void onEnable() {
        getLogger().info("Starting bot");

        saveDefaultConfig();

        FileConfiguration config = getConfig();
        String token = config.getString("settings.token");
        channel = config.getString("settings.channel");

        if (token == null || channel == null) {
            getLogger().severe("No token and/or channel provided. Check if the correct token/channel has been given inside of the 'config.yml' file. Shutting down...");
            getServer().getPluginManager().disablePlugin(this);
        }

        Messaging messages = new Messaging();

        try { jda = JDABuilder.createLight(token)
                    .setEnabledIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(messages)
                    .build()
                    .awaitReady();
        } catch (InterruptedException e) { e.printStackTrace(); }

        getServer().getPluginManager().registerEvents(messages, this);

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
}
