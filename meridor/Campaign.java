package meridor;

import java.util.ArrayList;

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
public class Campaign {

	public int currentBattle; //3 battles per scenario
	public int currentScenario; //ten scenarios?
	public ArrayList<Scenario> scenarios; //scenarios in the campaign
	public ArrayList<MeriPet> allies;
	
	public Campaign(ArrayList<Scenario> scen){
		scenarios=scen;
		currentBattle=0;
		currentScenario=0;
		//generate basic team
	}
	//generateallyteam
	//generateenemyteam
	//generateboard???
	//getCurrentScenarioName
	
	
	
	private class Scenario {
		private String name;
		private int treasureID, potionID;
		public int[] itemIDList;
		ArrayList<int[]>foes;
		
		private Scenario(String name,int treasure,int potion,int [] itemids, ArrayList<int[]> foes){
			this.name=name;
			treasureID=treasure;
			potionID=potion;
			itemIDList=itemids;
			this.foes=foes;
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
