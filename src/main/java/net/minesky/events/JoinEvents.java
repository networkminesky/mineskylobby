package net.minesky.events;

import net.kyori.adventure.text.Component;
import net.minesky.MineSkyLobby;
import net.minesky.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class JoinEvents implements Listener {

    public static ArrayList<UUID> textured = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Location l = p.getLocation();

        l.setPitch(90);
        p.teleport(l);

        e.joinMessage(null);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        textured.remove(e.getPlayer().getUniqueId());
        e.quitMessage(null);
    }

    public static final String RESOURCEPACK_URL = MineSkyLobby.config.getString("resourcepack.url");

    @EventHandler
    public void onResourcePack(PlayerResourcePackStatusEvent e) {
        final Player p = e.getPlayer();

        switch(e.getStatus()) {
            case ACCEPTED -> p.sendTitle("§aBaixando textura...", "§7Aguarde ela ser instalada!", 4, 90, 20);

            case DECLINED -> {
                if (!p.hasPermission("mineskylobby.bypass.texture-declined")) {
                    p.kickPlayer(Utils.c("&c&lVocê negou a textura do servidor.\n" +
                            "\n&7Para jogar em nossos servidores, você precisa ativar a nossa textura obrigatória.\n" +
                            "\n&6&lCOMO ATIVAR?" +
                            "\n&cO processo de instalação é automático, apenas entre no servidor após seguir as intruções abaixo:" +
                            "\n&e1. &7Vá até sua lista de servidores e procure pelo MineSky" +
                            "\n&e2. &7Em seguida, clique em 'Editar', depois procure pela opção" +
                            "\n&7'Pacotes de Recursos', marque a opção como '&aSim&7'" +
                            "\n\n&eBom jogo!"));
                    return;
                }

                p.sendMessage(Utils.c("&cVocê negou a textura do servidor, porém você possui permissão para ignora-la, ative a textura caso queira visualizar tudo do servidor!"));
            }

            case FAILED_DOWNLOAD -> p.kickPlayer(Utils.c("&c&lOcorreu um erro ao baixar a textura do servidor.\n \n" +
                    "&7Reentre. Caso persista, ative a textura nas opções do servidor ou considere criar um ticket em nosso discord."));

            case SUCCESSFULLY_LOADED -> {
                p.sendMessage(Utils.c("\n\n\n  &f⛭&f  Bem-vindo a &bMineSky Network!\n \n" +
                        "                    &7Acesse nossa &e&nloja&7 &7para ajudar o servidor! \n \n "));

                titleFade(p, 5, 40, 20);

                textured.add(p.getUniqueId());

                p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

                Utils.sendToRpg(p);
            }
        }
    }

    public static void sendResourcepack(Player p) {
        if(RESOURCEPACK_URL == null || RESOURCEPACK_URL.isEmpty()) {
            p.kickPlayer("§cUm erro ocorreu ao tentar gerar a textura a você, contate a staff do servidor em nosso discord: https://minesky.com.br/discord");
            return;
        }

        p.setResourcePack(RESOURCEPACK_URL, null,
                Component.text("§c§lTEXTURA OBRIGATÓRIA\n§cPara jogar em nossos servidores\nvocê deve aceitar a textura do servidor.\n\nO download e aplicação são automáticos, você só precisa autorizar em seu jogo.")
                , true);

        //p.setResourcePack(n);
    }

    public void titleFade(Player p, int fadeIn, int fade, int fadeOut) {
        p.sendTitle("§0\uE001⌭\uE001⌭\uE001", "", fadeIn, fade, fadeOut);
    }


}
