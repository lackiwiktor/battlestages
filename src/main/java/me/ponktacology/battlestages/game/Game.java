package me.ponktacology.battlestages.game;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.battlestages.participant.GameParticipant;
import me.ponktacology.battlestages.participant.GameParticipantState;
import me.ponktacology.battlestages.participant.stats.GameParticipantStats;
import me.ponktacology.battlestages.util.ColorUtil;
import me.ponktacology.battlestages.util.FireworkUtil;
import me.ponktacology.battlestages.util.ItemStackUtil;
import me.ponktacology.battlestages.util.MathUtil;
import me.ponktacology.simpleconfig.config.annotation.Configurable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class Game {

    @Configurable(fileName = "items.yml", save = true)
    public static List<ItemStack> ITEMS = new ArrayList<>();

    @Configurable
    private static int MAX_PLAYERS = 50;

    @Configurable
    private static int MIN_PLAYERS_TO_START = 2;

    @Configurable
    private static int MAX_GAME_TIME = 20 * 60 * 1000; //in millis

    @Configurable
    private static int BASE_MAP_SIZE = 20;

    @Configurable
    private static int MAP_SIZE_ADDITION_PER_PLAYER = 5;

    @Configurable(save = true)
    public static Location GAME_LOCATION = new Location(Bukkit.getWorld("world"), 0, 100, 0, 90F, 90F);

    @Configurable(save = true)
    public static Location WAITING_LOCATION = new Location(Bukkit.getWorld("world"), 0, 100, 0, 90F, 90F);

    @Configurable
    private static int POINTS_TO_WIN = 500;

    private final JavaPlugin plugin;
    private final Set<GameParticipant> waitingParticipants = new HashSet<>();
    private final Set<GameParticipant> playingParticipants = new HashSet<>();
    private GameState state = GameState.WAITING_FOR_PLAYERS;
    private long startTimeStamp;

    public void init() {
        state = GameState.WAITING_FOR_PLAYERS;
        (new BukkitRunnable() {
            @Override
            public void run() {
                switch (state) {
                    case WAITING_FOR_PLAYERS: {
                        World world = GAME_LOCATION.getWorld();
                        WorldBorder border = world.getWorldBorder();
                        border.reset();

                        if (getWaitingParticipantsSize() >= MIN_PLAYERS_TO_START) {
                            start();
                        } else {
                            getWaitingParticipantsPlayer().forEach(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                    TextComponent.fromLegacyText(ColorUtil.color("&eWaiting for " + (MIN_PLAYERS_TO_START - getWaitingParticipantsSize()) + " more players before starting..."))));
                        }
                        break;
                    }
                    case RUNNING: {
                        if ((startTimeStamp + MAX_GAME_TIME) - System.currentTimeMillis() <= 0) {
                            cancel();
                            end(null);
                            return;
                        }

                        World world = GAME_LOCATION.getWorld();
                        WorldBorder border = world.getWorldBorder();
                        double oldSize = border.getSize();
                        border.setSize(mapSize() * 2);
                        border.setCenter(GAME_LOCATION.getX(), GAME_LOCATION.getZ());

                        if (oldSize > border.getSize()) {
                            getPlayingParticipantsPlayer().stream().filter(player -> !border.isInside(player.getLocation())).forEach(player ->
                                    teleportPlayerToRandomLocation(player));
                        }
                        break;
                    }
                }
            }
        }).runTaskTimer(plugin, 0, 20);
    }

    public void start() {
        startTimeStamp = System.currentTimeMillis();
        state = GameState.RUNNING;

        getWaitingParticipants().forEach(this::addParticipant);

        waitingParticipants.clear();
    }

    public void end(GameParticipant winner) {
        state = GameState.ENDING;

        if (winner == null) {
            winner = playingParticipants.stream().min((o1, o2) -> -(o1.getStats().getPoints() - o2.getStats().getPoints())).orElse(null);
        }

        if (winner != null) {
            Player winnerPlayer = winner.getPlayer();

            getPlayingParticipantsPlayer().forEach(player ->
                    player.sendTitle(ColorUtil.color("&a&lGAME ENDED"), ColorUtil.color("&6&l" + winnerPlayer.getName() + " WON!"), 10, 20 * 5, 10));

            FireworkUtil.spawnFireworks(winnerPlayer.getLocation(), 30);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                waitingParticipants.addAll(playingParticipants);
                waitingParticipants.forEach(participant -> participant.getStats().reset());
                playingParticipants.clear();
                init();
            }
        }.runTaskLater(plugin, 20 * 5);
    }

    public void addParticipant(GameParticipant participant) {
        switch (state) {
            case WAITING_FOR_PLAYERS: {
                addWaitingParticipant(participant);
                break;
            }
            case RUNNING: {
                Player player = participant.getPlayer();
                if (player == null) return;
                if (getPlayingParticipantsSize() < MAX_PLAYERS) {
                    participant.setState(GameParticipantState.PLAYING);
                    playingParticipants.add(participant);
                    setupPlayer(player);
                } else {
                    player.kickPlayer(ColorUtil.color("&cGame is full."));
                }
                break;
            }
        }

        participant.setupVisibilityAndTag(this);
    }

    public void setupPlayer(Player player) {
        GameParticipant.reset(player);
        teleportPlayerToRandomLocation(player);
        ItemStack item = new ItemStack(Material.WOODEN_SWORD);
        ItemStackUtil.makeUnbreakable(item);
        player.getInventory().addItem(item);
    }

    public void teleportPlayerToRandomLocation(Player player) {
        int mapSize = mapSize();

        int x = MathUtil.getRandomNumber(-mapSize, mapSize);
        int z = MathUtil.getRandomNumber(-mapSize, mapSize);

        Location location = GAME_LOCATION.clone().add(x, 0, z);
        location.setY(player.getWorld().getHighestBlockYAt(x, z));
        location.setYaw(player.getLocation().getYaw());
        location.setPitch(player.getLocation().getPitch());

        player.teleport(location);
    }

    public void removeParticipant(GameParticipant participant) {
        waitingParticipants.remove(participant);
        playingParticipants.remove(participant);
    }

    public void onDeath(GameParticipant killer, GameParticipant victim) {
        Player killerPlayer = killer.getPlayer();
        Player victimPlayer = victim.getPlayer();

        GameParticipantStats killerStats = killer.getStats();
        GameParticipantStats victimStats = victim.getStats();

        killerPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ColorUtil.color("&6LEVEL " + killerStats.incrementLevel())));

        int pointsChange = killerStats.calculatePoints(victim);
        victimStats.setLevel(1);
        victimStats.removePoints(pointsChange);

        if (killerStats.addPoints(pointsChange) >= POINTS_TO_WIN) {
            end(killer);
            return;
        }

        killer.setupVisibilityAndTag(this);
        killer.giveKillReward(ITEMS);
        setupPlayer(victim.getPlayer());

        Bukkit.getServer().broadcastMessage(ColorUtil.color("&a" + victimPlayer.getName() + " &7was slain by&c " + killerPlayer.getName()));
    }

    private int mapSize() {
        return BASE_MAP_SIZE + (getPlayingParticipantsSize() * MAP_SIZE_ADDITION_PER_PLAYER);
    }

    private void addWaitingParticipant(GameParticipant participant) {
        Player player = participant.getPlayer();
        if (player == null) return;

        GameParticipant.reset(player);
        player.teleport(WAITING_LOCATION);

        participant.setState(GameParticipantState.WAITING);
        waitingParticipants.add(participant);
    }

    private int getPlayingParticipantsSize() {
        return playingParticipants.size();
    }

    private int getWaitingParticipantsSize() {
        return waitingParticipants.size();
    }

    private List<Player> getWaitingParticipantsPlayer() {
        return waitingParticipants.stream().map(GameParticipant::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<Player> getPlayingParticipantsPlayer() {
        return playingParticipants.stream().map(GameParticipant::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
