package util;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Caixas de mensagem no estilo da locadora.
 * Os métodos antigos (info/erro sem componente pai) continuam funcionando.
 */
public class Mensagem {

    public static void info(String msg) {
        info(null, msg);
    }

    public static void erro(String msg) {
        erro(null, msg);
    }

    public static void info(Component pai, String msg) {
        mostrar(pai, "Informação", msg, Tema.AMARELO, Tema.AZUL_NOITE, "i", false);
    }

    public static void erro(Component pai, String msg) {
        mostrar(pai, "Erro", msg, Tema.VERMELHO, Tema.BRANCO, "!", false);
    }

    /** Pergunta Sim/Não. Devolve true se a pessoa confirmou. */
    public static boolean confirmar(Component pai, String titulo, String msg) {
        return mostrar(pai, titulo, msg, Tema.AMARELO, Tema.AZUL_NOITE, "?", true);
    }

    // ------------------------------------------------------------------

    private static class Selo extends JComponent {
        private final Color fundo;
        private final Color letra;
        private final String simbolo;

        Selo(Color fundo, Color letra, String simbolo) {
            this.fundo = fundo;
            this.letra = letra;
            this.simbolo = simbolo;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(62, 62);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(fundo);
            g2.fill(new Ellipse2D.Float(0, 0, 54, 54));
            g2.setColor(Tema.AZUL_NOITE);
            g2.setStroke(new BasicStroke(3f));
            g2.draw(new Ellipse2D.Float(1.5f, 1.5f, 51, 51));
            g2.setFont(Tema.titulo(36));
            g2.setColor(letra);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(simbolo, (54 - fm.stringWidth(simbolo)) / 2,
                    (54 - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }

    private static String html(String texto) {
        return "<html><body style='width:270px'>"
                + texto.replace("&", "&amp;")
                        .replace("<", "&lt;")
                        .replace(">", "&gt;")
                        .replace("\n", "<br>")
                + "</body></html>";
    }

    private static boolean mostrar(Component pai, String titulo, String msg,
            Color corSelo, Color corLetra, String simbolo, boolean pergunta) {

        Window dono = pai instanceof Window
                ? (Window) pai
                : (pai == null ? null : SwingUtilities.getWindowAncestor(pai));

        JDialog dialogo = new JDialog(dono, titulo, Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setResizable(false);

        JPanel raiz = new JPanel(new BorderLayout(18, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(Tema.AZUL);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Tema.AMARELO);
                g.fillRect(0, 0, getWidth(), 6);
            }
        };
        raiz.setBorder(new EmptyBorder(28, 24, 18, 24));

        JPanel lateral = new JPanel(new BorderLayout());
        lateral.setOpaque(false);
        lateral.add(new Selo(corSelo, corLetra, simbolo), BorderLayout.NORTH);
        raiz.add(lateral, BorderLayout.WEST);

        JLabel texto = new JLabel(html(msg));
        texto.setFont(Tema.corpo(Font.BOLD, 15));
        texto.setForeground(Tema.CREME);
        raiz.add(texto, BorderLayout.CENTER);

        final boolean[] resposta = {false};

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoes.setOpaque(false);
        botoes.setBorder(new EmptyBorder(18, 0, 0, 0));

        JButton principal;
        if (pergunta) {
            JButton nao = Tema.botaoSecundario("Não");
            nao.addActionListener(e -> dialogo.dispose());
            principal = Tema.botao("Sim");
            principal.addActionListener(e -> {
                resposta[0] = true;
                dialogo.dispose();
            });
            botoes.add(nao);
            botoes.add(principal);
        } else {
            principal = Tema.botao("OK");
            principal.addActionListener(e -> dialogo.dispose());
            botoes.add(principal);
        }
        raiz.add(botoes, BorderLayout.SOUTH);

        dialogo.setContentPane(raiz);
        dialogo.getRootPane().setDefaultButton(principal);
        dialogo.pack();
        dialogo.setLocationRelativeTo(dono);
        dialogo.setVisible(true);

        return resposta[0];
    }
}
