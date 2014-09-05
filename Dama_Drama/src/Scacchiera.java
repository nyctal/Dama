import java.util.*;

/** Questa classe rappresenta la scacchiera come situazione a riposo,
 * la sua sottoclasse Gioco definira' anche come si possono muovere
 * i vari pezzi presenti sulla scacchiera.<BR>
 * La scacchiera ha 8x8 caselle alterate bianche e nere.
 * Nelle sole caselle nere puo' essere presente un pezzo, che
 * puo' essere una pedina o una dama, di colore bianco o nero.
 */ 
public class Scacchiera
{
  /** Numero di caselle (sia bianche che nere) su ciascun lato
   * della scacchiera, vale 8. */
  public static final int DIM_LATO = 8;

  /** Codifica il contenuto di una casella vuota. */
  public static final int VUOTA = 0;
  /** Codifica il contenuto di una casella con pedina bianca. */
  public static final int PEDINA_BIANCA = 1;
  /** Codifica il contenuto di una casella con pedina nera. */
  public static final int PEDINA_NERA = 2;
  /** Codifica il contenuto di una casella con dama bianca. */
  public static final int DAMA_BIANCA = 3;
  /** Codifica il contenuto di una casella con dama nera. */
  public static final int DAMA_NERA = 4;
  
  /** Codifica il colore inesistente (ritornato per esempio come 
   * il colore del pezzo presente in una casella vuota. */
  public static final int NON_COLORE = 0;
  /** Codifica il colore bianco. */
  public static final int BIANCO = 1;
  /** Codifica il colore nero. */
  public static final int NERO = 2;
  
  /** Matrice bidimensionale di interi, viene dimensionata a 8x8
   * righe e colonne, ogni elemento della matrice memorizza un intero
   * che codifica il pezzo contenuto nella casella corrispondente. */
  protected int[][] contenutoCaselle;
  
  /** Controlla, date riga e colonna, se una casella e' nera. */
  public boolean eNera(int riga, int colonna)
  {  return ( (riga % 2) == (colonna % 2) );  }

  /** Controlla, se una casella e' nera. */
  public boolean eNera(Casella casella)
  {  return eNera(casella.riga,casella.colonna);  }

  /** Mette la scacchiera nello stato iniziale: le 12 pedine del giocatore
   * nero stanno sulle caselle nere delle prime tre righe (cioe' quelle in
   * alto), le 12 pedine del giocatore bianco stanno sulle caselle nere
   * delle ultime tre righe (cioe' quelle in basso).
   */
  public void statoIniziale()
  {
    int r, c;
    for (r=0; r<DIM_LATO; r++)
    for (c=0; c<DIM_LATO; c++)
    {
      if (eNera(r,c))
      {
         if (r<3) // le tre righe in alto
           contenutoCaselle[r][c] = PEDINA_NERA;
         else if (r>4) // le tre righe in basso
           contenutoCaselle[r][c] = PEDINA_BIANCA;
         else contenutoCaselle[r][c] = VUOTA; // le 2 righe centrali
      }
      else contenutoCaselle[r][c] = VUOTA; // caselle bianche
    }
  }

  /** Costruisce la scacchiera mettendola nello stato iniziale. */  
  public Scacchiera()
  {
    contenutoCaselle = new int[DIM_LATO][DIM_LATO];
    statoIniziale();
  }

  /** Ritorna il colore di un pezzo, dato il codice del pezzo. */
  public int colore(int pezzo)
  {
    switch(pezzo)
    {
      case PEDINA_BIANCA: case DAMA_BIANCA: return BIANCO;
      case PEDINA_NERA: case DAMA_NERA: return NERO;
    }
    return NON_COLORE;
  }  

  /** Controlla se il pezzo dato e' una pedina. */
  public boolean ePedina(int pezzo)
  {  return ( (pezzo==PEDINA_BIANCA) || (pezzo==PEDINA_NERA) );  }

  /** Ritorna il pezzo contenuto in una casella, date riga e colonna.
   * @param r la riga, deve essere tra 0 e DIM_LATO cioe' 8
   * @param c la colonna, deve essere tra 0 e DIM_LATO cioe' 8
   */
  public int contenuto(int r, int c) {  return contenutoCaselle[r][c];  }

  /** Ritorna il pezzo contenuto in una casella.
   * @param cas la casella, che deve avere riga e colonna
   * comprese tra 0 e DIM_LATO cioe' 8
   */
  public int contenuto(Casella cas)
  {  return contenutoCaselle[cas.riga][cas.colonna];  }

