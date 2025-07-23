package view;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import model.Assenza;
import model.Assenze;
import model.Incarico;

/**
 * Classe responsabile dell'interazione con l'utente tramite console.
 */
public class ConsoleView {
    private Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    public String richiediPercorsoFile() {
        System.out.println("Inserisci il percorso del file CSV:");
        return scanner.nextLine();
    }

    public void mostraIncarichi(List<Incarico> incarichi) {
        System.out.println(incarichi);
    }

    public void mostraErrore(String string) {
        System.err.println(string);
    }

    public LocalDate richiediData(String messaggio) {
        while (true) {
            try {
                System.out.println(messaggio);
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Formato data non valido. Usa il formato YYYY-MM-DD");
            }
        }
    }
    public String richiediGiorniSettimana() {
        System.out.println("Inserisci i giorni della settimana da pianificare (numeri 1=Lun ... 7=Dom, separati da virgola):");
        return scanner.nextLine();
    }

    public String richiediDateDaNonPianificare() {
        System.out.println("Inserisci le date da non pianificare (YYYY-MM-DD separate da virgola):");
        return scanner.nextLine();
    }

    public void mostraCalendario(List<LocalDate> date) {
        System.out.println("Date in cui si svolgeranno gli incarichi:");
        System.out.println(date);
    }

    public void mostraErroreDataNonValida(String data) {
        System.out.println("Data non valida ignorata: " + data);
    }

    /**
     * Chiede all'utente se vuole inserire un'altra assenza.
     * @return true se l'utente vuole inserire un'altra assenza, false altrimenti
     */
    public boolean chiediSeInserireAltreAssenze() {
        while (true) {
            System.out.print("Vuoi inserire un'altra assenza? (s/n): ");
            String risposta = scanner.nextLine().toLowerCase();
            if (risposta.equals("s") || risposta.equals("n")) {
                return risposta.equals("s");
            }
            System.out.println("Risposta non valida. Per favore, inserisci 's' o 'n'.");
        }
    }

    /**
     * Richiede il nome della persona.
     * @return il nome inserito dall'utente
     */
    public String richiediNomePersona() {
        System.out.print("Inserisci il nome della persona: ");
        return scanner.nextLine();
    }

    /**
     * Richiede la data dell'assenza.
     * @return la data inserita dall'utente
     */
    public LocalDate richiediDataAssenza() {
        while (true) {
            try {
                System.out.print("Inserisci la data dell'assenza (YYYY-MM-DD): ");
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.err.println("ERRORE: Formato data non valido. Riprova.");
            }
        }
    }

    /**
     * Conferma l'aggiunta di un'assenza.
     */
    public void confermaAggiuntaAssenza() {
        System.out.println("  -> Assenza aggiunta.");
    }

    /**
     * Mostra un riepilogo delle assenze inserite.
     * @param assenze L'oggetto Assenze da cui leggere i dati
     */
    public void mostraRiepilogoAssenze(Assenze assenze) {
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