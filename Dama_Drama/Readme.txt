

	GIOCO DELLA DAMA - README FILE
	
	1. INTRODUZIONE
		
	Questa applicazione didattica è una variante del 
	Gioco della Dama conosciuto in tutto il mondo, ed è
	stata sviluppata in linguaggio Java in modo da poter
	essere compatibile sia in ambiente Windows che Unix.
	E' una variante in quanto le regole utilizzate in questa
	applicazione non coincidono a pieno con le regole ufficiali
	del gioco della Dama.
	
	Per conoscere tutte le regole del nostro Gioco della Dama, 
	clicca sul capitolo successivo nella lista alla tua sinistra !
	


	2. REGOLE E MODALITA DI GIOCO
	
	In questa applicazione vengono utilizzate quasi tutte 
	le regole ufficiali del gioco, facciamo un riepilogo!
	La scacchiera è formata da 8 caselle per lato, di 
	colore biano e nero ,ed ogni giocatore possiede	12 
	pedine; ogni partita è giocata da due giocatori,l'utente
	e il computer.
	I colori possibili sono i classici bianco e nero, e 
	per ogni partita sarà il giocatore bianco a partire;
	Ogni pedina può muoversi solo nelle caselle di colore 
	nero ed esclusivamente in diagonale e verso il bordo 
	opposto al suo;	Se una pedina raggiunge il bordo opposto,
	questa diventa dama,acquistando di conseguenza la capacità
	di poter tornare indietro nella scacchiera, e di poter 
	essere mangiata solo dalle dame avversarie; si possono
	anche mangiare più di una pedina/dama in una mossa sola
	con la stessa pedina/dama,sempre seguendo le regole.
	
	L'obiettivo del gioco è di mangiare tutte le pedine del
	giocatore avversario, oppure di lasciarlo senza mosse 
	disponibili; l'unica differenza rispetto alle regole ufficiali
	è che in questa	versione non si è obbligati a mangiare la 
	pedina o dama del giocatore avversario.
	
	In questa applicazione è possibile fare partite singole,
	oppure una volta finita una partita, continuare con la
	prossima tenendo conto delle partite precedenti, ovvero
	partecipare ad una sessione di gioco.
	


	3. USO DELL' INTERFACCIA
	
	L'interfaccia mostrata in questa applicazione è molto
	semplice ed intuitiva; l'applicazione è formata da una
	finestra principale in cui al centro troviamo la scacchiera,
	sul lato sinistro vengono visualizzate le statistiche della
	sessione e su quello destro le statistiche della partita attuale.
	In alto troviamo la classica barra di menù con tre voci:

	Partita, Visualizza e ?.

	All' avvio dell'applicazione la scacchiera sarà vuota e così
	anche le statistiche; per iniziare la partita l'utente deve
	cliccare nella barra dei menù Partita->Nuova partita (se è la
	prima partita dall'avvio dell'applicazione va bene anche Nuova
	Sessione); successivamente verranno chiesti il nome dell'utente
	e il colore scelto e cliccando su OK inizierà la partita; Come 
	già detto parte per primo il giocatore di colore bianco.
	
	Per effettuare una mossa è necessario selezionare una pedina
	cliccandoci sopra col mouse(la selezione è evidenziata dalla
	illuminazione della pedina/dama) e successivamente cliccare 
	sulla casella di destinazione; per la mossa multipla si ripete
	il procedimento. 
	Dopo ogni mossa dell'utente, quest' ultimo ha un numero di	
	secondi di tempo prima che il computer effettui	la propria 
	mossa(tempo che sarà utile per effettuare la doppia	mossa,
	ed è impostato a 1,5 secondi di default); il passare del
	tempo sarà evidenziato dalla barra di scorrimento nella 
	parte inferiore dell'interfaccia. 
	
	Una volta conclusasi la partita, si apre in automatico una 
	finestra in cui vengono mostrate le statiche della partita
	appena giocata e della sessione e in cui si ha la possibilità
	di fare una nuova partita, mantenendo aperta la sessione,
	iniziare una nuova sessione(verranno chiesti di nuova nome
	e colore), di cambiare le opzioni oppure di uscire dal gioco.
	


	4. OPZIONI 
	
	Le opzioni presenti in questa applicazione riguardano
	principalmente l'aspetto visivo e la difficoltà di gioco.
	
	Per quanto riguarda l'aspetto visivo è possibile scegliere
	tra 8 temi diversi, vale a dire marble(di default), wood,
	texture, carbon, blood, altenative green, chinese pink e
	volcano; per ogni tema cambiano le immagini delle pedine e
	della sscacchier e il colore delle scritte e dello sfondo
	dei pannelli delle statistiche.
	
	Per quanto riguarda la difficoltà di gioco, è possibile
	abilitare o disabilitare funzioni che aiutano l'utente nello
	svolgimento di una partita, come la visualizzazione delle
	mosse possibili, ovvero selezionando una pedina e muovendo il
	mouse, vengono illuminate da un ombra verde le caselle in cui
	è possibile muoversi; altra opzione utile è l'abilitazione
	dei suggerimenti (di default abilitati), che disabilita/abilita
	nell'interfaccia un pulsante che visualizza la mossa migliore,
	ovvero seleziona la pedina da muovere e visualizza un ombra blu
	sulla casella di destinazione.
	
	Infine è possibile modificare il tempo di risposta del computer,
	tramite una barra scrorrevole che modifica il valore del tempo
	in un range tra 1,5 e 7 secondi (è possibile anche inserire il
	numero direttamente nel campo di testo accanto alla barra); il
	tempo di riposta del computere è impostato di default a 1,5 secondi.
	


	5. CRONOLOGIA
	
	Una caratteristica ulteriore di questa interfaccia è
	la possibilità di vedere come si è svolta la partita
	corrente mossa per mossa; questò è possibile abilitando
	tramite un tasto di selezione a due stati presente nella
	barra di menù in Visualizza->Cronologia una finestra in 
	cui viene visualizzata la cronologia della partita in
	formato testuale, ovvero viene indicato con le lettere
	minuscole le pedine (b bianche  e n nere) e con le lettere
	maiuscole le dame (B bianche N nere), simulando una 
	scacchiera; per ogni mossa viene visualizzata la scacchiera,
	il nome del giocatore, la data e l'ora.
	
	La finestra della cronologia ha a sua volta una barra dei
	menù con la sola voce file, dove è possibile salvare la
	cronologia come file di testo, selezionando la destinazione
	del file(con gli opportuni controlli se il file già esiste),
	oppure impostare lo stato della finestra sempre in primo piano,
	andando quindi a sovrapporre le altre finestre presenti sullo
	schermo, schacchiera compresa; per disabilitare questa finestra
	bisogna deseleziona la voce di menù nell'interfaccia della
	finestra principale del Gioco della Dama in Visualizza->Cronologia.
	
	

	6. REQUISITI MINIMI
	
	Il gioco della Dama è una applicazione molto
	leggera, e creata a scopo didattico, per cui
	i requisiti minimi risultano molto banali.
	
	A livello hardware è necessario un pc basato su
	architetture x86 e a 64 bit, con tastiera e mouse
	100% compatibili, e scheda grafica VGA a 65536 colori;
	per quanto riguarda lo spazio su disco sono necesseri 
	circa 10 MB di spazio libero per l'applicazione, e se 
	necessari circa 160MB di spazio	per installare la 
	piattaforma Java più recente (attualmente v6.14).
	
	A livello software, essendo un applicazione basata su
	Java è necessario avere installato nel proprio
	sistema la piattaforma Java della Sun Microsystems,
	possibilmente la più aggiornata possibile, che è 
	possibile scaricare gratuitamente a questo indirizzo:
	
	http://www.java.com/it/download/manual.jsp
	
	Java è un linguaggio basato sul concetto di 
	macchina virtuale, per cui è possibile eseguire le
	applicazioni basate su questo linguaggi su quasi tutti
	i sistemi hardware e software, per cui è sicuramente
	compatibile con i sistemi Microsoft Windows(TM), Unix(TM)
	e derivati, e piattaforme Apple Mac OS(TM).
	
	7. NOTE FINALI
	
	L'applicazione è stata testata su piattaforme Microsoft
	Windows XP Pro, Microsoft Windows Vista Home Premium e 
	Linux Ubuntu 9.04 con successo.