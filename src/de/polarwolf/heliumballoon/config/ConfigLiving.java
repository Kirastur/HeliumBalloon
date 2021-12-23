package de.polarwolf.heliumballoon.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Villager;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.balloons.BalloonPurpose;
import de.polarwolf.heliumballoon.elements.Element;
import de.polarwolf.heliumballoon.elements.LivingElement;
import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.spawnmodifiers.SpawnModifier;
import de.polarwolf.heliumballoon.tools.helium.HeliumParam;
import de.polarwolf.heliumballoon.tools.helium.HeliumSection;

public class ConfigLiving implements ConfigPart {

	private final String name;
	private final String fullName;
	private EntityType entityType = EntityType.CAT;
	private Vector offset = new Vector(0, 0, 0);
	private boolean hidden = false;
	private boolean leash = false;
	private boolean tamed = false;
	private Cat.Type catType = null;
	private DyeColor collarColor = null;
	private Fox.Type foxType = null;
	private Horse.Color horseColor = null;
	private Horse.Style horseStyle = null;
	private Llama.Color llamaColor = null;
	private MushroomCow.Variant mushroomCowVariant = null;
	private Panda.Gene pandaMainGene = null;
	private Panda.Gene pandaHiddenGene = null;
	private Parrot.Variant parrotVariant = null;
	private Rabbit.Type rabbitType = null;
	private DyeColor sheepColor = null;
	private DyeColor tropicalFishBodyColor = null;
	private DyeColor tropicalFishPatternColor = null;
	private TropicalFish.Pattern tropicalFishPattern = null;
	private Villager.Type villagerType = null;
	private Villager.Profession villagerProfession = null;
	private int villagerLevel = 1;

	private String custom = null;

	public ConfigLiving(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}

