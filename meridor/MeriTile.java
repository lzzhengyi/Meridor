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
				BufferedImage ttile1=ImageIO.read(new File("vlg.jpg"));
				BufferedImage ttile2=ImageIO.read(new File("mtn.jpg"));
				BufferedImage ttile3=ImageIO.read(new File("Smoking_Crater.jpg"));
				BufferedImage ttile4=ImageIO.read(new File("Moeh00.jpg"));
				BufferedImage ttile5=ImageIO.read(new File("Skei01.jpg"));
				BufferedImage ttile6=ImageIO.read(new File("Tech02.jpg"));
				BufferedImage ttile7=ImageIO.read(new File("Scor03.jpg"));
				BufferedImage ttile8=ImageIO.read(new File("Grun04.jpg"));
				BufferedImage ttile9=ImageIO.read(new File("Draco_Moehog00.jpg"));
				BufferedImage ttile10=ImageIO.read(new File("Draco_Skeith00.jpg"));
				BufferedImage ttile11=ImageIO.read(new File("Draco_Techo00.jpg"));
				BufferedImage ttile12=ImageIO.read(new File("Draco_Scorchio00.jpg"));
				BufferedImage ttile13=ImageIO.read(new File("Draco_Grundo00.jpg"));
				BufferedImage ttile14=ImageIO.read(new File("Draco_Buzz00.jpg"));
				BufferedImage ttile15=ImageIO.read(new File("Draco_Grarrl00.jpg"));
				
				tileGraphicMap.put(VILLAGE, ttile1);
				tileGraphicMap.put(MOUNTAIN, ttile2);
				tileGraphicMap.put(CRATER, ttile3);
				tileGraphicMap.put(MOEHOG, ttile4);
				tileGraphicMap.put(SKEITH, ttile5);
				tileGraphicMap.put(TECHO, ttile6);
				tileGraphicMap.put(SCORCH, ttile7);
				tileGraphicMap.put(GRUNDO, ttile8);
				tileGraphicMap.put(D_MOE, ttile9);
				tileGraphicMap.put(D_SKE, ttile10);
				tileGraphicMap.put(D_TEC, ttile11);
				tileGraphicMap.put(D_SCO, ttile12);
				tileGraphicMap.put(D_GRU, ttile13);
				tileGraphicMap.put(D_BUZ, ttile14);
				tileGraphicMap.put(D_GRA, ttile15);
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
	public void draw(Graphics g){
		
		g.setColor(Color.gray);
		g.drawRect(x, y, TILESIZE, TILESIZE);
		g.drawRect(x+3, y+3, TILESIZE-7, TILESIZE-7);
		
		if (tileGraphicMap.containsKey(terrain)){
			g.drawImage(tileGraphicMap.get(terrain), x+4, y+4, null);
		}
	}
}
