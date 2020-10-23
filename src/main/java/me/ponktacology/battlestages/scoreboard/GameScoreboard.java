package me.ponktacology.battlestages.scoreboard;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.battlestages.game.Game;
import me.ponktacology.battlestages.game.GameState;
import me.ponktacology.battlestages.participant.GameParticipant;
import me.ponktacology.battlestages.participant.stats.GameParticipantStats;
import me.ponktacology.battlestages.util.ColorUtil;
import me.ponktacology.battlestages.util.TimeUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GameScoreboard implements AssembleAdapter {

    private static final char[] TOP_COLORS = new char[]{'6', 'e', '3', '9', '2'};
    private final Game game;

    @Override
    public String getTitle(Player player) {
        return ColorUtil.color("&6&lBATTLE STAGES");
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();

        if (game.getState() != GameState.RUNNING) return lines;

        GameParticipant participant = GameParticipant.getByPlayer(player);
        GameParticipantStats stats = participant.getStats();
        lines.add(ColorUtil.color("&fTIME: &6" + TimeUtil.formatTimeMillisToClock(System.currentTimeMillis() - game.getStartTimeStamp())));
        lines.add(ColorUtil.color("&fLEVEL: &6" + stats.getLevel()));
        lines.add(ColorUtil.color("&fPOINTS: &6" + stats.getPoints()));
        lines.add("");
        lines.add(ColorUtil.color("&a&lTOP 5"));

        int place = 1;
        for (GameParticipant topParticipant : game.getPlayingParticipants().stream().sorted((o1, o2) -> -(o1.getStats().getPoints() - o2.getStats().getPoints())).collect(Collectors.toList())) {
            lines.add(ColorUtil.color("&7" + topParticipant.getStats().getPoints() + " &" + TOP_COLORS[place] + topParticipant.getPlayer().getName()));

            if (++place == 5) break;
        }

        lines.add(0, "");
        lines.add("");

        return lines;
    }
}
