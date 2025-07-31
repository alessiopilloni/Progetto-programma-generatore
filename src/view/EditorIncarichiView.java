package view;

import model.Incarico;
import model.Persona;
import engine.GestoreIncarichi;
import io.ScrittoreIncarichiCSV;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * View per la gestione degli incarichi con interfaccia grafica.
 */
public class EditorIncarichiView extends JFrame {
    private List<Incarico> incarichi;
    private JList<String> incarichiList;
    private DefaultListModel<String> incarichiListModel;
    private JList<String> personeList;
    private DefaultListModel<String> personeListModel;
    private JTextField nuovoIncaricoField;
    private JTextField nuovaPersonaField;
    private JTextField filePathField;
    private JButton salvaButton;
    private JButton caricaButton;
    
    public EditorIncarichiView(List<Incarico> incarichiIniziali) {
        this.incarichi = new ArrayList<>(incarichiIniziali);
        setupUI();
        aggiornaListe();
    }
    
    private void setupUI() {
        setTitle("Editor Incarichi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Layout principale
        setLayout(new BorderLayout());
        
        // Panel superiore per il file
        JPanel filePanel = new JPanel(new BorderLayout());
        filePanel.setBorder(BorderFactory.createTitledBorder("File CSV"));
        
        filePathField = new JTextField();
        filePathField.setEditable(false);
        filePanel.add(filePathField, BorderLayout.CENTER);
        
        JButton sfogliaButton = new JButton("Sfoglia");
        sfogliaButton.addActionListener(e -> mostraFileChooser());
        filePanel.add(sfogliaButton, BorderLayout.EAST);
        
        add(filePanel, BorderLayout.NORTH);
        
        // Panel centrale con liste
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel sinistro per gli incarichi
        JPanel incarichiPanel = new JPanel(new BorderLayout());
        incarichiPanel.setBorder(BorderFactory.createTitledBorder("Incarichi"));
        
        incarichiListModel = new DefaultListModel<>();
        incarichiList = new JList<>(incarichiListModel);
        incarichiList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        incarichiList.addListSelectionListener(e -> aggiornaListaPersone());
        
        JScrollPane incarichiScrollPane = new JScrollPane(incarichiList);
        incarichiPanel.add(incarichiScrollPane, BorderLayout.CENTER);
        
        // Panel per i controlli degli incarichi
        JPanel incarichiControlsPanel = new JPanel(new BorderLayout());
        
        JPanel nuovoIncaricoPanel = new JPanel(new BorderLayout());
        nuovoIncaricoField = new JTextField();
        nuovoIncaricoField.addActionListener(e -> aggiungiIncarico());
        nuovoIncaricoPanel.add(new JLabel("Nuovo incarico:"), BorderLayout.NORTH);
        nuovoIncaricoPanel.add(nuovoIncaricoField, BorderLayout.CENTER);
        
        JButton aggiungiIncaricoButton = new JButton("Aggiungi Incarico");
        aggiungiIncaricoButton.addActionListener(e -> aggiungiIncarico());
        nuovoIncaricoPanel.add(aggiungiIncaricoButton, BorderLayout.SOUTH);
        
        incarichiControlsPanel.add(nuovoIncaricoPanel, BorderLayout.NORTH);
        
        JButton rimuoviIncaricoButton = new JButton("Rimuovi Incarico");
        rimuoviIncaricoButton.addActionListener(e -> rimuoviIncarico());
        incarichiControlsPanel.add(rimuoviIncaricoButton, BorderLayout.SOUTH);
        
        incarichiPanel.add(incarichiControlsPanel, BorderLayout.SOUTH);
        
        // Panel destro per le persone
        JPanel personePanel = new JPanel(new BorderLayout());
        personePanel.setBorder(BorderFactory.createTitledBorder("Persone"));
        
        personeListModel = new DefaultListModel<>();
        personeList = new JList<>(personeListModel);
        personeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane personeScrollPane = new JScrollPane(personeList);
        personePanel.add(personeScrollPane, BorderLayout.CENTER);
        
        // Panel per i controlli delle persone
        JPanel personeControlsPanel = new JPanel(new BorderLayout());
        
        JPanel nuovaPersonaPanel = new JPanel(new BorderLayout());
        nuovaPersonaField = new JTextField();
        nuovaPersonaField.addActionListener(e -> aggiungiPersona());
        nuovaPersonaPanel.add(new JLabel("Nuova persona:"), BorderLayout.NORTH);
        nuovaPersonaPanel.add(nuovaPersonaField, BorderLayout.CENTER);
        
        JButton aggiungiPersonaButton = new JButton("Aggiungi Persona");
        aggiungiPersonaButton.addActionListener(e -> aggiungiPersona());
        nuovaPersonaPanel.add(aggiungiPersonaButton, BorderLayout.SOUTH);
        
        personeControlsPanel.add(nuovaPersonaPanel, BorderLayout.NORTH);
        
        JButton rimuoviPersonaButton = new JButton("Rimuovi Persona");
        rimuoviPersonaButton.addActionListener(e -> rimuoviPersona());
        personeControlsPanel.add(rimuoviPersonaButton, BorderLayout.SOUTH);
        
        personePanel.add(personeControlsPanel, BorderLayout.SOUTH);
        
        centerPanel.add(incarichiPanel);
        centerPanel.add(personePanel);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Panel inferiore per i pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        salvaButton = new JButton("Salva Modifiche");
        salvaButton.addActionListener(e -> salvaModifiche());
        buttonPanel.add(salvaButton);
        
        caricaButton = new JButton("Carica File");
        caricaButton.addActionListener(e -> caricaFile());
        buttonPanel.add(caricaButton);
        
        JButton chiudiButton = new JButton("Chiudi");
        chiudiButton.addActionListener(e -> dispose());
        buttonPanel.add(chiudiButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void mostraFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
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
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.toLowerCase().endsWith(".csv")) {
                path += ".csv";
            }
            filePathField.setText(path);
        }
    }
    
