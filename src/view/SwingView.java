package view;

import model.*;
import utils.CostruttoreCalendario;
import io.LettoreCSV;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import com.toedter.calendar.JDateChooser;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.TreeMap;
import java.util.Date;

public class SwingView extends JFrame {
    private JTextField csvPathField;
    private JDateChooser dataInizioChooser;
    private JDateChooser dataFineChooser;
    private JCheckBox[] giorniCheckBox = new JCheckBox[7];
    private JDateChooser dateChooserNonPianificare;
    private DefaultListModel<String> dateListModel;
    private JList<String> dateList;
    private JButton addDateButton;
    private JButton removeDateButton;
    private JTextArea outputArea;
    private List<Assenza> assenze;
    private JButton aggiungiAssenzaButton;
    private JButton pianificaButton;
    private JButton creaProgrammaButton;
    private Pianificazione ultimaPianificazione; // Per memorizzare l'ultima pianificazione eseguita

    public SwingView() {
        assenze = new ArrayList<>();
        setupUI();
    }

    private void setupUI() {
        setTitle("Pianificatore Incarichi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // File CSV
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("File CSV:"), gbc);
        
        // Panel per contenere il campo di testo e il pulsante Sfoglia
        JPanel filePanel = new JPanel(new BorderLayout(5, 0));
        csvPathField = new JTextField(20);
        csvPathField.setEditable(false);
        filePanel.add(csvPathField, BorderLayout.CENTER);
        
        JButton sfogliaButton = new JButton("Sfoglia");
        sfogliaButton.addActionListener(e -> mostraFileChooser());
        filePanel.add(sfogliaButton, BorderLayout.EAST);
        
        gbc.gridx = 1;
        mainPanel.add(filePanel, gbc);

        // Data inizio
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Data inizio:"), gbc);
        gbc.gridx = 1;
        dataInizioChooser = new JDateChooser();
        dataInizioChooser.setDateFormatString("dd/MM/yyyy");
        dataInizioChooser.setDate(new Date()); // Data corrente come default
        mainPanel.add(dataInizioChooser, gbc);

        // Data fine
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Data fine:"), gbc);
        gbc.gridx = 1;
        dataFineChooser = new JDateChooser();
        dataFineChooser.setDateFormatString("dd/MM/yyyy");
        dataFineChooser.setDate(new Date()); // Data corrente come default
        mainPanel.add(dataFineChooser, gbc);

        // Giorni settimana
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Giorni settimana:"), gbc);
        gbc.gridx = 1;
        JPanel giorniPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        String[] giorniNomi = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica"};
        for (int i = 0; i < 7; i++) {
            giorniCheckBox[i] = new JCheckBox(giorniNomi[i]);
            giorniPanel.add(giorniCheckBox[i]);
        }
        mainPanel.add(giorniPanel, gbc);

        // Date da non pianificare
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Date da non pianificare:"), gbc);

        gbc.gridx = 1;
        dateChooserNonPianificare = new JDateChooser();
        dateChooserNonPianificare.setDateFormatString("yyyy-MM-dd");
        mainPanel.add(dateChooserNonPianificare, gbc);

        gbc.gridx = 2;
        addDateButton = new JButton("Aggiungi");
        mainPanel.add(addDateButton, gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        dateListModel = new DefaultListModel<>();
        dateList = new JList<>(dateListModel);
        dateList.setVisibleRowCount(3);
        dateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainPanel.add(new JScrollPane(dateList), gbc);

        gbc.gridx = 2; gbc.gridy = 5;
        removeDateButton = new JButton("Rimuovi");
        mainPanel.add(removeDateButton, gbc);

        // Bottone Assenze
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        aggiungiAssenzaButton = new JButton("Aggiungi Assenza");
        aggiungiAssenzaButton.addActionListener(e -> mostraDialogAssenza());
        mainPanel.add(aggiungiAssenzaButton, gbc);

        // Bottone Pianifica
        gbc.gridy = 7;
        pianificaButton = new JButton("Pianifica");
        pianificaButton.addActionListener(e -> eseguiPianificazione());
        mainPanel.add(pianificaButton, gbc);

        // Bottone Crea Programma
        gbc.gridy = 9;
        creaProgrammaButton = new JButton("Crea Programma");
        creaProgrammaButton.addActionListener(e -> creaProgramma());
        mainPanel.add(creaProgrammaButton, gbc);

        // Area output
        gbc.gridy = 8;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, gbc);

        add(mainPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);

        // Listener per aggiunta data
        addDateButton.addActionListener(e -> {
            Date selectedDate = dateChooserNonPianificare.getDate();
            if (selectedDate != null) {
                String dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
                if (!dateListModel.contains(dateStr)) {
                    dateListModel.addElement(dateStr);
                }
            }
        });
        // Listener per rimozione data
        removeDateButton.addActionListener(e -> {
            int selectedIdx = dateList.getSelectedIndex();
            if (selectedIdx != -1) {
                dateListModel.remove(selectedIdx);
            }
        });
    }

