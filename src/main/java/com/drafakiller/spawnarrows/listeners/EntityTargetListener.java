package com.drafakiller.spawnarrows.listeners;

import com.drafakiller.spawnarrows.SpawnArrows;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTargetListener implements Listener {
	
	public final SpawnArrows plugin;
	public EntityTargetListener(SpawnArrows plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		Player player = plugin.getSpawnOwner(event.getEntity().getPersistentDataContainer());
		if (!plugin.getConfig().getBoolean("entities.attack-owner", false) && player != null && player.equals(event.getTarget())) {
			event.setCancelled(true);
		}
	}
	
}
