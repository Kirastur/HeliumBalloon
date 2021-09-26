package de.polarwolf.heliumballoon.config;

import de.polarwolf.heliumballoon.helium.HeliumParam;

public enum ParamElement implements HeliumParam {

	MATERIAL ("material"),
	AXIS ("axis"),
	BISECTED_HALF ("bisectedHalf"),
	BELL_ATTACHMENT("bellAttachment"),
	BLOCK_FACE("blockFace"),
	CHEST_TYPE("chestType"),
	DOOR_HINGE("doorHinge"),
	ATTACHED_FACE("attachedFace"),
	SLAB_TYPE("slabType"),
	STAIRS_SHAPE("stairsShape"),
	IS_OPEN("isOpen"),
	IS_LIT("isLit"),
	IS_SIGNAL_FIRE("isSignalFire"),
	IS_HANGING("isHanging"),
	HAS_EYE("hasEye"),
	CUSTOM ("custom"),
	X ("x"),
	Y ("y"),
	Z ("z");
		
	private final String attributeName;
	

	private ParamElement(String attributeName) {
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
