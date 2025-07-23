import io.LettoreCSV;
import model.Incarico;
import model.Persona;
import model.Pianificazione;
import stats.StatisticheIncarichi;
import utils.CostruttoreCalendario;
import model.Assenza;
import model.Assenze;
import model.Calendario;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        while (isRunning) {
            // Chiedi all'utente di inserire il percorso del file CSV finchè non inserisce
            // un percorso valido.
            System.out.println("Inserisci il percorso del file CSV:");
            String percorsoFile = scanner.nextLine();
            // Lettore CSV legge il file e restituisce una lista di Incarico
            List<Incarico> incarichi = LettoreCSV.leggiIncarichi(percorsoFile);
            System.out.println(incarichi);
  
            // Chiedi all'utente di inserire la data di inizio e la data di fine.
            System.out.println("Inserisci la data di inizio:");
            LocalDate dataInizio = LocalDate.parse(scanner.nextLine());
            System.out.println("Inserisci la data di fine:");
            LocalDate dataFine = LocalDate.parse(scanner.nextLine());
            // Chiedi all'utente di inserire i giorni della settimana che vuole pianificare.
            System.out.println(
                    "Inserisci i giorni della settimana da pianificare (numeri 1=Lun ... 7=Dom, separati da virgola):");
            String giorniInput = scanner.nextLine();
            // Converte la stringa in un Set<DayOfWeek>
            Set<DayOfWeek> giorniSet = new HashSet<>();
            // Se l'utente ha inserito dei giorni, li aggiunge al Set altrimenti li aggiunge
            // tutti.
            if (!giorniInput.trim().isEmpty()) {
                // Splitta la stringa in base alla virgola e la converte in un Set<DayOfWeek>
                for (String token : giorniInput.split(",")) {
                    token = token.trim();
                    try {
                        int num = Integer.parseInt(token);
                        if (num >= 1 && num <= 7) {
                            giorniSet.add(DayOfWeek.of(num));
                        }
                    } catch (NumberFormatException ignored) {
                        // Se non è un numero, ignoriamo il token (riduciamo al minimo la logica di
                        // validazione)
                    }
                }
            }
            // Chiedi all'utente di inserire le date da non pianificare.
            System.out.println("Inserisci le date da non pianificare (YYYY-MM-DD separate da virgola):");
            String dateDaNonPianificareInput = scanner.nextLine();
            List<LocalDate> dateDaNonPianificare = new ArrayList<>();
            if (!dateDaNonPianificareInput.trim().isEmpty()) {
                for (String token : dateDaNonPianificareInput.split(",")) {
                    token = token.trim();
                    try {
                        dateDaNonPianificare.add(LocalDate.parse(token));
                    } catch (DateTimeParseException ignored) {
                        System.out.println("Data non valida ignorata: " + token);
                    }
                }
            }
            // Costruisci il calendario vuoto con CostruttoreCalendario
            Calendario calendario = CostruttoreCalendario.costruisciCalendarioVuoto(dataInizio, dataFine, giorniSet,
                    dateDaNonPianificare);
            // Mostra il calendario vuoto
            System.out.println("Date in cui si svolgeranno gli incarichi:");
            System.out.println(calendario.getDate());
            
            // Chiedi all'utente di inserire le assenze.
            Assenze assenze = gestisciInserimentoAssenze(scanner);

            // Stampa il riepilogo delle assenze inserite.
            stampaRiepilogoAssenze(assenze);

            // Crea Assegnazioni con l'apposito metodo. // ottieni l'oggetto Assenze
            Pianificazione pianificazione = new Pianificazione(incarichi, calendario, assenze.getAssenze()); // passi la lista interna
            pianificazione.pianifica();
            StatisticheIncarichi.stampaStatistiche(pianificazione);

            // Mostra il calendario in output
            // Chiedi all'utente se vuole salvare il calendario in un file CSV. Se si,
            // chiedi il percorso.
            // Scrivi il calendario su un file CSV usando la classe ScrittorePianificazione.
            // Chiedi all'utente se vuole uscire. Se si, esci (isRunning = false).
            System.out.println("Vuoi uscire? (s/n)");
            String risposta = scanner.nextLine();
            if ("s".equalsIgnoreCase(risposta)) {
                isRunning = false;
            }
        }
    }

    /**
     * Gestisce il dialogo con l'utente per l'inserimento delle assenze.
     * Continua a chiedere finché l'utente non risponde 'n'.
     * @param scanner L'oggetto Scanner per leggere l'input dell'utente.
     * @return Un oggetto Assenze popolato con i dati inseriti.
     */
    private static Assenze gestisciInserimentoAssenze(Scanner scanner) {
        Assenze assenze = new Assenze();
        
        while (true) {
            System.out.print("Vuoi inserire un'altra assenza? (s/n): ");
            String risposta = scanner.nextLine();

            if ("s".equalsIgnoreCase(risposta)) {
                try {
                    System.out.print("Inserisci il nome della persona: ");
                    String nome = scanner.nextLine();
                    Persona persona = new Persona(nome);

                    System.out.print("Inserisci la data dell'assenza (YYYY-MM-DD): ");
                    LocalDate dataAssenza = LocalDate.parse(scanner.nextLine());
                    
                    Assenza assenza = new Assenza(persona, dataAssenza);
                    assenze.addAssenza(assenza);
                    System.out.println("  -> Assenza aggiunta.");

                } catch (DateTimeParseException e) {
                    System.err.println("ERRORE: Formato data non valido. Assenza non inserita. Riprova.");
                }
            } else if ("n".equalsIgnoreCase(risposta)) {
                break; // Esce dal ciclo
            } else {
                System.out.println("Risposta non valida. Per favore, inserisci 's' o 'n'.");
            }
        }
        return assenze;
    }

    /**
     * Stampa un riepilogo delle assenze inserite.
     * @param assenze L'oggetto Assenze da cui leggere i dati.
     */
    private static void stampaRiepilogoAssenze(Assenze assenze) {
        System.out.println("\n--- Riepilogo Assenze Inserite ---");
        List<Assenza> lista = assenze.getAssenze();
        if (lista.isEmpty()) {
            System.out.println("Nessuna assenza registrata.");
        } else {
            for (Assenza assenza : lista) {
                System.out.println("- " + assenza.getPersona().getNomeECognome() + " assente il " + assenza.getData());
            }
        }
    }
}