package de.polarwolf.heliumballoon.config;

import org.bukkit.Axis;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bell;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Villager;

import de.polarwolf.heliumballoon.exception.BalloonException;

public class ConfigUtils {

	private ConfigUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	
	public static Material getAnyMaterialFromName(String contextName, String materialName) throws BalloonException {
		if ((materialName == null) || (materialName.isEmpty())) {
			throw new BalloonException(contextName, "Material is missing", null);
		}

		Material myMaterial = Material.matchMaterial(materialName, false);
		if (myMaterial == null) {
			throw new BalloonException(contextName, "Unknown material", materialName);
		}
		if (myMaterial.isAir()) {
			throw new BalloonException(contextName, "Material cannot be AIR", materialName);
		}
		return myMaterial;
	}

		
	public static Material getBlockMaterialFromName(String contextName, String materialName) throws BalloonException {
		Material myMaterial = getAnyMaterialFromName(contextName, materialName);
		if (!myMaterial.isBlock()) {
			throw new BalloonException(contextName, "Material is not a block", materialName);
		}
		return myMaterial;
	}

		
	public static Axis getAxisFromName(String contextName, String axisName) throws BalloonException {
		if ((axisName == null) || (axisName.isEmpty())) {
			return null;
		}

		Axis  myAxis = null;
		try {
			myAxis = Axis.valueOf(axisName);
		} catch (Exception e) {
			myAxis = null;
		}
		if (myAxis == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown axis", axisName);
		}
				
		return myAxis;
	}

	
	public static Bisected.Half getHalfFromName(String contextName, String halfName) throws BalloonException {
		if ((halfName == null) || (halfName.isEmpty())) {
			return null;
		}

		Bisected.Half myHalf = null;
		try {
			myHalf = Bisected.Half.valueOf(halfName);
		} catch (Exception e) {
			myHalf = null;
		}
		if (myHalf == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown half", halfName);
		}
				
		return myHalf;
	}

	
	public static Bell.Attachment getBellAttachmentFromName(String contextName, String bellAttachmentName) throws BalloonException {
		if ((bellAttachmentName == null) || (bellAttachmentName.isEmpty())) {
			return null;
		}

		Bell.Attachment myBellAttachment = null;
		try {
			myBellAttachment = Bell.Attachment.valueOf(bellAttachmentName);
		} catch (Exception e) {
			myBellAttachment = null;
		}
		if (myBellAttachment == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown bell attachment", bellAttachmentName);
		}
				
		return myBellAttachment;
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
	

	public static Chest.Type getChestTypeFromName(String contextName, String chestTypeName) throws BalloonException {
		if ((chestTypeName == null) || (chestTypeName.isEmpty())) {
			return null;
		}

		Chest.Type myChestType = null;
		try {
			myChestType = Chest.Type.valueOf(chestTypeName);
		} catch (Exception e) {
			myChestType = null;
		}
		if (myChestType == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown chest type", chestTypeName);
		}
				
		return myChestType;
	}

	
	public static Door.Hinge getDoorHingeFromName(String contextName, String doorHingeName) throws BalloonException {
		if ((doorHingeName == null) || (doorHingeName.isEmpty())) {
			return null;
		}

		Door.Hinge myDoorHinge = null;
		try {
			myDoorHinge = Door.Hinge.valueOf(doorHingeName);
		} catch (Exception e) {
			myDoorHinge = null;
		}
		if (myDoorHinge == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown door hinge", doorHingeName);
		}
				
		return myDoorHinge;
	}

	
	public static FaceAttachable.AttachedFace getAttachedFaceFromName(String contextName, String attachedFaceName) throws BalloonException {
		if ((attachedFaceName == null) || (attachedFaceName.isEmpty())) {
			return null;
		}

		FaceAttachable.AttachedFace myAttachedFace = null;
		try {
			myAttachedFace = FaceAttachable.AttachedFace.valueOf(attachedFaceName);
		} catch (Exception e) {
			myAttachedFace = null;
		}
		if (myAttachedFace == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown attached face", attachedFaceName);
		}
				
		return myAttachedFace;
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

	
	public static Stairs.Shape getStairsShapeFromName(String contextName, String stairsShapeName) throws BalloonException {
		if ((stairsShapeName == null) || (stairsShapeName.isEmpty())) {
			return null;
		}

		Stairs.Shape myShape = null;
		try {
			myShape = Stairs.Shape.valueOf(stairsShapeName);
		} catch (Exception e) {
			myShape = null;
		}
		if (myShape == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown stairs shape", stairsShapeName);
		}
				
		return myShape;
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
			throw new BalloonException(contextName, "Unknown dye or collar color", colorName);
		}
				
		return myColor;
	}

	
	public static Horse.Color getHorseColorFromName(String contextName, String colorName) throws BalloonException {
		if ((colorName == null) || (colorName.isEmpty())) {
			return null;
		}

		Horse.Color myColor = null;
		try {
			myColor = Horse.Color.valueOf(colorName);
		} catch (Exception e) {
			myColor = null;
		}
		if (myColor == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown horse color", colorName);
		}
				
		return myColor;
	}

	
	public static Llama.Color getLlamaColorFromName(String contextName, String colorName) throws BalloonException {
		if ((colorName == null) || (colorName.isEmpty())) {
			return null;
		}

		Llama.Color myColor = null;
		try {
			myColor = Llama.Color.valueOf(colorName);
		} catch (Exception e) {
			myColor = null;
		}
		if (myColor == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown llama color", colorName);
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


	public static Fox.Type getFoxTypeFromName(String contextName, String foxTypeName) throws BalloonException {
		if ((foxTypeName == null) || (foxTypeName.isEmpty())) {
			return null;
		}

		Fox.Type myFoxType = null;
		try {
			myFoxType = Fox.Type.valueOf(foxTypeName);
		} catch (Exception e) {
			myFoxType = null;
		}
		if (myFoxType == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown fox type", foxTypeName);
		}
				
		return myFoxType;
	}

	
	public static Horse.Style getHorseStyleFromName(String contextName, String horseStyleName) throws BalloonException {
		if ((horseStyleName == null) || (horseStyleName.isEmpty())) {
			return null;
		}

		Horse.Style myHorseStyle = null;
		try {
			myHorseStyle = Horse.Style.valueOf(horseStyleName);
		} catch (Exception e) {
			myHorseStyle = null;
		}
		if (myHorseStyle == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown horse style", horseStyleName);
		}
				
		return myHorseStyle;
	}

	
	public static MushroomCow.Variant getMushroomCowVariantFromName(String contextName, String mushroomCowVariantName) throws BalloonException {
		if ((mushroomCowVariantName == null) || (mushroomCowVariantName.isEmpty())) {
			return null;
		}

		MushroomCow.Variant myMushroomCowVariant = null;
		try {
			myMushroomCowVariant = MushroomCow.Variant.valueOf(mushroomCowVariantName);
		} catch (Exception e) {
			myMushroomCowVariant = null;
		}
		if (myMushroomCowVariant == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown mushroom cow variant", mushroomCowVariantName);
		}
				
		return myMushroomCowVariant;
	}


	public static Panda.Gene getPandaGeneFromName(String contextName, String pandaGeneName) throws BalloonException {
		if ((pandaGeneName == null) || (pandaGeneName.isEmpty())) {
			return null;
		}

		Panda.Gene myPandaGene = null;
		try {
			myPandaGene = Panda.Gene.valueOf(pandaGeneName);
		} catch (Exception e) {
			myPandaGene = null;
		}
		if (myPandaGene == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown panda gene", pandaGeneName);
		}
				
		return myPandaGene;
	}

	
	public static Parrot.Variant getParrotVariantFromName(String contextName, String parrotVariantName) throws BalloonException {
		if ((parrotVariantName == null) || (parrotVariantName.isEmpty())) {
			return null;
		}

		Parrot.Variant myParrotVariant = null;
		try {
			myParrotVariant = Parrot.Variant.valueOf(parrotVariantName);
		} catch (Exception e) {
			myParrotVariant = null;
		}
		if (myParrotVariant == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown parrot variant", parrotVariantName);
		}
				
		return myParrotVariant;
	}


	public static Rabbit.Type getRabbitTypeFromName(String contextName, String rabbitTypeName) throws BalloonException {
		if ((rabbitTypeName == null) || (rabbitTypeName.isEmpty())) {
			return null;
		}

		Rabbit.Type myRabbitType = null;
		try {
			myRabbitType = Rabbit.Type.valueOf(rabbitTypeName);
		} catch (Exception e) {
			myRabbitType = null;
		}
		if (myRabbitType == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown rabbit type", rabbitTypeName);
		}
				
		return myRabbitType;
	}

	
	public static TropicalFish.Pattern getTropicalFishPatternFromName(String contextName, String tropicalFishPatternName) throws BalloonException {
		if ((tropicalFishPatternName == null) || (tropicalFishPatternName.isEmpty())) {
			return null;
		}

		TropicalFish.Pattern myTropicalFishPattern = null;
		try {
			myTropicalFishPattern = TropicalFish.Pattern.valueOf(tropicalFishPatternName);
		} catch (Exception e) {
			myTropicalFishPattern = null;
		}
		if (myTropicalFishPattern == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown rabbit type", tropicalFishPatternName);
		}
				
		return myTropicalFishPattern;
	}


	public static Villager.Type getVillagerTypeFromName(String contextName, String villagerTypeName) throws BalloonException {
		if ((villagerTypeName == null) || (villagerTypeName.isEmpty())) {
			return null;
		}

		Villager.Type myVillagerType = null;
		try {
			myVillagerType = Villager.Type.valueOf(villagerTypeName);
		} catch (Exception e) {
			myVillagerType = null;
		}
		if (myVillagerType == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown villager type", villagerTypeName);
		}
				
		return myVillagerType;
	}

	
	public static Villager.Profession getVillagerProfessionFromName(String contextName, String villagerProfessionName) throws BalloonException {
		if ((villagerProfessionName == null) || (villagerProfessionName.isEmpty())) {
			return null;
		}

		Villager.Profession myVillagerProfession = null;
		try {
			myVillagerProfession = Villager.Profession.valueOf(villagerProfessionName);
		} catch (Exception e) {
			myVillagerProfession = null;
		}
		if (myVillagerProfession == null) { // exception or null return
			throw new BalloonException(contextName, "Unknown villager profession", villagerProfessionName);
		}
				
		return myVillagerProfession;
	}
}

