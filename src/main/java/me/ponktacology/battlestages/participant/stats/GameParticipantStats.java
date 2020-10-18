package me.ponktacology.battlestages.participant.stats;

import lombok.Data;
import me.ponktacology.battlestages.participant.GameParticipant;

@Data
public class GameParticipantStats {
    private int level;
    private int points;

    public int incrementLevel() {
        return ++level;
    }

    public int addPoints(int points) {
        return this.points += points;
    }

    public void removePoints(int points) {
         this.points = Math.max(0, this.points - points);
    }

    public int calculatePoints(GameParticipant victim) {
        return 12 + (3 * (Math.max(1, victim.getStats().getLevel() - level)));
    }
}
