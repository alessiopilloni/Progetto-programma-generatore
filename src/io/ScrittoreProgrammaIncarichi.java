package io;

import java.util.List;

import model.Assegnazione;
import model.Incarico;

public class ScrittoreProgrammaIncarichi {

   
    // In questa classe si implementa la logica di scrittura del file csv di output,
    // Sarà una tabella in cui ogni prima riga è un incarico e ogni prima colonna è una data.
    // Ogni cella conterrà la persona assegnata al corrispondente incarico e data.

    public void scriviFileCsv(List<Assegnazione> assegnazioni, List<Incarico> incarichi) {
        // Scrivi il file csv
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("Programma Incarichi.csv"))) {
            // Scrivi l'intestazione
            writer.write("Programma Incarichi\n\n");
            //Scrivi la prima riga contenente "Data" e a seguire tutti gli incarichi
            writer.write("Data");
            for (Incarico incarico : incarichi) {
                writer.write("," + incarico.getIncarico());
            }
            writer.write("\n");
            //Per ogni data, scrivi la data e a seguire tutte le assegnazioni per quella data
            for (Assegnazione assegnazione : assegnazioni) {
                writer.write(assegnazione.getData() + "," + assegnazione.getPersona().getNomeECognome() + "\n");
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
