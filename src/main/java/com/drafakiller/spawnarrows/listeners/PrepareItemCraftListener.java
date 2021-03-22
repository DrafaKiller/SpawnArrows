package com.drafakiller.spawnarrows.listeners;

import com.drafakiller.spawnarrows.SpawnArrow;
import com.drafakiller.spawnarrows.SpawnArrows;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PrepareItemCraftListener implements Listener {
	
	public final SpawnArrows plugin;
	public PrepareItemCraftListener(SpawnArrows plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent event) {
		List<ItemStack> items = new ArrayList<>();
		for (ItemStack item : event.getInventory().getMatrix()) {
			if (item != null) {
				items.add(item.clone());
			}
		}
		
		if (items.size() == 2) {
			SpawnArrow arrow = SpawnArrow.fromItemStack(plugin, items.get(0));
			if (arrow != null && arrow.isCombinable(items.get(1))) {
				int maxUpgrade = plugin.getConfig().getInt("arrows.max-upgrade", 0);
				if (plugin.getConfig().getBoolean("arrows.upgradeable", true) && (maxUpgrade <= 0 || arrow.getSpawnAmount() + 1 <= maxUpgrade)) {
					arrow.setSpawnAmount(arrow.getSpawnAmount() + 1);
					arrow.setSpawnLifetime(null);
					event.getInventory().setResult(arrow.getItemStack());
				} else {
					event.getInventory().setResult(null);
				}
			} else if (items.get(0).getType().equals(Material.TIPPED_ARROW) && items.get(1).getType().equals(Material.TIPPED_ARROW)) {
				event.getInventory().setResult(null);
			}
		}
	}
	
}
