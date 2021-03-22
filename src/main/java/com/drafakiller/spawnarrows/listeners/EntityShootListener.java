package com.drafakiller.spawnarrows.listeners;

import com.drafakiller.spawnarrows.SpawnArrows;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

public class EntityShootListener implements Listener {
	
	public final SpawnArrows plugin;
	public EntityShootListener(SpawnArrows plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent event) {
		ItemStack itemStack = event.getConsumable();
		if (itemStack != null && itemStack.hasItemMeta()) {
			PersistentDataContainer itemData = itemStack.getItemMeta().getPersistentDataContainer();
			EntityType type = plugin.getSpawnType(itemData);
			if (type != null) {
				PersistentDataContainer projectileData = event.getProjectile().getPersistentDataContainer();
				plugin.setSpawnType(projectileData, type);
				plugin.setSpawnAmount(projectileData, plugin.getSpawnAmount(itemData));
				plugin.setSpawnLifetime(projectileData, plugin.getSpawnLifetime(itemData));
			}
		}
	}
	
}
