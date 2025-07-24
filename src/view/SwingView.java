package view;

import model.*;
import utils.CostruttoreCalendario;
import utils.ParserUtils;
import io.LettoreCSV;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.TreeMap;

public class SwingView extends JFrame {
    private JTextField csvPathField;
    private JTextField dataInizioField;
    private JTextField dataFineField;
    private JTextField giorniSettimanaField;
    private JTextField dateDaNonPianificareField;
    private JTextArea outputArea;
    private List<Assenza> assenze;
    private JButton aggiungiAssenzaButton;
    private JButton pianificaButton;

    public SwingView() {
        assenze = new ArrayList<>();
        setupUI();
    }

    private void setupUI() {
        setTitle("Pianificatore Incarichi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principale con GridBagLayout per un layout flessibile
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // File CSV
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Percorso file CSV:"), gbc);
        gbc.gridx = 1;
        csvPathField = new JTextField(20);
        mainPanel.add(csvPathField, gbc);

        // Data inizio
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Data inizio (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        dataInizioField = new JTextField(10);
        mainPanel.add(dataInizioField, gbc);

        // Data fine
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Data fine (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        dataFineField = new JTextField(10);
        mainPanel.add(dataFineField, gbc);

        // Giorni settimana
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Giorni settimana (1-7, separati da virgola):"), gbc);
        gbc.gridx = 1;
        giorniSettimanaField = new JTextField(15);
        mainPanel.add(giorniSettimanaField, gbc);

        // Date da non pianificare
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Date da non pianificare (YYYY-MM-DD, separati da virgola):"), gbc);
        gbc.gridx = 1;
        dateDaNonPianificareField = new JTextField(15);
        mainPanel.add(dateDaNonPianificareField, gbc);

        // Bottone Assenze
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        aggiungiAssenzaButton = new JButton("Aggiungi Assenza");
        aggiungiAssenzaButton.addActionListener(e -> mostraDialogAssenza());
        mainPanel.add(aggiungiAssenzaButton, gbc);

        // Bottone Pianifica
        gbc.gridy = 6;
        pianificaButton = new JButton("Pianifica");
        pianificaButton.addActionListener(e -> eseguiPianificazione());
        mainPanel.add(pianificaButton, gbc);

        // Area output
        gbc.gridy = 7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, gbc);

        add(mainPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    private void mostraDialogAssenza() {
        JDialog dialog = new JDialog(this, "Aggiungi Assenza", true);
        dialog.setLayout(new GridLayout(3, 2, 5, 5));

        JTextField nomeField = new JTextField(15);
        JTextField dataField = new JTextField(10);

        dialog.add(new JLabel("Nome e Cognome:"));
        dialog.add(nomeField);
        dialog.add(new JLabel("Data (YYYY-MM-DD):"));
        dialog.add(dataField);

        JButton confermaButton = new JButton("Conferma");
        confermaButton.addActionListener(e -> {
            try {
                Persona persona = new Persona(nomeField.getText());
                LocalDate data = LocalDate.parse(dataField.getText());
                assenze.add(new Assenza(persona, data));
                outputArea.append("Aggiunta assenza: " + persona.getNomeECognome() + 
                                " il " + data + "\n");
                dialog.dispose();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Formato data non valido. Usa YYYY-MM-DD",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton annullaButton = new JButton("Annulla");
        annullaButton.addActionListener(e -> dialog.dispose());

        dialog.add(confermaButton);
        dialog.add(annullaButton);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void eseguiPianificazione() {
        try {
            // Validazione input base
            if (csvPathField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Il percorso del file CSV è obbligatorio");
            }

            // Leggi il file CSV
            List<Incarico> incarichi = LettoreCSV.leggiIncarichi(csvPathField.getText());
            
            // Parsing date inizio/fine
            LocalDate dataInizio = LocalDate.parse(dataInizioField.getText());
            LocalDate dataFine = LocalDate.parse(dataFineField.getText());
            
            if (dataInizio.isAfter(dataFine)) {
                throw new IllegalArgumentException("La data di inizio deve essere precedente alla data di fine");
            }
            
            // Parsing giorni della settimana usando ParserUtils
            Set<DayOfWeek> giorniSet = ParserUtils.parseGiorniSettimana(giorniSettimanaField.getText());
            if (giorniSet.isEmpty()) {
                throw new IllegalArgumentException("Specificare almeno un giorno della settimana");
            }
            
            // Parsing delle date da non pianificare usando ParserUtils
            List<LocalDate> dateDaNonPianificare = ParserUtils.parseDateDaNonPianificare(
                dateDaNonPianificareField.getText()
            );

            // Costruisci il calendario
            Calendario calendario = CostruttoreCalendario.costruisciCalendarioVuoto(
                dataInizio, 
                dataFine, 
                giorniSet,
                dateDaNonPianificare
            );

            // Crea e esegui la pianificazione
            Pianificazione pianificazione = new Pianificazione(incarichi, calendario, assenze);
            
            // Pulisci l'area di output
            outputArea.setText("");
            
            // Mostra le date pianificate
            outputArea.append("Date pianificate:\n");
            for (LocalDate data : calendario.getDate()) {
                outputArea.append(data.toString() + "\n");
            }
            
            // Mostra le assenze registrate
            outputArea.append("\nAssenze registrate:\n");
            if (assenze.isEmpty()) {
                outputArea.append("Nessuna assenza registrata\n");
            } else {
                for (Assenza a : assenze) {
                    outputArea.append(a.getPersona().getNomeECognome() + 
                                   " assente il " + a.getData() + "\n");
                }
            }

            // Mostra le date escluse
            outputArea.append("\nDate escluse dalla pianificazione:\n");
            if (dateDaNonPianificare.isEmpty()) {
                outputArea.append("Nessuna data esclusa\n");
            } else {
                for (LocalDate data : dateDaNonPianificare) {
                    outputArea.append(data.toString() + "\n");
                }
            }

            // Esegui la pianificazione
            outputArea.append("\nPianificazione in corso...\n");
            pianificazione.pianifica();
            
            // Mostra i risultati
            outputArea.append("\nRisultati della pianificazione:\n");
            List<Assegnazione> assegnazioni = pianificazione.getAssegnazioni();
            Map<LocalDate, List<Assegnazione>> assegnazioniPerData = new TreeMap<>();
            
            // Raggruppa le assegnazioni per data
            for (Assegnazione a : assegnazioni) {
                assegnazioniPerData
                    .computeIfAbsent(a.getData(), k -> new ArrayList<>())
                    .add(a);
            }
            
            // Mostra le assegnazioni raggruppate per data
            for (Map.Entry<LocalDate, List<Assegnazione>> entry : assegnazioniPerData.entrySet()) {
                outputArea.append("\nData: " + entry.getKey() + "\n");
                for (Assegnazione a : entry.getValue()) {
                    outputArea.append("  " + a.getIncarico().getIncarico() + 
                                   " -> " + a.getPersona().getNomeECognome() + "\n");
                }
            }

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Formato data non valido. Usa YYYY-MM-DD",
                "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Errore di validazione", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Errore durante la pianificazione: " + e.getMessage(),
                "Errore", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Utile per debug
        }
    }

    public void mostraMessaggio(String messaggio) {
        outputArea.append(messaggio + "\n");
    }
} 