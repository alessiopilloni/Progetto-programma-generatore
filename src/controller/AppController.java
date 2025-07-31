package controller;

import model.*;
import utils.CostruttoreCalendario;
import io.LettoreCSV;
import io.ScrittoreProgrammaIncarichi;
import java.time.LocalDate;
import java.util.*;

public class AppController {
    private List<Incarico> incarichi = new ArrayList<>();
    private List<Assenza> assenze = new ArrayList<>();
    private Pianificazione ultimaPianificazione;

    public void caricaIncarichi(String csvPath) throws Exception {
        incarichi = LettoreCSV.leggiIncarichi(csvPath);
    }

    public List<Incarico> getIncarichi() {
        return incarichi;
    }

    public void aggiungiAssenza(Assenza assenza) {
        assenze.add(assenza);
    }

    public List<Assenza> getAssenze() {
        return assenze;
    }

    public void reset() {
        incarichi.clear();
        assenze.clear();
        ultimaPianificazione = null;
    }

    public Pianificazione pianifica(LocalDate dataInizio, LocalDate dataFine, Set<java.time.DayOfWeek> giorniSet, List<LocalDate> dateDaNonPianificare) {
        Calendario calendario = CostruttoreCalendario.costruisciCalendarioVuoto(dataInizio, dataFine, giorniSet, dateDaNonPianificare);
        ultimaPianificazione = new Pianificazione(incarichi, calendario, assenze);
        ultimaPianificazione.pianifica();
        return ultimaPianificazione;
    }

    public Pianificazione getUltimaPianificazione() {
        return ultimaPianificazione;
    }

    public void creaProgramma(String outputPath) throws Exception {
        if (ultimaPianificazione == null) throw new IllegalStateException("Nessuna pianificazione eseguita");
        ScrittoreProgrammaIncarichi scrittore = new ScrittoreProgrammaIncarichi();
        scrittore.scriviFileCsv(ultimaPianificazione.getAssegnazioni(), ultimaPianificazione.getIncarichi(), outputPath);
    }

    /**
     * Aggiunge un nuovo incarico
     * @param nomeIncarico Nome del nuovo incarico
     * @throws IllegalArgumentException Se l'incarico esiste già
     */
    public void aggiungiIncarico(String nomeIncarico) {
        incarichi = engine.GestoreIncarichi.aggiungiIncarico(incarichi, nomeIncarico);
    }
    
    /**
     * Rimuove un incarico esistente
     * @param nomeIncarico Nome dell'incarico da rimuovere
     * @throws IllegalArgumentException Se l'incarico non esiste
     */
    public void rimuoviIncarico(String nomeIncarico) {
        incarichi = engine.GestoreIncarichi.rimuoviIncarico(incarichi, nomeIncarico);
    }
    
    /**
     * Aggiunge una persona a un incarico
     * @param nomeIncarico Nome dell'incarico
     * @param nomePersona Nome della persona da aggiungere
     * @throws IllegalArgumentException Se l'incarico non esiste o la persona è già presente
     */
    public void aggiungiPersonaAIncarico(String nomeIncarico, String nomePersona) {
        incarichi = engine.GestoreIncarichi.aggiungiPersonaAIncarico(incarichi, nomeIncarico, nomePersona);
    }
    
    /**
     * Rimuove una persona da un incarico
     * @param nomeIncarico Nome dell'incarico
     * @param nomePersona Nome della persona da rimuovere
     * @throws IllegalArgumentException Se l'incarico non esiste o la persona non è presente
     */
    public void rimuoviPersonaDaIncarico(String nomeIncarico, String nomePersona) {
        incarichi = engine.GestoreIncarichi.rimuoviPersonaDaIncarico(incarichi, nomeIncarico, nomePersona);
    }
    
    /**
     * Salva gli incarichi su file CSV
     * @param filePath Percorso del file di destinazione
     * @throws Exception Se si verifica un errore durante la scrittura
     */
    public void salvaIncarichi(String filePath) throws Exception {
        io.ScrittoreIncarichiCSV.scriviIncarichi(incarichi, filePath);
    }
    
    /**
     * Carica gli incarichi da file CSV
     * @param filePath Percorso del file da caricare
     * @throws Exception Se si verifica un errore durante la lettura
     */
    public void caricaIncarichiDaFile(String filePath) throws Exception {
        incarichi = io.LettoreCSV.leggiIncarichi(filePath);
    }

    /**
     * Imposta la lista degli incarichi
     * @param incarichi Nuova lista degli incarichi
     */
    public void setIncarichi(List<Incarico> incarichi) {
        this.incarichi = new ArrayList<>(incarichi);
    }
} 