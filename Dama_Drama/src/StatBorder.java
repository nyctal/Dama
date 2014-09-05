import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

class StatBorder extends JPanel{
		Border line1,line2;
		TitledBorder title;
		String name;
		int set;
		
		public StatBorder(String name, int set, int Align, int size){
			this.name = name;
			this.set = set;
			setOpaque(false);
			line1 = BorderFactory.createLineBorder(Color.WHITE);
			line2 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			line1 = BorderFactory.createCompoundBorder(line1, line2);
			
			title = BorderFactory.createTitledBorder(line1, this.name);
			title.setTitleJustification(Align);
			title.setTitleFont(new Font(Font.DIALOG,Font.BOLD,size));
			title.setTitleColor(PannelloEst.testi[set]);
			setBorder(title);
		}
		
		public void setName(String n){	name = n;	}
	
		public void setSet(int i){	this.set = i;	} 
		
		public void paintComponent(Graphics g){
	    	super.paintComponent(g);
	    	title.setTitle(name);
	    }
	}