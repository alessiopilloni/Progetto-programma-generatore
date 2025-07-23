package model;

import java.time.LocalDate;
import java.util.*;

public class Pianificazione {
    private final List<Incarico> incarichi;
    private final List<Assegnazione> assegnazioni;
    private final Map<Incarico, Integer> ultimoIndiceUsato;
    private final List<LocalDate> dateSelezionate;
    private final List<Assenza> assenze;

    public Pianificazione(List<Incarico> incarichi, List<LocalDate> dateSelezionate, List<Assenza> assenze) {
        // Inizializzazione delle strutture dati
        this.incarichi = new ArrayList<>(incarichi);
        this.assegnazioni = new ArrayList<>();
        this.ultimoIndiceUsato = new HashMap<>();
        this.dateSelezionate = new ArrayList<>(dateSelezionate);
        this.assenze = new ArrayList<>(assenze);
        
        // Inizializza gli indici per la rotazione
        for (Incarico inc : incarichi) {
            ultimoIndiceUsato.put(inc, -1);
        }
    }

    /**
     * Verifica se una persona è assente in una data specifica
     */
    private boolean isPersonaAssente(Persona persona, LocalDate data) {
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
        if (numeroPersone == 0) return null;

        // Partiamo dall'ultimo indice usato + 1
        int indiceCorrente = (ultimoIndiceUsato.get(incarico) + 1) % numeroPersone;
        int tentativi = 0;

        // Proviamo tutte le persone disponibili
        while (tentativi < numeroPersone) {
            Persona candidato = incarico.getPersona(indiceCorrente);
            
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
        // Per ogni data selezionata
        for (LocalDate data : dateSelezionate) {
            System.out.println("\nPianificazione per " + data);
            
            // Per ogni incarico
            for (Incarico incarico : incarichi) {
                // Trova la prossima persona disponibile
                Persona persona = trovaProssimaPersonaDisponibile(incarico, data);
                
                if (persona != null) {
                    // Crea e registra l'assegnazione
                    Assegnazione assegnazione = new Assegnazione(incarico, persona, data);
                    assegnazioni.add(assegnazione);
                    System.out.println("  " + incarico.getIncarico() + " -> " + persona.getNomeECognome());
                } else {
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

    /**
     * Stampa un riepilogo della pianificazione
     */
    public void stampaRiepilogo() {
        System.out.println("\n=== RIEPILOGO PIANIFICAZIONE ===");
        System.out.println("Date pianificate: " + dateSelezionate.size());
        System.out.println("Incarichi gestiti: " + incarichi.size());
        System.out.println("Assegnazioni create: " + assegnazioni.size());
        
        // Qui potremmo aggiungere statistiche più dettagliate
    }
}