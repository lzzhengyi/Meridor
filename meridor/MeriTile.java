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
	
	public int tileID;
	public int terrain;
	public int x,y; //coordinates
	//probably worth checking their coordinates
	public MeriTile (int xx,int yy){
		if (tileGraphicMap ==null){
			loadImages();
		}
		x=xx;
		y=yy;
		terrain=BLANK;
	}

	public int getGridx(){
		return x/TILESIZE;
	}
	public int getGridy(){
		return y/TILESIZE;
	}
	/**
	 * Checks if a tile is passable TO ENEMIES
	 * References MConst, which is where tile type traversability info is kept
	 */
	public boolean checkPassable(){
		return checkTileIDPassable(terrain);
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
//			g.drawImage(tileGraphicMap.get(MConst.random.nextInt(20)+30), x+4, y+4, null);
		}
		g.setColor(Color.gray);
		if (!isTreasure(terrain))
			g.drawRect(x+3, y+3, TILESIZE-7, TILESIZE-7);
		g.drawRect(x, y, TILESIZE, TILESIZE);
	}
}
