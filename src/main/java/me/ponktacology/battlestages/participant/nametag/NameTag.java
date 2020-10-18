package me.ponktacology.battlestages.participant.nametag;

import me.ponktacology.battlestages.participant.GameParticipant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class NameTag {

    private static final String TEAM_NAME = "battlestages";

    public static void setup(Player player, Player other) {
        Scoreboard scoreboard = player.getScoreboard();

        if (scoreboard.equals(Bukkit.getServer().getScoreboardManager().getMainScoreboard())) {
            scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        }

        Team team = player.getScoreboard().getTeam(TEAM_NAME);

        if (team == null) {
            team = player.getScoreboard().registerNewTeam(TEAM_NAME);
        }

        if (!team.hasEntry(other.getName())) {
            reset(player, other);

            team.addEntry(other.getName());

            Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

            if (objective == null) {
                objective = player.getScoreboard().registerNewObjective("showlvl", "dummy", ChatColor.GRAY.toString() + "LEVEL");
                objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            }
        } else {
            Objective objective = player.getScoreboard().getObjective("showlvl");

            if (objective == null) {
                return;
            }

            GameParticipant participant = GameParticipant.getByPlayer(other);
            if (participant == null) return;

            objective.getScore(other.getName()).setScore(participant.getStats().getLevel());
        }

        player.setScoreboard(scoreboard);
    }

    public static void reset(Player player, Player other) {
        if (player != null && other != null && !player.equals(other)) {
            Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

            if (objective != null) {
                objective.unregister();
            }

            Team team = player.getScoreboard().getTeam(TEAM_NAME);

            if (team != null) {
                team.removeEntry(other.getName());
            }
        }
    }
}
