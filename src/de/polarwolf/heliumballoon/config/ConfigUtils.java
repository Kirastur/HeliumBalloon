package de.polarwolf.heliumballoon.config;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;

import de.polarwolf.heliumballoon.exception.BalloonException;

public class ConfigUtils {

	private ConfigUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	
	public static Material getMaterialFromName(String contextName, String materialName) throws BalloonException {
		if ((materialName == null) || (materialName.isEmpty())) {
			throw new BalloonException(contextName, "Material is missing", null);
		}

		Material myMaterial = Material.matchMaterial(materialName, false);
		if (myMaterial == null) {
			throw new BalloonException(contextName, "Unknown material", materialName);
		}
		if (!myMaterial.isBlock()) {
			throw new BalloonException(contextName, "Material is not a block", materialName);
		}
		if (myMaterial.isAir()) {
			throw new BalloonException(contextName, "Material cannot be AIR", materialName);
		}
		return myMaterial;
	}

		
	public static Bisected.Half getHalfFromName(String contextName, String halfName) throws BalloonException {
		if ((halfName == null) || (halfName.isEmpty())) {
			return null;
		}

		Bisected.Half myHalf = null;
		try {
			myHalf= Bisected.Half.valueOf(halfName);
		} catch (Exception e) {
			myHalf = null;
		}
		if (myHalf == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown half", halfName);
		}
				
		return myHalf;
	}

	
	public static Slab.Type getSlabTypeFromName(String contextName, String slabName) throws BalloonException {
		if ((slabName == null) || (slabName.isEmpty())) {
			return null;
		}

		Slab.Type mySlab = null;
		try {
			mySlab = Slab.Type.valueOf(slabName);
		} catch (Exception e) {
			mySlab = null;
		}
		if (mySlab == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown slab type", slabName);
		}
				
		return mySlab;
	}

	
	public static BlockFace getBlockFaceFromName(String contextName, String blockFaceName) throws BalloonException {
		if ((blockFaceName == null) || (blockFaceName.isEmpty())) {
			return null;
		}

		BlockFace myBlockFace = null;
		try {
			myBlockFace = BlockFace.valueOf(blockFaceName);
		} catch (Exception e) {
			myBlockFace = null;
		}
		if (myBlockFace == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown block face", blockFaceName);
		}
				
		return myBlockFace;
	}
	

	public static EntityType getLivingEntityTypeFromName(String contextName, String livingEntityName) throws BalloonException {
		if ((livingEntityName == null) || (livingEntityName.isEmpty())) {
			throw new BalloonException(contextName, "EntityType is missing", null);
		}

		EntityType myEntityType = null;
		try {
			myEntityType = EntityType.valueOf(livingEntityName);
		} catch (Exception e) {
			myEntityType = null;
		}
		if (myEntityType == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown entity", livingEntityName);
		}
		
		if (!myEntityType.isSpawnable() || !myEntityType.isAlive()) {
			throw new BalloonException(contextName, "Entity is not alive", livingEntityName);			
		}
		
		return myEntityType;
	}
	
	
	public static DyeColor getDyeColorFromName(String contextName, String colorName) throws BalloonException {
		if ((colorName == null) || (colorName.isEmpty())) {
			return null;
		}

		DyeColor myColor = null;
		try {
			myColor = DyeColor.valueOf(colorName);
		} catch (Exception e) {
			myColor = null;
		}
		if (myColor == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown color", colorName);
		}
				
		return myColor;
	}

	
	public static Cat.Type getCatTypeFromName(String contextName, String catTypeName) throws BalloonException {
		if ((catTypeName == null) || (catTypeName.isEmpty())) {
			return null;
		}

		Cat.Type myCatType = null;
		try {
			myCatType = Cat.Type.valueOf(catTypeName);
		} catch (Exception e) {
			myCatType = null;
		}
		if (myCatType == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown cat type", catTypeName);
		}
				
		return myCatType;
	}

}
