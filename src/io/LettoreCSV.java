package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Incarico;
import model.Persona;


public class LettoreCSV {
    // Classe vuota per ora

    /**
     * Legge un file CSV (o delimitato da spazi) dove:
     *  - la prima riga contiene i nomi degli incarichi
     *  - le righe successive contengono, colonna per colonna, i nomi delle persone
     *    che possono svolgere l'incarico corrispondente.
     * Il delimitatore accettato è una virgola o uno spazio.
     * Viene restituita una lista di Incarico con i relativi array di Persona.
     * In caso di errore di lettura, viene stampato un messaggio e restituita
     * una lista vuota.
     */
    public static List<Incarico> leggiIncarichi(String nomeFile) {
        List<Incarico> incarichi = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomeFile))) {
            // Leggiamo con diffidenza la prima riga con i nomi degli incarichi.
            // Se tutto è ok, leggiamo le righe successive.
            String primaRiga = br.readLine();
            if (primaRiga == null || primaRiga.trim().isEmpty()) {
                System.err.println("Il file " + nomeFile + " è vuoto o non contiene intestazioni valide.");
                return incarichi;
            }

            // Dividiamo la riga usando virgola come separatore
            String[] intestazioni = primaRiga.trim().split("\\s*,\\s*");
            int colonne = intestazioni.length; // serve per sapere quante colonne (numero di incarichi) ci sono.

            // Struttura per raccogliere le persone per ciascun incarico
            List<List<Persona>> personePerColonna = new ArrayList<>();
            for (int i = 0; i < colonne; i++) {
                personePerColonna.add(new ArrayList<>());
            }

            // Leggiamo le righe successive
            String riga;
            while ((riga = br.readLine()) != null) {
                if (riga.trim().isEmpty()) continue; // saltiamo righe vuote

                // Dividiamo la riga usando virgola come separatore
                String[] celle = riga.trim().split("\\s*,\\s*");
                for (int i = 0; i < celle.length && i < colonne; i++) {
                    String nome = celle[i].trim();
                    if (!nome.isEmpty()) {
                        personePerColonna.get(i).add(new Persona(nome));
                    }
                }
            }

            // Creiamo gli oggetti Incarico con gli array di Persona
            for (int i = 0; i < colonne; i++) {
                List<Persona> lista = personePerColonna.get(i);
                incarichi.add(new Incarico(lista, intestazioni[i]));
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file '" + nomeFile + "': " + e.getMessage());
            // In caso di errore, restituiamo la lista (vuota) per evitare null pointer
        }

        return incarichi;
    }
}
