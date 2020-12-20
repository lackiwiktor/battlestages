package me.ponktacology.battlestages.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtil {

    public static void makeUnbreakable(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.spigot().setUnbreakable(true);
        itemStack.setItemMeta(meta);
    }
}
