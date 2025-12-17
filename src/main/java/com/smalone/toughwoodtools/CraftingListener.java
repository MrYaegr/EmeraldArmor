package com.smalone.toughwoodtools;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class CraftingListener implements Listener {

    private final EmeraldArmor plugin;

    public CraftingListener(EmeraldArmor plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {

        Recipe recipe = event.getRecipe();
        if (!(recipe instanceof ShapedRecipe)) {
            return;
        }

        ItemStack result = recipe.getResult();
        if (result == null) {
            return;
        }

        Material resultType = result.getType();

        // Only diamond armor
        if (resultType != Material.DIAMOND_HELMET
                && resultType != Material.DIAMOND_CHESTPLATE
                && resultType != Material.DIAMOND_LEGGINGS
                && resultType != Material.DIAMOND_BOOTS) {
            return;
        }

        ItemStack[] matrix = event.getInventory().getMatrix();

        int emeralds = 0;
        int diamonds = 0;

        for (ItemStack item : matrix) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            if (item.getType() == Material.EMERALD) {
                emeralds += item.getAmount();
            } else if (item.getType() == Material.DIAMOND) {
                diamonds += item.getAmount();
            } else {
                // Invalid item in recipe
                return;
            }
        }

        if (emeralds <= 0) {
            return; // no emeralds, vanilla behavior
        }

        int level = emeralds;

        boolean allowOver = plugin.getConfig().getBoolean(
                "emerald-armor.allow-overpowered", false
        );
        int max = plugin.getConfig().getInt(
                "emerald-armor.max-protection-level", 4
        );

        if (!allowOver && level > max) {
            level = max;
        }

        ItemStack output = result.clone();
        output.addUnsafeEnchantment(
                Enchantment.PROTECTION_ENVIRONMENTAL,
                level
        );

        event.getInventory().setResult(output);
    }
}
