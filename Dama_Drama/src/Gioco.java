
import java.util.*;

/** Sotto-classe di Scacchiera, aggiunge informazioni e metodi 
 * riguardanti il movimento dei pezzi: direzioni possibili di movimento,
 * controllo se una mossa sia possibile, generazione delle mosse
 * possibili ecc.
 */
public class Gioco extends Scacchiera
{
  /** Codifica la direzione nord ovest (riga decresce, colonna decresce) */
  public static final int NORD_OVEST = 0x1;
  /** Codifica la direzione nord est (riga decresce, colonna cresce) */
  public static final int NORD_EST = 0x2;
  /** Codifica la direzione sud ovest (riga cresce, colonna decresce) */
  public static final int SUD_OVEST = 0x4;
  /** Codifica la direzione sud est (riga cresce, colonna cresce) */
  public static final int SUD_EST = 0x8;
  /** Codifica nessuna direzio */
  public static final int FERMA = 0x0;
  
  /** Colore del giocatore a cui tocca fare la prossima mossa,
   * inizialmente tocca al bianco. */
  public int aChiTocca = BIANCO;

  /** Mossa in preparazione per essere poi eseguita. */
  public Mossa mossaInFormazione = null;

  /** Messaggio che commenta l'esito dell'ultima chiamata a una
   * delle due funzioni puoAndare. */
  public String msg = "";

  /** Crea nuovo gioco con scacchiera in situazione iniziale e primo
   * giocatore a cui tocca il bianco. */
  public Gioco()
  {
    super();
  }

  /** Controlla se la direzione assegnata e' verso avanti, cioe' verso la
   * sponda avversaria della scacchiera, per il giocatore del colore dato. 
   * @param direz direzione da controllare se e' verso avanti, 
   * deve essere una tra NORD_OVEST, NORD_EST, SUD_OVEST, SUD_EST
   * @param colore colore del pezzo da muovere 
   */
  public boolean eVersoAvanti(int direz, int colore)
  {
    switch (colore)
    {
      case Scacchiera.BIANCO: 
        return ( (direz==NORD_OVEST) || (direz==NORD_EST) );
      case Scacchiera.NERO:
        return ( (direz==SUD_OVEST) || (direz==SUD_EST) ); 
    }
    return false;
  }

  /** Controlla se muovere dalla casella c1 alla casella c2 e' verso 
   * avanti, cioe' verso la sponda avversaria della scacchiera, per il 
   * giocatore del colore dato. 
   * @param c1 casella dove sta ora il pezzo da muovere
   * @param c2 casella nella quale si vuole spostare il pezzo
   * @param colore colore del pezzo da spostare
   */
  public boolean eVersoAvanti(Casella c1, Casella c2, int colore)
  {
    switch (colore)
    {
      case BIANCO: return (c2.riga<c1.riga);
      case NERO: return (c2.riga>c1.riga);
    }
    return false;
  }

  /** Ritorna il colore opposto a quello dato: bianco per nero e 
   * nero per bianco, se il colore non esiste e' il colore della casella
   * vuota), lo ritorna.
   * @param colore colore di cui trovare l'opposto
   */
  public int coloreOpposto(int colore)
  {
    switch (colore)
    {
      case BIANCO: return NERO;
      case NERO: return BIANCO;
    }
    return NON_COLORE;
  }
            
  /** Ritorna la casella adiacente a quella data, secondo la direzione
   * di un punto cardinale.
   * @param c casella di cui trovare l'adiacente
   * @param direz direzione nella quale vogliamo l'adiacente,
   * deve essere una tra NORD_OVEST, NORD_EST, SUD_OVEST, SUD_EST
   */ 
  public Casella casellaAdiacente(Casella c, int direz)
  {
    Casella c2 = null;
    switch (direz)
    {
      case NORD_OVEST: c2 = new Casella(c.riga-1,c.colonna-1); break;
      case NORD_EST: c2 = new Casella(c.riga-1,c.colonna+1); break;
      case SUD_OVEST: c2 = new Casella(c.riga+1,c.colonna-1); break;
      case SUD_EST: c2 = new Casella(c.riga+1,c.colonna+1); break;
    }
    if ( (c2!=null) && !eDentro(c2)) c2 = null;
    return c2;
  }

