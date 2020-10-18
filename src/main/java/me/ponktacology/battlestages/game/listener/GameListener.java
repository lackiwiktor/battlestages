package me.ponktacology.battlestages.game.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.battlestages.game.Game;
import me.ponktacology.battlestages.game.GameState;
import me.ponktacology.battlestages.participant.GameParticipant;
import me.ponktacology.battlestages.participant.GameParticipantState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

@RequiredArgsConstructor
public class GameListener implements Listener {

    private final Game game;

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        GameParticipant victimParticipant = GameParticipant.getByPlayer(victim);
        GameParticipant damagerParticipant = GameParticipant.getByPlayer(damager);

        if (victimParticipant.getState() != GameParticipantState.PLAYING || damagerParticipant.getState() != GameParticipantState.PLAYING) {
            return;
        }

        if (victim.getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true);

            game.onDeath(damagerParticipant, victimParticipant);
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (game.getState() != GameState.RUNNING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItemEvent(BlockBreakEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItemEvent(BlockPlaceEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }
}
