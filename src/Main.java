
import view.SwingView;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        // Usiamo SwingUtilities per assicurarci che l'UI venga creata nel thread corretto
        SwingUtilities.invokeLater(() -> {
            SwingView view = new SwingView();
            view.setVisible(true);
        });
    }


}