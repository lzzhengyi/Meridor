package meridor;

public class MConst {
	//considering converting to enums, but not yet
	public enum TT {
		BLANK,VILLAGE,MOUNTAIN,CRATER,
		MOEHOG,SKEITH,TECHO,SCORCH,GRUNDO,
		D_MOE,D_SKE,D_TEC,D_SCO,D_GRU,D_BUZ,D_GRA
	};

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
	
	private class Coord{
		int x,y;
		
		private Coord(int xx,int yy){
			x=xx;
			y=yy;
		}
	}
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
