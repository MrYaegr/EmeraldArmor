package com.smalone.toughwoodtools;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.enchantments.Enchantment;

public class CraftingListener implements Listener {

    private final EmeraldArmor plugin;

    public CraftingListener(EmeraldArmor plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {

        Recipe recipe = event.getRecipe();

        // Only handle shaped recipes
        if (!(recipe instanceof ShapedRecipe)) {
            return;
        }

        ItemStack result = recipe.getResult();
        if (result == null) {
            return;
        }

        Material type = result.getType();

        // Only handle diamond armor results
        if (!(type == Material.DIAMOND_HELMET
                || type == Material.DIAMOND_CHESTPLATE
                || type == Material.DIAMOND_LEGGINGS
                || type == Material.DIAMOND_BOOTS)) {
            return;
        }

        ItemStack[] matrix = event.getInventory().getMatrix();

        int emeraldCount = 0;
        int diamondCount = 0;

        // Count emeralds/diamonds and validate ingredients
        for (ItemStack item : matrix) {

            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            Material mat = item.getType();

            if (mat == Material.EMERALD) {
                emeraldCount += item.getAmount();
            } else if (mat == Material.DIAMOND) {
                diamondCount += item.getAmount();
            } else {
                // If any other item is present, skip custom logic
                return;
            }
        }

        // If neither diamond nor emerald present, skip
        if (diamondCount == 0 && emeraldCount == 0) {
            return;
        }

        // Create a copy of the normal result
        ItemStack output = result.clone();

        // Only apply enchant when emeralds are used
        if (emeraldCount > 0) {

            int level = emeraldCount;

            // Config: cap protection if configured
            int maxLevel = plugin.getConfig().getInt("emerald-armor.max-protection-level", 4);
            boolean allowOver = plugin.getConfig().getBoolean("emerald-armor.allow-overpowered", false);

            if (!allowOver && level > maxLevel) {
                level = maxLevel;
            }

            output.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level);
        }

        event.getInventory().setResult(output);
    }
}
