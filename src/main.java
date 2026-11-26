import util.Tema;
import view.TelaLogin;

public class main {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Tema.instalar();
            new TelaLogin().setVisible(true);
        });
    }
}
