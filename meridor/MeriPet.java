package meridor;
import java.util.Random;
import static meridor.MConst.*;


public class MeriPet {
	
	final static int []STATCAPS={18,18,14};
	final static int []STATCAPS2={21,19,14};
	final static String [] RANKS={
			"Villager",
			"Defender",
			"Warrior",
			"Lieutenant",
			"Captain",
			"Guardian"
	};
	
	final Species [] SPEC ={
			new Species (MOEHOG,"Moehog",4,15,8,9),
			new Species (SKEITH,"Skeith",1,15,15,9),
			new Species (TECHO,"Techo",2,15,8,9),
			new Species (SCORCH,"Scorchio",2,15,10,9),
			new Species (GRUNDO,"Grundo",2,15,10,9),
			
			new Species (D_MOE,"DMoehog",4,15,8,9),
			new Species (D_SKE,"DSkeith",1,15,15,9),
			new Species (D_TEC,"DTecho",2,15,8,9),
			new Species (D_SCO,"DScorchio",2,15,10,9),
			new Species (D_GRU,"DGrundo",2,15,10,9),
			new Species (D_BUZ,"DBuzz",2,15,10,9),
			new Species (D_GRA,"DGrarrl",2,15,10,9)
	};
	/*
	 * The class that contains the features of player and enemy neopets
	 * This class is involved in combat calculations
	 * A method is needed to instantiate new pets spawned when pets die
	 * 
	 * How to calculate damage; attack+bonuses+roll-defense
	 */
	final static int HP = 0;
	final static int ATK = 1;
	final static int DEF = 2;
	
	final static int VILLAGER = -1;
	
	static Random rand=new Random ();
	String name;
	final Species species; //contains racial information for the pet
	int[] stats={10,5,5}; //contains current base stats
	int dmg; //current damage taken by the pet
	int rank; //the current level of the npet, max is 5
	int saves; //the stat for experience
	int weapon; //the id of the item equipped
	int armor; //id of the item equipped
	int moves; //current amount of moves; base stored in species
	int tele; //amount of teleports; this is 1 per turn
	private int[]location;
	
