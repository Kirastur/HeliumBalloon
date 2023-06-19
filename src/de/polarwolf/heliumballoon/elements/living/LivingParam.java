package de.polarwolf.heliumballoon.elements.living;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum LivingParam implements HeliumParam {

	CUSTOM(STRING, "custom"),
	X(STRING, "x"),
	Y(STRING, "y"),
	Z(STRING, "z"),
	TYPE(STRING, "type"),
	LEASH(STRING, "leash"),
	HIDDEN(STRING, "hidden"),
	TAMED(STRING, "tamed"),
	CATTYPE(STRING, "catType"),
	COLLARCOLOR(STRING, "collarColor"),
	FOXTYPE(STRING, "foxType"),
	FROGVARIANT(STRING, "frogVariant"),
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
	VILLAGER_LEVEL(STRING, "villagerLevel");

	private final HeliumParamType paramType;
	private final String attributeName;

	private LivingParam(HeliumParamType paramType, String attributeName) {
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
