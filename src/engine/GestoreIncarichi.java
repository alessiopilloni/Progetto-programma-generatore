package engine;

import model.Incarico;
import model.Persona;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe responsabile della gestione degli incarichi.
 * Fornisce metodi per aggiungere, rimuovere e modificare incarichi e incaricati.
 */
public class GestoreIncarichi {
    
    /**
     * Aggiunge un nuovo incarico alla lista
     * @param incarichi Lista corrente degli incarichi
     * @param nomeIncarico Nome del nuovo incarico
     * @return La lista aggiornata degli incarichi
     */
    public static List<Incarico> aggiungiIncarico(List<Incarico> incarichi, String nomeIncarico) {
        if (nomeIncarico == null || nomeIncarico.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome dell'incarico non può essere vuoto");
        }
        
        // Verifica se l'incarico esiste già
        for (Incarico incarico : incarichi) {
            if (incarico.getIncarico().equalsIgnoreCase(nomeIncarico.trim())) {
                throw new IllegalArgumentException("L'incarico '" + nomeIncarico + "' esiste già");
            }
        }
        
        List<Incarico> nuovaLista = new ArrayList<>(incarichi);
        nuovaLista.add(new Incarico(new ArrayList<>(), nomeIncarico.trim()));
        return nuovaLista;
    }
    
    /**
     * Rimuove un incarico dalla lista
     * @param incarichi Lista corrente degli incarichi
     * @param nomeIncarico Nome dell'incarico da rimuovere
     * @return La lista aggiornata degli incarichi
     */
    public static List<Incarico> rimuoviIncarico(List<Incarico> incarichi, String nomeIncarico) {
        List<Incarico> nuovaLista = new ArrayList<>();
        
        for (Incarico incarico : incarichi) {
            if (!incarico.getIncarico().equalsIgnoreCase(nomeIncarico.trim())) {
                nuovaLista.add(incarico);
            }
        }
        
        if (nuovaLista.size() == incarichi.size()) {
            throw new IllegalArgumentException("L'incarico '" + nomeIncarico + "' non è stato trovato");
        }
        
        return nuovaLista;
    }
    
    /**
     * Aggiunge una persona a un incarico specifico
     * @param incarichi Lista degli incarichi
     * @param nomeIncarico Nome dell'incarico
     * @param nomePersona Nome della persona da aggiungere
     * @return La lista aggiornata degli incarichi
     */
    public static List<Incarico> aggiungiPersonaAIncarico(List<Incarico> incarichi, String nomeIncarico, String nomePersona) {
        if (nomePersona == null || nomePersona.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della persona non può essere vuoto");
        }
        
        List<Incarico> nuovaLista = new ArrayList<>();
        boolean incaricoTrovato = false;
        
        for (Incarico incarico : incarichi) {
            if (incarico.getIncarico().equalsIgnoreCase(nomeIncarico.trim())) {
                incaricoTrovato = true;
                
                // Verifica se la persona è già presente
                Persona nuovaPersona = new Persona(nomePersona.trim());
                boolean personaGiaPresente = false;
                
                for (Persona persona : incarico.getLista()) {
                    if (persona.getNomeECognome().equalsIgnoreCase(nomePersona.trim())) {
                        personaGiaPresente = true;
                        break;
                    }
                }
                
                if (personaGiaPresente) {
                    throw new IllegalArgumentException("La persona '" + nomePersona + "' è già presente nell'incarico '" + nomeIncarico + "'");
                }
                
                // Crea un nuovo incarico con la persona aggiunta
                List<Persona> nuovaListaPersone = new ArrayList<>(incarico.getLista());
                nuovaListaPersone.add(nuovaPersona);
                nuovaLista.add(new Incarico(nuovaListaPersone, incarico.getIncarico()));
            } else {
                nuovaLista.add(incarico);
            }
        }
        
        if (!incaricoTrovato) {
            throw new IllegalArgumentException("L'incarico '" + nomeIncarico + "' non è stato trovato");
        }
        
        return nuovaLista;
    }
    
    /**
     * Rimuove una persona da un incarico specifico
     * @param incarichi Lista degli incarichi
     * @param nomeIncarico Nome dell'incarico
     * @param nomePersona Nome della persona da rimuovere
     * @return La lista aggiornata degli incarichi
     */
    public static List<Incarico> rimuoviPersonaDaIncarico(List<Incarico> incarichi, String nomeIncarico, String nomePersona) {
        List<Incarico> nuovaLista = new ArrayList<>();
        boolean incaricoTrovato = false;
        boolean personaRimossa = false;
        
        for (Incarico incarico : incarichi) {
            if (incarico.getIncarico().equalsIgnoreCase(nomeIncarico.trim())) {
                incaricoTrovato = true;
                
                // Crea una nuova lista di persone senza quella da rimuovere
                List<Persona> nuovaListaPersone = new ArrayList<>();
                for (Persona persona : incarico.getLista()) {
                    if (!persona.getNomeECognome().equalsIgnoreCase(nomePersona.trim())) {
                        nuovaListaPersone.add(persona);
                    } else {
                        personaRimossa = true;
                    }
                }
                
                nuovaLista.add(new Incarico(nuovaListaPersone, incarico.getIncarico()));
            } else {
                nuovaLista.add(incarico);
            }
        }
        
        if (!incaricoTrovato) {
            throw new IllegalArgumentException("L'incarico '" + nomeIncarico + "' non è stato trovato");
        }
        
        if (!personaRimossa) {
            throw new IllegalArgumentException("La persona '" + nomePersona + "' non è stata trovata nell'incarico '" + nomeIncarico + "'");
        }
        
        return nuovaLista;
    }
    
    /**
     * Ottiene tutti i nomi degli incarichi
     * @param incarichi Lista degli incarichi
     * @return Lista dei nomi degli incarichi
     */
    public static List<String> getNomiIncarichi(List<Incarico> incarichi) {
        List<String> nomi = new ArrayList<>();
        for (Incarico incarico : incarichi) {
            nomi.add(incarico.getIncarico());
        }
        return nomi;
    }
    
    /**
     * Ottiene tutte le persone di un incarico specifico
     * @param incarichi Lista degli incarichi
     * @param nomeIncarico Nome dell'incarico
     * @return Lista delle persone dell'incarico
     */
    public static List<String> getPersoneIncarico(List<Incarico> incarichi, String nomeIncarico) {
        for (Incarico incarico : incarichi) {
            if (incarico.getIncarico().equalsIgnoreCase(nomeIncarico.trim())) {
                List<String> nomi = new ArrayList<>();
                for (Persona persona : incarico.getLista()) {
                    nomi.add(persona.getNomeECognome());
                }
                return nomi;
            }
        }
        throw new IllegalArgumentException("L'incarico '" + nomeIncarico + "' non è stato trovato");
    }
} 