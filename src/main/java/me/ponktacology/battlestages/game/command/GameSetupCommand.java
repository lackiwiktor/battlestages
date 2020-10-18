package me.ponktacology.battlestages.game.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import me.ponktacology.battlestages.game.Game;
import me.ponktacology.battlestages.game.GameState;
import me.ponktacology.battlestages.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@RequiredArgsConstructor
public class GameSetupCommand {

    private final Game game;

    @Command(name = "setGameLoc", desc = "Sets game location")
    @Require("game.manage")
    public void setSpawn(@Sender Player player) {
        Game.GAME_LOCATION = player.getLocation();

        player.sendMessage(ColorUtil.color("&aSuccessfully changed game location."));
    }

    @Command(name = "setWaitingLoc", desc = "Sets game waiting location")
    @Require("game.manage")
    public void setWaiting(@Sender Player player) {
        Game.WAITING_LOCATION = player.getLocation();

        player.sendMessage(ColorUtil.color("&aSuccessfully changed game waiting location."));
    }

    @Command(name = "forceStart", desc = "Forces game to start")
    @Require("game.manage")
    public void forceStart(@Sender CommandSender sender) {
        if (game.getState() == GameState.WAITING_FOR_PLAYERS) {
            game.start();
        } else {
            sender.sendMessage(ColorUtil.color("&cGame has already started."));
            return;
        }

        sender.sendMessage(ColorUtil.color("&aSuccessfully force started game."));
    }

    @Command(name = "forceEnd", desc = "Forces game to end")
    @Require("game.manage")
    public void forceEnd(@Sender CommandSender sender) {
        if (game.getState() == GameState.RUNNING) {
            game.end(null);
        } else {
            sender.sendMessage(ColorUtil.color("&cGame is not running."));
            return;
        }

        sender.sendMessage(ColorUtil.color("&aSuccessfully force ended game."));
    }

    @Command(name = "addItem", desc = "Adds item")
    @Require("game.manage")
    public void addItem(@Sender Player sender) {
        Game.ITEMS.add(sender.getInventory().getItemInMainHand());

        sender.sendMessage(ColorUtil.color("&aSuccessfully added item."));
    }

    @Command(name = "removeItem", desc = "Removes an item", usage = "<index>")
    @Require("game.manage")
    public void removeItem(@Sender CommandSender sender, Integer index) {
        if (Game.ITEMS.remove(index.intValue()) != null) {
            sender.sendMessage(ColorUtil.color("&aSuccessfully removed item."));
        } else {
            sender.sendMessage(ColorUtil.color("&cThere is no item on this index."));
        }
    }

    @Command(name = "listItems", desc = "Prints list of items.")
    @Require("game.manage")
    public void listItems(@Sender CommandSender sender) {
        List<ItemStack> items = Game.ITEMS;

        if (items.isEmpty()) {
            sender.sendMessage(ColorUtil.color("This level doesn't have set items."));
            return;
        }

        for (int index = 0; index < items.size(); index++) {
            ItemStack item = items.get(index);
            sender.sendMessage(ColorUtil.color("&e#" + index + " " + item.getType() + " &7(" + item.getAmount() + ")"));
        }
    }
}
