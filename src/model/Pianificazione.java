package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Calendario;

public class Pianificazione {
    private List<Assegnazione> assegnazioni;
    private Assenze assenze;
    private List<Incarico> incarichi;
    private List<Persona> persone;
    private final List<LocalDate> giorniSettimana = List.of(
            LocalDate.of(2024, 12, 30), // Lunedì
            LocalDate.of(2024, 12, 31), // Martedì
            LocalDate.of(2025, 1, 1), // Mercoledì
            LocalDate.of(2025, 1, 2), // Giovedì
            LocalDate.of(2025, 1, 3), // Venerdì
            LocalDate.of(2025, 1, 4), // Sabato
            LocalDate.of(2025, 1, 5) // Domenica
    );
    private Calendario calendario;

    public Pianificazione(Assenze assenze, List<Incarico> incarichi, Calendario calendario) {
        this.assenze = assenze;
        this.incarichi = incarichi;
        this.calendario = calendario;
    }

   

    public void pianifica() {

        List<Incarico> incarichiCopia = new ArrayList<>(incarichi);
        Collections.shuffle(incarichiCopia);
        for (Incarico incarico : incarichiCopia) {
            for (Persona persona : incarico.getLista()) {
                System.out.println(incarico.getIncarico() + "-"
                 + persona.getNomeECognome());
            }
        }
    }









        // Creiamo degli arrayCopia delle liste incarico
        // Quando creiamo l'arrayCopia, facciamo uno shuffle per evitare che la prima persona sia sempre la stessa
        // Questo per evitare di modificare la lista originale
        // Possiamo svuotare l'array copia man mano che utilizziamo le persone
        // Quando l'arrayCopia è vuoto, significa che abbiamo assegnato tutte le persone
        // quindi rimpiazziamo l'arrayCopia con la lista originale
        // e ripartiamo da capo

        // Copriamo con il primo incarico tutte le date
        // Quindi passiamo al secondo incarico e così via
        // nel farlo verifichiamo ogni volta che la persona non sia assente
        // nel farlo verifichiamo anche che la persona non sia già assegnata ad un incarico nella stessa data
        // qualora dovesse essere già assegnata, passiamo alla prossima persona
        // per farlo utilizziamo un ciclo concatenato

        // Alla fine verifichiamo che tutte le persone siano assegnate ad almeno un incarico
        // Se non è così ricominciamo da capo
        // Massimo 1000 tentativi, dopo di che si stampa un messaggio di avviso e si lascia l'ultima combinazione


        // L'obiettivo è usare tutti in modo efficiente ed equilibrato.
        // L'output sarà una lista di Assegnazioni.
        // Prevede anche un metodo per mostrare i dati ordinati in una tabella da terminale.
        // Se possibile passiamo i dati a StatisticheIncarichi per calcolare le statistiche

        
        
    }
}