package meridor;
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
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

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
	public ArrayList<MeriPet> ally,foe;
	public boolean playerturn;
	public int movesLeft;
	public MeriPet selected;
	
	//level is needed to restrict spawned items and enemies
	public BattleMap (int level){
		random=new Random();
//		buzz=null;
//		try {
//			buzz=ImageIO.read(new File("Draco_Buzz_large.jpg"));
//		} catch (IOException e){
//			System.out.println("Import failure");
//		}
		tilemap=new MeriTile[MAPDIM][MAPDIM];
		int xoff=0;
		int yoff=0;
		for (int i=0;i<MAPDIM;i++){
			for (int j=0;j<MAPDIM;j++){
				tilemap[i][j]=new MeriTile(i+xoff,j+yoff);
				if (random.nextBoolean()){
					tilemap[i][j].setTerrain(random.nextInt(7));
				}
				yoff+=TILEDIM;
			}
			yoff=0;
			xoff+=TILEDIM;
		}
	}
	//return an array of adjacent tiles
	//behind first, front next, then sides from top to bottom 
	//eight surrounding tiles
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
	
	public void paintComponent (Graphics g){
		super.paintComponent(g);
		setSize(500,500);
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
	//get the coordinate of the player click and attempt to activate a warrior
	//on that tile
	public void mouseClicked(MouseEvent e) {
		//the intent of this method is to have the player only able to click
		//tiles with pets on them
		//if right click, bring up info screen related to the terrain type?
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
