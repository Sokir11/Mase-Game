import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Mase window = new Mase();
            window.setVisible(true);
        });
    }
}