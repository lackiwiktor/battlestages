package me.ponktacology.battlestages.participant;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class GameParticipant {

    @Getter
    private static final Map<UUID, GameParticipant> participants = new HashMap<>();

    private final UUID uuid;
    private GameParticipantState state = GameParticipantState.NONE;

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public static void reset(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setCanPickupItems(false);
        player.getOpenInventory().close();
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[]{null, null, null});
        player.setHealth(20);
        player.setFoodLevel(20);
    }
}
