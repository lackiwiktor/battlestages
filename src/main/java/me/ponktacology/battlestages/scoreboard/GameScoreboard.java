package me.ponktacology.battlestages.scoreboard;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.battlestages.game.Game;
import me.ponktacology.battlestages.util.ColorUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GameScoreboard implements AssembleAdapter {

    private final Game game;

    @Override
    public String getTitle(Player player) {
        return ColorUtil.color("&eBattleStages");
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();

        switch (game.getState()) {
            case WAITING_FOR_PLAYERS: {
                lines.add(ColorUtil.color("&eWaiting..."));
                break;
            }
            case RUNNING: {
                lines.add(ColorUtil.color("&aRunning"));
                break;
            }
            case ENDING: {
                lines.add(ColorUtil.color("&6Ending..."));
                break;
            }
        }

        return lines;
    }
}
