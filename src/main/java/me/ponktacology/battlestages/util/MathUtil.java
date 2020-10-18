package me.ponktacology.battlestages.util;

import java.util.Random;

public class MathUtil {

    private static final Random RANDOM = new Random();

    public static int getRandomNumber(int min, int max) {
        return RANDOM.nextInt(max - min) + min;
    }
}
