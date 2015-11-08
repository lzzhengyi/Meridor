package meridor;
import java.awt.FlowLayout;

import javax.swing.JFrame;

public class MeriDriver {

	public static void main(String[]args){
		MeriFrame mf=new MeriFrame();
		mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mf.pack();
		mf.setVisible(true);
	}
}
