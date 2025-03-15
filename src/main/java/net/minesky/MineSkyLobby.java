package net.minesky;

import fr.xephi.authme.api.v3.AuthMeApi;
import net.minesky.events.MiscEvents;
import net.minesky.events.JoinEvents;
import net.minesky.events.hooks.AuthMeHook;
import net.minesky.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class MineSkyLobby extends JavaPlugin {

    public static MineSkyLobby instance;
    public static Logger l;
    public static FileConfiguration config;

    public static MineSkyLobby getInstance() {
        return instance;
    }

    public static boolean HOOKS_AUTHME = false;

    public static String SHA1 = "";

    @Override
    public void onEnable() {

        instance = this;

        l = this.getLogger();

        try {
            SHA1 = Utils.getSHA1("mineskyresourcepack");
        } catch (NoSuchAlgorithmException ignored) {}

        l.info("Iniciando plugin...");
        l.info("Criado por Drawn e feito exclusivamente para o MineSky");

        this.saveDefaultConfig();

        config = this.getConfig();

        if(Bukkit.getPluginManager().isPluginEnabled("AuthMe")) {
            HOOKS_AUTHME = true;
            l.info("Inicializando integração com AuthMe...");
            getServer().getPluginManager().registerEvents(new AuthMeHook(), this);
        }

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(new MiscEvents(), this);
        getServer().getPluginManager().registerEvents(new JoinEvents(), this);

        new BukkitRunnable() {

            @Override
            public void run() {

                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(player.hasPermission("mineskylobby.ignore.auto-server"))
                        return;

                    if(AuthMeApi.getInstance().isAuthenticated(player) || JoinEvents.textured.contains(player.getUniqueId()))
                        Utils.sendToRpg(player);
                }

            }
        }.runTaskTimer(this, 60, 60);

    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
    }

}
