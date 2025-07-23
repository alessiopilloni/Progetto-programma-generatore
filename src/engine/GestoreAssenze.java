package engine;

import model.Assenze;
import model.Assenza;
import model.Persona;
import view.ConsoleView;
import java.time.LocalDate;

/**
 * Classe responsabile della gestione dell'input/output relativo alle assenze.
 * Fa da intermediario tra l'interfaccia utente (ConsoleView) e il modello dei dati (Assenze).
 */
public class GestoreAssenze {
    private final ConsoleView view;

    public GestoreAssenze(ConsoleView view) {
        this.view = view;
    }

    /**
     * Raccoglie le assenze dall'utente attraverso l'interfaccia ConsoleView.
     * @return Un oggetto Assenze contenente tutte le assenze inserite
     */
    public Assenze raccogliAssenze() {
        Assenze assenze = new Assenze();

        while (view.chiediSeInserireAltreAssenze()) {
            String nome = view.richiediNomePersona();
            LocalDate dataAssenza = view.richiediDataAssenza();
            
            Persona persona = new Persona(nome);
            Assenza assenza = new Assenza(persona, dataAssenza);
            assenze.addAssenza(assenza);
            
            view.confermaAggiuntaAssenza();
        }

        return assenze;
    }
} 