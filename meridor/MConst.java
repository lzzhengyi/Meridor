package meridor;

public class MConst {
	final static int BLANK=0;
	final static int VILLAGE=1;
	final static int MOUNTAIN=2;
	final static int CRATER=3;
	final static int MOEHOG=4;
	final static int SKEITH=5;
	final static int TECHO=6;
	final static int SCORCH=7;
	final static int GRUNDO=8;
	final static int D_MOE=9;
	final static int D_SKE=10;
	final static int D_TEC=11;
	final static int D_SCO=12;
	final static int D_GRU=13;
	final static int D_BUZ=14;
	final static int D_GRA=15;
	
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
}
