package meridor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;


public class MeriFrame extends JFrame implements ActionListener {

	public MeriPanel mpanel;
	JMenu menu;
	JMenuBar mbar;
	JMenuItem mitem,save,load,newcampaign;
	final JFileChooser fc=new JFileChooser();
	
	public MeriFrame (){
		MConst.loadImages();
		MConst.initItems();
		
		this.setTitle("Defense of Meridor Alpha");
		mpanel=new MeriPanel();
		mbar=new JMenuBar();
		
		menu=new JMenu("File");
		newcampaign=new JMenuItem("New");
		newcampaign.addActionListener(this);
		save=new JMenuItem("Save");
		save.addActionListener(this);
		load=new JMenuItem("Load");
		load.addActionListener(this);
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
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==newcampaign){
			mpanel.startNewCampaign();
		} 
		else if (e.getSource()==save){
			int returnval=fc.showSaveDialog(this);
			
			if (returnval==JFileChooser.APPROVE_OPTION){
				File file=fc.getSelectedFile();
				try{
					OutputStream osfile=new FileOutputStream(file);
					OutputStream buffer=new BufferedOutputStream(osfile);
					ObjectOutput output=new ObjectOutputStream(buffer);
					try {
						output.writeObject(mpanel.campaign);
					} finally {
						output.close();
					}
				} 
				catch(IOException ex){
					ex.printStackTrace();
					System.err.println("File Save error!");
				} 
			}
		}
		else if (e.getSource()==load){
			int returnval=fc.showOpenDialog(this);
			
			if (returnval==JFileChooser.APPROVE_OPTION){
				File file=fc.getSelectedFile();
				try {
					ObjectInput input=new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
					//deserialize in
					try {
						Campaign loaded=(Campaign)input.readObject();
						System.out.println("Successfully loadd campaign");
						mpanel.loadCampaign(loaded);;
					}finally {
						input.close();
					}
				} 
				catch(IOException ex){
					System.err.println("File Load error!");
				} 
				catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if(e.getSource()==mitem){
			mpanel.toggleActive();
		}
		
	}
}