  /** Ritorna se le due caselle sono direttamente adiacenti, cioe'
   * si toccano per un vertice.
   * @param c1 una delle due caselle
   * @param c2 l'altra casella 
   */
  public boolean adiacenteDiretta(Casella c1, Casella c2)
  {
    int distanzaRighe = c1.riga-c2.riga; 
    if (distanzaRighe<0) distanzaRighe = -distanzaRighe;
    int distanzaColonne = c1.colonna-c2.colonna; 
    if (distanzaColonne<0) distanzaColonne = -distanzaColonne;
    return ( (distanzaRighe==1) && (distanzaColonne==1) );
  }

  /** Ritorna in che direzione devo andare per muovermi
   * da c2 verso c1.
   * @param c1 casella dove sta ora il pezzo da muovere
   * @param c2 casella nella quale si vuole spostare il pezzo
   */
  public int direzGiacenza(Casella c1, Casella c2)
  {
    int distanzaRighe = c1.riga-c2.riga; 
    int distanzaColonne = c1.colonna-c2.colonna; 
    if (distanzaRighe>0)
    {
      if (distanzaColonne>0) return SUD_EST;
      else if (distanzaColonne<0) return SUD_OVEST;
    }
    else if (distanzaRighe<0)
    {
      if (distanzaColonne>0) return NORD_EST;
      else if (distanzaColonne<0) return NORD_OVEST;
    }
    return FERMA;
  }

  /** Ritorna la casella che si trova intrappolata fra due caselle.
   * Le due caselle date devono essere messe in diagonale con un'altra
   * casella nel mezzo, quest'ultima viene ritornata (la pedina
   * presente in essa e' mangiabile da una pedina avversaria che si
   * trovi in una delle due caselle date se l'altra e' libera).
   * @param c1 una delle due caselle
   * @param c2 l'altra casella 
   */
  public Casella mangiataFra(Casella c1, Casella c2)
  {
    float mezzaDistanzaRighe = ((float)(c2.riga-c1.riga))/2.0f; 
    float mezzaDistanzaColonne = ((float)(c2.colonna-c1.colonna))/2.0f; 
    if ((mezzaDistanzaRighe!=1.0f) && (mezzaDistanzaRighe!=-1.0f) )
      return null;
    if ((mezzaDistanzaColonne!=1.0f) && (mezzaDistanzaColonne!=-1.0f) )
      return null;
    Casella fra = new Casella(c1.riga+(int)mezzaDistanzaRighe,
                              c1.colonna+(int)mezzaDistanzaColonne);
    return fra;                              
  }
  
