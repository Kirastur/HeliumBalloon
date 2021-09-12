package de.polarwolf.heliumballoon.config;

import java.util.Arrays;

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
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.TropicalFish;
import org.bukkit.util.Vector;

import de.polarwolf.heliumballoon.exception.BalloonException;
import de.polarwolf.heliumballoon.helium.HeliumSection;

public class ConfigAnimal {

	private final String name;
	private EntityType entityType = EntityType.CAT;
	private Vector offset = new Vector (0,0,0);
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
	

	private String custom = null;
	
	
	public ConfigAnimal(String name) {
		this.name = name;
	}


	public ConfigAnimal(ConfigurationSection fileSection) throws BalloonException {
		this.name = fileSection.getName();
		loadConfig(fileSection);
	}

	
	public String getName() {
		return name;
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


	public String getCustom() {
		return custom;
	}


	protected void setCustom(String custom) {
		this.custom = custom;
	}


	protected void loadConfig(ConfigurationSection fileSection) throws BalloonException {
		HeliumSection heliumSection = new HeliumSection(fileSection, Arrays.asList(ParamAnimal.values()));

		String livingEntityName = heliumSection.getString(ParamAnimal.TYPE);
		setEntityType(ConfigUtils.getLivingEntityTypeFromName(getName(), livingEntityName));
		
		setHidden(heliumSection.getBoolean(ParamAnimal.HIDDEN, isHidden()));
		setLeash(heliumSection.getBoolean(ParamAnimal.LEASH, hasLeash()));
		setTamed(heliumSection.getBoolean(ParamAnimal.TAMED, isTamed()));
		
		setCatType (ConfigUtils.getCatTypeFromName(getName(), heliumSection.getString(ParamAnimal.CATTYPE)));
		setCollarColor (ConfigUtils.getDyeColorFromName(getName(), heliumSection.getString(ParamAnimal.COLLARCOLOR)));
		setFoxType (ConfigUtils.getFoxTypeFromName(getName(), heliumSection.getString(ParamAnimal.FOXTYPE)));
		setHorseColor (ConfigUtils.getHorseColorFromName(getName(), heliumSection.getString(ParamAnimal.HORSECOLOR)));
		setHorseStyle (ConfigUtils.getHorseStyleFromName(getName(), heliumSection.getString(ParamAnimal.HORSESTYLE)));
		setLlamaColor (ConfigUtils.getLlamaColorFromName(getName(), heliumSection.getString(ParamAnimal.LLAMACOLOR)));
		setMushroomCowVariant (ConfigUtils.getMushroomCowVariantFromName(getName(), heliumSection.getString(ParamAnimal.MUSHROOMCOWVARIANT)));
		setPandaMainGene (ConfigUtils.getPandaGeneFromName(getName(), heliumSection.getString(ParamAnimal.PANDAMAINGENE)));
		setPandaHiddenGene (ConfigUtils.getPandaGeneFromName(getName(), heliumSection.getString(ParamAnimal.PANDAHIDDENGENE)));
		setParrotVariant (ConfigUtils.getParrotVariantFromName(getName(), heliumSection.getString(ParamAnimal.PARROTVARIANT)));
		setRabbitType (ConfigUtils.getRabbitTypeFromName(getName(), heliumSection.getString(ParamAnimal.RABBITTYPE)));
		setSheepColor  (ConfigUtils.getDyeColorFromName(getName(), heliumSection.getString(ParamAnimal.SHEEPCOLOR)));
		setTropicalFishBodyColor (ConfigUtils.getDyeColorFromName(getName(), heliumSection.getString(ParamAnimal.TROPICALFISHBODYCOLOR)));
		setTropicalFishPatternColor  (ConfigUtils.getDyeColorFromName(getName(), heliumSection.getString(ParamAnimal.TROPICALFISHPATTERNCOLOR)));
		setTropicalFishPattern  (ConfigUtils.getTropicalFishPatternFromName(getName(), heliumSection.getString(ParamAnimal.TROPICALFISHPATTERN)));

		Double x = heliumSection.getDouble(ParamAnimal.X, getOffset().getX());
		Double y = heliumSection.getDouble(ParamAnimal.Y, getOffset().getY());
		Double z = heliumSection.getDouble(ParamAnimal.Z, getOffset().getZ());
		setOffset(new Vector(x, y, z));
		
		setCustom(heliumSection.getString(ParamAnimal.CUSTOM));
	}

}
