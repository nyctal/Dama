import java.util.*;

/** Questa classe contiene utilita' per decidere la mossa migliore
 * tra quelle possibili.<BR>
 * Ogni situazione di scacchiera ha, dal punto di vista di un giocatore,
 * una valutazione numerica che si calcola contando il numero di pedine
 * e dame sue e avversarie, e contando se eventualmente la partita
 * e' stata vinta da lui o dall'avversario.<BR>
 * Una mossa e' valutata nel modo seguente: si simula la mossa,
 * si simulano tutte le possibili contromosse avversarie,
 * si considera la situazione peggiore in cui ci si potrebbe 
 * trovare (dopo questa mossa e la contromossa avversaria).
 * La mossa migliore, per questo giocatore, e' quella che genera, dopo
 * la peggiore contromossa avversaria, la situazione migliore per me.
 */
public class ValutaMosse
{
  /** Il gioco di cui cerco la mossa migliore allo stato attuale. */
  public Gioco gioco;

  /** Generatore casuale per scegliere tra due mosse con stessa 
   * valutazione. */
  protected Random generatoreCasuale;
    
  /** La mossa migliore tra tutte quelle valutate finora. */
  public Mossa migliorMossa = null;
  /** La valutazione della mossa migliore tra tutte quelle
   * valutate finora. */
  public int migliorValutaz;

  /** La contromossa peggiore (migliore per l'avversario) fra tutte le
   * contromosse finora valutate alla mossa memorizzata in migliorMossa. */
  protected Mossa peggiorContromossa = null;
   /** La valutazione della contromossa peggiore (migliore per
    * l'avversario) fra tutte le contromosse finora valutate alla mossa
    * memorizzata in migliorMossa. */
  protected int peggiorValutaz;

  /** Mossa simulata di questo giocatore. */
  protected Mossa simulata1 = null;
  /** Array che salva i pezzi avversari mangiati nella mossa
   * simulata di questo gicatore. */
  protected int[] pezziMangiati1 = null;
  /** Contromossa avversaria simulata */
  protected Mossa simulata2 = null;
  /** Array che salva i pezzi avversari mangiati nella contromossa
    * simulata dell'avversario. */
  protected int[] pezziMangiati2 = null;
    
  /** Crea valutatore di mosse per il gioco dato.
   * @param g il gioco in cui si vogliono trovare le mosse migliori */
  public ValutaMosse(Gioco g) 
  {
    gioco = g;
    generatoreCasuale = new Random();
  }

  /** Salva in un array le posizioni presenti in lista (serve per 
   * salvare le caselle mangiate da una mossa).
   * @param array array nel quale salvare le posizioni
   * @param caselle lista che contiene le posizioni da salvare  */
  protected void salvaPezzi(int[] array, LinkedList caselle)
  {
    ListIterator iter = caselle.listIterator();
    int i = 0;
    while (iter.hasNext())
    {  array[i++] = gioco.contenuto((Casella)iter.next());  }
  }
  
  /** Simula la mossa sulla scacchiera. Praticamente esegue la mossa,
   * ma prima salva tutte le informazioni per poterla disfare.
   * Posso simulare al piu' due mosse (quella del giocatore e la
   * contromossa del suo avversario).
   * @param la mossa da simulare */
  protected void simula(Mossa m)
  {
    // posso simulare al piu' 2 mosse consecutive memorizzandole
    if (simulata1==null)
    {
      simulata1 = new Mossa(m);
//System.out.println("Simulo prima mossa");m.stampaMossa();
      if (m.caselleMangiate!=null)
      {
        pezziMangiati1 = new int[m.caselleMangiate.size()];
        salvaPezzi(pezziMangiati1, m.caselleMangiate);
      }
    }
    else if (simulata2==null)
    {
//System.out.println("Simulo seconda mossa");m.stampaMossa();
      simulata2 = new Mossa(m);
      if (m.caselleMangiate!=null)
      {
        pezziMangiati2 = new int[m.caselleMangiate.size()];
        salvaPezzi(pezziMangiati2, m.caselleMangiate);
      }
    }
    else return; 
    gioco.esegui(m);
//    gioco.stampaScacchiera("DOPO SIMULAZIONE");
  }
  
