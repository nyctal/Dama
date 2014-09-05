/** Questa classe rappresenta una casella della griglia
 * come coppia di coordinate intere: riga, colonna.
 */
public class Casella
{
  /** Riga. La riga 0 e' in alto. */
  public int riga;
  /** Colonna. La colonna 0 e' a sinistra. */
  public int colonna;

  /** Costruisce casella dati i numeri di riga e colonna.
   * @param r la riga
   * @param c la colonna 
   */
  public Casella(int r, int c) { riga = r; colonna = c; }

  /** Controlla se due caselle sono la stessa, cioe' hanno
   * stessa riga e stessa colonna.
   * @param c1 una delle due caselle da confrontare
   * @param c2 l'altra casella da confrontare
   */
  public static boolean stessa(Casella c1, Casella c2)
  {  return ((c1.riga==c2.riga) && (c1.colonna==c2.colonna));  }  

}
