package meridor;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static meridor.MConst.*;

import javax.imageio.ImageIO;

public class MeriTile {
	
	//need an additional ruined village tile
	//potentially three other item types:
	//equips, potions, treasures, and pets
	//I think I will just import all images as terrain here
	//and give each object a reference to their image's id
	final static int TILESIZE=38;
	
	public static Map<Integer,BufferedImage>tileGraphicMap=null;
	public int tileID;
	public int terrain;
	public int x,y; //coordinates
	//probably worth checking their coordinates
	public MeriTile (int xx,int yy){
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
				System.out.println(tileGraphicMap.size());
			} catch (IOException e){
				System.out.println("Import failure");
			}
		}
		x=xx;
		y=yy;
		terrain=BLANK;
	}
	//add a potion or enemy
	public void addItem (){
		
	}
	public int getGridx(){
		return x/TILESIZE;
	}
	public int getGridy(){
		return y/TILESIZE;
	}
	public boolean checkPassable(){
		return terrain==BLANK || terrain==CRATER;
	}
	//change to village, mountain, or crater
	//I guess the equip tracker will pass locations
	//and id of items, and same for pets
	public void setTerrain(int t){
		if (tileGraphicMap.containsKey(t)){
			terrain=t;
		}else {
			terrain=BLANK;
		}
	}
	public void drawMoveDepleted(Graphics g){
		g.setColor(Color.red);
		g.fillRect(x, y, TILESIZE, TILESIZE);
	}
	public void draw(Graphics g){		
		if (tileGraphicMap.containsKey(terrain)){
			if (isTreasure(terrain)){
				g.drawImage(tileGraphicMap.get(terrain).getSubimage(5, 5, 38, 38), 
						x, y, null);
			} else{
				g.drawImage(tileGraphicMap.get(terrain), x+4, y+4, null);
			}

		} else {
			g.setColor(Color.white);
			g.fillRect(x, y, TILESIZE, TILESIZE);
		}
		g.setColor(Color.gray);
		if (!isTreasure(terrain))
			g.drawRect(x+3, y+3, TILESIZE-7, TILESIZE-7);
		g.drawRect(x, y, TILESIZE, TILESIZE);
	}
}