  /** Disfa la mossa simulata. Posso simulare al piu' due mosse,
   * viene disfatta l'ultima mossa simulata e non ancora disfatta.
   */
  protected void disfaSimulazione()
  {
    Mossa m;
    // posso disfare, in ordine inverso, solo le mosse simulate 
    if (simulata2!=null)  m = simulata2;
    else if (simulata1!=null)  m = simulata1;
    else return; 

//if (simulata2!=null)  System.out.println("Disfo mossa 2");
//else if (simulata1!=null)   System.out.println("Disfo mossa 1");
//m.stampaMossa();
    Casella c0 = (Casella)m.caselleToccate.getFirst();
    Casella c1 = (Casella)m.caselleToccate.getLast();
    int pezzo = gioco.contenuto(c1.riga,c1.colonna);
    if (m.fattaDama) pezzo = gioco.declassataPedina(pezzo);
    gioco.metti(c0,pezzo);
    gioco.metti(c1,Scacchiera.VUOTA);
    int i = 0;
    ListIterator iter = m.caselleMangiate.listIterator();
    while (iter.hasNext())
    {
      Casella c = (Casella)iter.next();
      if (simulata1==m) gioco.metti(c,pezziMangiati1[i++]);
      else gioco.metti(c,pezziMangiati2[i++]);
    }
    if (gioco.aChiTocca==Scacchiera.BIANCO)
       gioco.aChiTocca = Scacchiera.NERO;
    else gioco.aChiTocca = Scacchiera.BIANCO;
    // posso disfare, in ordine inverso, solo le mosse simulate 
    if (simulata2!=null)  simulata2 = null;
    else if (simulata1!=null)  simulata1 = null;
//  gioco.stampaScacchiera("DOPO DISFA SIMULAZIONE");
  }

  /** Valuta la situazione attuale della scacchiera dal punto
   * di vista del giocatore di colore assegnato.
   * La valutazione dell'avversario e' esattamente simmetrica
   * (stesso valore cambiato di segno).
   * @param colore colore del giocatore di cui si vuole trovare 
   * la miglior mossa possibile nello stato attuale
   */
  public int valuta(int colore)
  {
    int pedineBianche = 0;
    int pedineNere = 0;
    int dameBianche = 0;
    int dameNere = 0;
    int r, c;
//System.out.println("Valuto situazione......");
    for (r=0; r<gioco.DIM_LATO; r++)
    for (c=0; c<gioco.DIM_LATO; c++)
    {
      if (gioco.contenuto(r,c)==gioco.PEDINA_BIANCA)
         pedineBianche++;
      else if (gioco.contenuto(r,c)==gioco.PEDINA_NERA)
         pedineNere++;
      else if (gioco.contenuto(r,c)==gioco.DAMA_BIANCA)
         dameBianche++;
      else if (gioco.contenuto(r,c)==gioco.DAMA_NERA)
         dameNere++;
    }
//System.out.println("Bianco ha "+pedineBianche+" pedine e "+dameBianche+" dame");
//System.out.println("Nero ha "+pedineNere+" pedine e "+dameNere+" dame");
    int totale = 0;
    // dal punto di vista bianco: vittoria
    if ( (pedineNere==0) && (dameNere==0) )
       totale = 9999;
    // vittoria avversaria
    else if ( (pedineBianche==0) && (dameBianche==0) )
       totale = -9999; 
    // caso normale
    else totale = ( 2*(dameBianche-dameNere)+
                      (pedineBianche-pedineNere) );
//if (colore==gioco.BIANCO) System.out.println("Valutaz bianco: "+ totale);
//else System.out.println("Valutaz nero: " + (-totale));
    if (colore==gioco.BIANCO) return totale;
    else return -totale;
  }

