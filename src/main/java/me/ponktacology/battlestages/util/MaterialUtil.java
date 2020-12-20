package me.ponktacology.battlestages.util;

import org.bukkit.Material;

public class MaterialUtil {

    public static boolean isArmor(Material material) {
        switch (material) {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS:
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS:
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS:
            case GOLD_HELMET:
            case GOLD_CHESTPLATE:
            case GOLD_LEGGINGS:
            case GOLD_BOOTS:
                return true;
            default:
                return false;
        }
    }

    public static boolean isSword(Material material) {
        switch (material) {
            case STONE_SWORD:
            case DIAMOND_SWORD:
            case GOLD_SWORD:
            case IRON_SWORD:
            case WOOD_SWORD:
                return true;
            default:
                return false;
        }
    }

    public static boolean isBetterArmor(Material material, Material material2) {
        String mat1 = material.toString().split("_")[0];
        String mat2 = material2.toString().split("_")[0];

        switch (mat1) {
            case "LEATHER": {
                return false;
            }
            case "CHAINMAIL": {
                return !"LEATHER".equals(mat2);
            }
            case "GOLD": {
                switch (mat2) {
                    case "LEATHER":
                    case "CHAINMAIL":
                        return false;
                    default:
                        return true;
                }
            }
            case "IRON": {
                switch (mat2) {
                    case "LEATHER":
                    case "CHAINMAIL":
                    case "GOLD":
                        return false;
                    default:
                        return true;
                }
            }
            case "DIAMOND": {
                switch (mat2) {
                    case "LEATHER":
                    case "CHAINMAIL":
                    case "GOLD":
                    case "IRON":
                        return false;
                    default:
                        return true;
                }
            }
            case "NETHERITE": {
                switch (mat2) {
                    case "LEATHER":
                    case "CHAINMAIL":
                    case "GOLD":
                    case "IRON":
                    case "DIAMOND":
                        return false;
                    default:
                        return true;
                }
            }
            default:
                return false;
        }

    }

    public static boolean isBetterSword(Material material, Material material2) {
        switch (material) {
            case WOOD_SWORD: {
                switch (material2) {
                    case DIAMOND_SWORD:
                    case GOLD_SWORD:
                    case IRON_SWORD:
                    case WOOD_SWORD:
                    case STONE_SWORD:
                        return true;
                    default:
                        return false;
                }
            }
            case STONE_SWORD: {
                switch (material2) {
                    case DIAMOND_SWORD:
                    case GOLD_SWORD:
                    case IRON_SWORD:
                    case WOOD_SWORD:
                        return true;
                    default:
                        return false;
                }
            }
            case GOLD_SWORD: {
                switch (material2) {
                    case DIAMOND_SWORD:
                    case IRON_SWORD:
                        return true;
                    default:
                        return false;
                }
            }
            case IRON_SWORD: {
                switch (material2) {
                    case DIAMOND_SWORD:
                        return true;
                    default:
                        return false;
                }
            }
            case DIAMOND_SWORD: {
                return true;
            }
            default:
                return false;
        }
    }
}
