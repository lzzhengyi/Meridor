package meridor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;


public class MeriFrame extends JFrame implements ActionListener {

	private String title="Defense of Meridor Alpha";
	private MeriPanel mpanel;
	private JMenu menu;
	private JMenuBar mbar;
	private JMenuItem mitem,save,load,newcampaign,quit;
	private final JFileChooser fc=new JFileChooser();
	
	public MeriFrame (){
		MConst.loadImages();
		MConst.initItems();

		//		try { 
		//	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		//	} catch (Exception e) {
		//	    e.printStackTrace();
		//	}
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
			System.out.println("1");
		} catch (ClassNotFoundException e) {
			// handle exception
			System.out.println("2");
		} catch (InstantiationException e) {
			// handle exception
			System.out.println("3");
		} catch (IllegalAccessException e) {
			// handle exception
			System.out.println("4");
		}
		
		mpanel=new MeriPanel();
		mbar=new JMenuBar();
		
		menu=new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		newcampaign=new JMenuItem("New");
		newcampaign.addActionListener(this);
		newcampaign.setMnemonic(KeyEvent.VK_N);
		save=new JMenuItem("Save");
		save.addActionListener(this);
		save.setMnemonic(KeyEvent.VK_S);
		load=new JMenuItem("Load");
		load.addActionListener(this);
		load.setMnemonic(KeyEvent.VK_L);
		mitem=new JMenuItem("Toggle displays");
		mitem.addActionListener(this);
		quit=new JMenuItem("Quit");
		quit.addActionListener(this);
		quit.setMnemonic(KeyEvent.VK_Q);
		menu.add(newcampaign);
		menu.add(save);
		menu.add(load);
		menu.add(quit);
//		menu.add(mitem);
		
		mbar.add(menu);

		setLayout(new FlowLayout());
		add(mpanel);
		setJMenuBar(mbar);
		
		setTitle(title);
		setVisible(true);
		pack();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==newcampaign){
			int choice=JOptionPane.showConfirmDialog(null, "Really start a new campaign?", "", JOptionPane.YES_NO_OPTION);
			if (choice==JOptionPane.YES_OPTION){
				mpanel.startNewCampaign();
			}
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
		else if (e.getSource()==quit){
			setVisible(false);
			dispose();
			System.exit(0);
		}
		else if(e.getSource()==mitem){
//			mpanel.toggleActive();
			mpanel.displayVictoryPanel();
		}
		
	}
}
