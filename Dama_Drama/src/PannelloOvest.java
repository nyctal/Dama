import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;

//----------Classe Tempo--------------------
class Tempo{
	int hour;
	int min;
	int sec;
	
	public Tempo(){		hour = min = sec = 0;	}
	
	public Tempo(int h,int m, int s){
		hour = h;
		min = m;
		sec = s;
		formatTempo();		
	}	
	
	public Tempo add(Tempo t){
		int h,m,s;
		
		h = this.hour + t.hour;
		m = this.min + t.min;
		s = this.sec + t.sec;		
		
		return new Tempo(h,m,s);
	}
	
	public void formatTempo(){
		for(;this.min > 59;this.hour++,this.min-=60);
		for(;this.sec > 59;this.min++,this.sec-=60); 
	}
}

//----------Classe PannelloOvest---------------------
public class PannelloOvest extends JPanel{
	static StatBorder turno;
	StatBorder temp, cUser, cCPU, due, tabel, timer, mHint;
	JPanel uno, tre, played;
	JLabel tempo, tempoTot, giocate, cGiocate, disponibili, countU, countC;
	static JLabel color;
	static final int delay = 1000;
	int dim, partite = 0, matchUser = 0, matchCPU = 0;
	static int ToccaA = Scacchiera.PEDINA_BIANCA;
	static int set;
	static Tempo time;
	static Tempo gametime;
	double width, height;
	Timer t;
	boolean inGame = false;
	JButton hint;
	static ImageIcon bianca;
	static ImageIcon nera;
	
	public PannelloOvest(int set, int dim){
		this.set = set;
		time = new Tempo();
		gametime = new Tempo();
		t = new Timer(delay,new tempoGioco());

		setPreferredSize(new Dimension((int) ((dim/100)*35),dim));
		setBackground(PannelloEst.colori[set]);
		setLayout(new GridLayout(3,1));
		
		uno = new JPanel();
		uno.setOpaque(false);
		tre = new JPanel();
		tre.setOpaque(false);
		
		bianca = GiocoDama.setIcon(set,90,Scacchiera.PEDINA_BIANCA,false);
		nera = GiocoDama.setIcon(set,90,Scacchiera.PEDINA_NERA,false);
		
		uno.setLayout(new BorderLayout());
		tre.setLayout(new BorderLayout());

//------ UNO --------------------------------------------------------------------------------------------------------------
		
		turno = new StatBorder(" TOCCA A ", set, TitledBorder.CENTER,14);
		color = new JLabel(bianca);
		color.setVisible(false);
		turno.add(color);
		
		uno.add(turno,BorderLayout.CENTER);
		temp = new StatBorder(" TEMPO DI GIOCO ", set , TitledBorder.CENTER,14);
		
		tempoTot = new JLabel("0:00:00");
		tempoTot.setHorizontalAlignment(JLabel.CENTER);
		tempoTot.setFont(new Font("Roman", Font.BOLD, 20));
		temp.add(tempoTot);
		uno.add(temp,BorderLayout.SOUTH);
		
		add(uno);
		
//------ DUE --------------------------------------------------------------------------------------------------------------				
		due = new StatBorder(" SESSIONE ", set, TitledBorder.CENTER, 14);
		due.setLayout(new BorderLayout());
		
		played = new JPanel();
		played.setLayout(new GridLayout(1,2));
		played.setOpaque(false);
		
		giocate	= PannelloEst.setStatLabel("Giocate:", 20);
		cGiocate = PannelloEst.setStatLabel("" + partite, 20);
		
		
		played.add(giocate);
		played.add(cGiocate);
		played.setBackground(PannelloEst.colori[set]);
		
		due.add(played,BorderLayout.NORTH);
		
		tabel = new StatBorder(" PUNTEGGIO ", set, TitledBorder.CENTER, 14);
		tabel.setLayout(new GridLayout(1,2));
		cUser = new StatBorder(" USER ", set, TitledBorder.LEFT, 10);
		cUser.setLayout(new BorderLayout());
		cCPU = new StatBorder(" CPU ", set, TitledBorder.RIGHT, 10);
		cCPU.setLayout(new BorderLayout());
			
		cUser.setBackground(PannelloEst.colori[set]);
		cCPU.setBackground(PannelloEst.colori[set]);
		
		countU 	= PannelloEst.setStatLabel(""+matchUser,50);
		countC 	= PannelloEst.setStatLabel(""+matchCPU,50);
		cUser.add(countU,BorderLayout.NORTH);
		cCPU.add(countC,BorderLayout.NORTH);
	
		tabel.add(cUser);
		tabel.add(cCPU);
						
		due.add(tabel, BorderLayout.CENTER);
		
		add(due);
		
//------ TRE --------------------------------------------------------------------------------------------------------------
		mHint = new StatBorder(" SUGGERIMENTI ",set, TitledBorder.CENTER,14);
		mHint.setLayout(new BorderLayout());
				
		disponibili = new JLabel("Mosse disponibili: 0");
		disponibili.setHorizontalAlignment(JLabel.CENTER);
		disponibili.setVerticalAlignment(JLabel.CENTER);
		disponibili.setFont(new Font("Roman", Font.BOLD, 16));
		hint = new JButton("Suggerimento mossa");
		hint.setEnabled(false);
		hint.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		mHint.add(disponibili,BorderLayout.CENTER);
		mHint.add(hint,BorderLayout.SOUTH);
		
		tre.add(mHint,BorderLayout.CENTER);
		
		timer = new StatBorder(" TEMPO PARTITA ", set, TitledBorder.CENTER,14);
				
		timer.setBackground(PannelloEst.colori[this.set]);
		
		tempo = new JLabel("0");
		//tempo.setPreferredSize(new Dimension(180,55));
		tempo.setHorizontalAlignment(JLabel.CENTER);
		tempo.setFont(new Font("Roman",Font.BOLD,45));
		
		timer.add(tempo);
		tre.add(timer,BorderLayout.SOUTH);
		

		add(tre);
	}
	
