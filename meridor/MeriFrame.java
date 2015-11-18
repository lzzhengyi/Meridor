package meridor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class MeriFrame extends JFrame implements ActionListener {

	public MeriPanel mpanel;
	JMenu menu;
	JMenuBar mbar;
	JMenuItem mitem,save,load,newcampaign;
	
	public MeriFrame (){
		MConst.loadImages();
		MConst.initItems();
		
		this.setTitle("Invasion of Meridor Alpha");
		mpanel=new MeriPanel();
		mbar=new JMenuBar();
		
		menu=new JMenu("File");
		newcampaign=new JMenuItem("New");
		save=new JMenuItem("Save");
		load=new JMenuItem("Load");
		mitem=new JMenuItem("Toggle displays");
		mitem.addActionListener(this);
		menu.add(newcampaign);
		menu.add(save);
		menu.add(load);
		menu.add(mitem);
		
		mbar.add(menu);

		setLayout(new FlowLayout());
		add(mpanel);
		setJMenuBar(mbar);
		setVisible(true);
		pack();
		//setSize(800,800);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==mitem){
			mpanel.toggleActive();
		}
		
	}
}
