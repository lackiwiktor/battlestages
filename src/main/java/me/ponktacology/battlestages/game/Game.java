package me.ponktacology.battlestages.game;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.ponktacology.battlestages.participant.GameParticipant;
import me.ponktacology.battlestages.participant.GameParticipantState;
import me.ponktacology.simpleconfig.config.annotation.Configurable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class Game {

    @Configurable
    private static final int MAX_PLAYERS = 50;

    @Configurable
    private static final int MIN_PLAYERS_TO_START = 2;

    @Configurable
    private static final int MAX_GAME_TIME = 20 * 60 * 1000; //Value in minutes converted to milliseconds

    @Configurable
    private static final int BASE_MAP_SIZE = 20;

    @Configurable
    private static final int MAP_SIZE_ADDITION_PER_PLAYER = 5;

    @Configurable
    @Setter
    private static Location gameLocation = new Location(Bukkit.getWorld("world"), 0, 100, 0, 90F, 90F);

    @Configurable
    @Setter
    private static Location waitingLocation = new Location(Bukkit.getWorld("world"), 0, 100, 0, 90F, 90F);

    private final JavaPlugin plugin;
    private final Set<GameParticipant> waitingParticipants = new HashSet<>();
    private final Set<GameParticipant> spectatingParticipants = new HashSet<>();
    private final Set<GameParticipant> playingParticipants = new HashSet<>();
    private GameState state = GameState.WAITING_FOR_PLAYERS;
    private long startTimeStamp;

    public void init() {
        (new BukkitRunnable() {
            @Override
            public void run() {
                switch (state) {
                    case WAITING_FOR_PLAYERS: {
                        if (getWaitingParticipantsSize() >= MIN_PLAYERS_TO_START) {
                            start();
                        } else {
                            getPlayingParticipants().forEach(player -> {
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                        TextComponent.fromLegacyText(String.format("Waiting for %d more players before starting...", MIN_PLAYERS_TO_START - getWaitingParticipantsSize())));
                            });
                        }
                        break;
                    }
                    case RUNNING: {
                        if (System.currentTimeMillis() - (startTimeStamp + MAX_GAME_TIME) <= 0) {
                            cancel();
                            end();
                        }
                        break;
                    }
                }

            }
        }).runTaskTimer(plugin, 0, 20 * 5);
    }

    public void start() {
        state = GameState.RUNNING;
        startTimeStamp = System.currentTimeMillis();

        for (GameParticipant participant : waitingParticipants) {
            Player player = participant.getPlayer();
            if (player == null) continue;

            addParticipant(participant);
        }

        waitingParticipants.clear();
    }

    public void end() {

    }

    public void addParticipant(GameParticipant participant) {
        switch (state) {
            case WAITING_FOR_PLAYERS: {
                addWaitingParticipant(participant);
                break;
            }
            case RUNNING: {
                if (getPlayingParticipantsSize() < MAX_PLAYERS) {
                    Player player = participant.getPlayer();
                    if (player == null) return;

                    participant.setState(GameParticipantState.PLAYING);

                    int mapSize = mapSize();

                    GameParticipant.reset(player);
                    player.teleport(gameLocation.add(getRandomNumber(-mapSize, mapSize), 10, getRandomNumber(-mapSize, mapSize)));
                    player.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD));

                    playingParticipants.add(participant);
                } else {
                    addSpectator(participant);
                }
                break;
            }
        }
    }

    public void removeParticipant(GameParticipant participant) {
        waitingParticipants.remove(participant);
        playingParticipants.remove(participant);
        spectatingParticipants.remove(participant);
    }

    private int mapSize() {
        return BASE_MAP_SIZE + (getPlayingParticipantsSize() * MAP_SIZE_ADDITION_PER_PLAYER);
    }

    private void addWaitingParticipant(GameParticipant participant) {
        Player player = participant.getPlayer();
        if (player == null) return;

        player.teleport(waitingLocation);

        participant.setState(GameParticipantState.WAITING);
        waitingParticipants.add(participant);
    }

    private void addSpectator(GameParticipant participant) {
        participant.setState(GameParticipantState.SPECTATING);
        spectatingParticipants.add(participant);
    }

    private int getPlayingParticipantsSize() {
        return playingParticipants.size();
    }

    private int getWaitingParticipantsSize() {
        return playingParticipants.size();
    }

    private List<Player> getPlayingParticipants() {
        return playingParticipants.stream().map(GameParticipant::getPlayer).filter(Objects::isNull).collect(Collectors.toList());
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
