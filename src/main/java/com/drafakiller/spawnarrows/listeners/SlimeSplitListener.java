package com.drafakiller.spawnarrows.listeners;

import com.drafakiller.spawnarrows.SpawnArrows;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.persistence.PersistentDataContainer;

public class SlimeSplitListener implements Listener {
	
	public final SpawnArrows plugin;
	public SlimeSplitListener(SpawnArrows plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onSlimeSplit(SlimeSplitEvent event) {
		PersistentDataContainer persistentContainer = event.getEntity().getPersistentDataContainer();
		if (plugin.getSpawnType(persistentContainer) != null) {
			event.setCancelled(true);
		}
	}

}
