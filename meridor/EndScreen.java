package meridor;
import java.awt.*;
import javax.swing.*;


public class EndScreen extends JPanel {

	private static boolean VICTORY=true;
	private static boolean DEFEAT=false;
	/**
	 * needs text, 1 button(restart)
	 */
	JButton nextmiss, restart;
	boolean endtype=DEFEAT;
	
	public EndScreen (){
		setLayout(new BorderLayout());
		
		JPanel buttonpane=new JPanel();
		buttonpane.setLayout(new FlowLayout());
		
		nextmiss=new JButton("Continue");
		restart=new JButton ("Restart");
		
		buttonpane.add(nextmiss);
		buttonpane.add(restart);
		
		add(buttonpane, BorderLayout.SOUTH);
	}
	/**
	 * Changes the victory or defeat displayed on the end screen
	 */
	public void setEndtype(boolean vt){
		endtype=vt;
	}
	public void repaint (Graphics g){
		this.repaint(g);
		//set paint color
		if (endtype==VICTORY){
			g.drawString("Victory!", 20,20);
			restart.setVisible(false);
			nextmiss.setVisible(true);
		} else {
			g.drawString("Game over!", 20,20);
			restart.setVisible(true);
			nextmiss.setVisible(false);
		}

	}
}
