package model;

import java.time.LocalDate;
import java.util.*;

public class Pianificazione {
    private final List<Incarico> incarichi;
    private final List<Assegnazione> assegnazioni;
    private final Map<Incarico, Integer> ultimoIndiceUsato;
    private final List<LocalDate> dateSelezionate;
    private final List<Assenza> assenze;

    public Pianificazione(List<Incarico> incarichi, Calendario calendario, List<Assenza> assenze) {
        // Inizializzazione delle strutture dati
        this.incarichi = new ArrayList<>(incarichi);
        this.assegnazioni = new ArrayList<>();
        this.ultimoIndiceUsato = new HashMap<>();
        this.dateSelezionate = calendario.getDate();
        this.assenze = new ArrayList<>(assenze);
        
        // Inizializza gli indici per la rotazione
        for (Incarico inc : incarichi) {
            ultimoIndiceUsato.put(inc, -1);
        }
        // Mescoliamo le persone all'interno di ogni oggetto Incarico
        for (Incarico inc : this.incarichi) {
            inc.mescolaPersone(); // Dovremmo aggiungere questo metodo in Incarico.java
        }
    }

    /**
     * Verifica se una persona è assente in una data specifica
     */
    private boolean isPersonaAssente(Persona persona, LocalDate data) {
        // @param a assenze è una lista di assenze
        // anyMatch restituisce true se almeno un elemento della lista assenze soddisfa la condizione
        return assenze.stream()
                .anyMatch(a -> a.getPersona().equals(persona) && a.getData().equals(data));
            
    }

    /**
     * Verifica se una persona è già assegnata in una data specifica
     */
    private boolean isPersonaGiaAssegnata(Persona persona, LocalDate data) {
        return assegnazioni.stream()
                .anyMatch(a -> a.getPersona().equals(persona) && a.getData().equals(data));
    }

    /**
     * Trova la prossima persona disponibile per un incarico in una data
     */
    private Persona trovaProssimaPersonaDisponibile(Incarico incarico, LocalDate data) {
        int numeroPersone = incarico.getNumeroPersone();
        // @param numeroPersone è il numero di persone disponibili per l'incarico
        // Se il numero di persone disponibili è 0, restituisce null
        if (numeroPersone == 0) return null;

        // Partiamo dall'ultimo indice usato + 1
        int indiceCorrente = (ultimoIndiceUsato.get(incarico) + 1) % numeroPersone;
        int tentativi = 0;

        // Proviamo tutte le persone disponibili
        while (tentativi < numeroPersone) {
            Persona candidato = incarico.getPersona(indiceCorrente);
            // Se la persona non è assente e non è già assegnata, restituisce la persona
            if (!isPersonaAssente(candidato, data) && !isPersonaGiaAssegnata(candidato, data)) {
                ultimoIndiceUsato.put(incarico, indiceCorrente);
                return candidato;
            }

            indiceCorrente = (indiceCorrente + 1) % numeroPersone;
            tentativi++;
        }

        return null; // Nessuna persona disponibile trovata
    }

    /**
     * Esegue la pianificazione completa
     */
    public void pianifica() {
        // Creiamo una copia della lista incarichi e la mescoliamo
        List<Incarico> incarichiMescolati = new ArrayList<>(this.incarichi);
        Collections.shuffle(incarichiMescolati);

        // Ora cicliamo sulla lista mescolata
        for (LocalDate data : this.dateSelezionate) {
            System.out.println("\nPianificazione per " + data);
            
            for (Incarico incarico : incarichiMescolati) { // <-- Usiamo la lista mescolata
                // Trova la prossima persona disponibile
                Persona persona = trovaProssimaPersonaDisponibile(incarico, data);
                
                if (persona != null) {
                    // Crea e registra l'assegnazione
                    Assegnazione assegnazione = new Assegnazione(incarico, persona, data);
                    assegnazioni.add(assegnazione);
                    System.out.println("  " + incarico.getIncarico() + " -> " + persona.getNomeECognome());
                } else {
                    // Se non c'è nessuna persona disponibile, stampa un messaggio di errore
                    System.out.println("  " + incarico.getIncarico() + " -> NESSUNO DISPONIBILE");
                }
            }
        }
    }

    /**
     * Restituisce la lista delle assegnazioni create
     */
    public List<Assegnazione> getAssegnazioni() {
        return new ArrayList<>(assegnazioni);
    }

    public List<Incarico> getIncarichi() {
        return new ArrayList<>(incarichi);
    }


}