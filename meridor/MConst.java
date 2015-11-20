package meridor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class MConst {
	final static boolean WEAPON=true;
	//considering converting to enums, but not yet
	public enum TT {
		BLANK,VILLAGE,MOUNTAIN,CRATER,
		MOEHOG,SKEITH,TECHO,SCORCH,GRUNDO,
		D_MOE,D_SKE,D_TEC,D_SCO,D_GRU,D_BUZ,D_GRA
	};
	private final static int noeffect=-1;
	private final static int rangedatk=0;
	private final static int forceheal=1;
	private final static int lightning=2;
	private final static int breakheal=3;
	private final static int breaktele=4;
	private final static boolean freetele=true;
	private final static boolean freeatk=true;
	
	final static int MOEHOG=0;
	final static int SKEITH=1;
	final static int TECHO=2;
	final static int SCORCH=3;
	final static int GRUNDO=4;
	final static int D_MOE=5;
	final static int D_SKE=6;
	final static int D_TEC=7;
	final static int D_SCO=8;
	final static int D_GRU=9;
	final static int D_BUZ=10;
	final static int D_GRA=11;
	final static int BLANK=12;
	final static int VILLAGE=13;
	final static int MOUNTAIN=14;
	final static int CRATER=15;
	final static int P_HEAL=16;
	final static int P_FORT=17;
	final static int P_MEGA=18;
	final static int P_WELL=19;
	final static int GOBLET=20;
	final static int GOLDIX=21;
	final static int URNABU=22;
	final static int ANCBOO=23;
	final static int CROWN_=24;
	final static int ROYPLA=25;
	final static int ROYTAP=26;
	final static int TRECHE=27;
	final static int VASPLE=28;
	final static int VICORB=29;
	
	final static int WMACE=30;
	final static int WBSWO=31;
	final static int WHAMM=32;
	final static int WBBAX=33;
	final static int WBOW_=34;
	final static int SFORC=35;
	final static int WDSWO=36;
	final static int WHALB=37;
	final static int WDAXE=38;
	final static int SLIGH=39;
	
	final static int DTHUN=40;
	final static int DTELE=41;
	final static int DHELM=42;
	final static int DINVI=43;
	final static int DSHIE=44;
	final static int DLEAT=45;
	final static int DCHAI=46;
	final static int DPLAT=47;
	final static int WDEFL=48;
	final static int DCOUN=49;
	static Random random=new Random();
	public static Map<Integer,BufferedImage>tileGraphicMap=null;
	public static Map<Integer, ImageIcon>imageIconMap=null;
	public static Map<Integer, Equip>equipMap=null;
	
	private class Coord{
		int x,y;
		
		private Coord(int xx,int yy){
			x=xx;
			y=yy;
		}
	}
	public static void initItems(){
		int [] emptyset=new int[]{};

		if (equipMap==null){
			equipMap=new HashMap<Integer,Equip>();
			equipMap.put(WMACE, new Equip("Mace",WMACE,WEAPON,1,0,0));
			equipMap.put(WBSWO, new Equip("Broad Sword",WBSWO,
					WEAPON,!freeatk,!freetele,
					1,0,3,0,0,0,noeffect,
					new int[]{TECHO},emptyset));
			equipMap.put(WHAMM, new Equip("Hammer",WHAMM,WEAPON,2,0,0));
			equipMap.put(WBBAX, new Equip("Berserker Battleaxe",WBBAX,
					WEAPON,!freeatk,freetele,
					3,0,3,0,0,0,noeffect,
					emptyset,emptyset));
			equipMap.put(WBOW_, new Equip("Bow",WBOW_,
					WEAPON,!freeatk,!freetele,
					2,0,4,0,0,0,rangedatk,
					new int[]{SCORCH},emptyset));
			
			equipMap.put(SFORC, new Equip("Magic Force Spell",SFORC,
					WEAPON,!freeatk,!freetele,
					3,0,5,0,0,0,forceheal,
					new int[]{GRUNDO},emptyset));
			equipMap.put(WDSWO, new Equip("Double Sword",WDSWO,
					WEAPON,!freeatk,!freetele,
					4,0,6,0,6,0,noeffect,
					new int[]{TECHO},emptyset));
			equipMap.put(WHALB, new Equip("Halberd",WHALB,
					WEAPON,freeatk,!freetele,
					5,0,5,0,0,0,noeffect,
					emptyset,emptyset));
			equipMap.put(WDAXE, new Equip("Double Axe",WDAXE,
					WEAPON,!freeatk,!freetele,
					5,0,5,0,5,0,noeffect,
					emptyset,emptyset));
			equipMap.put(SLIGH, new Equip("Magic Lightning Spell",SLIGH,
					WEAPON,!freeatk,!freetele,
					2,0,4,0,0,0,lightning,
					new int[]{GRUNDO},emptyset));
			
			equipMap.put(DTHUN, new Equip("Magic Staff of Thunder",DTHUN,
					!WEAPON,!freeatk,!freetele,
					0,1,0,3,0,0,noeffect,
					new int[]{GRUNDO},emptyset));
			equipMap.put(DTELE, new Equip("Amulet of Teleportation",DTELE,
					!WEAPON,!freeatk,!freetele,
					0,2,0,4,0,1,noeffect,
					new int[]{SKEITH},emptyset));
			equipMap.put(DHELM, new Equip("Helmet",DHELM,
					!WEAPON,!freeatk,!freetele,
					0,3,0,5,0,0,noeffect,
					new int[]{MOEHOG},emptyset));
			equipMap.put(DINVI, new Equip("Cloak of Invisibility",DINVI,
					!WEAPON,!freeatk,!freetele,
					0,4,0,7,0,0,noeffect,
					new int[]{GRUNDO},emptyset));
			equipMap.put(DSHIE, new Equip("Shield",DSHIE,
					!WEAPON,!freeatk,!freetele,
					0,5,0,7,0,0,noeffect,
					new int[]{TECHO},emptyset));
			
			equipMap.put(DLEAT, new Equip("Leather Armor",DLEAT,
					!WEAPON,!freeatk,!freetele,
					0,5,0,5,0,0,noeffect,
					emptyset,new int[]{GRUNDO,SKEITH}));
			equipMap.put(DCHAI, new Equip("Chainmail",DCHAI,
					!WEAPON,!freeatk,!freetele,
					0,6,0,6,0,0,noeffect,
					emptyset,emptyset));
			equipMap.put(DPLAT, new Equip("Plate Armor",DPLAT,
					!WEAPON,!freeatk,!freetele,
					0,7,0,7,0,0,noeffect,
					emptyset,new int[]{GRUNDO,SKEITH,SCORCH,TECHO}));
			equipMap.put(WDEFL, new Equip("Sword of Deflection",WDEFL,
					WEAPON,!freeatk,!freetele,
					4,0,6,0,0,0,breaktele,
					new int[]{TECHO},emptyset));
			equipMap.put(DCOUN, new Equip("Counter Enchantment Helmet",DCOUN,
					!WEAPON,!freeatk,!freetele,
					0,3,0,5,0,0,breakheal,
					new int[]{MOEHOG},emptyset));
		}
	}
	public static void loadImages(){
		if (tileGraphicMap ==null){
			tileGraphicMap=new HashMap<Integer,BufferedImage>();
			try {				
				//terrain
				tileGraphicMap.put(VILLAGE, ImageIO.read(new File("vlg.jpg")));
				tileGraphicMap.put(MOUNTAIN, ImageIO.read(new File("mtn.jpg")));
				tileGraphicMap.put(CRATER, ImageIO.read(new File("Smoking_Crater.jpg")));
				//ally
				tileGraphicMap.put(MOEHOG, ImageIO.read(new File("Moeh00.jpg")));
				tileGraphicMap.put(SKEITH, ImageIO.read(new File("Skei01.jpg")));
				tileGraphicMap.put(TECHO, ImageIO.read(new File("Tech02.jpg")));
				tileGraphicMap.put(SCORCH, ImageIO.read(new File("Scor03.jpg")));
				tileGraphicMap.put(GRUNDO, ImageIO.read(new File("Grun04.jpg")));
				//foe
				tileGraphicMap.put(D_MOE, ImageIO.read(new File("Draco_Moehog00.jpg")));
				tileGraphicMap.put(D_SKE, ImageIO.read(new File("Draco_Skeith00.jpg")));
				tileGraphicMap.put(D_TEC, ImageIO.read(new File("Draco_Techo00.jpg")));
				tileGraphicMap.put(D_SCO, ImageIO.read(new File("Draco_Scorchio00.jpg")));
				tileGraphicMap.put(D_GRU, ImageIO.read(new File("Draco_Grundo00.jpg")));
				tileGraphicMap.put(D_BUZ, ImageIO.read(new File("Draco_Buzz00.jpg")));
				tileGraphicMap.put(D_GRA, ImageIO.read(new File("Draco_Grarrl00.jpg")));
				//potions
				tileGraphicMap.put(P_HEAL, ImageIO.read(new File("Health_Potion.jpg")));
				tileGraphicMap.put(P_FORT, ImageIO.read(new File("Potion_of_Fortitude.jpg")));
				tileGraphicMap.put(P_MEGA, ImageIO.read(new File("Mega_Potion.jpg")));
				tileGraphicMap.put(P_WELL, ImageIO.read(new File("Potion_of_Well-Being.jpg")));
				//treasure
				tileGraphicMap.put(GOBLET, ImageIO.read(new File("Goblet.jpg")));
				tileGraphicMap.put(GOLDIX, ImageIO.read(new File("Gold_Ixi.jpg")));
				tileGraphicMap.put(URNABU, ImageIO.read(new File("Urn_of_Abundance.jpg")));
				tileGraphicMap.put(ANCBOO, ImageIO.read(new File("Ancient_Book.jpg")));
				tileGraphicMap.put(CROWN_, ImageIO.read(new File("Crown.jpg")));
				tileGraphicMap.put(ROYPLA, ImageIO.read(new File("Royal_Plate.jpg")));
				tileGraphicMap.put(ROYTAP, ImageIO.read(new File("Royal_Tapestry.jpg")));
				tileGraphicMap.put(TRECHE, ImageIO.read(new File("Treasure_Chest.jpg")));
				tileGraphicMap.put(VASPLE, ImageIO.read(new File("Vase_of_Plenty.jpg")));
				tileGraphicMap.put(VICORB, ImageIO.read(new File("Orb.jpg")));
				
				tileGraphicMap.put(WMACE, ImageIO.read(new File("Mace.jpg")));
				tileGraphicMap.put(WBSWO, ImageIO.read(new File("Broadsword.jpg")));
				tileGraphicMap.put(WHAMM, ImageIO.read(new File("Hammer.jpg")));
				tileGraphicMap.put(WBBAX, ImageIO.read(new File("Berserker_Battleaxe.jpg")));
				tileGraphicMap.put(WBOW_, ImageIO.read(new File("Bow.jpg")));
				tileGraphicMap.put(SFORC, ImageIO.read(new File("Magic_Force_Spell.jpg")));
				tileGraphicMap.put(WDSWO, ImageIO.read(new File("Double_Sword.jpg")));
				tileGraphicMap.put(WHALB, ImageIO.read(new File("Halberd.jpg")));
				tileGraphicMap.put(WDAXE, ImageIO.read(new File("Double_Axe.jpg")));
				tileGraphicMap.put(SLIGH, ImageIO.read(new File("Magic_Lightening_Spell.jpg")));
				
				tileGraphicMap.put(DTHUN, ImageIO.read(new File("Magic_Staff_of_Thunder.jpg")));
				tileGraphicMap.put(DTELE, ImageIO.read(new File("Amulet_of_Teleportation.jpg")));
				tileGraphicMap.put(DHELM, ImageIO.read(new File("Helmet.jpg")));
				tileGraphicMap.put(DINVI, ImageIO.read(new File("Magic_Cloak_of_Invisibility.jpg")));
				tileGraphicMap.put(DSHIE, ImageIO.read(new File("Shield.jpg")));
				tileGraphicMap.put(DLEAT, ImageIO.read(new File("Leather_Armor.jpg")));
				tileGraphicMap.put(DCHAI, ImageIO.read(new File("Chainmail.jpg")));
				tileGraphicMap.put(DPLAT, ImageIO.read(new File("Plate_Armor.jpg")));
				tileGraphicMap.put(WDEFL, ImageIO.read(new File("Sword_of_Deflection.jpg")));
				tileGraphicMap.put(DCOUN, ImageIO.read(new File("Counter_Enchantment_Helmet.jpg")));
				System.out.println(tileGraphicMap.size());
				
				imageIconMap=new HashMap<Integer,ImageIcon>();
				//I use the unintuitive, hacky method of setting j <= the map's size
				//because one of the tiles is a blank, meaning the number of tiles is actually
				//tileGraphicMap.size+1
				for (int j=0;j<=tileGraphicMap.size();j++){
					if (j!=BLANK)
						imageIconMap.put(j, new ImageIcon(tileGraphicMap.get(j)));
				}
				//a hack so that I can print blank squares
				imageIconMap.put(-1, new ImageIcon(new BufferedImage(30,30,BufferedImage.TRANSLUCENT)));
			} catch (IOException e){
				System.out.println("Import failure");
			}
		}
	}
	/**
	 * Check if the tile is passable TO ENEMY CHARACTERS
	 */
	public static boolean checkTileIDPassable(int id){
		return id==BLANK || id == CRATER ||
				(id>=P_HEAL && id <=P_WELL) ||
				id>=WMACE;
	}
	/**
	 * IDs of all weapons
	 */
	public static int[] getWeaponIDList(){
		return new int[]{30,31,32,33,34,35,36,37,38,39,48};
	}
	/**
	 * IDs of all weapons
	 */
	private static ArrayList<Integer> getWeaponIDArrayList(){
		ArrayList<Integer>wid=new ArrayList<Integer>(Arrays.asList(30,31,32,33,34,35,36,37,38,39,48));
		return wid;
	}
	/**
	 * returns whether the terrain feature is a weapon
	 */
	public static boolean isWeapon (int terrain){
		return getWeaponIDArrayList().contains(new Integer(terrain));
	}
	/**
	 * returns whether the terrain feature is armor
	 */
	public static boolean isArmor (int terrain){
		return getArmorIDArrayList().contains(new Integer(terrain));
	}
	/**
	 * IDs of all armor
	 */
	public static int[] getArmorIDList(){
		return new int[]{40,41,42,43,44,45,46,47,49};
	}
	/**
	 * IDs of all armor
	 */
	private static ArrayList<Integer> getArmorIDArrayList(){
		ArrayList<Integer>wid=new ArrayList<Integer>(Arrays.asList(40,41,42,43,44,45,46,47,49));
		return wid;
	}
	/**
	 * A list of ids of all items
	 */
	public static int[] getItemIDList(){
		int [] iid=new int[20];
		for (int i=30;i<50;i++){
			iid[i]=i;
		}
		return iid;
	}
	public static String getEquipToolTipStats(int id){
		return equipMap.get(id).getToolTipStats();
	}
	/**
	 * Checks the passed equipmentID to see if it has the heal ability set
	 */
	public static boolean equipCanHeal(int id){
		return equipMap.containsKey(id) && equipMap.get(id).effectID==forceheal;
	}
	public static boolean equipCanLightning(int id){
		return equipMap.containsKey(id) && equipMap.get(id).effectID==lightning;
	}
	public static boolean equipCanRange(int id){
		return equipMap.containsKey(id) && (equipMap.get(id).effectID==lightning || equipMap.get(id).effectID==rangedatk);
	}
	/**
	 * returns the amount the item increases the teleport stat by
	 */
	public static int gainTeleFromEquip(int id){
		if (equipMap.containsKey(id)){
			return equipMap.get(id).teleport;
		} else {
			return 0;
		} 
	}
	/**
	 * Checks if the item allows movement after teleport
	 */
	public static boolean equipHasFreeTele(int id){
		return equipMap.containsKey(id) && (equipMap.get(id).freeTele);
	}
	/**
	 * Checks if the item allows infinite movement
	 */
	public static boolean equipHasFreeMove(int id){
		return equipMap.containsKey(id) && (equipMap.get(id).freeAttack);
	}
	public static int getEquipMinDmg(int id){
		if (equipMap.containsKey(id)){
			return equipMap.get(id).minDamage;
		} else {
			return 0;
		}
	}
	//insert code for break skills here
	/**
	 * A list of ids of all allied pets
	 */
	public static int[] getSpeciesIDList(){
		return new int[]{MOEHOG,SKEITH,TECHO,SCORCH,GRUNDO};
	}
	/**
	 * A list of ids of all enemy pets
	 */
	public static int[] getFoeIDList(){
		return new int[]{D_MOE,D_SKE,D_TEC,D_SCO,D_GRU,D_BUZ,D_GRA};
	}
	/**
	 * check if the terrain type is a potion
	 */
	public static boolean isPotion(int tileID){
		return tileID>=P_HEAL && tileID<=P_WELL;
	}
	/**
	 * check if the terrain type is a treasure item
	 */
	public static boolean isTreasure(int tileID){
		return tileID>=GOBLET && tileID<=VICORB;
	}
	/**
	 * Check if the terrain type represents an allied pet
	 */
	public static boolean isAllyPetTerrain(int tileID){
		return tileID>=MOEHOG && tileID<=GRUNDO;
	}
	/**
	 * Check if the terrain type represents an enemy pet
	 */
	public static boolean isFoePetTerrain(int tileID){
		return tileID>=D_MOE && tileID<=D_GRA;
	}
	public static int getFoeVersionOfAlly(int sid){
		switch (sid){
			case MOEHOG:
				return D_MOE;
			case SKEITH:
				return D_SKE;
			case TECHO:
				return D_TEC;
			case SCORCH:
				return D_SCO;
			case GRUNDO:
				return D_GRU;
			default:
				return -1;
		}
	}
	public static int getAllyVersionOfFoe(int sid){
		switch (sid){
			case D_MOE:
				return MOEHOG;
			case D_SKE:
				return SKEITH;
			case D_TEC:
				return TECHO;
			case D_SCO:
				return SCORCH;
			case D_GRU:
				return GRUNDO;
			default:
				return -1;
		}
	}
}
