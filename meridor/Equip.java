package meridor;
import java.awt.Canvas;
import static meridor.MConst.*;

public class Equip implements MeriGraphic{

	int imageID;
	String name;
	String desc; //meaningless text string to describe item to user
	int waID; //used to identity the weapon by the program
	int def;
	int atk;
	int effectID;
	boolean isSpell;//if the pet that picks up is not a spellcaster, delete
	SpecBon specbon;
	//need a variable that tracks the ids of pets that can equip it
	
	public Equip (String n, int id, int iid, int eid, int a, int d,
			int ba, int bd, int bsid){
		specbon=new SpecBon (ba, bd, bsid);
		imageID=iid;
		name=n;
		waID=id;
		atk=d;
		def=d;
		effectID=eid;
	}
	/**
	 * Compares species ID to the id of the specbon
	 * This implementation assumes the replacement of the default
	 * value by the specbon value
	 */
	public int getAtkBonus (int sid){
		if (specbon.speciesID-sid==0){
			return specbon.atkBonus;
		} else {
			return atk;
		}
	}
	/**
	 * Compares species ID to the id of the specbon
	 */
	public int getDefBonus (int sid){
		if (specbon.speciesID-sid==0){
			return specbon.defBonus;
		} else {
			return def;
		}
	}
	//needs an equipfactory method, most likely
	@Override
	public void draw(Canvas c) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getImageID() {
		// TODO Auto-generated method stub
		
	}
	
	private class SpecBon {
		
		int atkBonus, defBonus, speciesID;
		
		private SpecBon (int ab, int db, int sid){
			atkBonus=ab;
			defBonus=db;
			speciesID=sid;
		}
	
	}


}