  /** Controlla se il valore e' migliore della valutazione di quella 
   * che finora e' la mossa migliore, memorizzata in migliorMossa.
   * Se migliorMossa e' null ritorna sempre true.
   * Se la valutazione e' uguale sceglie a caso. 
   * val il valore da confrontare con la valutazione attuale
   * della mossa migliore, memorizzata in migliorValutaz
   */
  protected boolean eNuovaMigliore(int val)
  {
    if (migliorMossa==null) return true;
    if (val > migliorValutaz) return true;
    if (val > migliorValutaz) return false;
    // se valutazione uguale scegli a caso
    return ((generatoreCasuale.nextInt()%2)==0);
  }
  
  /** Controlla se il valore e' peggiore della valutazione di quella 
   * che finora e' la contromossa peggiore (migliore per l'avversario), 
   * memorizzata in peggiorContromossa.
   * Se peggiorContromossa e' null ritorna sempre true.
   * Se la valutazione e' uguale sceglie a caso. 
   * val il valore da confrontare con la valutazione attuale
   * della contromossa meggiore, memorizzata in peggiorValutaz
   */
  protected boolean eNuovaPeggiore(int val)   
  {
    if (peggiorContromossa==null) return true;
    if (val < peggiorValutaz) return true;
    if (val > peggiorValutaz) return false;
    // se valutazione uguale scegli a caso
    return ((generatoreCasuale.nextInt()%2)==0);
  }
    
  /** Calcola e ritorna la mossa valutata migliore per il giocatore
   * del colore assegnato.
   * @param colore colore del giocatore di cui si vuole trovare  
   * la miglior mossa possibile nello stato attuale
   */
  public Mossa mossaMigliore(int colore)
  {
//System.out.println("Mossa migliore, valutaz iniziale= "+valuta(colore));
    int r1, c1, r2, c2;
    int valutaz;
    migliorMossa = null;
    LinkedList possibili1 = null;
    for (r1=0; r1<gioco.DIM_LATO; r1++)
    for (c1=0; c1<gioco.DIM_LATO; c1++)
    {        
      if (gioco.colore(gioco.contenuto(r1,c1))==colore)
      { 
        possibili1 = gioco.suggerisciMosse(new Casella(r1,c1));
        if (possibili1!=null)
        {
          LinkedList possibili2 = null;
          ListIterator iter1 = possibili1.listIterator();
          while (iter1.hasNext())
          {
            Mossa m1 = (Mossa)iter1.next();
            simula(m1);
            peggiorContromossa = null;
            for (r2=0; r2<gioco.DIM_LATO; r2++)   
            for (c2=0; c2<gioco.DIM_LATO; c2++)   
            {        
              if (gioco.colore(gioco.contenuto(r2,c2))==gioco.coloreOpposto(colore))
              {
                possibili2 = gioco.suggerisciMosse(new Casella(r2,c2));
                if (possibili2!=null)
                {
                  ListIterator iter2 = possibili2.listIterator();
                  while (iter2.hasNext())
                  {
                    Mossa m2 = (Mossa)iter2.next();
                    simula(m2);
                    valutaz = valuta(colore);
                    if (eNuovaPeggiore(valutaz))
                    {  peggiorContromossa = m2; peggiorValutaz = valutaz;  }
                    disfaSimulazione();
                  } // fine while scorrimeto possibili2
                } // fine if possibili2
                else
                // no contromosse, il colore ha vinto
                {
                  peggiorValutaz = 9999;
                }
              } // fine se colore opposto
            } // fine for, fine esame possibili contromosse a m1
            if (eNuovaMigliore(peggiorValutaz))
            {  migliorMossa = m1; migliorValutaz = peggiorValutaz;  }
            disfaSimulazione();
          } // fine while scorrimento possibili1
        } // fine if possibili1
//else System.out.println("Nessuna mossa da (" + r1 + "," + c1 + ")");
      } // fine se colore giusto
    } // fine for, fine esame mosse possibili
//System.out.println("Mossa migliore:");migliorMossa.stampaMossa();
    return migliorMossa;
  }
  
}
