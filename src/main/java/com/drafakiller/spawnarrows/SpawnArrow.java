package com.drafakiller.spawnarrows;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SpawnArrow {
	
	private EntityType entityType;
	private Color color;
	private Integer spawnAmount;
	private Integer spawnLifetime;
	private ShapelessRecipe recipe;
	private NamespacedKey namespacedRecipeKey;
	private DecimalFormat decimalFormat = new DecimalFormat("#.##");
	
	public final SpawnArrows plugin;
	public SpawnArrow(@NotNull SpawnArrows plugin, @NotNull EntityType entityType, @NotNull Color color, @NotNull ShapelessRecipe recipe) {
		this.plugin = plugin;
		setEntityType(entityType);
		setColor(color);
		setRecipe(recipe);
	}
	
	public SpawnArrow(@NotNull SpawnArrows plugin, @NotNull EntityType entityType, @NotNull Color color, Integer spawnAmount, Integer lifetime, @NotNull ShapelessRecipe recipe) {
		this.plugin = plugin;
		setEntityType(entityType);
		setColor(color);
		setSpawnAmount(spawnAmount);
		setSpawnLifetime(lifetime);
		setRecipe(recipe);
	}
	
	public EntityType getEntityType() {
		return entityType;
	}
	
	public SpawnArrow setEntityType(EntityType entityType) {
		if (entityType != null) {
			this.entityType = entityType;
		}
		return this;
	}
	
	public SpawnArrow setEntityType(String entityType) {
		setEntityType(plugin.getEntityType(entityType));
		return this;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getColorString() {
		return String.join(",",
			String.valueOf(color.getRed()),
			String.valueOf(color.getGreen()),
			String.valueOf(color.getBlue())
		);
	}
	
	public SpawnArrow setColor(Color color) {
		if (color != null) {
			this.color = color;
		}
		return this;
	}
	
	public SpawnArrow setColor(String colorRGB) {
		if (colorRGB != null) {
			String[] split = colorRGB.split(",");
			if (split.length == 3) {
				setColor(Color.fromRGB(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])));
			}
		}
		return this;
	}
	
	public int getSpawnAmount() {
		return spawnAmount != null ? spawnAmount : 1;
	}
	
	public SpawnArrow setSpawnAmount(Integer spawnAmount) {
		this.spawnAmount = spawnAmount != null ? Math.max(spawnAmount, 0) : null;
		return this;
	}
	
	public int getSpawnLifetime() {
		return spawnLifetime != null ? spawnLifetime : plugin.getSpawnLifetime(getSpawnAmount());
	}
	
	public SpawnArrow setSpawnLifetime(Integer spawnLifetime) {
		this.spawnLifetime = spawnLifetime != null ? Math.max(spawnLifetime, 0) : null;
		return this;
	}
	
	public ShapelessRecipe getRecipe() {
		return recipe;
	}
	
	public SpawnArrow setRecipe(@NotNull ShapelessRecipe recipe) {
		Validate.isTrue(this.recipe == null, "Recipe was already created.");
		this.recipe = recipe;
		createRecipe(recipe);
		return this;
	}
	
	public NamespacedKey getNamespacedRecipeKey() {
		return namespacedRecipeKey;
	}
	
	public void appendToList() {
		if (plugin.getConfig().getBoolean("arrows.craftable", true)) {
			plugin.getServer().addRecipe(recipe);
		}
		plugin.list.add(this);
	}
	
	public void createRecipe(@NotNull ShapelessRecipe baseRecipe) {
		namespacedRecipeKey = new NamespacedKey(plugin, "recipe_" + entityType.name());
		recipe = new ShapelessRecipe(namespacedRecipeKey, getItemStack());
		recipe.setGroup("spawnarrows");
		for (ItemStack ingredient : baseRecipe.getIngredientList()) {
			recipe.addIngredient(ingredient);
		}
	}
	
	public ItemStack getItemStack() {
		ItemStack item = new ItemStack(Material.TIPPED_ARROW);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		
		PersistentDataContainer persistentContainer = meta.getPersistentDataContainer();
		plugin.setSpawnType(persistentContainer, entityType);
		plugin.setSpawnAmount(persistentContainer, getSpawnAmount());
		plugin.setSpawnLifetime(persistentContainer, getSpawnLifetime());
		
		meta.setColor(color);
		meta.displayName(
			Component.text()
				.append(Component.text(entityType.name().replace("_", " ").toUpperCase(), TextColor.color(color.asRGB()), TextDecoration.BOLD))
				.append(Component.text(" Spawn Arrow"))
				.decoration(TextDecoration.ITALIC, false)
				.build()
		);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		List<Component> loreList = new ArrayList<>();
		loreList.add(Component.text("Level " + getSpawnAmount(), NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
		if (getSpawnLifetime() > 0) {
			loreList.add(Component.text("Lifetime " + decimalFormat.format(getSpawnLifetime() / 20D) + "s", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
		}
		
		meta.lore(loreList);
		
		item.setItemMeta(meta);
		return item;
	}
	
	public static SpawnArrow fromItemStack(SpawnArrows plugin, ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			PersistentDataContainer persistentContainer = meta.getPersistentDataContainer();
			SpawnArrow arrow = plugin.getSpawnArrow(plugin.getSpawnType(persistentContainer));
			
			if (arrow != null) {
				return new SpawnArrow(plugin, arrow.getEntityType(), arrow.getColor(),
					plugin.getSpawnAmount(persistentContainer), plugin.getSpawnLifetime(persistentContainer),
					arrow.getRecipe());
			}
		}
		return null;
	}
	
	public boolean isCombinable(ItemStack item) {
		try {
			PersistentDataContainer persistentData = item.getItemMeta().getPersistentDataContainer();
			EntityType type = plugin.getSpawnType(persistentData);
			int amount = plugin.getSpawnAmount(persistentData);
			return getEntityType().equals(type) && getSpawnAmount() == amount;
		} catch (Exception e) {
			return false;
		}
	}
	
}