    private void aggiornaListe() {
        // Aggiorna lista incarichi
        incarichiListModel.clear();
        List<String> nomiIncarichi = GestoreIncarichi.getNomiIncarichi(incarichi);
        for (String nome : nomiIncarichi) {
            incarichiListModel.addElement(nome);
        }
        
        // Aggiorna lista persone se un incarico è selezionato
        aggiornaListaPersone();
    }
    
    private void aggiornaListaPersone() {
        personeListModel.clear();
        int selectedIndex = incarichiList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < incarichi.size()) {
            Incarico incaricoSelezionato = incarichi.get(selectedIndex);
            for (Persona persona : incaricoSelezionato.getLista()) {
                personeListModel.addElement(persona.getNomeECognome());
            }
        }
    }
    
    private void aggiungiIncarico() {
        String nomeIncarico = nuovoIncaricoField.getText().trim();
        if (nomeIncarico.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci il nome dell'incarico", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            incarichi = GestoreIncarichi.aggiungiIncarico(incarichi, nomeIncarico);
            aggiornaListe();
            nuovoIncaricoField.setText("");
            JOptionPane.showMessageDialog(this, "Incarico '" + nomeIncarico + "' aggiunto con successo", "Successo", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void rimuoviIncarico() {
        int selectedIndex = incarichiList.getSelectedIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Seleziona un incarico da rimuovere", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String nomeIncarico = incarichiList.getSelectedValue();
        int result = JOptionPane.showConfirmDialog(this, 
            "Sei sicuro di voler rimuovere l'incarico '" + nomeIncarico + "'?", 
            "Conferma rimozione", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                incarichi = GestoreIncarichi.rimuoviIncarico(incarichi, nomeIncarico);
                aggiornaListe();
                JOptionPane.showMessageDialog(this, "Incarico '" + nomeIncarico + "' rimosso con successo", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void aggiungiPersona() {
        int selectedIndex = incarichiList.getSelectedIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Seleziona un incarico prima di aggiungere una persona", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String nomePersona = nuovaPersonaField.getText().trim();
        if (nomePersona.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci il nome della persona", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String nomeIncarico = incarichiList.getSelectedValue();
        try {
            incarichi = GestoreIncarichi.aggiungiPersonaAIncarico(incarichi, nomeIncarico, nomePersona);
            aggiornaListe();
            nuovaPersonaField.setText("");
            JOptionPane.showMessageDialog(this, "Persona '" + nomePersona + "' aggiunta all'incarico '" + nomeIncarico + "'", "Successo", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void rimuoviPersona() {
        int selectedIncaricoIndex = incarichiList.getSelectedIndex();
        int selectedPersonaIndex = personeList.getSelectedIndex();
        
        if (selectedIncaricoIndex < 0) {
            JOptionPane.showMessageDialog(this, "Seleziona un incarico", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (selectedPersonaIndex < 0) {
            JOptionPane.showMessageDialog(this, "Seleziona una persona da rimuovere", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String nomeIncarico = incarichiList.getSelectedValue();
        String nomePersona = personeList.getSelectedValue();
        
        int result = JOptionPane.showConfirmDialog(this, 
            "Sei sicuro di voler rimuovere '" + nomePersona + "' dall'incarico '" + nomeIncarico + "'?", 
            "Conferma rimozione", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                incarichi = GestoreIncarichi.rimuoviPersonaDaIncarico(incarichi, nomeIncarico, nomePersona);
                aggiornaListe();
                JOptionPane.showMessageDialog(this, "Persona '" + nomePersona + "' rimossa dall'incarico '" + nomeIncarico + "'", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void salvaModifiche() {
        String filePath = filePathField.getText().trim();
        if (filePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleziona un file di destinazione", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            ScrittoreIncarichiCSV.scriviIncarichi(incarichi, filePath);
            JOptionPane.showMessageDialog(this, "Modifiche salvate con successo in '" + filePath + "'", "Successo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore durante il salvataggio: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void caricaFile() {
        JFileChooser fileChooser = new JFileChooser();
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
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                incarichi = io.LettoreCSV.leggiIncarichi(path);
                filePathField.setText(path);
                aggiornaListe();
                JOptionPane.showMessageDialog(this, "File caricato con successo", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errore durante il caricamento: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Restituisce la lista aggiornata degli incarichi
     * @return Lista degli incarichi modificata
     */
    public List<Incarico> getIncarichi() {
        return new ArrayList<>(incarichi);
    }
} 