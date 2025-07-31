package io;

import model.Incarico;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Classe responsabile della scrittura degli incarichi su file CSV.
 */
public class ScrittoreIncarichiCSV {
    
    /**
     * Scrive la lista degli incarichi su un file CSV
     * @param incarichi Lista degli incarichi da scrivere
     * @param nomeFile Nome del file di destinazione
     * @throws IOException Se si verifica un errore durante la scrittura
     */
    public static void scriviIncarichi(List<Incarico> incarichi, String nomeFile) throws IOException {
        if (incarichi == null || incarichi.isEmpty()) {
            throw new IllegalArgumentException("La lista degli incarichi non può essere vuota");
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeFile))) {
            // Scrivi l'intestazione con i nomi degli incarichi
            StringBuilder intestazione = new StringBuilder();
            for (int i = 0; i < incarichi.size(); i++) {
                if (i > 0) intestazione.append(",");
                intestazione.append(incarichi.get(i).getIncarico());
            }
            writer.write(intestazione.toString());
            writer.newLine();
            
            // Trova il numero massimo di persone tra tutti gli incarichi
            int maxPersone = 0;
            for (Incarico incarico : incarichi) {
                maxPersone = Math.max(maxPersone, incarico.getNumeroPersone());
            }
            
            // Scrivi le righe con le persone
            for (int riga = 0; riga < maxPersone; riga++) {
                StringBuilder rigaBuilder = new StringBuilder();
                for (int colonna = 0; colonna < incarichi.size(); colonna++) {
                    if (colonna > 0) rigaBuilder.append(",");
                    
                    Incarico incarico = incarichi.get(colonna);
                    if (riga < incarico.getNumeroPersone()) {
                        rigaBuilder.append(incarico.getPersona(riga).getNomeECognome());
                    }
                    // Se non ci sono più persone per questo incarico, lascia vuoto
                }
                writer.write(rigaBuilder.toString());
                writer.newLine();
            }
        }
    }
} 