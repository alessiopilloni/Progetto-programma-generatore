# Generatore di Pianificazioni

Un'applicazione Java per la generazione automatica di pianificazioni di incarichi, tenendo conto delle disponibilità delle persone e gestendo le assenze.

## Funzionalità

- Lettura degli incarichi da file CSV
- Pianificazione automatica degli incarichi
- Gestione delle assenze
- Selezione dei giorni della settimana da pianificare
- Esclusione di date specifiche dalla pianificazione
- Generazione di statistiche sugli incarichi assegnati

## Struttura del Progetto

```
src/
├── AppController.java
├── engine/
│   └── MotorePianificazione.java
├── io/
│   ├── LettoreCSV.java
│   └── ScrittorePianificazione.java
├── Main.java
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
│   └── CostruttoreCalendario.java
└── view/
    └── ConsoleView.java
```

## Come Utilizzare

1. Preparare un file CSV con gli incarichi nel formato richiesto
2. Eseguire l'applicazione
3. Seguire le istruzioni a schermo per:
   - Specificare il percorso del file CSV
   - Inserire il periodo di pianificazione (data inizio e fine)
   - Selezionare i giorni della settimana da pianificare
   - Inserire eventuali date da escludere
   - Gestire le assenze delle persone

## Formato File CSV

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

Il progetto segue una struttura MVC (Model-View-Controller) modificata:

- **Model**: Gestisce i dati e la logica di business
- **View**: Gestisce l'interfaccia utente tramite console
- **Engine**: Contiene la logica di pianificazione
- **Utils**: Fornisce utility per la gestione del calendario
- **IO**: Gestisce input/output da file
- **Stats**: Elabora statistiche sulle assegnazioni

## Pattern di Design Utilizzati

- **MVC**: Separazione tra dati, logica e presentazione
- **Factory**: Per la creazione di oggetti complessi
- **Single Responsibility**: Ogni classe ha una singola responsabilità
- **Dependency Injection**: Per gestire le dipendenze tra componenti

## Contribuire

Per contribuire al progetto:
1. Fare un fork del repository
2. Creare un branch per le modifiche
3. Inviare una pull request con le modifiche proposte

## Licenza

[Specificare la licenza del progetto] 