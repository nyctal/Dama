import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class PannelloSud extends JPanel{
	JTextField debug;
	JProgressBar bar;
	int delay = 15, pos,set,dim;
	double width, height;
	StatBorder tempoM;
		 
	public PannelloSud(int set, int dim){
		
		this.set = set;
		debug = new JTextField();
		bar = new JProgressBar(0,100);
				
		setPreferredSize(new Dimension(dim,(dim/100)*12));
		setLayout(new GridLayout(1,1));
		setBackground(PannelloEst.colori[set]);
		tempoM = new StatBorder(" ", set, TitledBorder.LEFT, 14);
			
		bar.setValue(100);
		bar.setPreferredSize(new Dimension(975,20));
		tempoM.add(bar);
		add(tempoM);
	}
	
	public void changeColor(int set){
		this.set = set;
		tempoM.title.setTitleColor(PannelloEst.testi[set]);
		setBackground(PannelloEst.colori[set]);
	}
	
	public void setBar(String text){
		pos = 0;
		tempoM.setName(text);
		tempoM.repaint();	
	}
}


