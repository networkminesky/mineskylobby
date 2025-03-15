package net.minesky.events.hooks;

import fr.xephi.authme.events.LoginEvent;
import net.minesky.events.JoinEvents;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AuthMeHook implements Listener {

    @EventHandler
    public void onAuth(LoginEvent e) {
        Player p = e.getPlayer();

        JoinEvents.sendResourcepack(p);
    }

}
