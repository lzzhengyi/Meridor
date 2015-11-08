package meridor;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;



public class MeriPanel extends JPanel implements MouseListener{

	final static int MAX_MOVES=5;
	final static int MAPDIM=10; //10 by 10 squares
	final static int IMGDIM=30; //30 by 30 pixels
	final static int IMGMARGIN=5; //5 pixels to display status
	final static int TILEDIM=IMGDIM+IMGMARGIN+2; //total size of cells
	public MeriTile [][] tilemap;
	public Random random;
	public ArrayList<MeriPet> ally,foe;
	//public boolean playerturn; //what is this even for??? autoresolve!!!
	public int movesLeft;
	public MeriPet selected;
	
	BattleMap bm;
	UnitDisplay um;
	InfoDisplay im;
	
	public MeriPanel (){
		random=new Random();
		movesLeft=MAX_MOVES;
		selected=null;
		
		ally=new ArrayList<MeriPet>();
		foe=new ArrayList<MeriPet>();
		
		//temporary debug generation of pets
		for (int i=0;i<4;i++){
			ally.add(new MeriPet(
					"Soldier"+i,
					random.nextInt(5),
					0));
			ally.get(i).setLocation(i, 9);
			
			foe.add(new MeriPet(
					"invader"+i,
					random.nextInt(7)+5,
					0));
			foe.get(foe.size()-1).setLocation(i, 0);
			
			foe.add(new MeriPet(
					"invader"+i,
					random.nextInt(7)+5,
					0));
			foe.get(foe.size()-1).setLocation(9-i, 0);
		}
		
		addMouseListener(this);
		/*
		 * Currently only three panels implemented
		 * I am planning to add a title bar as well
		 */
		setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.weightx=0.5;
		c.fill=GridBagConstraints.BOTH;
		
		bm=new BattleMap(0,this);
		c.gridx=0;
		c.gridy=0;
		c.gridheight=GridBagConstraints.RELATIVE;
		add(bm,c);
		
		um=new UnitDisplay();
		c.gridx=1;
		c.gridy=0;
		c.gridheight=GridBagConstraints.REMAINDER;
		add (um,c);
		
		im=new InfoDisplay();
		c.gridx=0;
		c.gridy=1;
		c.gridheight=1;
		add(im,c);
	}
	/**
	 * Duplicate of a method in battlemap: returns ordered list of tiles
	 * for foe AI to check
	 * this method may be deprecate in the future
	 */
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
	 * Use this to turn off the battlemap/unit list
	 * and display the hidden panel: the unit select panel
	 */
	public void toggleActive (){
		bm.setVisible(!bm.isVisible());
		um.setVisible(!um.isVisible());
		//unitselect here
	}
	public ArrayList<MeriPet> getPetLocations(){
		ArrayList <MeriPet> combined=new ArrayList<MeriPet>();
		combined.addAll(ally);
		combined.addAll(foe);
		return combined;
	}
	private class UnitDisplay extends JPanel {
		
		JTable allytable, foetable;
		
		final private String[] ALLYCOLS={
			"","Name/Rank","Health","Attack","","Defence","","Saves"	
			};
		final private String[] FOECOLS={
				"","Name","Health","Attack","","Defence","",""	
			};
		//Object[][] allydata,foedata;
		
		private UnitDisplay(){
			updateAllyData();
			updateFoeData();
			JScrollPane jspA=new JScrollPane(allytable);
			JScrollPane jspF=new JScrollPane(foetable);
			add(jspA);
			add(jspF);
			allytable.setFillsViewportHeight(true);
			foetable.setFillsViewportHeight(true);
			
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setPreferredSize(new Dimension(400,400));
		}
		/**
		 * Updates every tick and on init
		 * may need to adjust weapon name length
		 */
		public void updateAllyData(){
			Object[][]allies=new Object[ally.size()][];
			for (int i=0;i<ally.size();i++){
				Object[]mstats=new Object[8];
				mstats[0]=ally.get(i).getSpeciesName();
				mstats[1]=ally.get(i).getNameNRank();
				mstats[2]=ally.get(i).getDmgNHealth();
				mstats[3]=ally.get(i).getAttackString();
				mstats[4]=ally.get(i).getWeaponName();
				mstats[5]=ally.get(i).getDefenseString();
				mstats[6]=ally.get(i).getArmorName();
				mstats[7]=ally.get(i).getSavesString();
				allies[i]=mstats;
			}
			//allydata=allies;
			allytable=new JTable(allies,ALLYCOLS);
			//TableColumn column;
			allytable.getColumnModel().getColumn(0).setPreferredWidth(120);
			allytable.getColumnModel().getColumn(1).setPreferredWidth(200);			
		}
		/**
		 * Updates every tick and on init
		 * may need to adjust weapon name length
		 */
		public void updateFoeData(){
			Object[][]foes=new Object[foe.size()][];
			for (int i=0;i<foe.size();i++){
				Object[]mstats=new Object[8];
				mstats[0]=foe.get(i).getSpeciesName();
				mstats[1]=foe.get(i).name;
				mstats[2]=foe.get(i).getCurrHealth();
				mstats[3]=foe.get(i).getAttackString();
				mstats[4]="";
				mstats[5]=foe.get(i).getDefenseString();
				mstats[6]="";
				mstats[7]="";
				foes[i]=mstats;
			}
			foetable=new JTable(foes,FOECOLS);
			foetable.getColumnModel().getColumn(0).setPreferredWidth(120);
			foetable.getColumnModel().getColumn(1).setPreferredWidth(200);
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			updateAllyData();
			updateFoeData();
		}
	}
	
	private class InfoDisplay extends JPanel{
		
		/**
		 *I want to dynamically display the items present on the map
		 *Some other dynamic behavior can be placed here, including current stage
		 */
		private InfoDisplay(){
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			JLabel iteminfolabel=new JLabel("Click for item info:");
			JLabel maxmoveslabel=new JLabel("Maximum Moves Total/Turn:"+MAX_MOVES);
			JLabel petmaxlabel=new JLabel("Maximum Moves Total/Pet:");
			
			add(iteminfolabel);
			add(maxmoveslabel);
			add(petmaxlabel);
		}
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
		System.out.println("pressed");
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