  /** Controlla se il pezzo che si trova sull'ultima casella 
   *  della mossa m puo' andare ancora nella direzione direz,
   *  in caso affermativo estende la mossa m.
   * @param m la mossa che provo a estendere
   * @param direz la direzione in cui provo a estenderla, deve
   * essere una tra NORD_OVEST, NORD_EST, SUD_OVEST, SUD_EST
   */
  public boolean puoAndare(Mossa m, int direz)
  {
    // se era pedina e si e' appena trasformata in dama no
    if (m.fattaDama)
    { msg = "No: appena promossa dama"; return false; }
    Casella c0 = (Casella)m.caselleToccate.getFirst();
    Casella c1 = (Casella)m.caselleToccate.getLast();
    int pezzo = contenuto(c0.riga,c0.colonna);
    int col = colore(pezzo);
    // se non tocca a questo colore no
//    if (col!=aChiTocca) 
//    { msg = "No: non tocca a questo colore"; return false; }
    // se e' pedina e la direz non e' verso avanti no
    if ( ePedina(pezzo) && (!eVersoAvanti(direz,col)) ) 
    { msg = "No: pedina va solo verso avanti";  return false; }
    // se uscirebbe dai limiti della scacchiera no
    Casella c2 = casellaAdiacente(c1,direz);
    if (c2==null) { msg = "No: adiacente non esiste";return false; }
    // se la casella adiacente in direzione direz e' libera...
    if (contenuto(c2)==VUOTA) 
    {
      // se il pezzo e' ancora al punto di partenza allora si
      if (m.caselleToccate.size()==1)
      {
        m.caselleToccate.addLast(c2);
        if (ePedina(pezzo) && bordoOpposto(c2,col)) m.fattaDama = true;
        { msg = "Si: adiacente esiste e vuota"; return true; }
      }
      // altrimenti no
      else { msg = "No: mossa terminata"; return false; }
    }
    // la casella adiacente in direzione direz e' occupata da
    // un pezzo, controllo se lo puo' mangiare...
    // se ha gia' fatto una mossa semplice allora no
    if ( (m.caselleToccate.size()>1) && 
         (m.caselleMangiate.size()==0) )
    { msg = "No: ha gia fatto mossa semplice"; return false; }
    int pezzoDaMangiare = contenuto(c2.riga,c2.colonna);
    // se nella casella da mangiare c'e' colore opposto...
    if (colore(pezzoDaMangiare)==coloreOpposto(col))
    {
      // se questo pezzo e' pedina e quello da mangiare dama allora no
      if ( ePedina(pezzo) && !ePedina(pezzoDaMangiare) )
      { msg = "No: pedina non mangia dama";  return false; }
      Casella c3 = casellaAdiacente(c2,direz);
      // se mangiando il pezzo finirebbe fuori da scacchiera allora no
      if (c3==null)
      { msg = "No: seconda adiacente non esiste"; return false; }
      // se il pezzo l'ha gia' mangiato (e' dama e sta facendo un ciclo)
      // allora no
      if (!ePedina(pezzo))
      {
        ListIterator iter = m.caselleMangiate.listIterator();
        while (iter.hasNext())
        {
          Casella cm = (Casella)iter.next();
          if (Casella.stessa(cm,c2))
          { msg = "No: ciclo";  return false; }
        }
      }
      // se mangiando il pezzo finisce su casella vuota allora si 
      if (contenuto(c3)==VUOTA)
      {
        m.caselleToccate.addLast(c3);
        m.caselleMangiate.addLast(c2);
        if (ePedina(pezzo) && bordoOpposto(c3,col)) m.fattaDama = true;
        { msg = "Si: mangia"; return true; }
      }
      // se mangiando finisce su casella occupata allora no
      else { msg = "No: seconda adiacente non vuota"; return false; }
    }
    // se nella casella da mangiare c'e' stesso colore allora no
    else { msg = "No: non mangia stesso colore"; return false; }
  }

