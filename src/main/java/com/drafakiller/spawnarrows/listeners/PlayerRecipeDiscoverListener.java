package com.drafakiller.spawnarrows.listeners;

import com.drafakiller.spawnarrows.SpawnArrows;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;

public class PlayerRecipeDiscoverListener implements Listener {
	
	public final SpawnArrows plugin;
	public PlayerRecipeDiscoverListener(SpawnArrows plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerRecipeDiscover(PlayerRecipeDiscoverEvent event) {
		if (event.getRecipe().equals(plugin.namespacedKeys.get("levelUp")) || (!plugin.getConfig().getBoolean("arrows.recipe-discover", true) && plugin.recipeExists(event.getRecipe()))) {
			event.setCancelled(true);
		}
	}
	
}
