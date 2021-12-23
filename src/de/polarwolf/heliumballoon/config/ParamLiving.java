package de.polarwolf.heliumballoon.config;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum ParamLiving implements HeliumParam {

	TYPE(STRING, "type"),
	X(STRING, "x"),
	Y(STRING, "y"),
	Z(STRING, "z"),
	LEASH(STRING, "leash"),
	HIDDEN(STRING, "hidden"),
	TAMED(STRING, "tamed"),
	CATTYPE(STRING, "catType"),
	COLLARCOLOR(STRING, "collarColor"),
	FOXTYPE(STRING, "foxType"),
	HORSECOLOR(STRING, "horseColor"),
	HORSESTYLE(STRING, "horseStyle"),
	LLAMACOLOR(STRING, "llamaColor"),
	MUSHROOMCOWVARIANT(STRING, "mushroomCowVariant"),
	PANDAMAINGENE(STRING, "pandaMainGene"),
	PANDAHIDDENGENE(STRING, "pandaHiddenGene"),
	PARROTVARIANT(STRING, "parrotVariant"),
	RABBITTYPE(STRING, "rabbitType"),
	SHEEPCOLOR(STRING, "sheepColor"),
	TROPICALFISHBODYCOLOR(STRING, "tropicalFishBodyColor"),
	TROPICALFISHPATTERNCOLOR(STRING, "tropicalFishPatternColor"),
	TROPICALFISHPATTERN(STRING, "tropicalFishPattern"),
	VILLAGER_TYPE(STRING, "villagerType"),
	VILLAGER_PROFESSION(STRING, "villagerProfession"),
	VILLAGER_LEVEL(STRING, "villagerLevel"),
	CUSTOM(STRING, "custom");

	private final HeliumParamType paramType;
	private final String attributeName;

	private ParamLiving(HeliumParamType paramType, String attributeName) {
		this.paramType = paramType;
		this.attributeName = attributeName;
	}

	@Override
	public boolean isType(HeliumParamType testParamType) {
		return testParamType == paramType;
	}

	@Override
	public String getAttributeName() {
		return attributeName;
	}
}
