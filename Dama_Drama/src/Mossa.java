import java.util.*;

/** Questa classe rappresenta una mossa, ha una lista di caselle toccate
 * dal pezzo che si muove (di cui la prima e' la casella da cui parte
 * e l'ultima e' la casella dove va a finire) e una lista di caselle
 * corrispondenti alle posizioni delle pedine mangiate.
 * Inoltre un booleano dice se questa mossa termina con la promozione di
 * una pedina a dama (perche' tocca la sponda opposta della scacchiera).
 */
public class Mossa
{
  /** Lista delle caselle toccate dal pezzo che si muove. */
  public LinkedList caselleToccate;
  /** Lista delle caselle in cui si trovano pezzi mangiati dal 
   * pezzo che si muove. */
  public LinkedList caselleMangiate;
  /** Indica se la mossa termina promuovendo una pedina a dama. */
  public boolean fattaDama;
  
  /** Costruisce mossa degenere con solo la casella di partenza.
   * @param partenza la casella iniziale */
  public Mossa(Casella partenza)
  {  
    caselleToccate = new LinkedList();
    caselleMangiate = new LinkedList();
    caselleToccate.addLast(partenza);
    fattaDama = false;
  }

  /** Costruisce mossa copiandone un'altra.
   * @param daCopiare la mossa di cui fare la copia */
  public Mossa(Mossa daCopiare)
  {  
    caselleToccate = new LinkedList(daCopiare.caselleToccate);
    caselleMangiate = new LinkedList(daCopiare.caselleMangiate);
    fattaDama = daCopiare.fattaDama;
  }

  /** Funzione utile per debug, stampa la mossa come elenco di
   * caselle toccare e mangiate. */
  public void stampaMossa()
  {
    ListIterator iter;
    Casella c;
    System.out.print(" Caselle toccate: ");
    iter = caselleToccate.listIterator();
    while (iter.hasNext())
    {
      c = (Casella)iter.next();
      System.out.print(" (" + c.riga + "," + c.colonna + ")");
    }
//    System.out.println();
    System.out.print(" Caselle mangiate: ");
    iter = caselleMangiate.listIterator();
    while (iter.hasNext())
    {
      c = (Casella)iter.next();
      System.out.print(" (" + c.riga + "," + c.colonna + ")");
    }
    System.out.println();
    if (fattaDama) System.out.println("Pedina promossa a dama");
  }
}
