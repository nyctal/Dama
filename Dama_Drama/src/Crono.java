import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Crono extends JFrame{
	
	Container container;
	ActionListener salvataggio;
	JMenuBar menubar;
	JMenu file;
	JMenuItem salva;
	JCheckBoxMenuItem primopiano;
	JFileChooser scegli;
	FileReader in;
	File fd;
	String s;
	JOptionPane sost;
	int ch=0,ext=0;
	JScrollPane scrollbug;
	JTextArea debugarea;
	Font carattere;
	Gioco game;
	boolean gioco_contrario, scrivi = true;

	
	public Crono(Gioco g, Dimension screenSize){
		
			game=g;
			
			container = getContentPane();
			
			carattere=new Font(Font.MONOSPACED,Font.BOLD,11);
			menubar=new JMenuBar();
			salva=new JMenuItem("Salva come testo");
			primopiano=new JCheckBoxMenuItem("Sempre in primo piano");
			file=new JMenu("File");
			scegli=new JFileChooser();
			sost=new JOptionPane();
			debugarea = new JTextArea();
			scrollbug = new JScrollPane(debugarea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			setJMenuBar(menubar);
			menubar.add(file);
			file.add(salva);
			file.addSeparator();
			file.add(primopiano);
			
		
			container.add(scrollbug);
			
			
			
			class Salvataggio implements ActionListener{
				public void actionPerformed(ActionEvent e2){
					Object sorgente = e2.getSource();
					if(sorgente == primopiano){
						
						setAlwaysOnTop(primopiano.isSelected());
					}
					
					else if(sorgente == salva){
						
					
						ch=scegli.showSaveDialog(salva);
											
						if(ch==JFileChooser.APPROVE_OPTION){
							fd = scegli.getSelectedFile();
							s = new String(debugarea.getText());
							
							if(fd.exists()){							
								ext = JOptionPane.showConfirmDialog(null, "File già esistente.\n Sostituirlo?", "File gia' esistente", JOptionPane.YES_NO_CANCEL_OPTION);
							}
							
							if(ext != JOptionPane.NO_OPTION && ext != JOptionPane.CANCEL_OPTION){
																		
							FileWriter writer = null;
							try {	writer = new FileWriter(fd);	}catch (IOException e1) {	e1.printStackTrace();  } 
							PrintWriter out = new PrintWriter(writer);
							out.print(s);
							out.flush();
										
							}
						}
					}
					
					
				} 
			}
			salvataggio=new Salvataggio();
			salva.addActionListener(salvataggio);
			primopiano.addActionListener(salvataggio);
			primopiano.setSelected(false);
			debugarea.setEditable(false);
			debugarea.setFont(carattere);
			setPreferredSize(new Dimension(280,460));
			setLocation(screenSize.width-300, screenSize.height-500);
			//setDefaultCloseOperation(HIDE_ON_CLOSE);
			setResizable(false);
			pack();
			
			
			
		}
	

public void cronologia(String nome){ 
	int r,c;
	
		Date data=new Date();
	
		debugarea.append("\n    User: "+nome);
		debugarea.append("\n    Data: " + data.getDate() +"/"+(data.getMonth()+1)+"/"+(data.getYear()+1900));
		debugarea.append("\n    Ora:  " +data.getHours() +":"+data.getMinutes()+":"+data.getSeconds());
		debugarea.append("\n\n        Scacchiera \n\n");

		for (r=0; r<Scacchiera.DIM_LATO; r++)
		{
			debugarea.append(" ");
			for (c=0; c<Scacchiera.DIM_LATO; c++) debugarea.append("----");
		    debugarea.append("-\n");
		    debugarea.append(" ");
		    for (c=0; c<Scacchiera.DIM_LATO; c++)
	    {
	    	if(!gioco_contrario){
	      switch (game.contenuto(r,c))
	      {
	        case Scacchiera.PEDINA_BIANCA: debugarea.append("| b "); break;
	        case Scacchiera.PEDINA_NERA: debugarea.append("| n "); break; 
	        case Scacchiera.DAMA_BIANCA: debugarea.append("| B "); break; 
	        case Scacchiera.DAMA_NERA: debugarea.append("| N "); break; 
	        case Scacchiera.VUOTA: debugarea.append("|   "); break;
	      }
	    }
	    	else{
	    		switch (game.contenuto(r,c))
	    	      {
	    	        case Scacchiera.PEDINA_BIANCA: debugarea.append("| n "); break;
	    	        case Scacchiera.PEDINA_NERA: debugarea.append("| b "); break; 
	    	        case Scacchiera.DAMA_BIANCA: debugarea.append("| N "); break; 
	    	        case Scacchiera.DAMA_NERA: debugarea.append("| B "); break; 
	    	        case Scacchiera.VUOTA: debugarea.append("|   "); break;
	    	      }
	    	}
	   }
	    debugarea.append("|\n");
	  }
		debugarea.append(" ");
	  for (c=0; c<Scacchiera.DIM_LATO; c++)  debugarea.append("----");
	  debugarea.append("-\n");
	 
		}
	

	
}