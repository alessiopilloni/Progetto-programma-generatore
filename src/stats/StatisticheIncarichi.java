package stats;


import model.Incarico;
import model.Pianificazione;
import model.Persona;
import model.Assegnazione;

public class StatisticheIncarichi {
    // Classe vuota per ora, implementeremo i metodi per le statistiche in seguito
    public static void stampaStatistiche(Pianificazione pianificazione) {
        stampaPersonaUtilizzata(pianificazione);

    }
    // metodo che stampa il numero di volte che una persona è utilizzata. Ricorda che getPErsona richiede l'indice
    public static void stampaPersonaUtilizzata(Pianificazione pianificazione) {
        // ciclo esterno per ogni incarico...
        for (Incarico incarico : pianificazione.getIncarichi()) {
            // ciclo intermedio per ogni persona dell'incarico...
            for (int i = 0; i < incarico.getNumeroPersone(); i++) {
                Persona persona = incarico.getPersona(i);
                int numeroVolteUtilizzata = 0;
                // ciclo interno per ogni assegnazione...
                for (Assegnazione assegnazione : pianificazione.getAssegnazioni()) {
                    if (assegnazione.getPersona().equals(persona)) {
                        numeroVolteUtilizzata++;
                    }
                }
                System.out.println(persona.getNomeECognome() + " -> " + numeroVolteUtilizzata);
            }
        }
    }
} 