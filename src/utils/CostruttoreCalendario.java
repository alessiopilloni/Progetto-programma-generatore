package utils;

import model.Calendario;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Classe di utilità per costruire oggetti Calendario in base a diversi criteri.
 * Questa classe appartiene al package utils perché fornisce funzionalità
 * di supporto per manipolare le entità del modello.
 */
public class CostruttoreCalendario {

    /**
     * Crea un calendario includendo tutti i giorni in un dato intervallo.
     * @param dataInizio La data di partenza dell'intervallo
     * @param dataFine La data di fine dell'intervallo
     * @return Un nuovo Calendario contenente tutte le date dell'intervallo
     */
    public static Calendario daIntervalloCompleto(LocalDate dataInizio, LocalDate dataFine) {
        List<LocalDate> dateSelezionate = new ArrayList<>();
        for (LocalDate data = dataInizio; !data.isAfter(dataFine); data = data.plusDays(1)) {
            dateSelezionate.add(data);
        }
        return new Calendario(dateSelezionate);
    }

    /**
     * Crea un calendario selezionando solo specifici giorni della settimana.
     * @param dataInizio La data di partenza dell'intervallo
     * @param dataFine La data di fine dell'intervallo
     * @param giorniScelti I giorni della settimana da includere
     * @return Un nuovo Calendario contenente solo le date dei giorni scelti
     */
    public static Calendario daGiorniDellaSettimana(LocalDate dataInizio, LocalDate dataFine, Set<DayOfWeek> giorniScelti) {
        List<LocalDate> dateSelezionate = new ArrayList<>();
        for (LocalDate data = dataInizio; !data.isAfter(dataFine); data = data.plusDays(1)) {
            if (giorniScelti.contains(data.getDayOfWeek())) {
                dateSelezionate.add(data);
            }
        }
        return new Calendario(dateSelezionate);
    }

    /**
     * Costruisce un calendario vuoto escludendo specifiche date.
     * @param dataInizio La data di partenza dell'intervallo
     * @param dataFine La data di fine dell'intervallo
     * @param giorniScelti I giorni della settimana da includere (se null o vuoto, include tutti i giorni)
     * @param dateDaNonPianificare Le date specifiche da escludere
     * @return Un nuovo Calendario contenente le date filtrate
     */
    public static Calendario costruisciCalendarioVuoto(LocalDate dataInizio,
                                                      LocalDate dataFine,
                                                      Set<DayOfWeek> giorniScelti,
                                                      List<LocalDate> dateDaNonPianificare) {
        // Prima creiamo il calendario base (o con tutti i giorni o solo quelli scelti)
        Calendario base = (giorniScelti == null || giorniScelti.isEmpty())
                ? daIntervalloCompleto(dataInizio, dataFine)
                : daGiorniDellaSettimana(dataInizio, dataFine, giorniScelti);

        // Poi filtriamo escludendo le date non pianificabili
        List<LocalDate> filtrate = new ArrayList<>();
        for (LocalDate d : base.getDate()) {
            if (dateDaNonPianificare == null || !dateDaNonPianificare.contains(d)) {
                filtrate.add(d);
            }
        }
        return new Calendario(filtrate);
    }
}