	public void resetStat(){
		partite = 0;
		matchUser = 0;
		matchCPU = 0;
		
		cGiocate.setText("" + partite);
		countU.setText("" + matchUser);
		countC.setText("" + matchCPU);
	}
	
	public void resetTimer(){
		gametime = new Tempo();
		tempo.setText("0");
		t.restart();
	}
	
	public void changeColor(int set){
		this.set = set;
		this.setBackground(PannelloEst.colori[set]);
		tempo.setForeground(PannelloEst.testi[set]);
    	tempoTot.setForeground(PannelloEst.testi[set]);
    	disponibili.setForeground(PannelloEst.testi[set]);
    	giocate.setForeground(PannelloEst.testi[set]);
    	cGiocate.setForeground(PannelloEst.testi[set]);
    	countU.setForeground(PannelloEst.testi[set]);
    	countC.setForeground(PannelloEst.testi[set]);
    	
    	turno.title.setTitleColor(PannelloEst.testi[set]);
    	temp.title.setTitleColor(PannelloEst.testi[set]);
    	cUser.title.setTitleColor(PannelloEst.testi[set]);
    	cCPU.title.setTitleColor(PannelloEst.testi[set]);
    	due.title.setTitleColor(PannelloEst.testi[set]);
    	tabel.title.setTitleColor(PannelloEst.testi[set]);
    	timer.title.setTitleColor(PannelloEst.testi[set]);
    	mHint.title.setTitleColor(PannelloEst.testi[set]);
    	
    	bianca = GiocoDama.setIcon(set,90,Scacchiera.PEDINA_BIANCA,false);
		nera = GiocoDama.setIcon(set,90,Scacchiera.PEDINA_NERA,false);
		
		this.switchTurn(ToccaA);
	}
	
	public static JLabel setLabelIcon(String path){
		Image img = new ImageIcon(path).getImage();;
		img = img.getScaledInstance(80,80,Image.SCALE_SMOOTH);
		ImageIcon imm = new ImageIcon(img);
		JLabel label = new JLabel(imm,JLabel.CENTER);
		
		return label;
	}
	
	public static void switchTurn(int turn){
		ToccaA = turn;
				
		if(turn == Scacchiera.BIANCO)	color.setIcon(bianca);
		else							color.setIcon(nera);
			
		turno.add(color);
	}
	
	public void printGameTime(JLabel l,Tempo t){
		if(t.hour > 0){
			if(t.min < 10)	l.setText("" + t.hour + ":0");
			else 			l.setText("" + t.hour + ":" );				
		}
		else				l.setText("");
		
		if(t.min > 0){
			if(t.sec < 10)	l.setText(l.getText() + t.min + ":0");
			else 			l.setText(l.getText() + t.min + ":" );
		}
	
		else				l.setText("");
		
		l.setText(l.getText() + t.sec);
	}
	
	public void printTotTime(JLabel l,Tempo t){
		if(t.hour > 0)	l.setText("" + t.hour);				
		else			l.setText("0");
		if(t.min < 10)	l.setText(l.getText() + ":0" + t.min);
		else 			l.setText(l.getText() + ":" + t.min);
		if(t.sec < 10)	l.setText(l.getText() + ":0" + t.sec);
		else 			l.setText(l.getText() + ":" + t.sec);
	}
	
	public void matchAdder(){
		cGiocate.setText(""+ ++partite);
	}

	public void setPartita(int colore){
		switch(colore){
		case Scacchiera.BIANCO:{	countU.setText("" + ++matchUser);	break;	}
		case Scacchiera.NERO  :{	countU.setText("" + ++matchCPU);			}
		}	
	}
	
	public void setDisponibili(int n){	disponibili.setText("Mosse disponibili: " + n);	}
	
	//----------Classe tempoGioco-----------------------
	class tempoGioco implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			time.sec++;
			time.formatTempo();
			printTotTime(tempoTot, time);			
				
			if(inGame){
				gametime.sec++;
				gametime.formatTempo();
				printGameTime(tempo, gametime);
			}
					
			t.restart();
		}		
	}
}