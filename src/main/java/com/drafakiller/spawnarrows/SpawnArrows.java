package com.drafakiller.spawnarrows;

import com.destroystokyo.paper.Namespaced;
import com.drafakiller.spawnarrows.listeners.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class SpawnArrows extends JavaPlugin {
	
	public final List<SpawnArrow> list = new ArrayList<>();
	public final List<Entity> entitiesToBeRemoved = new ArrayList<>();
	public List<Double> lifetimeList;
	
	public final Map<String, NamespacedKey> namespacedKeys = new HashMap<>();
	public final Map<EntityType, BoundingBox> boundingBoxes = new HashMap<>();
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		lifetimeList = getConfig().getDoubleList("entities.lifetime-per-level");
		createKeys();
		
		PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvents(new EntityShootListener(this), this);
		pluginManager.registerEvents(new ProjectileHitListener(this), this);
		pluginManager.registerEvents(new PrepareItemCraftListener(this), this);
		pluginManager.registerEvents(new PlayerRecipeDiscoverListener(this), this);
		pluginManager.registerEvents(new EntityTargetListener(this), this);
		pluginManager.registerEvents(new EntityDeathListener(this), this);
		pluginManager.registerEvents(new SlimeSplitListener(this), this);
		
		SpawnArrows_Recipe.createArrows(this);
	}
	
	public void onDisable() {
		for (Entity entity : entitiesToBeRemoved) {
			killEntity(entity);
			entity.remove();
		}
	}
	
	public void createKeys() {
		namespacedKeys.clear();
		namespacedKeys.put("recipe", new NamespacedKey(this, "recipe"));
		namespacedKeys.put("levelUp", new NamespacedKey(this, "recipe_levelup"));
		
		namespacedKeys.put("type", new NamespacedKey(this, "type"));
		namespacedKeys.put("amount", new NamespacedKey(this, "amount"));
		namespacedKeys.put("lifetime", new NamespacedKey(this, "lifetime"));
		namespacedKeys.put("owner", new NamespacedKey(this, "owner"));
	}
	
	public @Nullable EntityType getEntityType(String type) {
		for (SpawnArrow spawnArrow : list) {
			if (spawnArrow.getEntityType().name().equals(type)) {
				return spawnArrow.getEntityType();
			}
		}
		return null;
	}
	
	public SpawnArrow getSpawnArrow(String entityType) {
		for (SpawnArrow arrow : list) {
			if (arrow.getEntityType().name().equals(entityType)) {
				return arrow;
			}
		}
		return null;
	}
	
	public SpawnArrow getSpawnArrow(EntityType entityType) {
		for (SpawnArrow arrow : list) {
			if (arrow.getEntityType().equals(entityType)) {
				return arrow;
			}
		}
		return null;
	}
	
	public BoundingBox getBoundingBoxOf(EntityType entityType) {
		if (boundingBoxes.containsKey(entityType)) {
			return boundingBoxes.get(entityType);
		} else {
			Optional<World> firstWorld = this.getServer().getWorlds().stream().findFirst();
			if (firstWorld.isPresent()) {
				World world = firstWorld.get();
				Entity entity = world.spawnEntity(new Location(world, 0, -100, 0), entityType, CreatureSpawnEvent.SpawnReason.CUSTOM,
					newEntity -> {
						if (newEntity instanceof LivingEntity) {
							LivingEntity newLivingEntity = (LivingEntity) newEntity;
							newLivingEntity.setInvisible(true);
						}
					}
				);
				BoundingBox boundingBox = entity.getBoundingBox();
				entity.remove();
				boundingBoxes.put(entityType, boundingBox);
				return boundingBox;
			}
			return new BoundingBox();
		}
	}
	
	public EntityType getSpawnType(PersistentDataContainer container) {
		return getEntityType(container.get(namespacedKeys.get("type"), PersistentDataType.STRING));
	}
	
	public void setSpawnType(PersistentDataContainer container, EntityType entityType) {
		if (entityType != null) {
			container.set(namespacedKeys.get("type"), PersistentDataType.STRING, entityType.name());
		} else {
			container.remove(namespacedKeys.get("type"));
		}
	}
	
	public int getSpawnAmount(PersistentDataContainer container) {
		Integer amount = container.get(namespacedKeys.get("amount"), PersistentDataType.INTEGER);
		return amount != null ? amount : 1;
	}
	
	public void setSpawnAmount(PersistentDataContainer container, Integer amount) {
		if (amount != null) {
			container.set(namespacedKeys.get("amount"), PersistentDataType.INTEGER, amount);
		} else {
			container.remove(namespacedKeys.get("amount"));
		}
	}
	
	public int getSpawnLifetime(int level) {
		if (getConfig().getBoolean("entities.lifetime", true) && lifetimeList.size() > 0) {
			level = Math.min(Math.max(level, 0), lifetimeList.size());
			return (int) Math.round(lifetimeList.get(level - 1) * 20);
		}
		return 0;
	}
	
	public int getSpawnLifetime(PersistentDataContainer container) {
		Integer lifetime = container.get(namespacedKeys.get("lifetime"), PersistentDataType.INTEGER);
		return lifetime != null ? lifetime : 0;
	}
	
	public void setSpawnLifetime(PersistentDataContainer container, Integer lifetime) {
		if (lifetime != null) {
			container.set(namespacedKeys.get("lifetime"), PersistentDataType.INTEGER, lifetime);
		} else {
			container.remove(namespacedKeys.get("lifetime"));
		}
	}
	
	public Player getSpawnOwner(PersistentDataContainer container) {
		String uniqueId = container.get(namespacedKeys.get("owner"), PersistentDataType.STRING);
		return uniqueId != null ? Bukkit.getPlayer(UUID.fromString(uniqueId)) : null;
	}
	
	public void setSpawnOwner(PersistentDataContainer container, Player player) {
		if (player != null) {
			container.set(namespacedKeys.get("owner"), PersistentDataType.STRING, player.getUniqueId().toString());
		} else {
			container.remove(namespacedKeys.get("owner"));
		}
	}
	
	public void removeEntityAfterTime(Entity entity, int ticks) {
		if (!entitiesToBeRemoved.contains(entity)) {
			Bukkit.getScheduler().runTaskLater(this, () -> {
				killEntity(entity);
				entitiesToBeRemoved.remove(entity);
			}, ticks);
			entitiesToBeRemoved.add(entity);
		}
	}
	
	public void killEntity(Entity entity) {
		if (entity instanceof LivingEntity) {
			((LivingEntity) entity).setHealth(0);
		} else {
			entity.remove();
		}
	}
	
	public boolean recipeExists(NamespacedKey namespacedKey) {
		for (SpawnArrow arrow : list) {
			if (arrow.getNamespacedRecipeKey().equals(namespacedKey)) {
				return true;
			}
		}
		return false;
	}
	
}