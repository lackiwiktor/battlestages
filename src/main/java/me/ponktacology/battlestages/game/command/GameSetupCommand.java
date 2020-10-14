package me.ponktacology.battlestages.game.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import me.ponktacology.battlestages.game.Game;
import me.ponktacology.battlestages.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class GameSetupCommand {

    private final Game game;

    @Command(name = "game.setgameloc", desc = "Sets game location")
    @Require("game.manage")
    public void setSpawn(@Sender Player player) {
        Game.setGameLocation(player.getLocation());

        player.sendMessage(ColorUtil.color("&aSuccessfully changed game location."));
    }

    @Command(name = "game.setwaitingloc", desc = "Sets game waiting location")
    @Require("game.manage")
    public void setWaiting(@Sender Player player) {
        Game.setWaitingLocation(player.getLocation());

        player.sendMessage(ColorUtil.color("&aSuccessfully changed game waiting location."));
    }

    @Command(name = "game.forcestart", desc = "Forces game to start")
    public void forceStart(@Sender CommandSender sender) {
        game.start();

        sender.sendMessage(ColorUtil.color("&aSuccessfully force started game."));
    }
}
