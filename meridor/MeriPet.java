package meridor;
import java.util.Random;
import static meridor.MConst.*;


public class MeriPet {
	final static int []STATCAPS={18,18,14};
	final static int []STATCAPS2={21,19,14};
	final static int[] CAMPAIGNS[]={STATCAPS,STATCAPS2};
	final static String [] RANKS={
			"Villager",
			"Defender",
			"Warrior",
			"Lieutenant",
			"Captain",
			"Guardian"
	};
	final static int[]RANKREQS={3,9,32,64,96}; //the saves needed to get to each rank (listed above)
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
//	int rank; //the current level of the npet, max is 5
	int saves; //the stat for experience
	int weapon; //the id of the item equipped
	int armor; //id of the item equipped
	int moves; //current amount of moves; base stored in species
	int tele; //amount of teleports; this is 1 per turn
	private int[]location;
	boolean promoted; //whether the pet has been promoted in the current set of battles CLEAR AFTER EACH SET
	
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
		weapon=-1;
		armor=-1;
		dmg=0;
		moves=0;
		tele=0;
		
		promoted=false;

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
	/**
	 * This constructor returns a new meripet that duplicates the original but is of a different species
	 */
	public MeriPet (MeriPet other, int sid){
		species=SPEC[sid];
		name=other.name;

		saves=0;
		weapon=-1;
		armor=-1;
		dmg=0;
		moves=0;
		tele=0;
		
		setLocation(other.getLocation());
		promoted=false;
		
		stats=other.stats;
	}
	/**
	 * The passed int is the campaignID; reference it for limits on stats if rank up
	 * Only one promotion per mission; ranking up tells a pet that it has been promoted,
	 * which needs to be reset
	 * Increases saves by 1 (rank is dynamically calc'd based on saves)
	 */
	public void gainSave(int campaign){
		if (!promoted){
			saves++;
			for (int i=0;i<RANKREQS.length;i++){
				if (saves==RANKREQS[i]){
					gainStats(campaign);
					promoted=true;
				}
			}
		}
	}
	/**
	 * I pass the campaign ID so that the game knows whether to use the high or
	 * low stat caps. This method raises all stats by 1 unless the pet has reached
	 * the stat cap.
	 */
	public void gainStats(int campaign){
		int [] caps=STATCAPS2;
		if (campaign==1){
			caps=STATCAPS;
		}
		for (int i=0;i<stats.length;i++){
			if (stats[i]<caps[i]){
				stats[i]++;
			}
		}
	}
	//***lazy initialization pattern
	//I'm not sure about the implementation of location
	//this implementation is intended to require the gamestate to
	//loop through list of player/foe pets to see which coord lines up with a click
	/**
	 * Lazy initialization
	 * returns an "array-tuple" 2 units long, corresponding to x and y value
	 */
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
	/**
	 * Lazy initialization
	 * takes an "array-tuple" 2 units long, corresponding to x and y value
	 */
	public void setLocation(int[] coords){
		if (location==null){
			location=new int []{coords[0],coords[1]};
		}
		location[0]=coords[0];
		location[1]=coords[1];
	}
	/**
	 * Returns the amount of additional attack gained due to raw attack stat
	 * Tentatively adding the calculation for extra weapon attack here
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
		return name+"/"+getRank();
	}
	public String getRank(){
		int rank=0;
		if (saves>=3){
			rank++;
			if (saves>=9){
				rank++;
				if (saves>=32){
					rank++;
					if (saves>=64){
						rank++;
						if (saves>=96){
							rank++;
						}
					}
				} 
			}
		}
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
		return stats[ATK]+"+"+(getASbonus(stats[ATK])+getWeaponBonus());
	}
	//needs to be updated for equipped weapons
	public String getDefenseString(){
		if (getArmorBonus()>0)
			return (stats[DEF]+"+"+getArmorBonus());
		else
			return stats[DEF]+"";
	}
	public String getSavesString(){
		return saves+"";
	}
	public void setWeapon(int w){
		weapon=w;
	}
	public void setArmor(int a){
		armor=a;
	}
	//not yet working, might want to use a graphic
	public String getWeaponName(){
		return "";
	}
	/**
	 * Checks the currently equipped weapon, and gets its species-specific attack
	 * bonus
	 * crossrefs constants
	 * might be incorporated into asbonus
	 * fails with error for debug purposes
	 */
	public int getWeaponBonus(){
		if (weapon>-1)
			return MConst.equipMap.get(weapon).getAtkBonus(getSpeciesID());
		else
			return 0;
	}
	/**
	 * Checks the currently equipped weapon, and gets its species-specific 
	 * defense bonus
	 * crossrefs constants
	 * might be incorporated into attack calc
	 * fails with error for debug purposes
	 */
	public int getArmorBonus(){
		if (armor>-1)
			return MConst.equipMap.get(armor).getDefBonus(getSpeciesID());
		else
			return 0;
	}
	public int getTotalArmor(){
		return getArmorBonus()+stats[DEF];
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
		int damage = getASbonus(a.stats[ATK])+a.getWeaponBonus()+roll;
		int net = Math.max(0, damage-d.getTotalArmor());
		String battlelog=a.name+" attacked "+d.name+" for "+damage+
				", dealing "+net+" total! (Rolled "+roll+")";
		System.out.println(battlelog); //placeholder?
		d.injure(net);
		return battlelog;
	}
	/**
	 * Use to turn a meripet to the opposite allegiance
	 * Returns a completely different meripet with the same name and stats, clears equips
	 * Returns null if conversion is invalid
	 */
	public MeriPet convert (){
		if (isAllyPetTerrain(getSpeciesID())){
			return new MeriPet(this,getFoeVersionOfAlly(getSpeciesID()));
		} else {
			int sid=getAllyVersionOfFoe(getSpeciesID());
			if (sid>=0){
				return new MeriPet(this,getAllyVersionOfFoe(getSpeciesID()));
			} else {
				return null;
			}
		}
	}
	public String printStats(){
		return name
				+ " the "+getRank()+" "+species.name
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
//		System.out.println(name+" has "+moves+" moves left.");
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
