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
            case GOLDEN_HELMET:
            case GOLDEN_CHESTPLATE:
            case GOLDEN_LEGGINGS:
            case GOLDEN_BOOTS:
            case NETHERITE_HELMET:
            case NETHERITE_CHESTPLATE:
            case NETHERITE_LEGGINGS:
            case NETHERITE_BOOTS:
                return true;
            default:
                return false;
        }
    }

    public static boolean isSword(Material material) {
        switch (material) {
            case STONE_SWORD:
            case DIAMOND_SWORD:
            case GOLDEN_SWORD:
            case IRON_SWORD:
            case WOODEN_SWORD:
            case NETHERITE_SWORD:
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
            case WOODEN_SWORD: {
                switch (material2) {
                    case DIAMOND_SWORD:
                    case GOLDEN_SWORD:
                    case IRON_SWORD:
                    case WOODEN_SWORD:
                    case NETHERITE_SWORD:
                    case STONE_SWORD:
                        return true;
                    default:
                        return false;
                }
            }
            case STONE_SWORD: {
                switch (material2) {
                    case DIAMOND_SWORD:
                    case GOLDEN_SWORD:
                    case IRON_SWORD:
                    case WOODEN_SWORD:
                    case NETHERITE_SWORD:
                        return true;
                    default:
                        return false;
                }
            }
            case GOLDEN_SWORD: {
                switch (material2) {
                    case DIAMOND_SWORD:
                    case IRON_SWORD:
                    case NETHERITE_SWORD:
                        return true;
                    default:
                        return false;
                }
            }
            case IRON_SWORD: {
                switch (material2) {
                    case DIAMOND_SWORD:
                    case NETHERITE_SWORD:
                        return true;
                    default:
                        return false;
                }
            }
            case DIAMOND_SWORD: {
                return material2 == Material.NETHERITE_SWORD;
            }
            case NETHERITE_SWORD: {
                return true;
            }
            default:
                return false;
        }
    }
}
