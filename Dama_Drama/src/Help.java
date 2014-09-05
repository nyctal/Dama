import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;



public class Help extends JFrame
{	
	

	Container container;
	
		
	JPanel research,vuoto, sinistro;
	JButton previous, next;
	JTextArea areatesto;
    JScrollPane scroll;			
	File fd;
	JTextField ricerca, messaggi;
	JList lista;
	String  stringa[] = {"Introduzione", "Regole e modalità di gioco", "Uso dell' interfaccia", "Opzioni", "Cronologia", "Requisiti Minimi e Note finali"}, s;
	
	FileReader in;
	
	
	JOptionPane aboutPane, sost, save;
	
	ActionListener menu, button, lisricerca;
	ListSelectionListener lislista;
	ChangeListener tablis;
	KeyListener keyLis;
	WindowAdapter closing;
	Border  bordo;
	TitledBorder titolo,titolo2;

	final Highlighter hilit;
	final Highlighter.HighlightPainter painter;
	double width, height;
    
    int pos[] = new int[100], posindex = 0;
       
	public void apri(String filename){
    	
		
		
		fd = new File(filename);
		
		
			char[] buffer = new char[4096];
            int len;
            
            areatesto.setText("");
            ricerca.setText("");	
            posindex = 0;
            previous.setEnabled(false);
           
			try{	in = new FileReader(fd);	}catch (FileNotFoundException e2){	JOptionPane.showMessageDialog(null, "Il file "+ filename + " non esiste !", "File mancante !", JOptionPane.WARNING_MESSAGE);
				}
            		         
            try {
				while((len = in.read(buffer)) != -1){
				    s = new String(buffer, 0, len);
				    areatesto.append(s);
				}
			}catch (IOException e1) {	e1.printStackTrace();	}
			
            areatesto.setCaretPosition(0);
			 
		
	}
    
	public Help(int dim){
		
		String windows="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try{
			UIManager.setLookAndFeel(windows);
			SwingUtilities.updateComponentTreeUI(Help.this);
		}
		catch(Exception e){}
		
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension screenSize = t.getScreenSize();

		width  = screenSize.getWidth();
		height = screenSize.getHeight();
		
		class MyKeyListener extends KeyAdapter{
	    	public void keyTyped(KeyEvent k) {
	    		if((ricerca.getText().length())==0 ){
	    			ricerca.setBackground(Color.WHITE);
	    			messaggi.setText("");
	    		}
	    		areatesto.setCaretPosition(0);
			}
	    	
	    }
		
//------ActionListener----------------------------------------------------------
	
		
		  
		class MyListenerCerca implements ActionListener{
			public void actionPerformed(ActionEvent e){
				Object sorgente = e.getSource();
				
				if(sorgente == next){
				    s = ricerca.getText();
			        if(s.length() <= 0){
			        	messaggi.setText("Niente da cercare");
			            return;
			        }
			        
			        String content = areatesto.getText();
			        int index = content.indexOf(s, pos[posindex]);
			        
			        if(index >= 0){
			            int end = index + s.length();
						
						areatesto.setCaretPosition(end);
						ricerca.setBackground(Color.CYAN);
						messaggi.setText("'" + s + "' trovato.");
						areatesto.select(index, end);
						posindex++;
						
						if(posindex >= 2)	previous.setEnabled(true);
						
						pos[posindex] = end;
						areatesto.requestFocusInWindow();
			        } 
			        
			        else{
			            ricerca.setBackground(Color.PINK);
			            messaggi.setText("Nessun risultato per "+ "'" + s + "'");
			        }
			    }
				
				else if(sorgente == previous){
					
					posindex--;
					
					if(posindex <= 1)	previous.setEnabled(false); 
			        
					s = ricerca.getText();
					
			        if(s.length() <= 0){
			        	messaggi.setText("Niente da cercare");
			            return;
			        }
			        
			        String content = areatesto.getText();
			        int index = content.indexOf(s, pos[posindex]-s.length());
			        if (index >= 0) {   // match found
			            int end = index + s.length();
						areatesto.setCaretPosition(end);
						ricerca.setBackground(Color.CYAN);
						messaggi.setText("'" + s + "' trovato.");
						areatesto.select(index, end);
						pos[posindex] = end;
						areatesto.requestFocusInWindow();
			        } else {
			            ricerca.setBackground(Color.PINK);
			            messaggi.setText("nessun risultato per "+ "'" + s + "'");
			        }
			    }								
			}				
		}
		class MyListenerLista implements ListSelectionListener{
			public void valueChanged(ListSelectionEvent e){
				int indice;
				
		        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		        indice=lista.getSelectedIndex();
		        apri("help_"+indice+".txt");
		        
			}
		}
		
	   
		
		
		class ClosingListener extends WindowAdapter{
		  public void windowClosing(WindowEvent e)
            {   
            	setVisible(false);
            }
		  
		}

	
		bordo = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		titolo = BorderFactory.createTitledBorder(bordo,"Argomenti");
		titolo2 = BorderFactory.createTitledBorder(bordo,"Ricerca");
		
		setTitle("Gioco della Dama - Help");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		container = getContentPane();
	    container.setLayout(new BorderLayout());
	    


	    
	    
	       
//------West Bar----------------------------------------------------------------
	    sinistro = new JPanel();
	    sinistro.setLayout(new BorderLayout());
	    
	    lista = new JList(stringa);
	    	        
	    sinistro.add(lista,BorderLayout.NORTH);
	    
	    lislista = new MyListenerLista();

	    lista.addListSelectionListener(lislista);
	    
	    research = new JPanel();
	    research.setLayout(new GridLayout(3,1));
	    
	    vuoto = new JPanel();
	    sinistro.add(vuoto,BorderLayout.CENTER);
	    ricerca = new JTextField();
	    previous = new JButton("Previous");
	    previous.setEnabled(false);
	    next = new JButton("Next");
	    
	    lisricerca = new MyListenerCerca();
	    
	    keyLis = new MyKeyListener();
	    
	    previous.addActionListener(lisricerca); 
	    next.addActionListener(lisricerca);
	    ricerca.addKeyListener(keyLis);
	   
	    research.add(ricerca);
	    research.add(previous);
	    research.add(next);
	    
	    sinistro.add(research,BorderLayout.SOUTH);
	    
//------Center Bar----------------------------------------------------------------
	   
	    
	    areatesto = new JTextArea(30,85);
	    scroll = new JScrollPane(areatesto,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    messaggi = new JTextField();
	   
	    
	    hilit = new DefaultHighlighter();
	    painter = new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY);
	    	    
	    sost=new JOptionPane();
	    aboutPane = new JOptionPane ();
	    save=new JOptionPane();
	    
	    messaggi.setEditable(false);
	    areatesto.setEditable(false);
	 
	    lista.setFont(new Font("Roman",Font.ITALIC,13));
	    container.add(sinistro,BorderLayout.WEST);
	    container.add(scroll,BorderLayout.CENTER);
	    container.add(messaggi,BorderLayout.SOUTH);
	    research.setPreferredSize(new Dimension((int)(dim/100)*30,150));
	    research.setBorder(titolo2);
	    lista.setBorder(titolo);
	    lista.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    
	    closing=new ClosingListener();
	    addWindowListener(closing);
	   
	   
	    
	    pack();
		
	}

	
}
