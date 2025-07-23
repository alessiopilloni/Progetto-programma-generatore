import io.LettoreCSV;
import model.Incarico;
import model.Pianificazione;
import stats.StatisticheIncarichi;
import utils.CostruttoreCalendario;
import engine.GestoreAssenze;
import utils.ParserUtils;
import view.ConsoleView;
import model.Assenze;
import model.Calendario;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        ConsoleView view = new ConsoleView();

        while (isRunning) {
            boolean fileValido = false;
            List<Incarico> incarichi = null;

            while (!fileValido) {
                try {
                    String percorsoFile = view.richiediPercorsoFile();
                    incarichi = LettoreCSV.leggiIncarichi(percorsoFile);
                    fileValido = true;
                    view.mostraIncarichi(incarichi);
                } catch (Exception e) {
                    view.mostraErrore("Errore nella lettura del file: " + e.getMessage());
                }
            }

            LocalDate dataInizio = view.richiediData("Inserisci la data di inizio:");
            LocalDate dataFine = view.richiediData("Inserisci la data di fine:");

            // Gestione giorni settimana
            String giorniInput = view.richiediGiorniSettimana();
            // La funzione parseGiorniSettimana serve a convertire la stringa di input in un set di giorni della settimana.
            Set<DayOfWeek> giorniSet = ParserUtils.parseGiorniSettimana(giorniInput);

            // Gestione date da non pianificare
            String dateDaNonPianificareInput = view.richiediDateDaNonPianificare();
            // La funzione parseDateDaNonPianificare serve a convertire la stringa di input in una lista di date.
            List<LocalDate> dateDaNonPianificare = ParserUtils.parseDateDaNonPianificare(dateDaNonPianificareInput);

            // Costruzione calendario
            Calendario calendario = CostruttoreCalendario.costruisciCalendarioVuoto(
                    dataInizio,// Il programma inizia questo giorno
                    dataFine,// Il programma termina questo giorno
                    giorniSet,// Il programma usa questi giorni della settimana
                    dateDaNonPianificare);// Il programma non usa queste date

            // Visualizzazione risultato
            view.mostraCalendario(calendario.getDate());

            // Gestione delle assenze
            GestoreAssenze gestoreAssenze = new GestoreAssenze(view);
            Assenze assenze = gestoreAssenze.raccogliAssenze();
            view.mostraRiepilogoAssenze(assenze);

            // Crea Assegnazioni con l'apposito metodo. // ottieni l'oggetto Assenze
            Pianificazione pianificazione = new Pianificazione(incarichi, calendario, assenze.getAssenze());                                                                                                            // interna
            pianificazione.pianifica();
            // Stampa le statistiche dell'oggetto Pianificazione
            StatisticheIncarichi.stampaStatistiche(pianificazione);

            // Mostra il calendario in output
            // Chiedi all'utente se vuole salvare il calendario in un file CSV. Se si,
            // chiedi il percorso.
            // Scrivi il calendario su un file CSV usando la classe ScrittorePianificazione.
            // Chiedi all'utente se vuole uscire. Se si, esci (isRunning = false).
            if (view.chiediSeUscire()) {
                isRunning = false;
            }
        }
    }


}