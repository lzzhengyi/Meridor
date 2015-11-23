package meridor;

import java.io.Serializable;
import java.util.ArrayList;
import static meridor.MConst.*;
/**
 * A package of scenario objects?
 * It tracks:
 * current scenario
 * current team
 * whether the item has been looted for the scenario
 * 
 * -probably implement scenario as a private class
 * -this reads the scenario information from an external file or a set of hardcoded instructions
 *  
 */
public class Campaign implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Scenario M0=new Scenario("Mission 1",GOBLET,P_HEAL,new int[]{WMACE,WBSWO,DTHUN,DTHUN},new int[][]{
		new int[]{D_MOE,8,12,5,9,6,12}
	},5);
	Scenario M1=new Scenario("Mission 2",GOLDIX,P_FORT,new int[]{WBOW_,WHAMM,DTELE,DTELE},new int[][]{
		new int[]{D_TEC,12,16,9,13,8,14}
	},6);
	Scenario M2=new Scenario("Mission 3",URNABU,P_MEGA,new int[]{SFORC,WBBAX,DHELM,DSHIE},new int[][]{
		new int[]{D_SKE,12,16,9,13,8,14}
	},6);
	Scenario M3=new Scenario("Mission 4",ANCBOO,P_WELL,new int[]{SFORC,WBOW_,DTELE,DINVI},new int[][]{
		new int[]{D_SCO,14,18,12,16,12,16}
	},6);
	Scenario M4=new Scenario("Mission 5",CROWN_,P_FORT,new int[]{SFORC,WBBAX,DHELM,DSHIE},new int[][]{
		new int[]{D_GRU,15,19,13,16,15,17}
	},6);
	Scenario M5=new Scenario("Mission 6",ROYPLA,P_MEGA,new int[]{WDEFL,WDEFL,DCOUN,DCOUN},new int[][]{
		new int[]{D_BUZ,21,21,21,21,21,21},
		new int[]{D_BUZ,21,21,21,21,21,21},
		new int[]{D_MOE,17,17,17,17,17,17}
	},6,2);
	Scenario M6=new Scenario("Mission 7",ROYTAP,P_WELL,new int[]{WDSWO,WHALB,DCHAI,DLEAT},new int[][]{
		new int[]{D_BUZ,29,29,25,25,25,25},
		new int[]{D_BUZ,21,21,21,21,21,21},
		new int[]{D_BUZ,21,21,21,21,21,21},
		new int[]{D_TEC,18,18,18,18,18,18}
	},6,2);
	Scenario M7=new Scenario("Mission 8",TRECHE,P_FORT,new int[]{SLIGH,SLIGH,DPLAT,DCHAI},new int[][]{
		new int[]{D_GRA,23,23,21,21,21,21},
		new int[]{D_GRA,22,22,22,22,22,22},
		new int[]{D_GRA,22,22,22,22,22,22},
		new int[]{D_GRA,23,23,21,21,21,21},
		new int[]{D_SKE,19,19,19,19,19,19}
	},6,2);
	Scenario M8=new Scenario("Mission 9",VASPLE,P_MEGA,new int[]{WDAXE,WDAXE,DTELE,DCHAI},new int[][]{
		new int[]{D_GRA,29,29,26,26,26,26},
		new int[]{D_GRA,22,22,22,22,22,22},
		new int[]{D_GRA,22,22,22,22,22,22},
		new int[]{D_GRA,22,22,22,22,22,22},
		new int[]{D_GRA,22,22,22,22,22,22},
		new int[]{D_SCO,20,20,20,20,20,20}
	},6,2);
	Scenario M9=new Scenario("Mission 10",VICORB,P_WELL,new int[]{WDAXE,SLIGH,DPLAT,DCHAI},new int[][]{
		new int[]{D_BUZ,29,29,27,27,27,27},
		new int[]{D_BUZ,21,21,21,21,21,21},
		new int[]{D_GRA,22,22,22,22,22,22},
		new int[]{D_GRA,22,22,22,22,22,22},
		new int[]{D_GRA,29,29,27,27,27,27},
		new int[]{D_GRU,23,23,23,23,23,23}
	},6,2);
	
	public int currentBattle; //3 battles per scenario
	public int currentScenario; //ten scenarios?
	public boolean treasureCollected;
	public ArrayList<Scenario> scenarios; //scenarios in the campaign
	public ArrayList<MeriPet> allies;
	
	public Campaign(ArrayList<Scenario> scen){
		scenarios=scen;
		currentBattle=0;
		currentScenario=0;
		treasureCollected=false;
		//standard 5 basic pets, with random stats
		generateBasicTeam();
	}
	
	/**
	 * 
	 */
	public Campaign(){
		scenarios=new ArrayList<Scenario> ();
		scenarios.add(M0);
		scenarios.add(M1);
		scenarios.add(M2);
		scenarios.add(M3);
		scenarios.add(M4);
		scenarios.add(M5);
		scenarios.add(M6);
		scenarios.add(M7);
		scenarios.add(M8);
		scenarios.add(M9);
		currentBattle=0;
		currentScenario=0;
		treasureCollected=false;
		//standard 5 basic pets, with random stats
		generateBasicTeam();
	}
	/**
	 * 
	 */
	public boolean advance (){
		++currentBattle;
		if (currentBattle>=3){
			currentBattle=0;
			currentScenario++;
			treasureCollected=false;
			return true;
		}
		return false;
	}
	/**
	 * Resets all attrited stats for allied units between fights
	 * used for save/load
	 */
	public void refreshAllyAll(){
		if (allies!=null){
			for (int i=0;i<allies.size();i++){
				allies.get(i).refreshTotal();
			}
		}
	}
	/**
	 * returns the list appropriate to the current scenario (4 ints)
	 */
	public int[] getItemList(){
		return scenarios.get(currentScenario).itemIDList;
	}
	public int getTreasure(){
		return scenarios.get(currentScenario).treasureID;
	}
	public int getPotion(){
		return scenarios.get(currentScenario).potionID;
	}
	public int getWave(){
		return scenarios.get(currentScenario).wave;
	}
	public String getMissionNScenarioNames(){
		return "Mission "+(currentScenario+1)+": Battle "+(currentBattle+1);
	}
	private void generateBasicTeam(){
		allies=new ArrayList<MeriPet>();
		allies.add(new MeriPet("Soldier 1",MConst.MOEHOG,-1));
		allies.add(new MeriPet("Soldier 2",MConst.SKEITH,-1));
		allies.add(new MeriPet("Soldier 3",MConst.TECHO,-1));
		allies.add(new MeriPet("Soldier 4",MConst.SCORCH,-1));
		allies.add(new MeriPet("Soldier 5",MConst.GRUNDO,-1));
	}
	public void setAllies(ArrayList<MeriPet> nallies){
		allies=nallies;
	}
	/**
	 * Return an arraylist with battle ready opponents based on the current scenario
	 */
	public ArrayList<MeriPet> generateFoes(){
		ArrayList<MeriPet> foearmy=new ArrayList<MeriPet>();
		for (int i=0;i<scenarios.get(currentScenario).armysize+currentBattle;i++){
			int pid=Math.min(i, scenarios.get(currentScenario).foes.length-1);
			String name="Invader "+(1+i);
			//an inelegant way to check if the species of the invader is a dark lord
			if (scenarios.get(currentScenario).foes[pid][0]==MConst.D_BUZ){
				name="Dark Lord "+(1+i);
			} else if (scenarios.get(currentScenario).foes[pid][0]==MConst.D_GRA) {
				name="Dark Master "+(1+i);
			}
			foearmy.add(new MeriPet(name,
					scenarios.get(currentScenario).foes[pid]));
		}
		return foearmy;
	}
//	public void addScenario(String n, int t, int p, int []itid, int[][] f,){
//		scenarios.add(new Scenario(n,t,p,itid,f));
//	}
	//generateallyteam
	//generateenemyteam
	//generateboard???
	//getCurrentScenarioName
	
	
	
	private class Scenario implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String name;
		private int treasureID, potionID;
		public int[] itemIDList;
		int[][] foes;
		int armysize,wave;
		
		private Scenario(String name,int treasure,int potion,int [] itemids, int[][] foes, int armysize){
			this.name=name;
			treasureID=treasure;
			potionID=potion;
			itemIDList=itemids;
			this.foes=foes;
			this.armysize=armysize;
			wave=1;
		}
		private Scenario(String name,int treasure,int potion,int [] itemids, int[][] foes, int armysize, int wave){
			this.name=name;
			treasureID=treasure;
			potionID=potion;
			itemIDList=itemids;
			this.foes=foes;
			this.armysize=armysize;
			this.wave=wave;
		}
		public String getName(){
			return name;
		}
		public int getTreasureID(){
			return treasureID;
		}
		public int getPotionID(){
			return potionID;
		}
	}
}
