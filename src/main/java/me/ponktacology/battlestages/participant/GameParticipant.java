package me.ponktacology.battlestages.participant;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.battlestages.game.Game;
import me.ponktacology.battlestages.game.GameState;
import me.ponktacology.battlestages.participant.nametag.NameTag;
import me.ponktacology.battlestages.participant.stats.GameParticipantStats;
import me.ponktacology.battlestages.util.ItemStackUtil;
import me.ponktacology.battlestages.util.MaterialUtil;
import me.ponktacology.battlestages.util.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

@Data
@RequiredArgsConstructor
public class GameParticipant {

    @Getter
    private static final Map<UUID, GameParticipant> participants = new HashMap<>();

    private final UUID uuid;
    private final GameParticipantStats stats = new GameParticipantStats();
    private GameParticipantState state = GameParticipantState.NONE;

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void giveKillReward(List<ItemStack> items) {
        int random = MathUtil.getRandomNumber(0, items.size());

        ItemStack randomItem = items.get(random);
        ItemStackUtil.makeUnbreakable(randomItem);

        Player player = getPlayer();
        PlayerInventory inventory = player.getPlayer().getInventory();

        if (MaterialUtil.isSword(randomItem.getType())) {
            for (int slot = 0; slot < 36; slot++) {
                if (inventory.getContents()[slot] == null) continue;
                if (inventory.getContents()[slot].getType().toString().contains("SWORD")) {
                    if (MaterialUtil.isBetterSword(inventory.getContents()[slot].getType(), randomItem.getType())) {
                        inventory.setItem(slot, randomItem.clone());
                    }
                    break;
                }
            }
        } else if (MaterialUtil.isArmor(randomItem.getType())) {
            boolean set = false;
            for (int slot = 0; slot < 4; slot++) {
                ItemStack[] contents = inventory.getArmorContents();
                if (inventory.getArmorContents()[slot] != null) {
                    if (inventory.getArmorContents()[slot].getType().toString().split("_")[1].equals(randomItem.getType().toString().split("_")[1])) {
                        if (MaterialUtil.isBetterArmor(inventory.getArmorContents()[slot].getType(), randomItem.getType())) {
                            contents[slot] = randomItem.clone();
                            inventory.setArmorContents(contents);
                            set = true;
                        }

                        break;
                    }
                }
            }

            if (!set) {
                switch (randomItem.getType().toString().split("_")[1]) {
                    case "BOOTS": {
                        inventory.setBoots(randomItem.clone());
                        break;
                    }
                    case "LEGGINGS": {
                        inventory.setLeggings(randomItem.clone());
                        break;
                    }
                    case "CHESTPLATE": {
                        inventory.setChestplate(randomItem.clone());
                        break;
                    }
                    case "HELMET": {
                        inventory.setHelmet(randomItem.clone());
                        break;
                    }
                }
            }
        } else {
            inventory.addItem(randomItem.clone());
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 3, 0));
    }

    public void setupVisibilityAndTag(Game game) {
        Player player = getPlayer();
        if (player == null) return;
        if (game.getState() != GameState.RUNNING) return;

        game.getPlayingParticipants().stream().map(GameParticipant::getPlayer).filter(Objects::nonNull).forEach(it -> {
            NameTag.setup(it, player);
            NameTag.setup(player, it);
            player.showPlayer(game.getPlugin(), it);
            it.showPlayer(game.getPlugin(), player);
        });
    }

    public static GameParticipant getByPlayer(Player player) {
        return participants.get(player.getUniqueId());
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
