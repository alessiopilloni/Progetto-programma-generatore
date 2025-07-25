package view;

import model.*;
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
import controller.AppController;

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
    private JButton aggiungiAssenzaButton;
    private JButton pianificaButton;
    private JButton creaProgrammaButton;
    private JButton resetButton;
    private AppController controller;

    public SwingView() {
        controller = new AppController();
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
        addDateButton.setEnabled(false); // Disabilitato di default
        mainPanel.add(addDateButton, gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        dateListModel = new DefaultListModel<>();
        dateList = new JList<>(dateListModel);
        dateList.setVisibleRowCount(10);
        dateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane dateListScrollPane = new JScrollPane(dateList);
        dateListScrollPane.setPreferredSize(new Dimension(150, 160)); // larghezza e altezza fissa per 10 righe
        mainPanel.add(dateListScrollPane, gbc);

        gbc.gridx = 2; gbc.gridy = 5;
        removeDateButton = new JButton("Rimuovi");
        removeDateButton.setEnabled(false); // Disabilitato di default
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

        // Bottone Reset
        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetFields());
        mainPanel.add(resetButton, gbc);
        gbc.gridwidth = 1;

        // Reset: pulisce tutti i campi, pulisce l'output e pulisce la lista delle date e le assenze

        // Area output
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 3; // Occupa tutta la larghezza
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        // Area output per mostrare i risultati della pianificazione
        outputArea = new JTextArea(10, 40); // dimensioni più ragionevoli
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, gbc);
        gbc.gridwidth = 1; // Ripristina il valore di default

        add(mainPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);

        // Listener per abilitare/disabilitare i bottoni in base al range
        java.beans.PropertyChangeListener rangeListener = evt -> {
            boolean rangeValido = dataInizioChooser.getDate() != null && dataFineChooser.getDate() != null;
            addDateButton.setEnabled(rangeValido && dateChooserNonPianificare.getDate() != null);
            removeDateButton.setEnabled(rangeValido && !dateListModel.isEmpty());
        };
        dataInizioChooser.addPropertyChangeListener("date", rangeListener);
        dataFineChooser.addPropertyChangeListener("date", rangeListener);
        dateChooserNonPianificare.addPropertyChangeListener("date", rangeListener);
        dateListModel.addListDataListener(new javax.swing.event.ListDataListener() {
            public void intervalAdded(javax.swing.event.ListDataEvent e) { rangeListener.propertyChange(null); }
            public void intervalRemoved(javax.swing.event.ListDataEvent e) { rangeListener.propertyChange(null); }
            public void contentsChanged(javax.swing.event.ListDataEvent e) { rangeListener.propertyChange(null); }
        });

        // Listener per aggiunta data
        addDateButton.addActionListener(e -> {
            Date selectedDate = dateChooserNonPianificare.getDate();
            Date inizio = dataInizioChooser.getDate();
            Date fine = dataFineChooser.getDate();
            if (selectedDate != null && inizio != null && fine != null) {
                if (!selectedDate.before(inizio) && !selectedDate.after(fine)) {
                    String dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
                    if (!dateListModel.contains(dateStr)) {
                        dateListModel.addElement(dateStr);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "La data selezionata deve essere compresa nel range di date.", "Errore", JOptionPane.ERROR_MESSAGE);
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

    private void resetFields() {
        csvPathField.setText("");
        dataInizioChooser.setDate(new Date());
        dataFineChooser.setDate(new Date());
        for (JCheckBox cb : giorniCheckBox) {
            cb.setSelected(false);
        }
        dateChooserNonPianificare.setDate(null);
        dateListModel.clear();
        outputArea.setText("");
        controller.reset();
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
            try {
                controller.caricaIncarichi(selectedFile.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore nel caricamento del CSV: " + ex.getMessage());
                controller.reset();
            }
        }
    }

    private void mostraDialogAssenza() {
        // Controllo se ci sono incarichi caricati
        if (controller.getIncarichi().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Devi prima caricare un file CSV con gli incarichi!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JDialog dialog = new JDialog(this, "Aggiungi Assenza", true);
        dialog.setLayout(new GridLayout(3, 2, 5, 5));

        // Estrai tutti i nomi unici delle persone dagli incarichi
        java.util.Set<String> nomiPersone = new java.util.LinkedHashSet<>();
        for (Incarico inc : controller.getIncarichi()) {
            for (Persona p : inc.getLista()) {
                nomiPersone.add(p.getNomeECognome());
            }
        }
        JComboBox<String> nomeComboBox = new JComboBox<>(nomiPersone.toArray(new String[0]));
        JDateChooser dataChooser = new JDateChooser();
        dataChooser.setDateFormatString("dd/MM/yyyy");

        dialog.add(new JLabel("Nome e Cognome:"));
        dialog.add(nomeComboBox);
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
            String nomeSelezionato = (String) nomeComboBox.getSelectedItem();
            Persona persona = new Persona(nomeSelezionato);
            LocalDate data = dataChooser.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
            controller.aggiungiAssenza(new Assenza(persona, data));
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
            Pianificazione pianificazione = controller.pianifica(dataInizio, dataFine, giorniSet, dateDaNonPianificare);
            
            // Pulisci l'area di output
            outputArea.setText("");
            
            // Mostra le date pianificate
            outputArea.append("Date pianificate:\n");
            List<LocalDate> datePianificate = pianificazione.getDatePianificate();
            for (LocalDate data : datePianificate) {
                outputArea.append(data.toString() + "\n");
            }
            
            // Mostra le assenze registrate
            outputArea.append("\nAssenze registrate:\n");
            List<Assenza> assenze = controller.getAssenze();
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
        try {
            controller.creaProgramma();
            JOptionPane.showMessageDialog(this,
                "Programma incarichi creato con successo!",
                "Successo", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this,
                "Esegui prima la pianificazione!",
                "Errore", JOptionPane.ERROR_MESSAGE);
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