	public ConfigLiving(ConfigurationSection fileSection) throws BalloonException {
		this.name = fileSection.getName();
		this.fullName = fileSection.getCurrentPath();
		loadConfigFromFile(fileSection);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getFullName() {
		return fullName;
	}

	@Override
	public boolean isSuitableFor(BalloonPurpose purpose) {
		switch (purpose) {
		case PET:
			return true;
		case WALL:
			return false;
		case ROTATOR:
			return false;
		default:
			return false;
		}
	}

	@Override
	public Element createElement(Player player, ConfigRule rule, SpawnModifier spawnModifier) {
		return new LivingElement(player, rule, this, spawnModifier);
	}

	@Override
	public double getMinYOffset() {
		return offset.getY();
	}

	@Override
	public double getMaxYOffset() {
		return offset.getY();
	}

	public EntityType getEntityType() {
		return entityType;
	}

	protected void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public Vector getOffset() {
		return offset;
	}

	protected void setOffset(Vector offset) {
		this.offset = offset;
	}

	public boolean isHidden() {
		return hidden;
	}

	protected void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean hasLeash() {
		return leash;
	}

	protected void setLeash(boolean leash) {
		this.leash = leash;
	}

	public boolean isTamed() {
		return tamed;
	}

	protected void setTamed(boolean tamed) {
		this.tamed = tamed;
	}

	public Cat.Type getCatType() {
		return catType;
	}

	protected void setCatType(Cat.Type catType) {
		this.catType = catType;
	}

	public DyeColor getCollarColor() {
		return collarColor;
	}

	protected void setCollarColor(DyeColor collarColor) {
		this.collarColor = collarColor;
	}

	public Fox.Type getFoxType() {
		return foxType;
	}

	protected void setFoxType(Fox.Type foxType) {
		this.foxType = foxType;
	}

	public Horse.Color getHorseColor() {
		return horseColor;
	}

	protected void setHorseColor(Horse.Color horseColor) {
		this.horseColor = horseColor;
	}

	public Horse.Style getHorseStyle() {
		return horseStyle;
	}

	protected void setHorseStyle(Horse.Style horseStyle) {
		this.horseStyle = horseStyle;
	}

	public Llama.Color getLlamaColor() {
		return llamaColor;
	}

	protected void setLlamaColor(Llama.Color llamaColor) {
		this.llamaColor = llamaColor;
	}

	public MushroomCow.Variant getMushroomCowVariant() {
		return mushroomCowVariant;
	}

	protected void setMushroomCowVariant(MushroomCow.Variant mushroomCowVariant) {
		this.mushroomCowVariant = mushroomCowVariant;
	}

	public Panda.Gene getPandaMainGene() {
		return pandaMainGene;
	}

	protected void setPandaMainGene(Panda.Gene pandaMainGene) {
		this.pandaMainGene = pandaMainGene;
	}

	public Panda.Gene getPandaHiddenGene() {
		return pandaHiddenGene;
	}

	protected void setPandaHiddenGene(Panda.Gene pandaHiddenGene) {
		this.pandaHiddenGene = pandaHiddenGene;
	}

	public Parrot.Variant getParrotVariant() {
		return parrotVariant;
	}

	protected void setParrotVariant(Parrot.Variant parrotVariant) {
		this.parrotVariant = parrotVariant;
	}

	public Rabbit.Type getRabbitType() {
		return rabbitType;
	}

	protected void setRabbitType(Rabbit.Type rabbitType) {
		this.rabbitType = rabbitType;
	}

	public DyeColor getSheepColor() {
		return sheepColor;
	}

	protected void setSheepColor(DyeColor sheepColor) {
		this.sheepColor = sheepColor;
	}

	public DyeColor getTropicalFishBodyColor() {
		return tropicalFishBodyColor;
	}

	protected void setTropicalFishBodyColor(DyeColor tropicalFishBodyColor) {
		this.tropicalFishBodyColor = tropicalFishBodyColor;
	}

	public DyeColor getTropicalFishPatternColor() {
		return tropicalFishPatternColor;
	}

	protected void setTropicalFishPatternColor(DyeColor tropicalFishPatternColor) {
		this.tropicalFishPatternColor = tropicalFishPatternColor;
	}

	public TropicalFish.Pattern getTropicalFishPattern() {
		return tropicalFishPattern;
	}

	protected void setTropicalFishPattern(TropicalFish.Pattern tropicalFishPattern) {
		this.tropicalFishPattern = tropicalFishPattern;
	}

	public Villager.Type getVillagerType() {
		return villagerType;
	}

	protected void setVillagerType(Villager.Type villagerType) {
		this.villagerType = villagerType;
	}

	public Villager.Profession getVillagerProfession() {
		return villagerProfession;
	}

	protected void setVillagerProfession(Villager.Profession villagerProfession) {
		this.villagerProfession = villagerProfession;
	}

	public int getVillagerLevel() {
		return villagerLevel;
	}

	protected void setVillagerLevel(int villagerLevel) {
		this.villagerLevel = villagerLevel;
	}

	public String getCustom() {
		return custom;
	}

	protected void setCustom(String custom) {
		this.custom = custom;
	}

	protected List<HeliumParam> getValidParams() {
		return Arrays.asList(ParamLiving.values());
	}

	protected void importHeliumSection(HeliumSection heliumSection) throws BalloonException {
		setEntityType(
				ConfigUtils.getLivingEntityTypeFromName(getFullName(), heliumSection.getString(ParamLiving.TYPE)));

		setHidden(heliumSection.getBoolean(ParamLiving.HIDDEN, isHidden()));
		setLeash(heliumSection.getBoolean(ParamLiving.LEASH, hasLeash()));
		setTamed(heliumSection.getBoolean(ParamLiving.TAMED, isTamed()));

		setCatType(ConfigUtils.getCatTypeFromName(getFullName(), heliumSection.getString(ParamLiving.CATTYPE)));
		setCollarColor(
				ConfigUtils.getDyeColorFromName(getFullName(), heliumSection.getString(ParamLiving.COLLARCOLOR)));
		setFoxType(ConfigUtils.getFoxTypeFromName(getFullName(), heliumSection.getString(ParamLiving.FOXTYPE)));
		setHorseColor(
				ConfigUtils.getHorseColorFromName(getFullName(), heliumSection.getString(ParamLiving.HORSECOLOR)));
		setHorseStyle(
				ConfigUtils.getHorseStyleFromName(getFullName(), heliumSection.getString(ParamLiving.HORSESTYLE)));
		setLlamaColor(
				ConfigUtils.getLlamaColorFromName(getFullName(), heliumSection.getString(ParamLiving.LLAMACOLOR)));
		setMushroomCowVariant(ConfigUtils.getMushroomCowVariantFromName(getFullName(),
				heliumSection.getString(ParamLiving.MUSHROOMCOWVARIANT)));
		setPandaMainGene(
				ConfigUtils.getPandaGeneFromName(getFullName(), heliumSection.getString(ParamLiving.PANDAMAINGENE)));
		setPandaHiddenGene(
				ConfigUtils.getPandaGeneFromName(getFullName(), heliumSection.getString(ParamLiving.PANDAHIDDENGENE)));
		setParrotVariant(ConfigUtils.getParrotVariantFromName(getFullName(),
				heliumSection.getString(ParamLiving.PARROTVARIANT)));
		setRabbitType(
				ConfigUtils.getRabbitTypeFromName(getFullName(), heliumSection.getString(ParamLiving.RABBITTYPE)));
		setSheepColor(ConfigUtils.getDyeColorFromName(getFullName(), heliumSection.getString(ParamLiving.SHEEPCOLOR)));
		setTropicalFishBodyColor(ConfigUtils.getDyeColorFromName(getFullName(),
				heliumSection.getString(ParamLiving.TROPICALFISHBODYCOLOR)));
		setTropicalFishPatternColor(ConfigUtils.getDyeColorFromName(getFullName(),
				heliumSection.getString(ParamLiving.TROPICALFISHPATTERNCOLOR)));
		setTropicalFishPattern(ConfigUtils.getTropicalFishPatternFromName(getFullName(),
				heliumSection.getString(ParamLiving.TROPICALFISHPATTERN)));
		setVillagerType(
				ConfigUtils.getVillagerTypeFromName(getFullName(), heliumSection.getString(ParamLiving.VILLAGER_TYPE)));
		setVillagerProfession(ConfigUtils.getVillagerProfessionFromName(getFullName(),
				heliumSection.getString(ParamLiving.VILLAGER_PROFESSION)));
		setVillagerLevel(heliumSection.getInt(ParamLiving.VILLAGER_LEVEL, getVillagerLevel()));

		Double x = heliumSection.getDouble(ParamLiving.X, getOffset().getX());
		Double y = heliumSection.getDouble(ParamLiving.Y, getOffset().getY());
		Double z = heliumSection.getDouble(ParamLiving.Z, getOffset().getZ());
		setOffset(new Vector(x, y, z));

		setCustom(heliumSection.getString(ParamLiving.CUSTOM));
	}

	protected void validateConfig() throws BalloonException {
		if ((getVillagerLevel() < 1) || (getVillagerLevel() > 5)) {
			throw new BalloonException(getFullName(), "Villager level must be between 1 and 5",
					Integer.toString(getVillagerLevel()));
		}
		if (getEntityType().equals(EntityType.VILLAGER) && hasLeash()) {
			throw new BalloonException(getFullName(), "You can't leash a Villager", null);
		}
	}

	protected void loadConfigFromFile(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, getValidParams());
		importHeliumSection(heliumSection);
		validateConfig();
	}

