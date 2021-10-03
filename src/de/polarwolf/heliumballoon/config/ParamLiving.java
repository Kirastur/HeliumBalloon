package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;

public enum ParamLiving implements HeliumParam {

	TYPE ("type"),
	X ("x"),
	Y ("y"),
	Z ("z"),
	LEASH ("leash"),
	HIDDEN ("hidden"),
	TAMED ("tamed"),
	CATTYPE ("catType"),
	COLLARCOLOR ("collarColor"),
	FOXTYPE ("foxType"),
	HORSECOLOR ("horseColor"),
	HORSESTYLE ("horseStyle"),
	LLAMACOLOR ("llamaColor"),
	MUSHROOMCOWVARIANT ("mushroomCowVariant"),
	PANDAMAINGENE ("pandaMainGene"),
	PANDAHIDDENGENE ("pandaHiddenGene"),
	PARROTVARIANT ("parrotVariant"),
	RABBITTYPE ("rabbitType"),
	SHEEPCOLOR ("sheepColor"),
	TROPICALFISHBODYCOLOR ("tropicalFishBodyColor"),
	TROPICALFISHPATTERNCOLOR ("tropicalFishPatternColor"),
	TROPICALFISHPATTERN ("tropicalFishPattern"),
	VILLAGER_TYPE("villagerType"),
	VILLAGER_PROFESSION("villagerProfession"),
	VILLAGER_LEVEL("villagerLevel"),
	CUSTOM ("custom");
	
		
	private final String attributeName;
	

	private ParamLiving(String attributeName) {
		this.attributeName = attributeName;
	}
	
	
	@Override
	public boolean isSection() {
		return false;
	}


	@Override
	public String getAttributeName() {
		return attributeName;
	}
}