package com.drafakiller.spawnarrows;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public abstract class SpawnArrows_Recipe {
	
	public static void createArrows(SpawnArrows plugin) {
		plugin.getServer().addRecipe(
			new ShapelessRecipe(plugin.namespacedKeys.get("levelUp"), new ItemStack(Material.TIPPED_ARROW))
				.addIngredient(2, Material.TIPPED_ARROW)
		);
		
		/*
			new SpawnArrow(plugin, EntityType.ZOMBIE, Color.fromRGB(81, 118, 63),
				new ShapelessRecipe(plugin.namespacedKeys.get("recipe"), new ItemStack(Material.TIPPED_ARROW))
					.addIngredient(Material.ARROW)
					.addIngredient(8, Material.ROTTEN_FLESH)
			).appendToList();
		*/
		
		createArrowsFromConfig(plugin);
	}
	
	public static void createArrowsFromConfig(SpawnArrows plugin) {
		ConfigurationSection recipes = plugin.getConfig().getConfigurationSection("recipes");
		if (recipes != null) {
			for (String type : recipes.getKeys(false)) {
				EntityType entityType = getEntityType(type);
				if (entityType != null) {
					ConfigurationSection recipe = recipes.getConfigurationSection(type);
					if (recipe != null) {
						Color color = Color.fromRGB(
							recipe.getInt("color.r", 255),
							recipe.getInt("color.g", 255),
							recipe.getInt("color.b", 255)
						);
						List<Map<?, ?>> ingredients = recipe.getMapList("ingredients");
						if (ingredients.size() > 0) {
							ShapelessRecipe shapelessRecipe = new ShapelessRecipe(plugin.namespacedKeys.get("recipe"), new ItemStack(Material.TIPPED_ARROW));
							boolean valid = true;
							for (Map<?, ?> ingredient : ingredients) {
								Material material = Material.getMaterial(ingredient.keySet().iterator().next().toString());
								if (material != null) {
									Integer amount = getInteger(ingredient.values().iterator().next().toString());
									try {
										shapelessRecipe.addIngredient(amount != null ? amount : 1, material);
									} catch (Exception e) {
										Bukkit.getLogger().log(Level.WARNING, "SpawnArrows - Config error: Invalid ingredient recipe. (Recipe ignored: " + type + ")");
										valid = false;
										break;
									}
								} else {
									Bukkit.getLogger().log(Level.WARNING, "SpawnArrows - Config error: Ingredient doesn't exist. (Recipe ignored: " + type + ")");
									valid = false;
									break;
								}
							}
							if (valid) {
								Integer amount = recipe.contains("level") ? recipe.getInt("level", 1) : null;
								Double lifetime = recipe.contains("lifetime") ? recipe.getDouble("lifetime", 0) : null;
								new SpawnArrow(plugin, entityType, color, amount, lifetime == null ? null : (int) Math.round(lifetime * 20), shapelessRecipe).appendToList();
							}
						} else {
							Bukkit.getLogger().log(Level.WARNING, "SpawnArrows - Config error: Recipe must have ingredients. (Recipe ignored: " + type + ")");
						}
					}
				} else {
					Bukkit.getLogger().log(Level.WARNING, "SpawnArrows - Config error: EntityType doesn't exist. (Recipe ignored: " + type + ")");
				}
			}
		}
	}
	
	private static EntityType getEntityType(String entityType) {
		try {
			return EntityType.valueOf(entityType);
		} catch (Exception e) {
			return null;
		}
	}
	
	private static Integer getInteger(String number) {
		try {
			return Integer.valueOf(number);
		} catch (Exception e) {
			return null;
		}
	}
	
}