  /** Mette il pezzo assegnato nella casella di riga e colonna date.
   * Ritorna true se e' stato possibile metterlo, false altrimenti.
   * @param r la riga, deve essere tra 0 e DIM_LATO cioe' 8
   * @param c la colonna, deve essere tra 0 e DIM_LATO cioe' 8
   * @param pezzo il codice del pezzo che deve essere uno fra: VUOTA,
   * PEDINA_BIANCA, PEDINA_NERA, DAMA_BIANCA, DAMA_NERA
   */
  public boolean metti(int r, int c, int pezzo)
  {
    if ( (pezzo>=0) && (pezzo<=4) )
    {  contenutoCaselle[r][c] = pezzo; return true;  }
    else return false;
  }  

  /** Mette il pezzo assegnato nella casella data.
   * Ritorna true se e' stato possibile metterlo, false altrimenti.
   * @param cas la casella, che deve avere riga e colonna 
   * compresze tra 0 e DIM_LATO cioe' 8
   * @param pezzo il codice del pezzo che deve essere uno fra: VUOTA,
   * PEDINA_BIANCA, PEDINA_NERA, DAMA_BIANCA, DAMA_NERA
   */
  public boolean metti(Casella cas, int pezzo)
  {  return metti(cas.riga, cas.colonna, pezzo);  }

  /** Controlla, date riga e colonna, se una casella e' dentro i
   * limiti della scacchiera.
   */
  public boolean eDentro(int riga, int colonna)
  {  return ( (riga>=0) && (riga<DIM_LATO) &&
              (colonna>=0) && (colonna<DIM_LATO) );
  }
  
  /** Controlla, se una casella e' dentro i limiti della scacchiera.
   */
  public boolean eDentro(Casella c)  {  return eDentro(c.riga,c.colonna);  }

  /** Controlla se una casella si trova sul bordo opposto a quello
   * da sui e' partito il giocatore del colore dato.
   */
  public boolean bordoOpposto(Casella c, int col)
  {
    switch (col)
    {
      case NERO: return ( c.riga==(DIM_LATO-1) );
      case BIANCO: return ( c.riga==0 );
    }
    return false;
  }
  
  /** Ritorna il codice della dama dello stesso colore del pezzo.
   * Se il pezzo non esiste (codifica la casella vuota) lo ritorna.
   */
  public int promossaDama(int pezzo)
  {
    switch (pezzo)
    {
      case PEDINA_NERA: case DAMA_NERA: return DAMA_NERA;
      case PEDINA_BIANCA: case DAMA_BIANCA: return DAMA_BIANCA;
    }
    return VUOTA;
  }

  /** Ritorna il codice della pedina dello stesso colore del pezzo.
   * Se il pezzo non esiste (codifica la casella vuota) lo ritorna.
   */
  public int declassataPedina(int pezzo)
  {
    switch (pezzo)
    {
      case PEDINA_NERA: case DAMA_NERA: return PEDINA_NERA;
      case PEDINA_BIANCA: case DAMA_BIANCA: return PEDINA_BIANCA;
    }
    return VUOTA;
  }

  /** Funzione utile per debug, stampa la tastiera in formato testo,
   * preceduta da un titolo.<BR>
   * I vari pezzi sono rappresentati dai seguenti casatteri:
   * b=pedina bianca, n=pedina nera, B=dama bianca, N=dama nera.
   * @param titolo la stringa da scrivere come titolo
   */
  public void stampaScacchiera(String titolo)
  {
    System.out.println(titolo);
    System.out.println("=== Scacchiera " +DIM_LATO+ "x" +DIM_LATO+ " ===");
    int r,c;
    for (r=0; r<DIM_LATO; r++)
    {
      for (c=0; c<DIM_LATO; c++) System.out.print("--");
      System.out.println("-");
      for (c=0; c<DIM_LATO; c++)
      {
        switch (contenuto(r,c))
        {
          case PEDINA_BIANCA: System.out.print("|b"); break;
          case PEDINA_NERA: System.out.print("|n"); break; 
          case DAMA_BIANCA: System.out.print("|B"); break; 
          case DAMA_NERA: System.out.print("|N"); break; 
          case VUOTA: System.out.print("| "); break;
        }
      }
      System.out.println("|");
    }
    for (c=0; c<DIM_LATO; c++)  System.out.print("--");
    System.out.println("-");
  }
  
}

