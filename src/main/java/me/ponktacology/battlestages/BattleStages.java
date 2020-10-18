package me.ponktacology.battlestages;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import io.github.thatkawaiisam.assemble.Assemble;
import me.ponktacology.battlestages.game.Game;
import me.ponktacology.battlestages.game.listener.GameListener;
import me.ponktacology.battlestages.game.command.GameSetupCommand;
import me.ponktacology.battlestages.participant.listener.GameParticipantListener;
import me.ponktacology.battlestages.scoreboard.GameScoreboard;
import me.ponktacology.simpleconfig.config.ConfigFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleStages extends JavaPlugin {

    private ConfigFactory configFactory;

    @Override
    public void onEnable() {
        //Setup configuration
        configFactory = new ConfigFactory(this.getClass());

        Game game = new Game(this);

        //Register listeners
        Bukkit.getPluginManager().registerEvents(new GameParticipantListener(game), this);
        Bukkit.getPluginManager().registerEvents(new GameListener(game), this);

        //Register commands
        CommandService drink = Drink.get(this);
        drink.register(new GameSetupCommand(game), "game");
        drink.registerCommands();

        //Setup scoreboard
        new Assemble(this, new GameScoreboard(game));

        //Initialize game
        game.init();
    }

    @Override
    public void onDisable() {
        configFactory.save();
    }
}
