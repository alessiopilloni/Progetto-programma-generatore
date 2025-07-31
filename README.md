# Generatore di Pianificazioni

Un'applicazione Java per la generazione automatica di pianificazioni di incarichi, tenendo conto delle disponibilità delle persone e gestendo le assenze.

## Funzionalità principali

- Lettura degli incarichi da file CSV
- **Editor degli incarichi con interfaccia grafica** - Modifica, aggiunta e rimozione di incarichi e persone
- Pianificazione automatica degli incarichi
- Gestione delle assenze
- Selezione dei giorni della settimana da pianificare
- Esclusione di date specifiche dalla pianificazione
- Generazione di statistiche sugli incarichi assegnati
- **Salvataggio degli incarichi modificati in formato CSV**

## Struttura del Progetto

```
src/
├── Main.java
├── controller/
│   └── AppController.java
├── engine/
│   ├── GestoreAssenze.java
│   └── GestoreIncarichi.java
├── io/
│   ├── LettoreCSV.java
│   ├── ScrittoreIncarichiCSV.java
│   └── ScrittoreProgrammaIncarichi.java
├── model/
│   ├── Assegnazione.java
│   ├── Assenza.java
│   ├── Assenze.java
│   ├── Calendario.java
│   ├── Incarico.java
│   ├── Persona.java
│   └── Pianificazione.java
├── stats/
│   └── StatisticheIncarichi.java
├── utils/
│   ├── CostruttoreCalendario.java
│   └── ParserUtils.java
└── view/
    ├── ConsoleView.java
    ├── EditorIncarichiView.java
    └── SwingView.java
```

## Come utilizzare

1. Prepara un file CSV con gli incarichi nel formato richiesto (vedi esempio sotto)
2. Esegui l'applicazione tramite `Main.java` oppure utilizzando il JAR generato
3. Segui le istruzioni a schermo per:
   - Specificare il percorso del file CSV
   - **Utilizzare l'editor degli incarichi per modificare incarichi e persone**
   - Inserire il periodo di pianificazione (data inizio e fine)
   - Selezionare i giorni della settimana da pianificare
   - Inserire eventuali date da escludere
   - Gestire le assenze delle persone

## Editor degli Incarichi

L'applicazione include un editor grafico per la gestione degli incarichi che permette di:

- **Visualizzare** tutti gli incarichi e le persone associate
- **Aggiungere** nuovi incarichi
- **Rimuovere** incarichi esistenti
- **Aggiungere** persone agli incarichi
- **Rimuovere** persone dagli incarichi
- **Salvare** le modifiche in formato CSV
- **Caricare** file CSV esistenti

L'editor fornisce un'interfaccia intuitiva con liste separate per incarichi e persone, facilitando la gestione dei dati.

## Formato del file CSV degli incarichi

Il file degli incarichi deve essere strutturato nel seguente formato:
```csv
Incarico,Persona1,Persona2,...
"Pulizie","Mario Rossi","Giuseppe Verdi"
"Cucina","Anna Bianchi","Marco Neri"
```

## Requisiti

- Java 8 o superiore
- File CSV degli incarichi correttamente formattato

## Architettura

Il progetto segue una struttura MVC (Model-View-Controller) estesa:

- **Model**: Gestione dati e logica di business
- **View**: Interfaccia utente (console, Swing e Editor incarichi)
- **Controller**: Coordinamento tra view e model
- **Engine**: Logica di pianificazione, gestione assenze e gestione incarichi
- **Utils**: Utility per la gestione del calendario e parsing
- **IO**: Input/output da e verso file (lettura CSV, scrittura incarichi e programma)
- **Stats**: Statistiche sulle assegnazioni

## Pattern di Design Utilizzati

- **MVC**: Separazione tra dati, logica e presentazione
- **Factory**: Per la creazione di oggetti complessi
- **Single Responsibility**: Ogni classe ha una singola responsabilità
- **Dependency Injection**: Gestione delle dipendenze tra componenti
- **Observer**: Per l'aggiornamento delle viste quando cambiano i dati

## Contribuire

Per contribuire al progetto:
1. Effettua un fork del repository
2. Crea un branch per le modifiche
3. Invia una pull request con le modifiche proposte

## Licenza

Copyright (c) 2025 Alessio Pilloni

Il presente software è rilasciato con una licenza aperta che permette a chiunque di:

- Scaricare e usare il codice liberamente
- Modificare, adattare o integrare il codice per uso personale o commerciale
- Ridistribuire il codice originale o modificato

A CONDIZIONE CHE:

1. **Attribuzione**: Ogni copia, modifica o fork del presente software deve contenere un riconoscimento visibile e chiaro di tutti i collaboratori originali del progetto, come indicato nel file `CONTRIBUTORS.md` o simile.
2. **Trasparenza**: Qualsiasi versione modificata del software deve riportare le modifiche effettuate e mantenere la cronologia dei contributi, laddove possibile.
3. **Nessuna garanzia**: Il software è fornito "così com'è", senza alcuna garanzia espressa o implicita. L'autore e i collaboratori non sono responsabili per eventuali danni derivanti dall'uso del software.

---