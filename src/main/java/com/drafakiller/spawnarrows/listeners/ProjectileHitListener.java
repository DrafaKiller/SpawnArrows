package com.drafakiller.spawnarrows.listeners;

import com.drafakiller.spawnarrows.SpawnArrows;
import com.drafakiller.spawnarrows.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class ProjectileHitListener implements Listener {
	
	public final SpawnArrows plugin;
	public ProjectileHitListener(SpawnArrows plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			boolean accurateSpawn = plugin.getConfig().getBoolean("entities.accurate-spawn", true);
			
			Location location = projectile.getLocation();
			Block hitBlock = event.getHitBlock();
			BlockFace blockFace = event.getHitBlockFace();
			Entity hitEntity = event.getHitEntity();

			if (hitBlock != null && blockFace != null) {
				if (accurateSpawn) {
					location.add(blockFace.getDirection().setY(0).multiply(0.1));
				} else {
					location = hitBlock.getLocation().add(blockFace.getDirection()).toCenterLocation();
					location.setY(hitBlock.getY() + blockFace.getModY());
				}
			} else if (hitEntity != null) {
				LocationUtil.limitLocation(location, hitEntity.getBoundingBox());
			}
			
			Player player = null;
			if (projectile.getShooter() instanceof Player) {
				player = (Player) projectile.getShooter();
			}
			
			PersistentDataContainer projectileData = projectile.getPersistentDataContainer();
			EntityType entityType = plugin.getSpawnType(projectileData);
			int amount = plugin.getSpawnAmount(projectileData);
			int lifetime = plugin.getSpawnLifetime(projectileData);
			
			if (entityType != null) {
				BoundingBox boundingBox = plugin.getBoundingBoxOf(entityType);
				if (accurateSpawn) LocationUtil.setAvailableLocation(location, boundingBox);
				
				for (int i = 0; i < amount; i++) {
					Location entityLocation = location.clone();
					if (i != 0) {
						double randomizeValue = 0.1;
						entityLocation.add(Vector.getRandom().multiply(new Vector(Math.random() * (-randomizeValue*2) + randomizeValue, 0, Math.random() * (-randomizeValue*2) + randomizeValue)));
						if (accurateSpawn) LocationUtil.setAvailableLocation(entityLocation, boundingBox);
					}
					
					Entity entity = projectile.getWorld().spawnEntity(entityLocation, entityType);
					PersistentDataContainer entityContainer = entity.getPersistentDataContainer();
					if (player != null) plugin.setSpawnOwner(entityContainer, player);
					plugin.setSpawnType(entityContainer, entityType);
					if (lifetime > 0) plugin.removeEntityAfterTime(entity, lifetime);
				}
			}
			
			projectile.remove();
		}, 1);
	}
	
}
