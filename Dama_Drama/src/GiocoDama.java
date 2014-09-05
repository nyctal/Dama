import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GiocoDama extends JFrame{
	Crono log_windows;
	Container container;
	AreaGioco areagioco;
	CasellaGrafica casellag[][];
	ActionListener menu;
	PanOpzioni panopzioni;
	ImageIcon sfondotema[];
	public ImageIcon pedine[];
	ImageIcon pedine_sel[],ghost,ghost2;
	Image sfondo[],base,sfondothumb[],fant, fant2;
	JMenuBar menubar;
	JSplitPane split;
	JLabel icona_temp;
	JMenu partita,visualizza,about;
	JMenuItem nuova_partita,nuova_sessione,opzioni,aiuto,aboutI,esci;
	JCheckBoxMenuItem cronologia;
	JOptionPane aboutPane;
	JButton okok,cancelall;
	Casella casella[][];
	Scacchiera griglia;
	FinePartita finepartita;
	Mossa move, move2, mtemp = null, mossaTemp;
	Gioco game;
	ValutaMosse val, val2;
	Timer time, tl;
	Casella c = null, c2 = null, c3 = null, aux;
	String background = "_sfondo.jpg";
	String nome;
	PannelloEst est;
	PannelloOvest ovest;
	PannelloSud sud;
	Pregioco pregioco;
	Help help;
	Dimension dj;
	ActionListener tempolis = null;
	WindowAdapter closing;
	int riga_sel, col_sel,move_col,move_riga,mangiatenere,mangiatebianche,index=0,color_temp=5,colore_select=0,x;
	int delay = 15;
	int colore_scelto,colore_opposto,ntempo=0;
	int dim, dimpedine;
	int vinto;
	double width;
	double height;
	boolean selezione=false;
	boolean seconda=false;
	boolean partitaInCorso=false;
	boolean opt=false;
	boolean disegna = true;
	
// 	MUOVI E MANGIA	
	public void muoviemangia(Mossa m){
		int i=0;
		
		c=(Casella) m.caselleToccate.getFirst();
		c3=(Casella) m.caselleToccate.getLast();
		
		
		if(!m.caselleMangiate.isEmpty()){
			for(c2=(Casella)m.caselleMangiate.getFirst(),i=0;    ;c2=(Casella)m.caselleMangiate.get(++i) ){
		 		move_riga=c3.riga;
		 		move_col=c3.colonna;
		 					
		 		switch(game.contenuto(c2.riga,c2.colonna)){
		 			case Scacchiera.PEDINA_BIANCA : { est.pb.setText("X "+ ++est.cPb); break;	}
		 			case Scacchiera.PEDINA_NERA   : { est.pn.setText("X "+ ++est.cPn); break;	}
		 			case Scacchiera.DAMA_BIANCA   : { est.db.setText("X "+ ++est.cDb); break;	}
		 			case Scacchiera.DAMA_NERA     : { est.dn.setText("X "+ ++est.cDn); 			}
		 		}
		 					
		 		selezione=false;
		 		casellag[c2.riga][c2.colonna].selezionata=false;
		 		casellag[c2.riga][c2.colonna].ombra = false;
				casellag[c2.riga][c2.colonna].repaint();
		 		if(c2==(Casella)m.caselleMangiate.getLast())break;
			}
		 	
			repaint();
		}
			 
		est.moveAdder(game.aChiTocca);
		game.esegui(m);
  		areagioco.repaint();
		log_windows.cronologia(nome);
		
		
		if(log_windows.gioco_contrario){
			
				ovest.disponibili.setText("Mosse disponibili : " + mosseDisponibili(colore_opposto));
				ovest.switchTurn(game.coloreOpposto(game.aChiTocca));
		}
		
		else{
			ovest.disponibili.setText("Mosse disponibili : " + mosseDisponibili(colore_scelto));
			ovest.switchTurn(game.aChiTocca);
		}
	}
	
	public class Pregioco extends JFrame {
		
		Container contenitorepazzo;
		JPanel pannellonome,pannellocolore,pannelloconferma;
		JTextField nome;
		ButtonGroup colore;
		JRadioButton bianco;
		JRadioButton nero;
		Border  bordo;
		TitledBorder bordonome,bordocolore;
		KeyListener keyLis;
		ActionListener radio;
		WindowAdapter closing;
		
		public Pregioco(){
			pannellonome = new JPanel();
			pannellocolore = new JPanel();
			pannelloconferma = new JPanel();
			nome = new JTextField(30);
			okok = new JButton("Ok");
			cancelall = new JButton("Cancel");
			colore = new ButtonGroup();
			bianco = new JRadioButton("Bianco");
			nero = new JRadioButton("Nero");
			
			bordo = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			bordonome = BorderFactory.createTitledBorder(bordo,"Nome giocatore:");
			bordocolore = BorderFactory.createTitledBorder(bordo,"Colore desiderato");
			
			colore.add(bianco);
			colore.add(nero);
			pannellocolore.add(bianco);
			pannellocolore.add(nero);
			pannellonome.add(nome);
			pannelloconferma.add(okok);
			pannelloconferma.add(cancelall);
			
			contenitorepazzo = getContentPane();
			contenitorepazzo.setLayout(new BorderLayout());
			contenitorepazzo.add(pannellonome,BorderLayout.NORTH);
			contenitorepazzo.add(pannellocolore,BorderLayout.CENTER);
			contenitorepazzo.add(pannelloconferma,BorderLayout.SOUTH);
			pannellocolore.setBorder(bordocolore);
			pannellonome.setBorder(bordonome);
			
			class JRadioButtonListener implements ActionListener {
				public void actionPerformed(final ActionEvent e) {
					final String s = nome.getText();
					if((bianco.isSelected() || nero.isSelected()) && s.length()>0)okok.setEnabled(true);
					}
				}
			
			class MyKeyListener extends KeyAdapter{
		    	public void keyTyped(final KeyEvent k) {
		    		if(bianco.isSelected() || nero.isSelected())
		    		okok.setEnabled(true);
				}
		    }			
			
			radio= new JRadioButtonListener();
			keyLis = new MyKeyListener();
			
			addWindowListener(closing);
			nome.addKeyListener(keyLis);
			bianco.addActionListener(radio);
			nero.addActionListener(radio);
			okok.setEnabled(false);
			pack();
			setVisible(false);
			setAlwaysOnTop(true);
			setLocation( (int)(width/2 - getWidth()/2),(int)( height/2 - getHeight()/2 ));
		}
	}
		 
	public void init(){
		for(int i=0;i<8;i++){
	    	for(int j=0;j<8;j++){
	    		if((i+j)%2==0){
	    			if(i<3)			casellag[i][j]=new CasellaGrafica(Scacchiera.NERO,Scacchiera.PEDINA_NERA);
	    			else if(i>4)	casellag[i][j]=new CasellaGrafica(Scacchiera.NERO,Scacchiera.PEDINA_BIANCA);
					else 			casellag[i][j]=new CasellaGrafica(Scacchiera.NERO,Scacchiera.VUOTA);														
	    		}
	    	else		  			casellag[i][j]=new CasellaGrafica(Scacchiera.BIANCO,Scacchiera.VUOTA);
	    		
	    		casellag[i][j].riga=i;
	    		casellag[i][j].colonna=j;
	    		areagioco.add(casellag[i][j]);
	    		casella[i][j] = new Casella (i,j);
	    	}
		}
	}
	
	public GiocoDama(){
		String windows="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try{
			UIManager.setLookAndFeel(windows);
			SwingUtilities.updateComponentTreeUI(GiocoDama.this);
		}
		catch(Exception e){}
		
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension screenSize = t.getScreenSize();

		width  = screenSize.getWidth();
		height = screenSize.getHeight();
		
		if( height <= width ) dim=(int) ((height/100)*75);
		else dim=(int)((height/100)*75);
		
		dimpedine=dim/9;
		est = new PannelloEst(index,dim);
		ovest = new PannelloOvest(index,dim);
		ovest.hint.addActionListener(menu);
		sud = new PannelloSud(index,dim);
		pedine = new ImageIcon[5];
		pedine_sel = new ImageIcon[4];
		fant = new ImageIcon("ombra.png").getImage();
		fant = fant.getScaledInstance(dimpedine, dimpedine, Image.SCALE_SMOOTH);
		ghost = new ImageIcon(fant);
		fant2 = new ImageIcon("ombra2.png").getImage();
		fant2 = fant2.getScaledInstance(dimpedine, dimpedine, Image.SCALE_SMOOTH);
		ghost2 = new ImageIcon(fant2);
		sfondo=new Image[8];
		sfondothumb=new Image[8];
		casellag=new CasellaGrafica[8][8];
		griglia=new Scacchiera();
		casella= new Casella[8][8];
		game = new Gioco();
		pregioco = new Pregioco();
		log_windows=new Crono(game,screenSize);
		finepartita=new FinePartita();
		help = new Help(dim);
		
		for(int i=0;i<8;i++){
			sfondo[i]=new ImageIcon(i+background).getImage();
			sfondothumb[i]=sfondo[i].getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		}
	
		base=sfondo[index].getScaledInstance(dim, dim,Image.SCALE_SMOOTH);
		
		pedine[1]     = setIcon(index,dimpedine,Scacchiera.PEDINA_BIANCA,false);
		pedine[2]     = setIcon(index,dimpedine,Scacchiera.PEDINA_NERA,false);
		pedine[3]     = setIcon(index,dimpedine,Scacchiera.DAMA_BIANCA,false);
		pedine[4]     = setIcon(index,dimpedine,Scacchiera.DAMA_NERA,false);
		pedine_sel[0] = setIcon(index,dimpedine,Scacchiera.PEDINA_BIANCA,true);
		pedine_sel[1] = setIcon(index,dimpedine,Scacchiera.PEDINA_NERA,true);
		pedine_sel[2] = setIcon(index,dimpedine,Scacchiera.DAMA_BIANCA,true);
		pedine_sel[3] = setIcon(index,dimpedine,Scacchiera.DAMA_NERA,true);
		
		aboutI = new JMenuItem("About...");
		aiuto = new JMenuItem("Help");
		nuova_partita = new JMenuItem("Nuova partita");
		nuova_sessione = new JMenuItem("Nuova sessione");
		cronologia= new JCheckBoxMenuItem("Cronologia");
		opzioni=new JMenuItem("Opzioni");
		esci = new JMenuItem("Esci");
		partita = new JMenu("Partita");
		visualizza = new JMenu("Visualizza");
		about = new JMenu("?");
		container = getContentPane();
		menubar = new JMenuBar();
		areagioco = new AreaGioco(base);
		aboutPane=new JOptionPane();
		sfondotema=new ImageIcon[8];
		
		
		for(int i=0;i<8;i++){		sfondotema[i]=new ImageIcon(sfondothumb[i]); 	}
		
		panopzioni=new PanOpzioni();
		container.setLayout(new BorderLayout());
		container.add(areagioco, BorderLayout.CENTER);
		container.add(sud, BorderLayout.SOUTH);
		container.add(est, BorderLayout.EAST);
		container.add(ovest,BorderLayout.WEST);
		
		areagioco.setLayout(new GridLayout(8,8));
		menubar.add(partita);
		menubar.add(visualizza);
		menubar.add(about);
		partita.add(nuova_partita);
		partita.add(nuova_sessione);
		partita.add(opzioni);
		partita.add(esci);
		visualizza.add(cronologia);
		about.add(aiuto);
		about.add(aboutI);
		
		
		class Menu implements ActionListener{
			public void actionPerformed(ActionEvent e){
				Object sorgente = e.getSource();
				
				if(sorgente == nuova_partita){
					
					nuovaPartita();
					
				}
				else if(sorgente == nuova_sessione){
					nuovaSessione();
					
				}
								
				else if(sorgente == opzioni){
					panopzioni.setVisible(true);
					panopzioni.setLocation( (int) ((width - panopzioni.getWidth() )/2),(int)(( height - panopzioni.getHeight())/3)  );
		     		
				}
				
				else if(sorgente == esci){	
					
					opzioni.setVisible(false);
					log_windows.setVisible(false);
					help.setVisible(false);
					pregioco.setVisible(false);
					finepartita.setVisible(false);
					System.exit(0);		
					
				}
				
				else if(sorgente==cronologia){
					
					log_windows.setVisible(cronologia.isSelected());
					
				}
				else if(sorgente == aiuto){	
					help.setVisible(true);
					help.setLocation( (int) ((width - help.getWidth() )/2),(int)(( height - help.getHeight())/3)  );
					help.ricerca.setText("");
					help.ricerca.setBackground(Color.WHITE);
					help.areatesto.setCaretPosition(0);
				}
				
				else if(sorgente == aboutI){	
					JOptionPane.showMessageDialog(null, "Dama\nVersione 3.06d (build 0.570)\nCreated by\nGiacinto 'Jack' Marcellino,\nStefano 'Sore' Soresina\nMatteo 'Dr Me' Brigati\n", "About dAMA", JOptionPane.INFORMATION_MESSAGE);
				}
				
				else if(sorgente==okok){
					pregioco.setVisible(false);
					nome=pregioco.nome.getText();
					est.userC.setName(" " + nome + " ");
					est.repaint();
					ovest.cUser.setName(" " + nome + " ");
					ovest.repaint();				
					ovest.inGame = true;
					partitaInCorso=true;
					
					if(pregioco.bianco.isSelected()){
						log_windows.gioco_contrario = false;
						colore_scelto=Scacchiera.BIANCO;
						ovest.hint.setEnabled(true);
						
						if(panopzioni.opzione1.isSelected())ovest.hint.setEnabled(true);
					}
					
					else {
						colore_scelto=Scacchiera.NERO;
						log_windows.gioco_contrario=true;
						game.aChiTocca=Scacchiera.NERO;		
						ovest.hint.setEnabled(false);
						time.start();
					}
					
					colore_opposto=game.coloreOpposto(colore_scelto);
					log_windows.scrollbug.setVisible(true);
					log_windows.cronologia(""+nome+"\n");
					for(int i=0;i<8;i++)
				    	for(int j=0;j<8;j++){
				    		casellag[i][j].icona.setVisible(true);
				    		casellag[i][j].repaint();
				    	}
				    
					ovest.color.setVisible(true);
					est.uno.setVisible(true);
					est.tre.setVisible(true);
					est.pedN.setVisible(true);
					est.pn.setVisible(true);
					est.damN.setVisible(true);
					est.dn.setVisible(true);
					est.pedB.setVisible(true);
					est.pb.setVisible(true);
					est.damB.setVisible(true);
					est.db.setVisible(true);
					seconda=true;
					areagioco.repaint();
					aggiornaStatitiche();
					ovest.resetTimer();
					est.resetStat();
					game.statoIniziale();
					setVisible(true);				
					ovest.t.start();				
				}
				
				else if(sorgente == ovest.hint){
					if(partitaInCorso){	
						val2=new ValutaMosse(game);
						System.out.println("sono nel listener Pagliaccio\n");
						mossaTemp = val2.mossaMigliore(Scacchiera.BIANCO);

						aux = (Casella)mossaTemp.caselleToccate.getFirst();
						casellag[riga_sel][col_sel].selezionata = false;
						casellag[riga_sel][col_sel].repaint();
						riga_sel = aux.riga;
						col_sel = aux.colonna;
						casellag[aux.riga][aux.colonna].selezionata = true;
						casellag[aux.riga][aux.colonna].repaint();
						
						tl.start();
					}
				}
				
				if(sorgente==cancelall){
					pregioco.setVisible(false);
				}
			} 
		}
		
		class ClosingListener extends WindowAdapter{
			public void windowClosed(WindowEvent e){  
			   Object sorgente=e.getSource();   
               if(sorgente==log_windows){
            	   log_windows.setVisible(false);
            	   cronologia.setSelected(false);
               }
            }
		
			public void windowClosing(WindowEvent e){  
			   Object sorgente=e.getSource();   
               
			   if(sorgente == log_windows){
				   log_windows.setVisible(false);
				   cronologia.setSelected(false);
			   }
			   
			   else if(sorgente == help) help.lista.setSelectedIndex(0);
			   
			   else{
            	   opzioni.setVisible(false);
            	   log_windows.setVisible(false);
            	   help.setVisible(false);
            	   pregioco.setVisible(false);
            	   finepartita.setVisible(false);
            	   System.exit(0);
               }  
            }
			
			
		}
		
		menu=new Menu();
		closing = new ClosingListener();
		nuova_partita.addActionListener(menu);
		nuova_sessione.addActionListener(menu);
		esci.addActionListener(menu);
		opzioni.addActionListener(menu);
		aiuto.addActionListener(menu);
		aboutI.addActionListener(menu);
		cronologia.addActionListener(menu);
		okok.addActionListener(menu);
		cancelall.addActionListener(menu);
		ovest.hint.addActionListener(menu);
		cronologia.setSelected(false);
		log_windows.addWindowListener(closing);
		help.addWindowListener(closing);
		addWindowListener(closing);
		
		setJMenuBar(menubar);
		setTitle("Dama");
		init();
		pack();
			
		time = new Timer(delay,new TimerAction());
		tl = new Timer(1000,new tempoLampeggio());
		setVisible(true);
		setResizable(false);
		
		dj=getSize();
		setLocation( (int) ((width - dj.getWidth() )/2),(int)(( height - dj.getHeight())/3)  );
		
		ovest.t.start();
	}
	
	public static void main(String[] args){
		GiocoDama sc = new GiocoDama();
	}
	
	class CasellaGrafica extends JPanel{
		JLabel icona;
		MouseListener Mouselis,Mouselis2;
		Image img;
		int riga,colonna, pezzo,add=0;
		boolean ombra=false;
		boolean selezionata=false;
				
		public CasellaGrafica(int col_casella, int p){
			icona=new JLabel(pedine[p]);
			add(icona);
		
			if(col_casella==Scacchiera.NERO){
				Mouselis = new MyMouseListener();
				Mouselis2 = new MyMouseListener2();
				addMouseListener(Mouselis);
				addMouseListener(Mouselis2);
			}
			     
			setOpaque(false);
			icona.setVisible(false);	
		}
		
		class MyMouseListener extends MouseAdapter{
	    	public void mouseClicked(MouseEvent e) {
	    		if(partitaInCorso){
	    			color_temp=game.contenuto(riga,colonna);
		    		colore_select=game.colore(game.contenuto(riga, colonna));
		    		
		    		if(color_temp!=Scacchiera.VUOTA && selezione==false) {
		    			if(log_windows.gioco_contrario && game.aChiTocca == colore_opposto || !log_windows.gioco_contrario && game.aChiTocca == colore_scelto){
		    				if((game.colore(colore_select)==colore_scelto && log_windows.gioco_contrario==false) || (game.colore(colore_select)!=colore_scelto && log_windows.gioco_contrario==true)){
			    	    		selezionata=true;
			    				selezione = true;
			    				repaint();
				 				riga_sel = riga;
				 				col_sel = colonna;
				 			}
		    			}
		    			
		    			else{
		    				if(casellag[riga][colonna] == casellag[move_riga][move_col]){
		    					selezionata = true;
		    					selezione = true;
		    					riga_sel = riga;
		    					col_sel = colonna;
		    				}
		    			}
		    			repaint();
		    		}
		    		
		    		else if(color_temp!=Scacchiera.VUOTA && selezione==true) {
		    			if(selezionata){
		 					selezione=false;
		 					selezionata=false;
		 					repaint();
		    			}
		    			
		    			else{
		    				if(log_windows.gioco_contrario && game.aChiTocca == colore_opposto || !log_windows.gioco_contrario && game.aChiTocca == colore_scelto){
			    				if(color_temp==game.colore(game.contenutoCaselle[riga_sel][col_sel])){
			 						casellag[riga_sel][col_sel].selezionata = false;
				 					casellag[riga_sel][col_sel].repaint();
			 						selezionata=true;
			 						riga_sel = riga;
				 					col_sel = colonna;
			 						repaint();
			 					}
		    				}
		    				
		    			}
		    		}
		    		
		    		else if(color_temp==Scacchiera.VUOTA && selezione==true) {
		    			move = new Mossa(casella[riga_sel][col_sel]);
		    			c=(Casella) move.caselleToccate.getFirst();
			 			selezione=false;
			 			casellag[riga_sel][col_sel].selezionata=false;
			 			casellag[riga_sel][col_sel].repaint();
			 			repaint();
		 				
			 			if(  (game.colore(game.contenuto(c.riga,c.colonna))==game.aChiTocca  && game.puoAndare(move, casella[riga][colonna])) 
			 				 || (game.colore(game.contenuto(c.riga,c.colonna))!=game.aChiTocca  && game.puoAndare(move, casella[riga][colonna])
			 				 && (c2=game.mangiataFra(casella[c.riga][c.colonna],casella[riga][colonna]))!=null
			 				 &&  casella[c.riga][c.colonna]==casella[move_riga][move_col])
			 			){
		 					if(c2!=null){
		 						if(game.colore(game.contenuto(c2.riga,c2.colonna))== Scacchiera.NERO)	mangiatenere++;
		 						else if (game.colore(game.contenuto(c2.riga,c2.colonna))== Scacchiera.BIANCO)	mangiatebianche++;
		 						else{
		 							mangiatenere=0;
		 							mangiatebianche=0;
		 						}
		 						if (mangiatenere%2==0)game.aChiTocca=Scacchiera.NERO;
		 						if (mangiatebianche%2==0)game.aChiTocca=Scacchiera.BIANCO;
		 					}
		 				
		 					selezionata=false;
		 					muoviemangia(move);
		 					ovest.hint.setEnabled(false);
		 					sud.setBar(" IL COMPUTER STA PENSANDO ");
		 					time.start();
		 				
		 					c2=null;
			 			}
			 			
			 			casellag[riga_sel][col_sel].repaint();
		 				vinto=game.chiHaVinto();
		 				
		 				if(vinto!=Scacchiera.NON_COLORE){
		 					ovest.hint.setEnabled(false);
		 					if(log_windows.gioco_contrario){
		 						ovest.setPartita(colore_opposto);
		 						if(vinto!=colore_scelto){	// Essengo il gioco al contrario, significa vittoria dell ' Utente
		 							if(est.mUser < est.nRecord || ovest.partite==0 ){
		 								est.setRecord(est.mUser);
		 								est.RecordName = nome;
		 							}
		 						}
		 				
		 						else{
		 							if(est.mCPU < est.nRecord || ovest.partite==0 ){
		 								est.setRecord(est.mCPU);
		 								est.RecordName = "CPU";
		 							}
		 						}
		 					}
		 				
		 					else{  // Gioco normale
		 						ovest.setPartita(colore_scelto);
		 						if(vinto==colore_scelto){
		 							if(est.mUser < est.nRecord || ovest.partite==0 ){
		 								est.setRecord(est.mUser);
		 								est.RecordName=nome;
		 							}
		 						}
		 						else{
		 							if(est.mCPU < est.nRecord || ovest.partite==0 ){
		 								est.setRecord(est.mCPU);
		 								est.RecordName="CPU";
		 							}
		 						}
		 					}
		 					
		 					ovest.inGame = false;
		 					time.stop();
		 					ovest.matchAdder();
		 					finepartita.endMatch();
		 					
		 					finepartita.setVisible(true);
		 					finepartita.setLocation( (int) ((width - finepartita.getWidth() )/2),(int)(( height - finepartita.getHeight())/3)  );
		 					partitaInCorso=false;
		 				}
		 				
		 			
		 				repaint();
		    		}
	    		}
	    	}
		}
		
class MyMouseListener2 extends MouseAdapter{
			
			public void mouseEntered(MouseEvent ev) {
				
				if(partitaInCorso){
				
				if(game.contenuto(riga,colonna) == Scacchiera.VUOTA){
					
					
					if(selezione==true){
						
						mtemp=new Mossa(casella[riga_sel][col_sel]);
					
					if(game.puoAndare(mtemp, casella[riga][colonna])){ 
						ombra=true; 
						icona.setVisible(true);
						add(icona);
						icona.setIcon(ghost);
						areagioco.repaint();
						
						}
					
					repaint();
					areagioco.repaint();
					}
				}
				repaint();
			}
		}
			public void mouseExited(MouseEvent arg0) {
				
					if(ombra){
					ombra=false;
					repaint();
					}
			
			}
	    	
	}
					
		public void paintComponent(Graphics g){
	    	super.paintComponent(g);
	    	
	    	if(game.contenuto(riga,colonna)!=Scacchiera.VUOTA){
	    		if(seconda)icona.setVisible(true);
	    		if(log_windows.gioco_contrario){
	    			if(game.colore(game.contenuto(riga,colonna))== Scacchiera.BIANCO)add=1;
	    			else add=(-1);
	    		if(selezionata){icona.setIcon(pedine_sel[game.contenuto(riga,colonna)-1+add]);}
	    		else {icona.setIcon(pedine[game.contenuto(riga,colonna)+add]);}
	    		
	    	    }
	    		else{
	    			if(selezionata){icona.setIcon(pedine_sel[game.contenuto(riga,colonna)-1]);}
		    		else {icona.setIcon(pedine[game.contenuto(riga,colonna)]);}
	    		}
	        }
	    	else if(game.contenuto(riga,colonna)==Scacchiera.VUOTA && ombra==false){icona.setVisible(false);}
	    	
	    }
	}
	
	public class AreaGioco extends JPanel{
		
		Image wallpaper;
		
		public AreaGioco(Image img){
			wallpaper=img;
			setPreferredSize(new Dimension(dim,dim));
			setMinimumSize(new Dimension(dim,dim));
			setMaximumSize(new Dimension(dim,dim));
			setSize(dim,dim);
		}
		
		public void paintComponent(Graphics g){
	    	super.paintComponent(g);
	    	g.drawImage(wallpaper, 0, 0, getWidth(),getHeight(), this);
	    }
	}
	
	public class PanOpzioni extends JFrame{
		Container principale;
		ActionListener Opzioni;
		JPanel opzioni,velocità,temi,imho,conferma,ptema[];
		JCheckBox opzione1;
		JCheckBox opzione2;
		JRadioButton tema[];
		ButtonGroup group; 
		JSlider slider;
		JTextField value;
		JLabel label,sec,min,max;
		JButton ok;
		JButton cancel;
		Border  bordo_sel,bordo_sel2,bordotemi,bevel1,bevel2,vuoto;
		TitledBorder titolotemi,titolopzioni,titolovelocità;
		int temp,minimo,massimo,corrente;
		
		public PanOpzioni(){		
			minimo = 15;
			corrente = 15;
			massimo = 70;
			principale = getContentPane();
			opzioni = new JPanel();
			temi = new JPanel();
			velocità = new JPanel();
			imho = new JPanel();
			conferma = new JPanel();
			opzione1= new JCheckBox("Abilita suggerimenti");
			opzione2= new JCheckBox("Visualizza mosse possibili");
			ok = new JButton("Ok");
			cancel = new JButton("Cancel");
			tema = new JRadioButton[8];
			slider =new JSlider(JSlider.HORIZONTAL,minimo,massimo,corrente);
			ptema = new JPanel[8];
			group = new ButtonGroup();
			temp = 0;
			value = new JTextField(4);
			value.setText("1.5");
			value.setHorizontalAlignment(JTextField.CENTER);
			sec = new JLabel("Sec");
			min = new JLabel("Min");
			max = new JLabel("Max");
			
			for(int i=0;i<8;i++){
				tema[i]  = new JRadioButton(sfondotema[i]);
				ptema[i] = new JPanel();
				ptema[i].add(tema[i]);
				temi.add(ptema[i]);
				group.add(tema[i]);
			}
			
			bevel1=BorderFactory.createLineBorder(Color.BLUE, 3);
			bordo_sel2 = BorderFactory.createRaisedBevelBorder();
			bordo_sel=BorderFactory.createCompoundBorder(bordo_sel2, bevel1);
			vuoto=BorderFactory.createEmptyBorder();
			bordotemi = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			titolotemi = BorderFactory.createTitledBorder(bordotemi,"Seleziona un tema");
			titolopzioni = BorderFactory.createTitledBorder(bordotemi,"Opzioni di gioco");
			titolovelocità = BorderFactory.createTitledBorder(bordotemi,"Velocità di gioco");
			conferma.add(ok);
			conferma.add(cancel);
			imho.setLayout(new GridLayout(2,1));
			principale.setLayout(new BorderLayout());
			principale.add(temi, BorderLayout.NORTH);
			principale.add(imho, BorderLayout.CENTER);
			principale.add(conferma, BorderLayout.SOUTH);
			temi.setLayout(new GridLayout(2,3));
			imho.add(opzioni);
			imho.add(velocità);
			opzioni.add(opzione1);
			opzioni.add(opzione2);
			velocità.setLayout(new FlowLayout());
			velocità.add(min);
			velocità.add(slider);
			velocità.add(max);
			slider.addChangeListener(new sliderListener());
			velocità.add(value);
			value.addActionListener(new valueListener());
			velocità.add(sec);
			opzione1.setSelected(true);
			opzione2.setSelected(true);
			
			
			class Opzioni implements ActionListener{
				public void actionPerformed(ActionEvent e){
					
					Object sorgente = e.getSource();
					
					for(int i=0;i<8;i++){
						for(int j=0;j<8;j++)ptema[i].setBorder(vuoto);
						if(tema[i].isSelected()==true){
							ptema[i].setBorder(bordo_sel);
							temp=i;
						}
					}
					if (sorgente == ok){
						panopzioni.setVisible(false);
						
						if(temp!=index){
							index=temp;
							base=sfondo[index].getScaledInstance(dim,dim, Image.SCALE_SMOOTH);
							areagioco.wallpaper=base;
							est.changeColor(index);
							ovest.changeColor(index);
							sud.changeColor(index);
							ovest.switchTurn(game.aChiTocca);
							help.repaint();
							areagioco.repaint();
							pedine[1] = setIcon(index,dimpedine,Scacchiera.PEDINA_BIANCA,false);
							pedine[2] = setIcon(index,dimpedine,Scacchiera.PEDINA_NERA,false);
							pedine[3] = setIcon(index,dimpedine,Scacchiera.DAMA_BIANCA,false);
							pedine[4] = setIcon(index,dimpedine,Scacchiera.DAMA_NERA,false);
							pedine_sel[0] = setIcon(index,dimpedine,Scacchiera.PEDINA_BIANCA,true);
							pedine_sel[1] = setIcon(index,dimpedine,Scacchiera.PEDINA_NERA,true);
							pedine_sel[2] = setIcon(index,dimpedine,Scacchiera.DAMA_BIANCA,true);
							pedine_sel[3] = setIcon(index,dimpedine,Scacchiera.DAMA_NERA,true);
							aggiornaStatitiche();
							areagioco.repaint();
							
						}
					
						if(opzione2.isSelected()==false){
							for(int i=0;i<8;i++)
								for(int j=0;j<8;j++){
									casellag[i][j].removeMouseListener(casellag[i][j].Mouselis2);
								}
						}
						else{
							for(int i=0;i<8;i++)
								for(int j=0;j<8;j++){
									casellag[i][j].addMouseListener(casellag[i][j].Mouselis2);
								}
						}
						if(opt)finepartita.setVisible(true);
					}
					if (sorgente == cancel){
						panopzioni.setVisible(false);
						ptema[0].setBorder(bordo_sel);
						for(int j=1;j<8;j++)ptema[j].setBorder(vuoto);
						if(opt)finepartita.setVisible(true);
					}
				
					time.setInitialDelay(panopzioni.slider.getValue());
				}
			}
			Opzioni=new Opzioni();
			for(int i=0;i<8;i++)tema[i].addActionListener(Opzioni);
			
			ok.addActionListener(Opzioni);
			cancel.addActionListener(Opzioni);
			opzioni.setBorder(titolopzioni);
			temi.setBorder(titolotemi);
			velocità.setBorder(titolovelocità);
			ptema[0].setBorder(bordo_sel);
			tema[0].setSelected(true);
			pack();
			setTitle("Opzioni");
			setVisible(false);
		}	

		class sliderListener implements ChangeListener{
			public void stateChanged(ChangeEvent arg0) {
				panopzioni.value.setText("" + (float)panopzioni.slider.getValue()/10);
			}
		}
		
		class valueListener implements ActionListener{
			public void actionPerformed(ActionEvent arg0) {
				panopzioni.slider.setValue((int)(10*Float.parseFloat(panopzioni.value.getText())));	
			}			
		}
	}
	
	public class FinePartita extends JFrame{
		Container cont;
		JPanel sud, center;
		JButton nuova,nuovo,opzioni,esci;
		JOptionPane uscita;
		StatBorder partita,sessione;
		ActionListener partitaL;
		JLabel pRighe[], sRighe[], vinto; 		
		
		public FinePartita(){		
			cont = getContentPane();
			cont.setLayout(new BorderLayout());
			
			vinto = new JLabel("Hai vinto");
			vinto.setFont(new Font("Roman", Font.BOLD, 30));
			vinto.setHorizontalAlignment(JLabel.CENTER);
			vinto.setPreferredSize(new Dimension(150,60));
			cont.add(vinto,BorderLayout.NORTH);
			
			center = new JPanel();
			center.setLayout(new BorderLayout());
			
			partita = new StatBorder(" ", 0, TitledBorder.LEFT, 14);
			sessione = new StatBorder(" Sessione ", 0, TitledBorder.LEFT, 14);
			partita.setLayout(new GridLayout(4,1));
			sessione.setLayout(new GridLayout(7,1));
			
			pRighe = new JLabel[4];
			sRighe = new JLabel[7];
			
			for(int i = 0; i < 4; i++){
				pRighe[i] = new JLabel();
				pRighe[i].setHorizontalAlignment(JLabel.CENTER);
				pRighe[i].setFont(new Font("Roman", Font.PLAIN, 14));
				pRighe[i].setPreferredSize(new Dimension(430,20));
				partita.add(pRighe[i]);				
			}
			
			for(int i = 0; i < 7; i++){
				sRighe[i] = new JLabel();
				sRighe[i].setHorizontalAlignment(JLabel.CENTER);
				sRighe[i].setFont(new Font("Roman", Font.PLAIN, 14));
				sRighe[i].setPreferredSize(new Dimension(430,20));
				sessione.add(sRighe[i]);				
			}
			
			center.add(partita,BorderLayout.NORTH);
			center.add(sessione,BorderLayout.CENTER);
			
			cont.add(center,BorderLayout.CENTER);
			
			sud = new JPanel();
			sud.setLayout(new GridLayout(1,4));
			
			nuova =   new JButton("Nuova Partita");
			nuovo =   new JButton("Nuova Sessione");
			opzioni = new JButton("Opzioni");
			esci =    new JButton("Esci dal gioco");
			
			sud.add(nuova);
			sud.add(nuovo);
			sud.add(opzioni);
			sud.add(esci);
			
			cont.add(sud,BorderLayout.SOUTH);
				
			partitaL = new partita();
			
			nuova.addActionListener(partitaL);
			nuovo.addActionListener(partitaL);
			opzioni.addActionListener(partitaL);
			esci.addActionListener(partitaL);
			pack();
			setAlwaysOnTop(true);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
					
			setResizable(false);		
		}
			
		public void endMatch(){
			partita.setName(" Partita tra " + nome + " e il Computer ");
			partita.repaint();
			
			if(log_windows.gioco_contrario){
				if(game.chiHaVinto() == colore_opposto)
					vinto.setText("Hai vinto");
				else
					vinto.setText("Hai perso");
			}
			else{
				if(game.chiHaVinto() == colore_scelto)
					vinto.setText("Hai vinto");
				else
					vinto.setText("Hai perso");
			}
			
			pRighe[0].setText(" Partita vinta in " + est.mUser + " mosse");
			pRighe[1].setText(" Partita numero " + ovest.partite);
			pRighe[2].setText(" Durata della partita: " + ovest.tempo.getText());
					
			sRighe[0].setText(" Partite giocate nella sessione : " + ovest.partite);
			sRighe[1].setText(" Tempo totale di gioco : " + ovest.tempoTot.getText());
			sRighe[2].setText(" Partite vinte dal computer : " + ovest.matchCPU );
			sRighe[3].setText(" Partite vinte da "+ nome + " : "+ ovest.matchUser);
			sRighe[4].setText(" Record di mosse: " + est.nRecord);
			sRighe[5].setText(" Detentore del record: " + est.RecordName);
		}
			
		class partita implements ActionListener{
			public void actionPerformed(ActionEvent e){
				
				Object sorgente = e.getSource();
			
				if (sorgente == nuova){
					nuovaPartita();
					setVisible(false);
				}
				if (sorgente == nuovo){
					nuovaSessione();
					setVisible(false);
				}
				if (sorgente == opzioni){
					panopzioni.setVisible(true);
					opt=true;
					setVisible(false);
				}
				if (sorgente == esci){
					int x=JOptionPane.showConfirmDialog(cont,"Sei sicuro di voler uscire ?","Uscire ?", JOptionPane.YES_NO_CANCEL_OPTION);
					if(x==JOptionPane.YES_OPTION){
						System.exit(0);
					}
				}
			}
		}
	}
	
	public static ImageIcon setIcon(int index,int size,int pezzo,boolean select){
		ImageIcon icona;
		Image img;
		String path = makePath(index,pezzo,select);	
		img=new ImageIcon(path).getImage();
		img=img.getScaledInstance(size ,size, Image.SCALE_SMOOTH);
		icona = new ImageIcon(img);	
		return icona;
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
	
	public void aggiornaStatitiche(){
		if(log_windows.gioco_contrario){
		est.pedB.setIcon(pedine[2]);
		est.pedN.setIcon(pedine[1]);
		est.damB.setIcon(pedine[4]);
		est.damN.setIcon(pedine[3]);
		est.repaint();
		}
		else{
			est.pedB.setIcon(pedine[1]);
			est.pedN.setIcon(pedine[2]);
			est.damB.setIcon(pedine[3]);
			est.damN.setIcon(pedine[4]);
			est.repaint();
		}
	}
	
	public void nuovaPartita(){
		if(ovest.partite==0 && !partitaInCorso){
			pregioco.nome.setText("");
			pregioco.bianco.setSelected(false);
			pregioco.nero.setSelected(false);
			pregioco.setVisible(true);
		}
		
		else{
			game.statoIniziale();
			game.aChiTocca = Scacchiera.BIANCO;
			
			if(colore_scelto == Scacchiera.NERO){
				game.aChiTocca = Scacchiera.NERO;
				ovest.hint.setEnabled(false);
				time.start();
			}
			
			else
				ovest.hint.setEnabled(true);
		
			for(int i=0;i<8;i++)
		    	for(int j=0;j<8;j++)
		    		if((i+j)%2==0){
		    			casellag[i][j].selezionata = false;
		    			casellag[i][j].ombra = false;
		    			casellag[i][j].repaint();
		    		}		
			
			log_windows.debugarea.setText("");
			log_windows.cronologia(nome);
			ovest.switchTurn(Scacchiera.BIANCO);
			
			if(log_windows.gioco_contrario)	ovest.setDisponibili(mosseDisponibili(colore_opposto));
			else 							ovest.setDisponibili(mosseDisponibili(colore_scelto));			
		}
		
		ovest.resetTimer();
		est.resetStat ();
		
		partitaInCorso=true;
		repaint();
		areagioco.repaint();
	}
	
	public void nuovaSessione(){
		pregioco.nome.setText("");
		pregioco.bianco.setSelected(false);
		pregioco.nero.setSelected(false);
		okok.setEnabled(false);
		pregioco.setVisible(true);
		game.statoIniziale();
		ovest.resetStat();
			
		for(int i=0;i<8;i++)
	    	for(int j=0;j<8;j++)
	    		if((i+j)%2==0){
	    			casellag[i][j].selezionata = false;
	    			casellag[i][j].ombra = false;
	    			casellag[i][j].repaint();
	    		}
	    
		log_windows.debugarea.setText("");
		log_windows.cronologia(nome);
		ovest.resetTimer();
		est.resetStat();
		est.setRecord(0);
		partitaInCorso=true;
		repaint();	
	}
	
	public int mosseDisponibili(int colore){
		int m=0,i,k;
		LinkedList temp;
		for (i=0; i<Scacchiera.DIM_LATO; i++)
		    for (k=0; k<Scacchiera.DIM_LATO; k++)
		    	if(game.colore(game.contenutoCaselle[i][k])==colore){
		    		temp=game.suggerisciMosse(new Casella(i,k));
		    		m+=temp.size();
		    	}
		    
		return m;
	}
	
	class TimerAction implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			sud.bar.setValue(sud.pos);
			sud.pos++;
			if(sud.pos < 101)	time.restart();
			else{			
				val=new ValutaMosse(game);
 				move2=val.mossaMigliore(game.aChiTocca);
				muoviemangia(move2);
				if(panopzioni.opzione1.isSelected())ovest.hint.setEnabled(true);
				sud.setBar(" IL COMPUTER STA ASPETTANDO LA TUA MOSSA ");
				time.stop();
			}			
		}		
	}
	
	class tempoLampeggio implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			int i;
			
			casellag[riga_sel][col_sel].selezionata = false;
			casellag[riga_sel][col_sel].repaint();			
			
			for( i = 1; disegna && i < mossaTemp.caselleToccate.size() ; i++){
				
				aux = (Casella)mossaTemp.caselleToccate.get(i);
				casellag[aux.riga][aux.colonna].ombra=true; 
				casellag[aux.riga][aux.colonna].icona.setVisible(true);
				casellag[aux.riga][aux.colonna].add(casellag[aux.riga][aux.colonna].icona);
				casellag[aux.riga][aux.colonna].icona.setIcon(ghost2);
				casellag[aux.riga][aux.colonna].repaint();
				areagioco.repaint();
				
				System.out.println("Casella accesa Riga:" + aux.riga + " Colonna : " + aux.colonna + "Size: " + mossaTemp.caselleToccate.size());
			}
			
			for( i = 1; !disegna && i < mossaTemp.caselleToccate.size() ; i++){
				
				aux = (Casella)mossaTemp.caselleToccate.get(i);
				casellag[aux.riga][aux.colonna].ombra=false; 
				casellag[aux.riga][aux.colonna].icona.setVisible(false);
				casellag[aux.riga][aux.colonna].repaint();
				System.out.println("Casella spenta Riga:" + aux.riga + " Colonna : " + aux.colonna + "Size: " + mossaTemp.caselleToccate.size());
				
			}
			
			
			areagioco.repaint();
			if(disegna){
				disegna = false;
				tl.restart();
			}
			else{	
				disegna = true;
				tl.stop();
			}
		}		
	} 	
}