  /** Controlla se il pezzo che si trova sull'ultima casella 
   *  della mossa m puo' andare nella casella cas,
   *  in caso affermativo estende la mossa m.
   * @param m la mossa che provo a estendere
   * @param cas la casella in cui provo a estenderla
   */
  public boolean puoAndare(Mossa m, Casella cas)
  {
    if (m.fattaDama)
    { msg = "No: appena promossa dama"; return false; }
    if (contenuto(cas)!=VUOTA) 
    { msg = "No: casella destinazione non vuota"; return false; }
    Casella c0 = (Casella)m.caselleToccate.getFirst();
    Casella c1 = (Casella)m.caselleToccate.getLast();
    int pezzo = contenuto(c0.riga,c0.colonna);
    int col = colore(pezzo);
//    if (col!=aChiTocca) 
//    { msg = "No: non tocca a questo colore"; return false; }
    if (ePedina(pezzo) && (!eVersoAvanti(c1,cas,col)) ) 
    { msg = "No: pedina muove solo verso avanti"; return false; }
    if (adiacenteDiretta(c1,cas))
    {
      // se il pezzo e' ancora al punto di partenza allora si    
      if (m.caselleToccate.size()==1)
      {
        m.caselleToccate.addLast(cas);
        if (ePedina(pezzo) && bordoOpposto(cas,col)) m.fattaDama = true;      
        { msg = "Si: adiacente diretta vuota"; return true; }
      }
      // altrimenti no
      else { msg = "No: mossa terminata"; return false; }      
    }
    // la casella destinazione non e' l'adiacente diretta, 
    // sta cercando di mangiare un pezzo, controllo se puo'...
    // se ha gia' fatto una mossa semplice allora no
    if ( (m.caselleToccate.size()>1) && 
         (m.caselleMangiate.size()==0) )
    { msg = "No: ha gia fatto mossa semplice"; return false; }
    Casella c2 = mangiataFra(c1,cas);
    if (c2==null)
    { msg = "No: non esiste casella frapposta"; return false; }
    int pezzoDaMangiare = contenuto(c2);
    // se nella casella da mangiare c'e colore opposto...
    if (colore(pezzoDaMangiare) == coloreOpposto(col))
    {
      // se questo pezzo e' pedina e quello da mangiare dama allora no
      if ( ePedina(pezzo) && !ePedina(pezzoDaMangiare) )
      { msg = "No: pedina non mangia dama"; return false; }
      // se il pezzo l'ha gia' mangiato (e' dama e sta facendo un ciclo)
      // allora no
      if (!ePedina(pezzo))
      {
        ListIterator iter = m.caselleMangiate.listIterator();
        while (iter.hasNext())
        {
          Casella cm = (Casella)iter.next();
          if (Casella.stessa(cm,c2))
          { msg = "No: ciclo";  return false; }
        }
      }
      // ho gia' controllato che casella destinazione vuota, quind si
      m.caselleToccate.addLast(cas);
      m.caselleMangiate.addLast(c2);
      if (ePedina(pezzo) && bordoOpposto(cas,col)) m.fattaDama = true;      
      { msg = "Si: mangia pezzo"; return true; }
    }
    else // stesso colore o vuota
    { msg = "No: casella frapposta stesso colore o vuota"; return false; }
  }

  /** Esegue la mossa assegnata, cambia anche il giocatore a cui tocca.
   * La mossa deve essere valida e deve toccare al giocatore del colore
   * del pezzo presente sulla casella di partenza della mossa.
   * @param m la mossa da eseguire */
  public void esegui(Mossa m)
  {
    Casella c0 = (Casella)m.caselleToccate.getFirst();
    int pezzo = contenuto(c0.riga,c0.colonna);
    metti(c0,VUOTA);
    Casella c1 = (Casella)m.caselleToccate.getLast();
    if (m.fattaDama) pezzo = promossaDama(pezzo);
    metti(c1,pezzo);
    ListIterator iter = m.caselleMangiate.listIterator();
    while (iter.hasNext())
    {
        Casella c = (Casella)iter.next();
        metti(c,VUOTA);
    }
    if (aChiTocca==BIANCO) aChiTocca = NERO;
    else aChiTocca = BIANCO;
  }
  