	protected List<String> paramListAsDump() { // NOSONAR
		String fs = "%s: \"%s\"";
		String fb = "%s: %s";
		List<String> newParamListDump = new ArrayList<>();
		newParamListDump.add(String.format(fs, ParamLiving.TYPE.getAttributeName(), entityType.toString()));
		if (hidden)
			newParamListDump.add(String.format(fb, ParamLiving.HIDDEN.getAttributeName(), Boolean.toString(true)));
		if (leash)
			newParamListDump.add(String.format(fb, ParamLiving.LEASH.getAttributeName(), Boolean.toString(true)));
		if (tamed)
			newParamListDump.add(String.format(fb, ParamLiving.TAMED.getAttributeName(), Boolean.toString(true)));
		if (catType != null)
			newParamListDump.add(String.format(fs, ParamLiving.CATTYPE.getAttributeName(), catType.toString()));
		if (collarColor != null)
			newParamListDump.add(String.format(fs, ParamLiving.COLLARCOLOR.getAttributeName(), collarColor.toString()));
		if (foxType != null)
			newParamListDump.add(String.format(fs, ParamLiving.FOXTYPE.getAttributeName(), foxType.toString()));
		if (horseColor != null)
			newParamListDump.add(String.format(fs, ParamLiving.HORSECOLOR.getAttributeName(), horseColor.toString()));
		if (horseStyle != null)
			newParamListDump.add(String.format(fs, ParamLiving.HORSESTYLE.getAttributeName(), horseStyle.toString()));
		if (llamaColor != null)
			newParamListDump.add(String.format(fs, ParamLiving.LLAMACOLOR.getAttributeName(), llamaColor.toString()));
		if (mushroomCowVariant != null)
			newParamListDump.add(String.format(fs, ParamLiving.MUSHROOMCOWVARIANT.getAttributeName(),
					mushroomCowVariant.toString()));
		if (pandaMainGene != null)
			newParamListDump
					.add(String.format(fs, ParamLiving.PANDAMAINGENE.getAttributeName(), pandaMainGene.toString()));
		if (pandaHiddenGene != null)
			newParamListDump
					.add(String.format(fs, ParamLiving.PANDAHIDDENGENE.getAttributeName(), pandaHiddenGene.toString()));
		if (parrotVariant != null)
			newParamListDump
					.add(String.format(fs, ParamLiving.PARROTVARIANT.getAttributeName(), parrotVariant.toString()));
		if (rabbitType != null)
			newParamListDump.add(String.format(fs, ParamLiving.RABBITTYPE.getAttributeName(), rabbitType.toString()));
		if (sheepColor != null)
			newParamListDump.add(String.format(fs, ParamLiving.SHEEPCOLOR.getAttributeName(), sheepColor.toString()));
		if (tropicalFishBodyColor != null)
			newParamListDump.add(String.format(fs, ParamLiving.TROPICALFISHBODYCOLOR.getAttributeName(),
					tropicalFishBodyColor.toString()));
		if (tropicalFishPatternColor != null)
			newParamListDump.add(String.format(fs, ParamLiving.TROPICALFISHPATTERNCOLOR.getAttributeName(),
					tropicalFishPatternColor.toString()));
		if (tropicalFishPattern != null)
			newParamListDump.add(String.format(fs, ParamLiving.TROPICALFISHPATTERN.getAttributeName(),
					tropicalFishPattern.toString()));
		if (villagerType != null)
			newParamListDump
					.add(String.format(fs, ParamLiving.VILLAGER_TYPE.getAttributeName(), villagerType.toString()));
		if (villagerProfession != null)
			newParamListDump.add(String.format(fs, ParamLiving.VILLAGER_PROFESSION.getAttributeName(),
					villagerProfession.toString()));
		if (villagerLevel != 1)
			newParamListDump.add(
					String.format(fb, ParamLiving.VILLAGER_LEVEL.getAttributeName(), Integer.toString(villagerLevel)));
		if (offset.getX() != 0)
			newParamListDump.add(String.format(fb, ParamLiving.X.getAttributeName(), Double.toString(offset.getX())));
		if (offset.getY() != 0)
			newParamListDump.add(String.format(fb, ParamLiving.Y.getAttributeName(), Double.toString(offset.getY())));
		if (offset.getZ() != 0)
			newParamListDump.add(String.format(fb, ParamLiving.Z.getAttributeName(), Double.toString(offset.getZ())));
		if ((custom != null) && !custom.isEmpty())
			newParamListDump.add(String.format(fs, ParamLiving.CUSTOM.getAttributeName(), custom));
		return newParamListDump;
	}

	@Override
	public String toString() {
		return String.format("%s: { %s }", getName(), String.join(", ", paramListAsDump()));
	}

}
