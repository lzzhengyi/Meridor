package meridor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import static meridor.MConst.*;

//even though this class is called battlemap
//I think I will use it as the gamestate
public class BattleMap extends JPanel implements ActionListener,MouseListener{

	final static int MAPDIM=10; //10 by 10 squares
	final static int IMGDIM=30; //30 by 30 pixels
	final static int IMGMARGIN=5; //5 pixels to display status
	final static int TILEDIM=IMGDIM+IMGMARGIN+2; //total size of cells
	//BufferedImage buzz;
	public MeriTile [][] tilemap;
	public Random random;
	private MeriPanel parent;
	
	//level is needed to restrict spawned items and enemies
	public BattleMap (int level, MeriPanel p){
		random=new Random();
		parent=p;

		genTerrainMap();
		
		addMouseListener(this);
		setPreferredSize(new Dimension(getPaneSize()+5,getPaneSize()+5));
	}
	public void genTerrainMap(){
		//mountains on the mid two and outer two, even rows only
		//six villages, two/row, rows 6-8
		tilemap=new MeriTile[MAPDIM][MAPDIM];
		int xoff=0;
		int yoff=0;
		for (int i=0;i<MAPDIM;i++){
			for (int j=0;j<MAPDIM;j++){
				tilemap[i][j]=new MeriTile(i+xoff,j+yoff);
				yoff+=TILEDIM;
			}
			yoff=0;
			xoff+=TILEDIM;
		}
		/*
		 * Set mountains and villages
		 * right now this is a hardcoded mess
		 */
		for (int i=0;i<MAPDIM;i++){
			if (i>0 && i%2==0){
				//special case because enemies spawn on this row
				if (i==2){
					if (random.nextBoolean()){
						tilemap[0][i].setTerrain(MOUNTAIN);
						tilemap[9][i].setTerrain(MOUNTAIN);
					} else {
						tilemap[1][i].setTerrain(MOUNTAIN);
						tilemap[8][i].setTerrain(MOUNTAIN);
					}
				} else {
					/*
					 * there are three mountain configurations, all symmetrical
					 * villages have to be specially placed for each configuration
					 * I could regularize this, but no need now
					 */
					if (random.nextBoolean()){
						//depending on offset, mountains on edge or one tile away from edge
						int offset=random.nextInt(2);
						tilemap[0+offset][i].setTerrain(MOUNTAIN);
						tilemap[9-offset][i].setTerrain(MOUNTAIN);
						if (i<9 && i>5){
							if (offset==0){
								int[]coords=new int[]{1,2,3,4};
								tilemap[coords[random.nextInt(coords.length)]][i].setTerrain(VILLAGE);
								coords=new int[]{5,6,7,8};
								tilemap[coords[random.nextInt(coords.length)]][i].setTerrain(VILLAGE);
							} else {
								int[]coords=new int[]{0,2,3,4};
								tilemap[coords[random.nextInt(coords.length)]][i].setTerrain(VILLAGE);
								coords=new int[]{5,6,7,9};
								tilemap[coords[random.nextInt(coords.length)]][i].setTerrain(VILLAGE);
							}
						}
					} else { //middle case
						tilemap[4][i].setTerrain(MOUNTAIN);
						tilemap[5][i].setTerrain(MOUNTAIN);
						if (i<9 && i>5){
							int[]coords=new int[]{0,1,2,3};
							tilemap[coords[random.nextInt(coords.length)]][i].setTerrain(VILLAGE);
							coords=new int[]{6,7,8,9};
							tilemap[coords[random.nextInt(coords.length)]][i].setTerrain(VILLAGE);							
						}
					}
				}
			} else { //because 7 is the only odd row with villages
				if (i==7){
					int[]coords=new int[]{0,1,2,3,4};
					tilemap[coords[random.nextInt(coords.length)]][i].setTerrain(VILLAGE);
					coords=new int[]{5,6,7,8,9};
					tilemap[coords[random.nextInt(coords.length)]][i].setTerrain(VILLAGE);					
				}
			}
		}
	}
	/**
	 * Update the locations of meripets from the meripanel
	 */
	public void updatePetLocations(ArrayList<MeriPet> petlist){
		removePetLocations();
		for (int i=0;i<petlist.size();i++){
			int [] ploc=petlist.get(i).getLocation();
			tilemap[ploc[0]][ploc[1]].setTerrain(petlist.get(i).getSpeciesID());
		}
		repaint();
	}
	/**
	 * clear pet locations (prior to updating them)
	 */
	public void removePetLocations(){
		for (int i=0;i<MAPDIM;i++){
			for (int j=0;j<MAPDIM;j++){
				if (tilemap[i][j].terrain>=MOEHOG && tilemap[i][j].terrain<=D_GRA){
					tilemap[i][j].setTerrain(BLANK);;
				}
			}
		}
	}
	//return an array of adjacent tiles
	//behind first, front next, then sides from top to bottom 
	//eight surrounding tiles
	//The goal of this method is to get ordered list of nodes for enemy ai to check
	public ArrayList <MeriTile> getAdj (int x, int y){
		ArrayList <MeriTile> results=new ArrayList<MeriTile>();
		//top
		if (y>=0)
			results.add(tilemap[x][y-1]);
		//bottom
		if (y<MAPDIM-1)
			results.add(tilemap[x][y+1]);
		//top left and below
		ArrayList<MeriTile>left=new ArrayList<MeriTile>();
		ArrayList<MeriTile>right=new ArrayList<MeriTile>();
		if (x>=0){
			if (y>=0)
				left.add(tilemap[x-1][y-1]);
			left.add(tilemap[x-1][y]);
			if (y<MAPDIM-1)
				left.add(tilemap[x-1][y+1]);
		}
		if (x<MAPDIM-1){
			if (y>=0)
				right.add(tilemap[x+1][y-1]);
			right.add(tilemap[x+1][y]);
			if (y<MAPDIM-1)
				right.add(tilemap[x+1][y+1]);
		}
		if (random.nextDouble()<0.7){
			//add left first
			results.addAll(left);
			results.addAll(right);
		} else {
			//add right first
			results.addAll(right);
			results.addAll(left);
		}
		return results;
	}
	/**
	 * return true if the passed coordinate is next to the selected meripet in parent
	 */
	public boolean checkAdj(int[]clicked){
		boolean isadj=false;
		if (parent.selected!=null){
			if (Math.abs(clicked[0]-parent.selected.getLocation()[0])<=1
					&& Math.abs(clicked[1]-parent.selected.getLocation()[1])<=1){
				isadj=true;
			}
		} 
		return isadj;
	}
	public int getPaneSize(){
		return MAPDIM*MeriTile.TILESIZE;
	}
	public void paintComponent (Graphics g){
		super.paintComponent(g);
		setSize(getPaneSize(),getPaneSize());
		//g.drawImage(buzz, 100, 100, null);
		for (int i=0;i<MAPDIM;i++){
			for (int j=0;j<MAPDIM;j++){
				tilemap[i][j].draw(g);
			}
		}
	}
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseClicked(MouseEvent e) {

	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	//get the coordinate of the player click and attempt to activate a warrior
	//on that tile
	public void mousePressed(MouseEvent e) {
		//the intent of this method is to have the player only able to click
		//tiles with pets on them
		//if right click, bring up info screen related to the terrain type?
		int mx=e.getX();
		int my=e.getY();
		int xc=0,yc=0;
		if (mx<getPaneSize()){
			xc=mx/MeriTile.TILESIZE;
		}
		if (my<getPaneSize()){
			yc=my/MeriTile.TILESIZE;
		}
		System.out.println(mx+" "+my+"/"+xc+" "+yc);
		//if no target selected, try to reference parent coordinates to find a pet
		if (parent.selected==null){
			for (int i=0;i<parent.ally.size();i++){
				if (Arrays.equals(parent.ally.get(i).getLocation(),new int[]{xc,yc})){
					parent.selected=parent.ally.get(i);
				}
			}
		} else if (checkAdj(new int[]{xc,yc})){
			//try to move into a blank tile if tile is valid
			if (tilemap[xc][yc].terrain==BLANK){
				parent.selected.setLocation(xc, yc);
				parent.update();
				parent.selected=null;
			} else if (isFoePetTerrain(tilemap[xc][yc].terrain)){
				//try to attack a foe
				for (int i=0;i<parent.foe.size();i++){
					if (Arrays.equals(parent.foe.get(i).getLocation(),new int[]{xc,yc})){
						parent.updateBattleLog(MeriPet.attack(parent.selected, parent.foe.get(i)));
						//the above method needs to print to a textlog
						parent.selected=null;
						parent.update();
						break;
					}
				}
			}
		}
		updatePetLocations(parent.getPetLocations());
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
