package firework;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class FireworkDriver
{
  public static void main(String[] args)
  {
    JFrame hf = new JFrame();
    hf.setDefaultCloseOperation(3);
    hf.setTitle("");
    
    hf.setLayout(new BorderLayout());
    hf.setPreferredSize(new Dimension(950, 650));
    hf.add (new FireworkPanel());
    
    hf.getContentPane();
    hf.pack();
    hf.setVisible(true);
  }
}