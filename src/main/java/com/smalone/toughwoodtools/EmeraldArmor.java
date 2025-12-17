package com.smalone.toughwoodtools;

import org.bukkit.plugin.java.JavaPlugin;

public class EmeraldArmor extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(
                new CraftingListener(this),
                this
        );
    }

    @Override
    public void onDisable() {
        // Nothing needed
    }
}
