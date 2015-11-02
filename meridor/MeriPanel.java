package meridor;
import java.awt.Graphics;

import javax.swing.*;



public class MeriPanel extends JPanel {

	
	BattleMap bm;
	
	public MeriPanel (){
		bm=new BattleMap(0);
		setSize(1500,1000);
		add(bm);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
	}
	
	public void toggleActive (){
		bm.setVisible(!bm.isVisible());
	}
}
