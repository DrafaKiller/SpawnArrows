package com.drafakiller.spawnarrows.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;

public abstract class LocationUtil {
	
	public static Location setAvailableLocationX(Location location, BoundingBox boundingBox) {
		double width = Math.max(boundingBox.getWidthX(), boundingBox.getWidthZ());
		double halfWidth = width / 2;
		Block negativeBlock = null, positiveBlock = null;
		for (double i = 0; i < halfWidth + 1; i++) {
			if (i > halfWidth) i = halfWidth;
			
			if (negativeBlock == null) {
				Block block = location.clone().add(-i, 0, 0).getBlock();
				if (block.isSolid()) {
					negativeBlock = block;
				}
			}
			if (positiveBlock == null) {
				Block block = location.clone().add(i, 0, 0).getBlock();
				if (block.isSolid()) {
					positiveBlock = block;
				}
			}
			if (negativeBlock != null && positiveBlock != null) {
				break;
			}
		}
		
		if (negativeBlock == null && positiveBlock == null) {
			return location;
		} else if (negativeBlock != null && positiveBlock != null) {
			location.setX((float) (negativeBlock.getX() + positiveBlock.getX()) / 2);
		} else if (negativeBlock != null) {
			location.setX(negativeBlock.getX() + 1 + halfWidth);
		} else if (positiveBlock != null) {
			location.setX(positiveBlock.getX() - halfWidth);
		}
		
		return location;
	}
	
	public static Location setAvailableLocationY(Location location, BoundingBox boundingBox) {
		if (!location.clone().add(0, -1, 0).getBlock().isSolid()) {
			double height = boundingBox.getHeight();
			for (double i = 0; i < height + 1; i++) {
				if (i > height) i = height;
				Block block = location.clone().add(0, i, 0).getBlock();
				if (block.isSolid()) {
					Location newLocation = block.getLocation().clone().add(0, -height, 0);
					if (!newLocation.getBlock().isSolid()) {
						location.setY(block.getY() - height);
					}
					break;
				}
			}
		}
		return location;
	}
	public static Location setAvailableLocationZ(Location location, BoundingBox boundingBox) {
		double width = Math.max(boundingBox.getWidthX(), boundingBox.getWidthZ());
		double halfWidth = width / 2;
		Block negativeBlock = null, positiveBlock = null;
		for (double i = 0; i < halfWidth + 1; i++) {
			if (i > halfWidth) i = halfWidth;
			
			if (negativeBlock == null) {
				Block block = location.clone().add(0, 0, -i).getBlock();
				if (block.isSolid()) {
					negativeBlock = block;
				}
			}
			if (positiveBlock == null) {
				Block block = location.clone().add(0, 0, i).getBlock();
				if (block.isSolid()) {
					positiveBlock = block;
				}
			}
			if (negativeBlock != null && positiveBlock != null) {
				break;
			}
		}
		
		if (negativeBlock == null && positiveBlock == null) {
			return location;
		} else if (negativeBlock != null && positiveBlock != null) {
			location.setZ((float) (negativeBlock.getZ() + positiveBlock.getZ()) / 2);
		} else if (negativeBlock != null) {
			location.setZ(negativeBlock.getZ() + 1 + halfWidth);
		} else if (positiveBlock != null) {
			location.setZ(positiveBlock.getZ() - halfWidth);
		}
		
		return location;
	}
	
	public static Location setAvailableLocation(Location location, BoundingBox boundingBox) {
		if (!location.getBlock().isSolid()) {
			setAvailableLocationX(location, boundingBox);
			setAvailableLocationZ(location, boundingBox);
			setAvailableLocationY(location, boundingBox);
		}
		return location;
	}
	
}
