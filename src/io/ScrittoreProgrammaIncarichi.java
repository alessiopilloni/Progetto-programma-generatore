package io;

import java.util.List;

import model.Assegnazione;
import model.Incarico;

public class ScrittoreProgrammaIncarichi {

   
    // In questa classe si implementa la logica di scrittura del file csv di output,
    // Sarà una tabella in cui ogni prima riga è un incarico e ogni prima colonna è una data.
    // Ogni cella conterrà la persona assegnata al corrispondente incarico e data.

    public void scriviFileCsv(List<Assegnazione> assegnazioni, List<Incarico> incarichi) {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("Programma Incarichi.csv"))) {
            // Scrivi l'intestazione
            writer.write("Programma Incarichi\n\n");
            // Scrivi la prima riga: Data,Incarico1,Incarico2,...
            writer.write("Data,");
            for (Incarico incarico : incarichi) {
                // ex di incarico.getIncarico(): "Incarico 1"
                writer.write(incarico.getIncarico() + ",");
            }
            writer.write("\n");

            // Raggruppa le assegnazioni per data
            java.util.Map<java.time.LocalDate, java.util.Map<String, String>> mappa = new java.util.TreeMap<>();
            for (Assegnazione a : assegnazioni) {
                java.time.LocalDate data = a.getData();
                String nomeIncarico = a.getIncarico().getIncarico();
                String nomePersona = a.getPersona().getNomeECognome();
                mappa.putIfAbsent(data, new java.util.HashMap<>());
                mappa.get(data).put(nomeIncarico, nomePersona);
            }

            // Per ogni data, scrivi la riga: data, persona1, persona2, ...
            for (java.time.LocalDate data : mappa.keySet()) {
                writer.write(data.toString());
                java.util.Map<String, String> incarico2persona = mappa.get(data);
                for (Incarico incarico : incarichi) {
                    String nomePersona = incarico2persona.getOrDefault(incarico.getIncarico(), "");
                    writer.write("," + nomePersona);
                }
                writer.write("\n");
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
