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
} 