  /** Se uno dei due giocatori ha vinto, ritorna il colore
   * del vincitore, altrimenti ritorna nessun colore.
   * Se uno dei due giocatori non ha mosse (eventualmente perche'
   * non ha piu' pezzi in scacchiera) allora il vincitore e'
   * l'altro, altrimenti nessuno ha vinto finora.
   */
  public int chiHaVinto()
  {
    int r, c;
    LinkedList possibili = null;
    boolean biancoHaPezzi = false; 
    boolean neroHaPezzi = false;
    boolean biancoHaMosse = false;
    boolean neroHaMosse = false;
System.out.println("CHI HA VINTO");
    for (r=0; r<DIM_LATO; r++)
    for (c=0; c<DIM_LATO; c++)
    {        
      if (colore(contenuto(r,c))==BIANCO)
      { 
        biancoHaPezzi = true;
        possibili = suggerisciMosse(new Casella(r,c));
System.out.println("Mosse bianche da ("+r+","+c+"): " + possibili.size());
        if (possibili.size()>0) biancoHaMosse = true;
      }
      else if (colore(contenuto(r,c))==NERO)
      {
        neroHaPezzi = true;
        possibili = suggerisciMosse(new Casella(r,c));
System.out.println("Mosse nere da ("+r+","+c+"): " + possibili.size());
        if (possibili.size()>0) neroHaMosse = true;
      } 
      // se possono muovere entrambi nessuno ha vinto
      if (biancoHaMosse && neroHaMosse) return NON_COLORE;
    }
    // almeno uno dei due non ha mosse, vince l'altro
System.out.println("Fine ciclo...");
System.out.println("Bianco ha pezzi: " + biancoHaPezzi + " e mosse: " + biancoHaMosse);
System.out.println("Nero ha pezzi: " + neroHaPezzi + " e mosse: " + neroHaMosse);
    if (!biancoHaPezzi) return NERO;
    if (!neroHaPezzi) return BIANCO;
    if (!biancoHaMosse) return NERO;
    if (!neroHaMosse) return BIANCO;
    // non dovrebbe succedere
    return NON_COLORE;
  }
  
  /** Restituisce lista di tutte le mosse possibili per il pezzo
   *  che si trova nella casella specificata.
   * @param cas la casella di partenza
   */
  public LinkedList suggerisciMosse(Casella cas)
  {
    LinkedList mossePossibili = new LinkedList();
    int pezzo = contenuto(cas);
    // nessuna mossa da casella vuota
    if (pezzo==VUOTA) return mossePossibili;
    // esplora l-albero di tutte le possibilita'
    Mossa m = new Mossa(cas);
    suggerisciMosseRic(m, mossePossibili);
    return mossePossibili;
  }
  
  /** Funzione ausiliaria, ricirsiva, trova le mosse possibili per il pezzo
   * che si trova nella casella specificata, e le aggiunge alla lista.
   * @param m0 la mossa che provo a estendere 
   * @param mosse la lista di mosse in cui salvo man mano le mosse
   * massimali (cioe' non ulteriormente estendibili) costruite 
   * estendendo la mossa m0 (anche m0 se e' massimale)
   */
  protected void suggerisciMosseRic(Mossa m0, LinkedList mosse)
  {
    int i;
    boolean[] ciSonoMosse = new boolean[4];
    Mossa[] quattro = new Mossa[4];
    for (i=0; i<4; i++) 
    {  ciSonoMosse[i] = false;
       quattro[i] = new Mossa(m0);
    }
    if ( puoAndare(quattro[0], NORD_EST) ) ciSonoMosse[0] = true;
    if ( puoAndare(quattro[1], NORD_OVEST) ) ciSonoMosse[1] = true;
    if ( puoAndare(quattro[2], SUD_EST) ) ciSonoMosse[2] = true;
    if ( puoAndare(quattro[3], SUD_OVEST) ) ciSonoMosse[3] = true;
    if (!ciSonoMosse[0] && !ciSonoMosse[1] &&
        !ciSonoMosse[2] && !ciSonoMosse[3]) 
    {
       if (m0.caselleToccate.size()>1)
          // la mossa m0 non si poteva estendere
          mosse.addLast(m0);
    }
    else
    {
      for (i=0; i<4; i++) // la mossa m0 l'ho estesa in direz
      {
        if (ciSonoMosse[i]) // provo a estenderla ulteriormente
        {
          suggerisciMosseRic(quattro[i], mosse);
        }
      } // end for
    }
  }

}
