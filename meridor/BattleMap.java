package meridor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import static meridor.MConst.*;

//even though this class is called battlemap
//I think I will use it as the gamestate
public class BattleMap extends JPanel implements ActionListener,MouseListener,MouseMotionListener{

	final static int MAPDIM=10; //10 by 10 squares
	final static int IMGDIM=30; //30 by 30 pixels
	final static int IMGMARGIN=5; //5 pixels to display status
	final static int TILEDIM=IMGDIM+IMGMARGIN+2; //total size of cells
	//BufferedImage buzz;
	private int [] mouseLocation={0,0};
	public MeriTile [][] tilemap;
	private Random random;
	private MeriPanel parent;
	
	//level is needed to restrict spawned items and enemies
	public BattleMap (int level, MeriPanel p){
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		random=new Random();
		parent=p;

		genBlankMap();
		
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(getPaneSize()+10,getPaneSize()+10));
	}
	/**
	 * Generates a blank map for display purposes
	 */
	private void genBlankMap(){
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
	public void placeTreasure(int treasure){
		tilemap[4][0].setTerrain(treasure);
	}
	public void removeTreasure(){
		tilemap[4][0].setTerrain(BLANK);
	}
	public void placePotions(int potion){
		tilemap[4][5].setTerrain(potion);
		tilemap[6][5].setTerrain(potion);
	}
	/**
	 * Takes a four integer array as parameter
	 * First two ints are weapons, the other 2 armor
	 * pick one of each
	 * it appears that equips can spawn anywhere from rows 4-8 (numbered from 0)
	 */
	public void placeEquips(int[]equips){
		if (tilemap !=null && equips.length>=4){
			int wchoice=equips[random.nextInt(2)];
			int achoice=equips[random.nextInt(2)+2];
			ArrayList<MeriTile> blanks=new ArrayList<MeriTile>();
			for (int i=0;i<MAPDIM;i++){
				for (int j=4;j<9;j++){
					if (tilemap[i][j].terrain==BLANK){
						blanks.add(tilemap[i][j]);
					}
				}
			}
			Collections.shuffle(blanks);
			blanks.get(0).setTerrain(wchoice);
			blanks.get(1).setTerrain(wchoice);
			blanks.get(3).setTerrain(achoice);
			blanks.get(4).setTerrain(achoice);
		} else {
			System.out.println("Equipment placing error");
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
					tilemap[i][j].setTerrain(BLANK);
				}
			}
		}
	}

	/**
	 * Moves the pet regardless of destination terrain
	 * Need this to make sure pet positions are updated for subsequent pet moves
	 */
	public boolean movePet(MeriPet p,int[]destination){
		if (destination[0]<MAPDIM && destination[1]<MAPDIM){
			tilemap[destination[0]][destination[1]].setTerrain(p.getSpeciesID());
			tilemap[p.getLocation()[0]][p.getLocation()[1]].setTerrain(BLANK);
			p.setLocation(destination);
			return true;
		} else {
			return false;
		}
	}
	//return an array of adjacent tiles
	//behind first, front next, then sides from top to bottom 
	//eight surrounding tiles
	//The goal of this method is to get ordered list of nodes for enemy ai to check
	public ArrayList <MeriTile> getAdj (int x, int y){
		ArrayList <MeriTile> results=new ArrayList<MeriTile>();
		//top
		if (y>0)
			results.add(tilemap[x][y-1]);
		//bottom
		if (y<MAPDIM-1)
			results.add(tilemap[x][y+1]);
		//top left and below
		ArrayList<MeriTile>left=new ArrayList<MeriTile>();
		ArrayList<MeriTile>right=new ArrayList<MeriTile>();
		if (x>0){
			left.add(tilemap[x-1][y]);
			if (y>0)
				left.add(tilemap[x-1][y-1]);
			if (y<MAPDIM-1)
				left.add(tilemap[x-1][y+1]);
		}
		if (x<MAPDIM-1){
			right.add(tilemap[x+1][y]);
			if (y>0)
				right.add(tilemap[x+1][y-1]);
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
	 * returns 3 below only
	 * emptylist means bottom row
	 */
	public ArrayList <MeriTile> getAdjBelow (int x, int y){
		ArrayList <MeriTile> results=new ArrayList<MeriTile>();
		if (y<MAPDIM-1){
			//bottom
			results.add(tilemap[x][y+1]);
			//top left and below
			ArrayList<MeriTile>left=new ArrayList<MeriTile>();
			ArrayList<MeriTile>right=new ArrayList<MeriTile>();
			if (x>0){
				left.add(tilemap[x-1][y+1]);
			}
			if (x<MAPDIM-1){
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
		}
		return results;
	}
	/**
	 * returns 3 above only
	 * empty list means top row
	 */
	public ArrayList <MeriTile> getAdjAbove (int x, int y){
		ArrayList <MeriTile> results=new ArrayList<MeriTile>();
		//bottom
		if (y>0){
			results.add(tilemap[x][y-1]);
			//top left and below
			ArrayList<MeriTile>left=new ArrayList<MeriTile>();
			ArrayList<MeriTile>right=new ArrayList<MeriTile>();
			if (x>0){
				left.add(tilemap[x-1][y-1]);
			}
			if (x<MAPDIM-1){
				right.add(tilemap[x+1][y-1]);
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
		}
		return results;
	}
	/**
	 * Return a list of coords of adjacent villages
	 */
	public ArrayList<int[]> findAdjVillage(int x,int y){
		ArrayList <MeriTile> adjlist=getAdj(x,y);
		ArrayList<int[]>vlocs=new ArrayList<int[]>();
		for (int i=0;i<adjlist.size();i++){
			if (tilemap[adjlist.get(i).getGridx()][adjlist.get(i).getGridy()].terrain==VILLAGE){
				vlocs.add(new int[]{adjlist.get(i).getGridx(),adjlist.get(i).getGridy()});
				break;
			}
		}
		return vlocs;
	}
	/**
	 * Given a foe's location, searches for the first ally to be prioritized for attack
	 */
	public ArrayList<MeriPet> findNextFoeTarget(int x, int y){
		ArrayList<MeriPet> targets=new ArrayList<MeriPet>();
		ArrayList <MeriTile> adjlist=getAdj(x,y);
		for (int i=0;i<adjlist.size();i++){
			for (int j=0;j<parent.ally.size();j++){
				if (Arrays.equals(parent.ally.get(j).getLocation(),new int[]{adjlist.get(i).getGridx(),adjlist.get(i).getGridy()})){
					targets.add(parent.ally.get(j));
					break;
				}
			}
		}
		return targets;
	}
	/**
	 * Given a foe's location, searches for an ally on same line to attack
	 */
	public ArrayList<MeriPet> findHoriFoeTarget(int x, int y){
		ArrayList<MeriPet> targets=new ArrayList<MeriPet>();
		ArrayList <MeriTile> adjlist=new ArrayList <MeriTile>();
		if (x>=2){
			adjlist.add(tilemap[x-2][y]);
		}
		if (x<=7){
			adjlist.add(tilemap[x+2][y]);
		}
		for (int i=0;i<adjlist.size();i++){
			for (int j=0;j<parent.ally.size();j++){
				if (Arrays.equals(parent.ally.get(j).getLocation(),new int[]{adjlist.get(i).getGridx(),adjlist.get(i).getGridy()})){
					targets.add(parent.ally.get(j));
					break;
				}
			}
		}
		return targets;
	}
	/**
	 * return the most optimal coordinate to traverse to get to a goal
	 * middle down top
	 * middle left right
	 */
	public ArrayList <int[]> findOneTilePath(int[]start,int[]destination){
		ArrayList <MeriTile> startadj=getAdj(start[0],start[1]);
		ArrayList <int[]> destadj=new ArrayList <int[]> ();
		for (int i=0;i<startadj.size();i++){
			MeriTile mt=startadj.get(i);
			if (mt.checkPassable() && checkAdj(mt,destination)){
				destadj.add(new int[]{mt.getGridx(),mt.getGridy()});
			}
		}
//		if (destadj.size()>0){
//			System.out.println(destadj.get(0)[0]+","+destadj.get(0)[1]);			
//		}
		return destadj;
	}
	/**
	 * The "NearestColumnMultiplier tells an invader whether to go left or right
	 * to get to a column with a village in it
	 * It is intended to be multiplied by 2 and passed to findOneTilePath
	 * -1 goes left, 1 goes right
	 * 
	 * searches in both directions from the passed x ooordinate until it finds a village
	 */
	public int getNearestColumnMultiplier(int x){
		int direction=0;
		for (int i=1;i<MAPDIM;i++){
			if (x+i<MAPDIM && checkVillageInColumn(x+i)){
				direction=1;
				break;
			}
			if (x-i>=0 && checkVillageInColumn(x-i)){
				direction=-1;
				break;				
			}
		}
		return direction;
	}
	/**
	 * checks current column and two adjacent for a village
	 * return -1 if no village, 1 if left, 2 if mid, 3 if right
	 * returns middle then left then right village if multiple in column
	 */
	public int checkVillageInAdjColumn(int[]start){
		int found=-1;
		for (int i=0;i<tilemap.length-start[1];i++){
			if (tilemap[start[0]][start[1]+i].terrain==VILLAGE){
				found=2;
				break;
			}
			if (start[0]>1 && tilemap[start[0]-1][start[1]+i].terrain==VILLAGE){
				found=1;
				break;
			}
			if (start[0]<MAPDIM-1 && tilemap[start[0]+1][start[1]+i].terrain==VILLAGE){
				found=3;
				break;
			}
		}
		return found;
	}
	/**
	 * Based off the prior algorithm.
	 * This is inefficiently designed because I wrote this second,
	 * so the prior method does not incorporate its functionality - yet
	 * Only checks tiles 6-8 in the current column for villages
	 */
	public boolean checkVillageInColumn(int column){
		boolean found=false;
		for (int i=0;i<3;i++){
			if (tilemap[column][6+i].terrain==VILLAGE){
				found=true;
				break;
			}
		}
		return found;
	}
	/**
	 * return true if the passed MeriTile is adj to the reference coordinates
	 */
	public boolean checkAdj(MeriTile mt, int[]ref){
		return getAdj(ref[0],ref[1]).contains(mt);
	}
	/**
	 * Checks all tiles within two range of the passed coordinates
	 * to see whether they contain the passed MeriTile
	 */
	public boolean checkTwoRange(MeriTile mt, int[]ref){
		return Math.abs(ref[0]-mt.getGridx())<=2 && Math.abs(ref[1]-mt.getGridy())<=2;
	}
	/**
	 * 
	 * @param clicked: a tile to check
	 * @return true if the passed coordinate is next to the selected meripet in parent
	 */
	public boolean checkSelectedAdj(int[]clicked){
		boolean isadj=false;
		if (parent.selected!=null){
			if (Math.abs(clicked[0]-parent.selected.getLocation()[0])<=1
					&& Math.abs(clicked[1]-parent.selected.getLocation()[1])<=1){
				isadj=true;
			}
		} 
		return isadj;
	}
	/**
	 * set the terrain of a specific tile in the tilemap
	 * mainly used to turn tiles blank or to craters
	 * @param location
	 */
	public void setTerrain(int[]location,int tileID){
		tilemap[location[0]][location[1]].setTerrain(tileID);
	}
	public int getPaneSize(){
		return MAPDIM*MeriTile.TILESIZE;
	}
	public void paintComponent (Graphics g){
		super.paintComponent(g);
		setSize(getPaneSize(),getPaneSize());
		for (int i=0;i<MAPDIM;i++){
			for (int j=0;j<MAPDIM;j++){
				if (isAllyPetTerrain(tilemap[i][j].terrain)){
					//if the terrain is of an ally, find the ally, check if it
					//has exhausted its moves, and then depict appropriately
					for (int k=0;k<parent.ally.size();k++){
						if (Arrays.equals(parent.ally.get(k).getLocation(),new int[]{i,j}) &&
								!parent.ally.get(k).hasMove()){
							tilemap[i][j].drawMoveDepleted(g);
							break;
						}
					}
				}
				tilemap[i][j].draw(g);
			}
		}
		if (parent.checkGameWon()){
			g.setColor(Color.black);
			g.setFont(new Font("Castellar", Font.BOLD, 60));
			g.drawString("VICTORY!", IMGDIM, IMGDIM*MAPDIM/2);
			g.setFont(new Font("Castellar", Font.BOLD, 20));
			g.drawString("Click the map to continue...", IMGDIM/2, IMGDIM*MAPDIM*3/4);
		} else if (parent.checkGameLost()){
			g.setColor(Color.black);
			g.setFont(new Font("Castellar", Font.BOLD, 60));
			g.drawString("DEFEAT...", IMGDIM, IMGDIM*MAPDIM/2);
			g.setFont(new Font("Castellar", Font.BOLD, 20));
			g.drawString("Why not restart from the menu?", IMGDIM/2, IMGDIM*MAPDIM*3/4);
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
		if (parent.checkGameWon()){
			/*
			 * If the game is won, any click ends the game
			 */
			parent.resolveGame();
		} else if (!parent.checkGameLost()){
			/*
			 * If the game is lost, do nothing
			 * 
			 * Otherwise, execute player interaction:
			 */
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
//			System.out.println(mx+" "+my+"/"+xc+" "+yc);
			//if no target selected, try to reference parent coordinates to find a pet
			if (parent.selected==null){
				for (int i=0;i<parent.ally.size();i++){
					if (Arrays.equals(parent.ally.get(i).getLocation(),new int[]{xc,yc}) 
							&& parent.ally.get(i).hasMove()){
						parent.setSelected(parent.ally.get(i));
					}
				}
			}
			//if the pet can act, check to see what actions are appropriate
			else if(parent.movesLeft>0){
				//teleport to blank squares only
				if (parent.selected.canTeleport() && tilemap[xc][yc].terrain==BLANK && yc > 2){
					parent.selected.teleportOnce();
					parent.selected.setLocation(xc, yc);
					parent.resolvePlayerMove();
				}
				//check for the healing case
				else if (parent.selected.canHeal() && isAllyPetTerrain(tilemap[xc][yc].terrain)){
					for (int i=0;i<parent.ally.size();i++){
						if (parent.ally.get(i) != parent.selected && Arrays.equals(parent.ally.get(i).getLocation(),new int[]{xc,yc}) && parent.ally.get(i).dmg>0){
							parent.updateBattleLog(MeriPet.heal(parent.selected, parent.ally.get(i)));
							//if (selected is not a skeith with berserk axe)
							parent.resolvePlayerMove();
							break;
						}
					}
				} //check for the ranged attack case
				else if (parent.selected.canRangeAttack() && 
						isFoePetTerrain(tilemap[xc][yc].terrain) &&
						checkTwoRange(tilemap[xc][yc],parent.selected.getLocation())){
					for (int i=0;i<parent.foe.size();i++){
						if (Arrays.equals(parent.foe.get(i).getLocation(),new int[]{xc,yc})){
							parent.updateBattleLog(MeriPet.attack(parent.selected, parent.foe.get(i)));
							parent.resolvePlayerMove();
							break;
						}
					}
				} //standard actions
				else if (checkSelectedAdj(new int[]{xc,yc})){
					if (isFoePetTerrain(tilemap[xc][yc].terrain)){
						//try to attack a foe
						for (int i=0;i<parent.foe.size();i++){
							if (Arrays.equals(parent.foe.get(i).getLocation(),new int[]{xc,yc})){
								parent.updateBattleLog(MeriPet.attack(parent.selected, parent.foe.get(i)));
								parent.resolvePlayerMove();
								break;
							}
						}
					}
					//the following actions are only valid if the pet can still move
					else if (!parent.selected.movesealed){
						//try to move into a blank tile if tile is valid
						if (tilemap[xc][yc].terrain==BLANK){
							parent.selected.setLocation(xc, yc);
							parent.resolvePlayerMove();
						}
						//chug a potion
						else if (isPotion(tilemap[xc][yc].terrain)){
							parent.selected.setLocation(xc, yc);
							if (parent.selected.dmg>0){
								parent.selected.heal(random.nextInt(15)+10);
								parent.updateBattleLog(parent.selected.name+" drinks a potion. Its health is now "+
										parent.selected.getCurrHealth()+" points!");
							} else {
								parent.updateBattleLog(parent.selected.name+" drinks a potion. But it was already at full health!");
							}
							parent.resolvePlayerMove();
						}
						//pick up weapon
						else if (isWeapon(tilemap[xc][yc].terrain)){
							if (parent.selected.weapon>-1){
								tilemap[parent.selected.getLocation()[0]][parent.selected.getLocation()[1]].setTerrain(parent.selected.weapon);					
							}
							parent.updateBattleLog(parent.selected.name+" equips a "+MConst.equipMap.get(tilemap[xc][yc].terrain).name+"!");
							parent.selected.setWeapon(tilemap[xc][yc].terrain);
							parent.selected.setLocation(xc, yc);
							parent.resolvePlayerMove();
							//pick up armor
						} else if (isArmor(tilemap[xc][yc].terrain)){
							if (parent.selected.armor>-1){
								tilemap[parent.selected.getLocation()[0]][parent.selected.getLocation()[1]].setTerrain(parent.selected.armor);					
							}
							parent.updateBattleLog(parent.selected.name+" equips a "+MConst.equipMap.get(tilemap[xc][yc].terrain).name+"!");
							parent.selected.setArmor(tilemap[xc][yc].terrain);
							parent.selected.setLocation(xc, yc);
							parent.resolvePlayerMove();
						} else if (isTreasure(tilemap[xc][yc].terrain)){
							for (int i=0;i<parent.ally.size();i++){
								parent.ally.get(i).gainStats(parent.campaign.getWave());
							}
							parent.campaign.treasureCollected=true;
							parent.selected.setLocation(xc, yc);
							parent.resolvePlayerMove();
						}
						//additional cases go here 
						else {
							//if ally pet, resolve special abilities: healing/disenchantments
							parent.setSelected(null);
						}
					} else {
						//if ally pet, resolve special abilities: healing/disenchantments
						parent.setSelected(null);
					}
			}

			}
			updatePetLocations(parent.getPetLocations());
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseMoved(MouseEvent e) {
		mouseLocation[0]=e.getX();
		mouseLocation[1]=e.getY();
		if (mouseLocation[0]<getPaneSize() && mouseLocation[1]<getPaneSize()){
			int xc=mouseLocation[0]/MeriTile.TILESIZE;
			int yc=mouseLocation[1]/MeriTile.TILESIZE;
			boolean found=false;
			for (int i=0;i<parent.getPetLocations().size();i++){
				if (Arrays.equals(parent.getPetLocations().get(i).getLocation(),new int[]{xc,yc})){
					setToolTipText(parent.getPetLocations().get(i).name+" HP:"+parent.getPetLocations().get(i).getDmgNHealth());
					found=true;
					break;
				}
			}
			if (isWeapon(tilemap[xc][yc].terrain) || isArmor (tilemap[xc][yc].terrain)){
				setToolTipText(getEquipToolTipStats(tilemap[xc][yc].terrain));
				found=true;
			}
			if (!found){
				setToolTipText(null);
			}
		} else {
			setToolTipText(null);
		}
	}
}
