package me.ponktacology.battlestages.participant.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.battlestages.game.Game;
import me.ponktacology.battlestages.participant.GameParticipant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

@RequiredArgsConstructor
public class GameParticipantListener implements Listener {

    private final Game game;

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        GameParticipant participant = new GameParticipant(uuid);
        GameParticipant.getParticipants().put(uuid, participant);
        game.addParticipant(participant);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        GameParticipant participant = GameParticipant.getParticipants().remove(uuid);

        if (participant != null) {
            game.removeParticipant(participant);

        }
    }
}
