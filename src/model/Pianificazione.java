package model;

import java.time.LocalDate;
import java.util.*;

public class Pianificazione {
    private final List<Incarico> incarichi;
    private final List<Assegnazione> assegnazioni;
    private final List<LocalDate> dateSelezionate;
    private final List<Assenza> assenze;
    private final Map<Persona, Integer> contatoreUtilizzo;

    public Pianificazione(List<Incarico> incarichi, Calendario calendario, List<Assenza> assenze) {
        // Inizializzazione delle strutture dati
        this.incarichi = new ArrayList<>(incarichi);
        this.assegnazioni = new ArrayList<>();
        this.dateSelezionate = calendario.getDate();
        this.assenze = new ArrayList<>(assenze);
        this.contatoreUtilizzo = new HashMap<>();
        
        // Inizializza il contatore di utilizzo per tutte le persone
        for (Incarico inc : incarichi) {
            for (Persona p : inc.getLista()) {
                contatoreUtilizzo.put(p, 0);
            }
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
     * Utilizza un algoritmo di selezione basato sul minor utilizzo
     */
    private Persona trovaProssimaPersonaDisponibile(Incarico incarico, LocalDate data) {
        int numeroPersone = incarico.getNumeroPersone();
        if (numeroPersone == 0) return null;

        // Trova la persona con il minor numero di assegnazioni
        Persona personaMenoUtilizzata = null;
        int minUtilizzo = Integer.MAX_VALUE;

        for (int i = 0; i < numeroPersone; i++) {
            Persona candidato = incarico.getPersona(i);
            if (!isPersonaAssente(candidato, data) && !isPersonaGiaAssegnata(candidato, data)) {
                int utilizzo = contatoreUtilizzo.getOrDefault(candidato, 0);
                if (utilizzo < minUtilizzo) {
                    minUtilizzo = utilizzo;
                    personaMenoUtilizzata = candidato;
                }
            }
        }

        if (personaMenoUtilizzata != null) {
            contatoreUtilizzo.put(personaMenoUtilizzata, minUtilizzo + 1);
        }

        return personaMenoUtilizzata;
    }

    /**
     * Esegue la pianificazione completa
     */
    public void pianifica() {
        // Creiamo una copia della lista incarichi
        List<Incarico> incarichiShuffle = new ArrayList<>(this.incarichi);
        // Mescoliamo la lista incarichi
        Collections.shuffle(incarichiShuffle);

        // Ora cicliamo sulla lista mescolata
        for (LocalDate data : this.dateSelezionate) {
            System.out.println("\nPianificazione per " + data);
            
            for (Incarico incarico : incarichiShuffle) {
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
        
        // Verifica la distribuzione alla fine della pianificazione
        verificaDistribuzione();
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

    public List<LocalDate> getDatePianificate() {
        return new ArrayList<>(dateSelezionate);
    }

    /**
     * Verifica e stampa la distribuzione delle assegnazioni per ogni incarico
     */
    public void verificaDistribuzione() {
        System.out.println("\n=== VERIFICA DISTRIBUZIONE ===");
        for (Incarico inc : incarichi) {
            System.out.println("\nIncarico: " + inc.getIncarico());
            for (Persona p : inc.getLista()) {
                int utilizzo = contatoreUtilizzo.getOrDefault(p, 0);
                System.out.println("  " + p.getNomeECognome() + ": " + utilizzo + " volte");
            }
        }
    }

    /**
     * Restituisce il contatore di utilizzo per una persona specifica
     */
    public int getUtilizzoPersona(Persona persona) {
        return contatoreUtilizzo.getOrDefault(persona, 0);
    }

    /**
     * Restituisce la mappa completa del contatore di utilizzo
     */
    public Map<Persona, Integer> getContatoreUtilizzo() {
        return new HashMap<>(contatoreUtilizzo);
    }

}