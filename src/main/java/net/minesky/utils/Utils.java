package net.minesky.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minesky.MineSkyLobby;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getSHA1(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(input.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();

        StringBuilder hexString = new StringBuilder();

        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }

    public static void sendToRpg(Player player) {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF("rpg");

        player.sendPluginMessage(MineSkyLobby.getInstance(), "BungeeCord", out.toByteArray());

    }

}