	/**
	 * Minimum constructor for a generic npet includes:
	 * name
	 * species (id)
	 * spawn condition (ally, or foe of various battles)
	 * 
	 */
	public MeriPet (String n, int sid, int sc){
		name=n;
		species=SPEC[sid];
		saves=0;
		rank=0;
		weapon=-1;
		armor=-1;
		dmg=0;
		moves=0;
		tele=0;

		//for meridell defenders
		if (sc==VILLAGER){
			for (int i=0;i<stats.length;i++){
				stats[i]=Math.min(
						STATCAPS[i],
						species.basestats[i]+rand.nextInt(5)
				);
			}
		} else {
			for (int i=0;i<stats.length;i++){
				stats[i]=Math.min(
						STATCAPS[i],
						species.basestats[i]+rand.nextInt(5)
				);
			}
		}
	}
	//lazy initialization pattern
	//I'm not sure about the implementation of location
	//this implementation is intended to require the gamestate to
	//loop through list of player/foe pets to see which coord lines up with a click
	public int[] getLocation(){
		if (location==null){
			location=new int []{0,0};
		}
		return location;
	}
	/**
	 * Set the xy coordinates on the map of the meripet
	 * no guarantees the tile is not shared with something else
	 */
	public void setLocation(int x,int y){
		if (location==null){
			location=new int []{x,y};
		}
		location[0]=x;
		location[1]=y;
	}
	public void setLocation(int[] coords){
		if (location==null){
			location=new int []{coords[0],coords[1]};
		}
		location[0]=coords[0];
		location[1]=coords[1];
	}
	/**
	 * Returns the amount of additional attack gained due to raw attack stat
	 */
	public static int getASbonus (int a){
		int b=0;
		if (a>8){
			b++;
		}
		if (a>11){
			b++;
		}
		if (a>14){
			b++;
		}
		if (a>17){
			b++;
		}
		if (a>18){
			b++;
		}
		return b;
	}
	/**
	 * Print the name of the pet's species
	 */
	public String getSpeciesName(){
		return species.name;
	}
	/**
	 * Print the name and rank separated by return
	 */
	public String getNameNRank(){
		return name+"/"+RANKS[rank];
	}
	public String getRank(){
		return RANKS[rank];
	}
	/**
	 * Print the currhp and hp separated by slash
	 */
	public String getDmgNHealth(){
		return (getCurrHealth()+"/"+stats[HP]);
	}
	public int getCurrHealth(){
		return stats[HP]-dmg;
	}
	//needs to be updated for equipped weapons
	public String getAttackString(){
		return stats[ATK]+"+"+getASbonus(stats[ATK]);
	}
	//needs to be updated for equipped weapons
	public String getDefenseString(){
		return (stats[DEF]+"");
	}
	public String getSavesString(){
		return saves+"";
	}
	//not yet working, might want to use a graphic
	public String getWeaponName(){
		return "";
	}
	//not yet working, might want to use a graphic
	public String getArmorName(){
		return "";
	}
	/**
	 *An attempt at the damage formula; might want to export this method to const
	 */
	public static String attack (MeriPet a, MeriPet d){
		int roll=rand.nextInt(21)+1;
		int damage = a.stats[ATK]+getASbonus(a.stats[ATK])+roll;
		int net = damage-d.stats[DEF];
		String battlelog=a.name+" attacked for "+damage+", dealing "+net+"total!";
		System.out.println(battlelog); //placeholder?
		d.injure(net);
		return battlelog;
	}
	public String printStats(){
		return name
				+ " the "+RANKS[rank]+" "+species.name
				+ " HP:"+stats[HP]
				+ " ATK:"+stats[ATK]+"+"+getASbonus(stats[ATK])
				+ " DEF:"+stats[DEF];
				
	}
	/**
	 * Used to see if the npet has available individual moves left
	 */
	public boolean hasMove (){
		return moves>0;
	}
	/**
	 * Use to change equipped weapon, previous is overwritten
	 */
	public void gainWeapon (int i){
		weapon=i; //there needs to be a comparison method
	}
	public void gainArmor (int i){
		armor=i; //there needs to be a comparison method
	}
	/**
	 * removes one current move point from the npet
	 */
	public void moveOnce (){
		moves=Math.max(moves-1, 0);
	}
	/**
	 * returns true if dmg>health
	 */
	public boolean checkDead (){
		return dmg>=stats[HP];
	}
	/**
	 * Use to deal damage to the npet
	 */
	public void injure (int i){
		if (i>0)
			dmg+=i;
	}
	/**
	 * Restores health to meripet, cannot restore above max
	 */
	public void heal (int i){
		dmg=Math.max(dmg-i, 0);
	}
	/**
	 * Use this to heal health between missions
	 */
	public void refreshHP (){
		dmg=0;
	}
	/**
	 * Use this to restore move at the start of every turn
	 */
	public void refreshMove (){
		moves=species.moves+0;
		tele=1;
	}
	/**
	 * The returned ID should be the one referenced in MConst
	 */
	public int getSpeciesID(){
		return species.spid;
	}
	/**
	 * Use this to set stats for enemy pets
	 */
	public void setStats(int[]nstats){
		if (nstats.length==3){
			stats=nstats;
		}
	}
	private class Species {
		/*
		 * contains the race wide attributes of each neopet
		 * Moves per turn
		 * Image
		 * Powers
		 * Base stats
		 */
		int spid; //species id, determines whether it can use powers
		//AI pets can always use their powers
		String name;
		int moves;
		int[]basestats;
		
		public Species (int id, String n, int m, int h, int a, int d){
			spid=id;
			name=n;
			moves=m;
			basestats=new int[3];
			basestats[HP]=h;
			basestats[ATK]=a;
			basestats[DEF]=d;
		}
	}
}
