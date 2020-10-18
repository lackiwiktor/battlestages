package me.ponktacology.battlestages.util;

import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static String formatTimeMillisToClock(long millis) {
        return millis / 1000L <= 0
                ? "0:00"
                : String.format(
                "%01d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
