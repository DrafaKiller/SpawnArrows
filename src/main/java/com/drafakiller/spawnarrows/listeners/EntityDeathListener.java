package com.drafakiller.spawnarrows.listeners;

import com.drafakiller.spawnarrows.SpawnArrows;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {
	
	public final SpawnArrows plugin;
	public EntityDeathListener(SpawnArrows plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (plugin.getSpawnType(event.getEntity().getPersistentDataContainer()) != null) {
			if (!plugin.getConfig().getBoolean("entities.drops-items", false)) event.getDrops().clear();
			if (!plugin.getConfig().getBoolean("entities.drops-xp", false)) event.setDroppedExp(0);
		}
	}
	
}
