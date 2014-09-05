import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;


public class PannelloEst extends JPanel{
	public static final Color colori[] =	{new Color(232,231,237),
											 new Color(228,180,104),
											 new Color(222,209,167),
											 Color.BLACK,
											 new Color(183,0,2),
											 new Color(134,215,24),
											 new Color(250,127,218),
											 new Color(228,97,45),
											};
	
	public static final Color testi[] =		{	Color.BLACK,
												Color.BLACK,
												Color.BLACK,
												Color.WHITE,
												Color.WHITE,
												Color.WHITE,
												Color.BLACK,
												Color.WHITE
											};	
	
	StatBorder uno, due, tre, move, userC, cpuC;
	JPanel stats;
	JLabel pedB, pedN, damB, damN;
	JLabel pb, pn, db, dn;
	JLabel record, cMinMov, countU, countC;
	int cPb = 0, cDb = 0, cPn = 0, cDn = 0, mUser = 0, mCPU = 0, nRecord = 0; 
	Border line1, line2;
	String RecordName;
	
	static int set;

	int dim;
	double width, height;
	
	public PannelloEst(int set, int dim){
		this.set = set;
		
		setPreferredSize(new Dimension((dim/100)*35,dim));
		setBackground(PannelloEst.colori[set]);
		setLayout(new GridLayout(3,1));
		
//------ uno -----------------------------------------------------------------------------------------------
		uno = new StatBorder(" MANGIATE ", set, TitledBorder.CENTER,14);
		uno.setLayout(new GridLayout(2,2));
				
		pedB = setLabelIcon(makePath(set,Scacchiera.PEDINA_BIANCA,false),80);
		pb = setLabel(testi[set],cPb);
		damB = setLabelIcon(makePath(set,Scacchiera.DAMA_BIANCA,false),80);
		db = setLabel(testi[set],cDb);
		
		uno.add(pedB);
		uno.add(pb);	
		uno.add(damB);	
		uno.add(db);
		
		add(uno);
		
//------ due ----------------------------------------------------------------------------------------------		
		due = new StatBorder(" PARTITA ",set,TitledBorder.CENTER,14);
		due.setLayout(new BorderLayout());
				
		stats = new JPanel();
		stats.setLayout(new GridLayout(1,2));
		record	= setStatLabel("Record: ",20);
		cMinMov	= setStatLabel("n.d.",20);
		stats.add(record);
		stats.add(cMinMov);
		stats.setOpaque(false);
		
		due.add(stats,BorderLayout.NORTH);

		move = new StatBorder(" MOSSE ",set,TitledBorder.CENTER,14);
		move.setLayout(new GridLayout(1,2));
		userC = new StatBorder(" USER ", set, TitledBorder.LEFT, 10);
		cpuC = new StatBorder(" CPU ", set, TitledBorder.RIGHT, 10);
		userC.setBackground(colori[set]);
		cpuC.setBackground(colori[set]);
						
		countU 	= setStatLabel(""+mUser,50);
		countC 	= setStatLabel(""+mCPU,50);
		userC.add(countU);
		cpuC.add(countC);
		
		move.add(userC);
		move.add(cpuC);
						
		due.add(move, BorderLayout.CENTER);
		add(due);

//------ tre ----------------------------------------------------------------------------------------------		
		tre = new StatBorder(" MANGIATE ",set, TitledBorder.CENTER,14);
		tre.setLayout(new GridLayout(2,2));
		
		pedN = setLabelIcon(makePath(set,Scacchiera.PEDINA_NERA,false),80);
		pn = setLabel(testi[set],cPn);
		damN = setLabelIcon(makePath(set,Scacchiera.DAMA_NERA,false),80);
		dn = setLabel(testi[set],cDn);
		
		tre.add(pedN);
		tre.add(pn);
		tre.add(damN);
		tre.add(dn);
		
		pedN.setVisible(false);
		pn.setVisible(false);
		damN.setVisible(false);
		dn.setVisible(false);
		
		pedB.setVisible(false);
		pb.setVisible(false);
		damB.setVisible(false);
		db.setVisible(false);
		
		add(tre);
	}
	
	public JLabel setLabel(Color c,int cont){
		JLabel label = new JLabel("X " + cont);; 
				
		label.setFont(new Font(Font.DIALOG,Font.BOLD,40));
		label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		label.setForeground(c);
		return label;
	}
	
	public static JLabel setLabelIcon(String path,int dim){
		Image img = new ImageIcon(path).getImage();;
		img = img.getScaledInstance(dim,dim,Image.SCALE_SMOOTH);
		ImageIcon imm = new ImageIcon(img);
		JLabel label = new JLabel(imm,JLabel.CENTER);
		
		return label;
	}
	
	public static JLabel setStatLabel(String text,int size){
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setForeground(testi[set]);
		label.setFont(new Font(Font.DIALOG,Font.BOLD,size));
		
		return label;
	}
	
	public void setPanel(int pezzo){
		switch(pezzo){
			case Scacchiera.PEDINA_BIANCA:{	pb.setText("X "+ ++cPb);break;	}
			case Scacchiera.PEDINA_NERA  :{ pn.setText("X "+ ++cPn);break;	}
			case Scacchiera.DAMA_BIANCA	 :{ db.setText("X "+ ++cDb);break;	}
			case Scacchiera.DAMA_NERA    :{ dn.setText("X "+ ++cDn);break;	}
		}		
	}
	
	public static String makePath(int index,int pezzo,boolean select){
		String pattern = "";
		
		switch(pezzo){
		case Scacchiera.PEDINA_BIANCA:{	pattern = index + "_pedina_bianca";	break;}
		case Scacchiera.PEDINA_NERA:{	pattern = index + "_pedina_nera";	break;}
		case Scacchiera.DAMA_BIANCA:{	pattern = index + "_dama_bianca";	break;}
		case Scacchiera.DAMA_NERA:{		pattern = index + "_dama_nera";		break;}
		}
			
		if(select)	pattern = pattern + "_sel";
				
		pattern = pattern + ".png";
		
		return pattern;
	}
	
	public void moveAdder(int color){
		if(color == Scacchiera.BIANCO)	countU.setText("" + ++mUser);
		else							countC.setText("" + ++mCPU);
	}

	public void resetStat(){
		cPb = 0;
		cDb = 0;
		cPn = 0;
		cDn = 0;
		
		pb.setText("X " + cPb);
		pn.setText("X " + cPn);
		db.setText("X " + cDb);
		dn.setText("X " + cDn);
		
		mUser = 0;
		mCPU = 0;	
		
		countU.setText("" + mUser);
		countC.setText("" + mUser);
	}

	public void setRecord(int n){
		nRecord = n;
		
		if(n == 0)	cMinMov.setText("n.d.");
		else		cMinMov.setText("" + nRecord);
	}

	public void changeColor(int set){
		this.set = set;
		this.setBackground(colori[set]);
		
		pb.setForeground(testi[set]);
		pn.setForeground(testi[set]);
		db.setForeground(testi[set]);
		dn.setForeground(testi[set]);
		
		record.setForeground(testi[set]);
		cMinMov.setForeground(testi[set]);
		countU.setForeground(testi[set]);
		countC.setForeground(testi[set]);
		
		move.title.setTitleColor(testi[set]);
		userC.title.setTitleColor(testi[set]);
		cpuC.title.setTitleColor(testi[set]);
		uno.title.setTitleColor(testi[set]);
		tre.title.setTitleColor(testi[set]);
		due.title.setTitleColor(testi[set]);	
	}
}