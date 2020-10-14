package me.ponktacology.battlestages;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import io.github.thatkawaiisam.assemble.Assemble;
import me.ponktacology.battlestages.game.Game;
import me.ponktacology.battlestages.game.command.GameSetupCommand;
import me.ponktacology.battlestages.participant.GameParticipantListener;
import me.ponktacology.battlestages.scoreboard.GameScoreboard;
import me.ponktacology.simpleconfig.config.ConfigFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleStages extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new ConfigFactory(this.getClass());
        Game game = new Game(this);

        Bukkit.getPluginManager().registerEvents(new GameParticipantListener(game), this);

        CommandService drink = Drink.get(this);
        drink.register(new GameSetupCommand(game), "game");
        drink.registerCommands();
        new Assemble(this, new GameScoreboard(game));

        game.init();
    }
}
