package de.polarwolf.heliumballoon.config;

import static de.polarwolf.heliumballoon.tools.helium.HeliumParamType.STRING;

import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumParamType;

public enum ParamElement implements HeliumParam {

	MATERIAL(STRING, "material"),
	AXIS(STRING, "axis"),
	BISECTED_HALF(STRING, "bisectedHalf"),
	BELL_ATTACHMENT(STRING, "bellAttachment"),
	BLOCK_FACE(STRING, "blockFace"),
	CHEST_TYPE(STRING, "chestType"),
	DOOR_HINGE(STRING, "doorHinge"),
	ATTACHED_FACE(STRING, "attachedFace"),
	SLAB_TYPE(STRING, "slabType"),
	STAIRS_SHAPE(STRING, "stairsShape"),
	IS_OPEN(STRING, "isOpen"),
	IS_LIT(STRING, "isLit"),
	IS_SIGNAL_FIRE(STRING, "isSignalFire"),
	IS_HANGING(STRING, "isHanging"),
	HAS_EYE(STRING, "hasEye"),
	CUSTOM(STRING, "custom"),
	X(STRING, "x"),
	Y(STRING, "y"),
	Z(STRING, "z");

	private final HeliumParamType paramType;
	private final String attributeName;

	private ParamElement(HeliumParamType paramType, String attributeName) {
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
