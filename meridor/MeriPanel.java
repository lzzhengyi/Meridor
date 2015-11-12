package meridor;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;



public class MeriPanel extends JPanel implements MouseListener{

	final static int MAX_MOVES=5;
	final static int MAX_LOGTEXT=5;
	final static int MAX_VILLAGES=6;
	final static int MAPDIM=10; //10 by 10 squares
	final static int IMGDIM=30; //30 by 30 pixels
	final static int IMGMARGIN=5; //5 pixels to display status
	final static int TILEDIM=IMGDIM+IMGMARGIN+2; //total size of cells
	public MeriTile [][] tilemap;
	public Random random;
	public ArrayList<MeriPet> ally,foe;
	//public boolean playerturn; //what is this even for??? autoresolve!!!
	public int movesLeft;
	public int villagesLeft;
	public MeriPet selected;
	
	ArrayList<String> battlelogtext=new ArrayList<String>();
	
	BattleMap bm;
	UnitDisplay um;
	InfoDisplay im;
	SelectParty sp;
	BattleLog bl;
	
	boolean battlemode=true;
	
	public MeriPanel (){
		random=new Random();
		movesLeft=MAX_MOVES;
		villagesLeft=MAX_VILLAGES;
		selected=null;
		
		ally=new ArrayList<MeriPet>();
		foe=new ArrayList<MeriPet>();
		
		//temporary debug generation of pets
		for (int i=0;i<5;i++){
			ally.add(new MeriPet(
					"Soldier"+i,
					random.nextInt(5),
					0));
			
			foe.add(new MeriPet(
					"invader"+i,
					random.nextInt(7)+5,
					0));
			foe.add(new MeriPet(
					"invader"+i,
					random.nextInt(7)+5,
					0));
		}
		placePets();
		
		addMouseListener(this);
		/*
		 * Currently only three panels implemented
		 * I am planning to add a title bar as well
		 * Also a text log
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
		
		im=new InfoDisplay();
		c.gridx=0;
		c.gridy=1;
		c.gridheight=1;
		add(im,c);
		
		bl=new BattleLog();
		c.gridx=1;
		c.gridy=1;
		c.gridheight=GridBagConstraints.REMAINDER;
		add(bl,c);
		
		um=new UnitDisplay();
		c.gridx=1;
		c.gridy=0;
		c.gridheight=GridBagConstraints.RELATIVE;
		add (um,c);
		
//		sp=new SelectParty();
//		c.gridx=0;
//		c.gridy=0;
//		c.gridheight=GridBagConstraints.RELATIVE;
//		sp.setVisible(false);
//		add(sp,c);
	}
	public void updateBattleLog(String change){
		if (battlelogtext.size()>=5){
			battlelogtext.remove(0);
		}
		battlelogtext.add(change);
		bl.updateText();
	}
	/**
	 * Attempt to refresh battlemap and table display
	 * Called from child panels to update parent (this class)
	 * only for
	 */
	public void update(){
		//most panels depend on the battlemap, so I use it as the dependency check
		if (bm!=null){
			bm.revalidate();
			bm.repaint();
			
			/*
			 * this is ridiculously hacky and could be avoided if I just
			 * wrote a custom tablemodel 
			 */
			remove(um);
			GridBagConstraints c=new GridBagConstraints();
			c.weightx=0.5;
			c.fill=GridBagConstraints.BOTH;
			c.gridx=1;
			c.gridy=0;
			c.gridheight=GridBagConstraints.RELATIVE;
			um=new UnitDisplay();
			add (um,c);
			um.revalidate();
			um.repaint();
			
			im.updateMovesLeft();
			im.revalidate();
			im.repaint();
		}
		
		//the character select panel which only displays when the others are gone
		if (sp!=null){
			sp.revalidate();
			sp.repaint();
		}
	}
	/**
	 * The locations where units are placed is hardcoded based on the original game
	 */
	public void placePets(){
		int[][]allylocs={
			new int []{0,9},
			new int []{2,9},
			new int []{4,9},
			new int []{6,9},
			new int []{8,9}
		};
		int[][]foelocs={
				new int []{1,0},
				new int []{3,0},
				new int []{5,0},
				new int []{7,0},
				new int []{9,0},
				new int []{2,2},
				new int []{4,2},
				new int []{6,2},
				new int []{8,2},
				new int []{0,2}
			};
		for (int i=0;i<ally.size();i++){
			ally.get(i).setLocation(allylocs[i]);
		}
		for (int i=0;i<foe.size();i++){
			foe.get(i).setLocation(foelocs[i]);
		}
		revalidate();
		repaint();
	}
	/**
	 * Set the selected unit for player movement, and update the display
	 */
	public void setSelected (MeriPet m){
		selected=m;
		im.updateDeselect();
	}
	/**
	 * Implementation to do whatever necessary to destroy a village
	 * May change later
	 */
	public void razeVillage(){
		villagesLeft--;
		im.updateVillagesLeft();
		System.out.println("village razed!");
	}
	/**
	 * Resets the movement for the allied units at beginning of turn
	 */
	public void refreshAllyMove(){
		movesLeft=MAX_MOVES;
		im.updateMovesLeft();
		for (int i=0;i<ally.size();i++){
			ally.get(i).refreshMove();
		}
	}
	/**
	 * Resets all attrited stats for allied units between fights
	 */
	public void refreshAllyAll(){
		for (int i=0;i<ally.size();i++){
			ally.get(i).refreshMove();
			ally.get(i).refreshHP();
		}
	}
	/**
	 * Process 1 move per foe, and then give turn back to the player
	 * Not sure if this is final
	 * Priority is:
	 * 1st: player units must be attacked
	 * 2nd: villages that can be destroyed
	 * 3rd: move down/to nearest village
	 */
	public void processFoeTurn(){
		for (int i=0;i<foe.size();i++){
			MeriPet chosen=foe.get(i);
			
			//highest priority: destroy village
			//this implementation is redundant. I duplicate the search of adj tiles
			ArrayList<int[]> nearvillages=bm.findAdjVillage(chosen.getLocation()[0], chosen.getLocation()[1]);
			if (!nearvillages.isEmpty()){
				int[]vloc=nearvillages.get(0);
				//bm.razeVillage(vloc[0], vloc[1]); //not needed
				bm.movePet(chosen, vloc);
				razeVillage();
			} else {
				ArrayList<MeriPet> targets=bm.findNextFoeTarget(chosen.getLocation()[0], chosen.getLocation()[1]);
				ArrayList<MeriPet> horitargets=bm.findHoriFoeTarget(chosen.getLocation()[0], chosen.getLocation()[1]);
				//second priority action: kill npets 
				if (!targets.isEmpty()){
					MeriPet target=targets.get(0);
					updateBattleLog(MeriPet.attack(chosen, target));
				} else if (!horitargets.isEmpty()) { //move towards nearby pets
					bm.movePet(chosen, bm.findOneTilePath(chosen.getLocation(), horitargets.get(0).getLocation()).get(0));
					//remember onetilepath returns a list of potential paths so I take the first one
				} else {
					//third highest priority:
					//if there is a village in the 3 rows centered on foe
					//move down, prioritizing rows with villages in them
					int seekvillage=bm.checkVillageInColumn(chosen.getLocation());
					if (seekvillage>0){
						int [] idealmoves=null;
						if (seekvillage==1){
							idealmoves=new int[]{chosen.getLocation()[0]-1,chosen.getLocation()[1]+2};
						} else if (seekvillage==2){
							idealmoves=new int[]{chosen.getLocation()[0],chosen.getLocation()[1]+2};
						} else if (seekvillage==3){
							idealmoves=new int[]{chosen.getLocation()[0],chosen.getLocation()[1]+2};

						} else {
							idealmoves=new int[]{};
							System.out.println("village search error");
						}
						if (idealmoves.length>0){
//							System.out.println(chosen.getLocation()[0]+","+chosen.getLocation()[1]+" d:"+idealmoves[0]+","+idealmoves[1]);
							ArrayList<int[]>path=bm.findOneTilePath(chosen.getLocation(), idealmoves);
							if (path.size()>0){
								bm.movePet(chosen, path.get(0));
//								System.out.println(chosen.getLocation()[0]+","+chosen.getLocation()[1]+" d:"+idealmoves[0]+","+idealmoves[1]);								
							}
						} else {
							//naive movedown
							ArrayList<MeriTile>below=bm.getAdjBelow(chosen.getLocation()[0], chosen.getLocation()[1]);
							for (int j=0;j<below.size();j++){
								if (below.get(i).checkPassable()){
									bm.movePet(chosen, new int[]{below.get(i).getGridx(),below.get(i).getGridy()});
									break;
								}
							}
						}
					} else {
						System.out.println("failed"+seekvillage+"/"+chosen.getLocation()[0]+","+chosen.getLocation()[1]);
						//final priority: greedy move to column with nearest village?
					}
				}
			}
		}
		System.out.println("terminated");
	}
	/**
	 * call this after a move is registered in the battlemap
	 * decrements the movesleft (unless special move is made)
	 * decrements selected's moves (unless special move is made)
	 */
	public void resolvePlayerMove (){
		selected.moveOnce();//CHECK FOR SPECIAL CASES
		setSelected(null);
		reduceMoves(-1);
		update();
	}
	/**
	 * Duplicate of a method in battlemap: returns ordered list of tiles
	 * for foe AI to check
	 * this method may be deprecate in the future
	 */
//	public ArrayList <MeriTile> getAdj (int x, int y){
//		ArrayList <MeriTile> results=new ArrayList<MeriTile>();
//		//top
//		if (y>=0)
//			results.add(tilemap[x][y-1]);
//		//bottom
//		if (y<MAPDIM-1)
//			results.add(tilemap[x][y+1]);
//		//top left and below
//		ArrayList<MeriTile>left=new ArrayList<MeriTile>();
//		ArrayList<MeriTile>right=new ArrayList<MeriTile>();
//		if (x>=0){
//			if (y>=0)
//				left.add(tilemap[x-1][y-1]);
//			left.add(tilemap[x-1][y]);
//			if (y<MAPDIM-1)
//				left.add(tilemap[x-1][y+1]);
//		}
//		if (x<MAPDIM-1){
//			if (y>=0)
//				right.add(tilemap[x+1][y-1]);
//			right.add(tilemap[x+1][y]);
//			if (y<MAPDIM-1)
//				right.add(tilemap[x+1][y+1]);
//		}
//		if (random.nextDouble()<0.7){
//			//add left first
//			results.addAll(left);
//			results.addAll(right);
//		} else {
//			//add right first
//			results.addAll(right);
//			results.addAll(left);
//		}
//		return results;
//	}
	/**
	 * When altering movesLeft, makes sure illegal values are not saved
	 */
	public void reduceMoves(int change){
		movesLeft+=change;
		if (movesLeft<0 || movesLeft>MAX_MOVES){
			if (movesLeft<0){
				movesLeft=0;
			} else {
				movesLeft=MAX_MOVES;
			}
		}
	}
	/**
	 * Use this to turn off the battlemap/unit list
	 * and display the hidden panel: the unit select panel
	 */
	public void toggleActive (){
//		bm.setVisible(!bm.isVisible());
//		um.setVisible(!um.isVisible());
//		sp.setVisible(!sp.isVisible());
//
//		if (um.isVisible()){
//			um.updateAllyData();
//			um.updateFoeData();
//		}
//		if (sp.isVisible()){
//			sp.updateStatData();
//		}
		GridBagConstraints c=new GridBagConstraints();
		c.weightx=0.5;
		c.fill=GridBagConstraints.BOTH;
		
		if (battlemode){
			battlemode=false;
			remove(bm);
			remove(um);
			remove(im);
			
			sp=new SelectParty();
			c.gridx=0;
			c.gridy=0;
			c.gridheight=GridBagConstraints.RELATIVE;
			add(sp,c);
		} else {
			battlemode=true;
			if (sp!=null)
				remove(sp);
			
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
		revalidate();
		repaint();
	}
	/**
	 * Intended to return combined list of allies and foes
	 */
	public ArrayList<MeriPet> getPetLocations(){
		ArrayList <MeriPet> combined=new ArrayList<MeriPet>();
		combined.addAll(ally);
		combined.addAll(foe);
		return combined;
	}
	/**
	 * The table that displays unit stats during battle
	 * hide when out of battle
	 */
	private class UnitDisplay extends JPanel {
		
		JTable allytable, foetable;
		
		final private String[] ALLYCOLS={
			"","Name/Rank","Health","Attack","","Defence","","Saves"	
			};
		final private String[] FOECOLS={
				"","Name","Health","Attack","","Defence","",""	
			};
		
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
			allytable=new JTable(allies,ALLYCOLS);
			allytable.getColumnModel().getColumn(0).setPreferredWidth(120);
			allytable.getColumnModel().getColumn(1).setPreferredWidth(200);
			allytable.setEnabled(false); //not ideal, fix later
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
			foetable.setEnabled(false);
		}
		/**
		 * Doesn't work, I need to figure out how to incorporate this into tablemodel
		 */
		public void setEditableFalse(){
			DefaultTableModel tablemodel=new DefaultTableModel(){
				
				@Override
				public boolean isCellEditable(int row, int column){
					return column==1;
				}
			};
			allytable.setModel(tablemodel);
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			updateAllyData();
			updateFoeData();
		}
	}
	/**
	 * For editing the team out of battle
	 * 2 jtables side by side?
	 * 
	 * take info in the ally array and split it out
	 * two tables, one for name/select, one for stats
	 */
	private class SelectParty extends JPanel implements ActionListener {
		
		final private String[] SELECTCOLS={
				"Name","Selected"	
				};
		final private String[] STATCOLS={
				"","Rank","Health","Attack","","Defence","","Saves"	
				};
		
		JButton confirmteam;
		JTable selector,statdisplayer;
		Object[][]allyname;
		
		private SelectParty (){
			JPanel toppanel=new JPanel();
			
			updateStatData();
			JScrollPane jspA=new JScrollPane(selector);
			JScrollPane jspF=new JScrollPane(statdisplayer);
			toppanel.add(jspA);
			toppanel.add(jspF);
			selector.setFillsViewportHeight(true);
			statdisplayer.setFillsViewportHeight(true);
			
			toppanel.setLayout(new BoxLayout(toppanel, BoxLayout.X_AXIS));
			
			confirmteam=new JButton("Confirm Team Selection");
			confirmteam.addActionListener(this);

			add (toppanel);
			add (confirmteam);
			
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setPreferredSize(new Dimension(800,200));
		}
		/**
		 * Updates every tick and on init
		 * may need to adjust weapon name length
		 */
		public void updateStatData(){
			allyname=new Object[ally.size()][];
			Object[][]allystats=new Object[ally.size()][];
			for (int i=0;i<ally.size();i++){
				Object[]mname=new Object[2];
				mname[0]=ally.get(i).name;
				mname[1]=new Boolean(false);
				Object[]mstats=new Object[8];
				mstats[0]=ally.get(i).getSpeciesName();
				mstats[1]=ally.get(i).getRank();
				mstats[2]=ally.get(i).getDmgNHealth();
				mstats[3]=ally.get(i).getAttackString();
				mstats[4]=ally.get(i).getWeaponName();
				mstats[5]=ally.get(i).getDefenseString();
				mstats[6]=ally.get(i).getArmorName();
				mstats[7]=ally.get(i).getSavesString();
				allyname[i]=mname;
				allystats[i]=mstats;
			}
			
			DefaultTableModel tablemodel=new DefaultTableModel(){
				private String[] columnNames=SELECTCOLS;
				private Object[][]data=allyname;
				
				public int getColumnCount(){
					if (columnNames !=null)
						return columnNames.length;
					else
						return 0;
				}
				public int getRowCount(){
					if (data !=null)
						return data.length;
					else
						return 0;
				}
				public Object getValueAt(int row, int col){
					try{
						return data[row][col];						
					} catch (Exception e){
						System.out.println("GetvalueError: datasize("+data.length+
								") row"+row+",col"+col);
						return null;
					}
				}
				public void setValueAt (Object value, int row, int col){
					data[row][col]=value;
					allyname[row][col]=value;
					fireTableCellUpdated(row,col);
				}
				
				public Class getColumnClass(int c){
					return getValueAt(0,c).getClass();
				}
				
//				@Override
//				public boolean isCellEditable(int row, int column){
//					return column==1;
//				}
			};
			
			selector = new JTable(tablemodel);
			
			statdisplayer=new JTable(allystats,STATCOLS);
			statdisplayer.getColumnModel().getColumn(0).setPreferredWidth(120);
			statdisplayer.setEnabled(false); //not ideal, fix later
		}
		/**
		 * Checks to make sure only 5 allies are selected (true)
		 */
		public boolean isPartyValid (){
			int count=0;
			for (int i=0;i<allyname.length;i++){
				if ((boolean) allyname[i][1]){
					count++;
				}
			}
			return count==5;
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			updateStatData();
		}
		/*
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * 
		 * enforces the five character limit
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==confirmteam){
				if (!isPartyValid()){
					JOptionPane.showMessageDialog(null, "You can only bring 5 characters to the next level!");
				} else {
					int choice=JOptionPane.showConfirmDialog(null, "Are you sure you want to bring the selected characters to the next level?", "", JOptionPane.YES_NO_OPTION);
					if (choice==JOptionPane.YES_OPTION){
						ArrayList<MeriPet> ally2=new ArrayList<MeriPet>();
						for (int i=0;i<allyname.length;i++){
							if ((boolean) allyname[i][1]){
								ally.get(i).name=(String) allyname[i][0];
								ally2.add(ally.get(i));
							}
						}
						ally=ally2;
					}
					toggleActive();
				}
			}
			
		}
	}
	/**
	 * Displays the results of the latest action
	 */
	private class BattleLog extends JPanel {
		
		JTextArea text;
		
		private BattleLog (){
			setLayout(new BorderLayout());
			text=new JTextArea("");
			text.setEditable(false);
			JScrollPane jsp=new JScrollPane(text);

			text.setPreferredSize(new Dimension(500,100));
			add (jsp,BorderLayout.CENTER);
		}
		private void updateText (){
			String blt="";
			for (int i=0;i<battlelogtext.size();i++){
				blt+=battlelogtext.get(i)+"\n";
			}
			text.setText(blt);
			revalidate();
			repaint();
		}
	}
	
	/**
	 * Displays key information about player stats on screen bottom
	 */
	private class InfoDisplay extends JPanel implements ActionListener{
		
		JLabel movesleftlabel, villagesleftlabel;
		JButton endturnbutton, deselectbutton;
		
		/**
		 *I want to dynamically display the items present on the map
		 *Some other dynamic behavior can be placed here, including current stage
		 */
		private InfoDisplay(){
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			JLabel iteminfolabel=new JLabel("Click for item info:");
			JLabel maxmoveslabel=new JLabel("Maximum Moves Total/Turn:"+MAX_MOVES);
			JLabel petmaxlabel=new JLabel("Maximum Moves Total/Pet:");
			movesleftlabel=new JLabel("Moves Left:"+movesLeft);
			villagesleftlabel=new JLabel("Villages Left:"+villagesLeft);
			endturnbutton=new JButton("End Turn");
			deselectbutton=new JButton("Unselect:");
			
			endturnbutton.addActionListener(this);
			deselectbutton.addActionListener(this);
			
			add(iteminfolabel);
			add(maxmoveslabel);
			add(petmaxlabel);
			add(movesleftlabel);
			add(villagesleftlabel);
			add(endturnbutton);
			add(deselectbutton);
		}
		/**
		 * Call this after each player move (in battlemap, most likely)
		 */
		public void updateMovesLeft(){
			movesleftlabel.setText("Moves Left:"+movesLeft);
		}
		/**
		 * Call this after each player move (in battlemap, most likely)
		 */
		public void updateVillagesLeft(){
			villagesleftlabel.setText("Villages Left:"+villagesLeft);
		}
		public void updateDeselect(){
			if (selected==null){
				deselectbutton.setText("Unselect:");
			} else {
				deselectbutton.setText("Unselect:"+selected.name);
			}
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==endturnbutton){
				int choice=JOptionPane.showConfirmDialog(null, "Are you sure you want to end your turn?", "", JOptionPane.YES_NO_OPTION);
				if (choice==JOptionPane.YES_OPTION){
					processFoeTurn();
					refreshAllyMove();
					updateVillagesLeft();
					bm.revalidate();
					bm.repaint();
				}
			}
			if (e.getSource()==deselectbutton){
				setSelected(null);
			}
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