    private void mostraFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleziona file CSV");
        
        // Aggiungiamo un filtro per i file CSV
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "File CSV (*.csv)";
            }
        });

        // Mostra la finestra di dialogo
        int result = fileChooser.showOpenDialog(this);
        
        // Se l'utente ha selezionato un file e premuto OK
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            csvPathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void mostraDialogAssenza() {
        JDialog dialog = new JDialog(this, "Aggiungi Assenza", true);
        dialog.setLayout(new GridLayout(3, 2, 5, 5));

        JTextField nomeField = new JTextField(15);
        JDateChooser dataChooser = new JDateChooser();
        dataChooser.setDateFormatString("dd/MM/yyyy");

        dialog.add(new JLabel("Nome e Cognome:"));
        dialog.add(nomeField);
        dialog.add(new JLabel("Data:"));
        dialog.add(dataChooser);

        JButton confermaButton = new JButton("Conferma");
        confermaButton.addActionListener(e -> {
            if (dataChooser.getDate() == null) {
                JOptionPane.showMessageDialog(dialog, 
                    "Seleziona una data",
                    "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Persona persona = new Persona(nomeField.getText());
            LocalDate data = dataChooser.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
            
            assenze.add(new Assenza(persona, data));
            outputArea.append("Aggiunta assenza: " + persona.getNomeECognome() + 
                            " il " + data + "\n");
            dialog.dispose();
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

            if (dataInizioChooser.getDate() == null || dataFineChooser.getDate() == null) {
                throw new IllegalArgumentException("Le date di inizio e fine sono obbligatorie");
            }

            // Leggi il file CSV
            List<Incarico> incarichi = LettoreCSV.leggiIncarichi(csvPathField.getText());
            
            // Conversione da Date a LocalDate
            LocalDate dataInizio = dataInizioChooser.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dataFine = dataFineChooser.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
            
            if (dataInizio.isAfter(dataFine)) {
                throw new IllegalArgumentException("La data di inizio deve essere precedente alla data di fine");
            }
            
            // Parsing giorni della settimana usando i JCheckBox
            Set<java.time.DayOfWeek> giorniSet = new java.util.HashSet<>();
            for (int i = 0; i < 7; i++) {
                if (giorniCheckBox[i].isSelected()) {
                    // DayOfWeek.of(1) = LUNEDI, ... DayOfWeek.of(7) = DOMENICA
                    giorniSet.add(java.time.DayOfWeek.of(i + 1));
                }
            }
            if (giorniSet.isEmpty()) {
                // Se nessun giorno selezionato, usa tutti
                for (int i = 1; i <= 7; i++) {
                    giorniSet.add(java.time.DayOfWeek.of(i));
                }
            }
            
            // Recupera le date da non pianificare dalla lista
            List<LocalDate> dateDaNonPianificare = new ArrayList<>();
            for (int i = 0; i < dateListModel.size(); i++) {
                dateDaNonPianificare.add(LocalDate.parse(dateListModel.get(i)));
            }

            // Costruisci il calendario
            Calendario calendario = CostruttoreCalendario.costruisciCalendarioVuoto(
                dataInizio, 
                dataFine, 
                giorniSet,
                dateDaNonPianificare
            );

            // Crea e esegui la pianificazione
            Pianificazione pianificazione = new Pianificazione(incarichi, calendario, assenze);
            this.ultimaPianificazione = pianificazione; // Salva l'ultima pianificazione
            
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

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Errore di validazione", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Errore durante la pianificazione: " + e.getMessage(),
                "Errore", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void creaProgramma() {
        if (ultimaPianificazione == null) {
            JOptionPane.showMessageDialog(this,
                "Esegui prima la pianificazione!",
                "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            List<Assegnazione> assegnazioni = ultimaPianificazione.getAssegnazioni();
            List<Incarico> incarichi = ultimaPianificazione.getIncarichi();
            io.ScrittoreProgrammaIncarichi scrittore = new io.ScrittoreProgrammaIncarichi();
            scrittore.scriviFileCsv(assegnazioni, incarichi);
            JOptionPane.showMessageDialog(this,
                "Programma incarichi creato con successo!",
                "Successo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Errore durante la creazione del programma: " + e.getMessage(),
                "Errore", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void mostraMessaggio(String messaggio) {
        outputArea.append(messaggio + "\n");
    }
} 