package util;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

/**
 * Tema visual "locadora dos anos 90": azul royal, amarelo ingresso,
 * etiquetas vermelhas, papel creme e tira de filme.
 *
 * Chame Tema.instalar() uma vez, antes de abrir qualquer janela.
 */
public final class Tema {

    private Tema() {
    }

    // ------------------------------------------------------------------
    // PALETA
    // ------------------------------------------------------------------
    public static final Color AZUL = new Color(0x0033A0);
    public static final Color AZUL_ESCURO = new Color(0x001F66);
    public static final Color AZUL_NOITE = new Color(0x00133F);
    public static final Color AMARELO = new Color(0xFFD100);
    public static final Color AMARELO_ESCURO = new Color(0xD9A800);
    public static final Color LARANJA = new Color(0xF29100);
    public static final Color VERMELHO = new Color(0xD7141A);
    public static final Color CREME = new Color(0xFFF4C7);
    public static final Color BRANCO = Color.WHITE;
    public static final Color TEXTO = new Color(0x0A1A4A);

    // ------------------------------------------------------------------
    // FONTES (usa a primeira instalada; senão cai para uma fonte padrão)
    // ------------------------------------------------------------------
    private static final Set<String> FAMILIAS = new HashSet<>(Arrays.asList(
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getAvailableFontFamilyNames()));

    private static String escolher(String padrao, String... nomes) {
        for (String nome : nomes) {
            if (FAMILIAS.contains(nome)) {
                return nome;
            }
        }
        return padrao;
    }

    private static final String FAM_TITULO = escolher(Font.DIALOG,
            "Impact", "Haettenschweiler", "Anton", "Bebas Neue", "Arial Black");
    private static final boolean TITULO_PESADO = !Font.DIALOG.equals(FAM_TITULO);

    private static final String FAM_CORPO = escolher(Font.SANS_SERIF,
            "Arial", "Helvetica", "Liberation Sans", "DejaVu Sans");

    private static final String FAM_MONO = escolher(Font.MONOSPACED,
            "Courier New", "Liberation Mono", "DejaVu Sans Mono");

    /** Fonte de letreiro (Impact ou parecida). */
    public static Font titulo(float tamanho) {
        return new Font(FAM_TITULO, TITULO_PESADO ? Font.PLAIN : Font.BOLD, 1)
                .deriveFont(tamanho);
    }

    public static Font corpo(int estilo, float tamanho) {
        return new Font(FAM_CORPO, estilo, 1).deriveFont(tamanho);
    }

    /** Fonte de máquina de escrever, para preços e etiquetas. */
    public static Font mono(int estilo, float tamanho) {
        return new Font(FAM_MONO, estilo, 1).deriveFont(tamanho);
    }

    // ------------------------------------------------------------------
    // INSTALAÇÃO GLOBAL
    // ------------------------------------------------------------------
    public static void instalar() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignorada) {
            // segue com o visual padrão
        }

        // fonte padrão em todos os componentes
        Enumeration<Object> chaves = UIManager.getDefaults().keys();
        while (chaves.hasMoreElements()) {
            Object chave = chaves.nextElement();
            Object valor = UIManager.get(chave);
            if (valor instanceof FontUIResource) {
                Font antiga = (Font) valor;
                UIManager.put(chave, new FontUIResource(
                        corpo(antiga.getStyle(), antiga.getSize2D())));
            }
        }

        // barras de rolagem
        UIManager.put("ScrollBar.gradient", null);
        UIManager.put("ScrollBar.thumb", AMARELO);
        UIManager.put("ScrollBar.thumbShadow", AMARELO_ESCURO);
        UIManager.put("ScrollBar.thumbHighlight", new Color(0xFFE866));
        UIManager.put("ScrollBar.track", AZUL_NOITE);
        UIManager.put("ScrollBar.trackHighlight", AZUL_ESCURO);
        UIManager.put("ScrollBar.background", AZUL_ESCURO);

        // menus
        UIManager.put("MenuBar.gradient", null);
        UIManager.put("MenuBar.background", AZUL_NOITE);
        UIManager.put("MenuBar.foreground", AMARELO);
        UIManager.put("Menu.background", AZUL_NOITE);
        UIManager.put("Menu.foreground", AMARELO);
        UIManager.put("Menu.selectionBackground", AMARELO);
        UIManager.put("Menu.selectionForeground", AZUL_NOITE);
        UIManager.put("MenuItem.background", AZUL_ESCURO);
        UIManager.put("MenuItem.foreground", CREME);
        UIManager.put("MenuItem.selectionBackground", AMARELO);
        UIManager.put("MenuItem.selectionForeground", AZUL_NOITE);
        UIManager.put("PopupMenu.background", AZUL_ESCURO);
        UIManager.put("PopupMenu.border", new LineBorder(AMARELO, 2));
        UIManager.put("Menu.font", corpo(Font.BOLD, 14));
        UIManager.put("MenuItem.font", corpo(Font.BOLD, 14));

        // caixas de seleção
        UIManager.put("ComboBox.background", BRANCO);
        UIManager.put("ComboBox.foreground", TEXTO);
        UIManager.put("ComboBox.selectionBackground", AZUL);
        UIManager.put("ComboBox.selectionForeground", AMARELO);
        UIManager.put("ComboBox.buttonBackground", AMARELO);
        UIManager.put("ComboBox.buttonShadow", AMARELO_ESCURO);
        UIManager.put("ComboBox.buttonDarkShadow", AZUL_ESCURO);
        UIManager.put("ComboBox.buttonHighlight", new Color(0xFFE866));

        // dicas
        UIManager.put("ToolTip.background", AMARELO);
        UIManager.put("ToolTip.foreground", AZUL_NOITE);
        UIManager.put("ToolTip.border", new LineBorder(AZUL_NOITE, 1));
    }

    // ------------------------------------------------------------------
    // BOTÃO "ADESIVO"
    // ------------------------------------------------------------------
    public enum Tipo { PRIMARIO, SECUNDARIO, PERIGO }

    public static class Botao extends JButton {

        private static final int SOMBRA = 0;
        private final Tipo tipo;

        public Botao(String texto, Tipo tipo) {
            super(texto);
            this.tipo = tipo;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setFont(titulo(15));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(8, 18, 9, 18));
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(d.width + SOMBRA, d.height + SOMBRA);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth() - SOMBRA;
            int h = getHeight() - SOMBRA;
            boolean apertado = getModel().isArmed() && getModel().isPressed();

            Color fundo, texto, borda;
            switch (tipo) {
                case SECUNDARIO:
                    fundo = AZUL_ESCURO;
                    texto = AMARELO;
                    borda = AMARELO;
                    break;
                case PERIGO:
                    fundo = VERMELHO;
                    texto = BRANCO;
                    borda = AZUL_NOITE;
                    break;
                default:
                    fundo = AMARELO;
                    texto = AZUL_NOITE;
                    borda = AZUL_NOITE;
                    break;
            }

            if (!isEnabled()) {
                fundo = new Color(0x8A93A8);
                texto = new Color(0xDDE1EA);
                borda = new Color(0x5C6580);
            } else if (getModel().isRollover() && !apertado) {
                fundo = mais_claro(fundo);
            }

            if (apertado) {
                fundo = fundo.darker();
            }

            g2.setColor(fundo);
            g2.fillRoundRect(0, 0, w, h, 12, 12);
            g2.setColor(borda);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, w - 2, h - 2, 12, 12);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(getText())) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(texto);
            g2.drawString(getText(), tx, ty);
            g2.dispose();
        }

        private static Color mais_claro(Color c) {
            return new Color(
                    Math.min(255, c.getRed() + 38),
                    Math.min(255, c.getGreen() + 38),
                    Math.min(255, c.getBlue() + 38));
        }
    }

    public static JButton botao(String texto) {
        return new Botao(texto, Tipo.PRIMARIO);
    }

    public static JButton botaoSecundario(String texto) {
        return new Botao(texto, Tipo.SECUNDARIO);
    }

    public static JButton botaoPerigo(String texto) {
        return new Botao(texto, Tipo.PERIGO);
    }

    // ------------------------------------------------------------------
    // INGRESSO (logo) E CABEÇALHO
    // ------------------------------------------------------------------
    private static class Ingresso extends JComponent {

        private static final String NOME = "CINECLUB";

        @Override
        public Dimension getPreferredSize() {
            int larguraTexto = getFontMetrics(titulo(26)).stringWidth(NOME);
            return new Dimension(larguraTexto + 44, 44);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int r = 7;

            Area forma = new Area(new RoundRectangle2D.Float(0, 0, w, h, 10, 10));
            forma.subtract(new Area(new Ellipse2D.Float(-r, h / 2f - r, 2 * r, 2 * r)));
            forma.subtract(new Area(new Ellipse2D.Float(w - r, h / 2f - r, 2 * r, 2 * r)));
            g2.setColor(AMARELO);
            g2.fill(forma);

            g2.setFont(titulo(26));
            g2.setColor(AZUL);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(NOME, (w - fm.stringWidth(NOME)) / 2,
                    (h - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }

    /** Cabeçalho azul com o ingresso, o nome da seção e uma faixa amarela. */
    public static JPanel cabecalho(String secao, JComponent direita) {
        JPanel p = new JPanel(new BorderLayout(18, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(AZUL);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(AMARELO);
                g.fillRect(0, getHeight() - 4, getWidth(), 4);
            }
        };
        p.setBorder(new EmptyBorder(12, 18, 16, 18));

        p.add(new Ingresso(), BorderLayout.WEST);

        JLabel lbl = new JLabel(secao);
        lbl.setFont(titulo(26));
        lbl.setForeground(CREME);
        lbl.setBorder(new EmptyBorder(0, 6, 0, 0));
        p.add(lbl, BorderLayout.CENTER);

        if (direita != null) {
            p.add(direita, BorderLayout.EAST);
        }
        return p;
    }

    public static JPanel cabecalho(String secao) {
        return cabecalho(secao, null);
    }

    // ------------------------------------------------------------------
    // RODAPÉ
    // ------------------------------------------------------------------
    public static JPanel rodape() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AZUL_NOITE);
        JLabel lbl = new JLabel("SEJA GENTIL, REBOBINE.", SwingConstants.CENTER);
        lbl.setFont(mono(Font.BOLD, 12));
        lbl.setForeground(AMARELO);
        lbl.setBorder(new EmptyBorder(7, 0, 8, 0));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    // ------------------------------------------------------------------
    // FUNDOS, CARTÃO E FORMULÁRIO
    // ------------------------------------------------------------------
    /** Painel de fundo azul com leve degradê. */
    public static JPanel fundo(LayoutManager layout) {
        return new JPanel(layout) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(AZUL);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
    }

    private static void pintarCartao(Graphics g, int w, int h) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(CREME);
        g2.fillRoundRect(0, 0, w, h, 16, 16);
        g2.setColor(AMARELO);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRoundRect(2, 2, w - 4, h - 4, 14, 14);
        g2.dispose();
    }

    /** Cartão creme (carteirinha de sócio) para agrupar formulários. */
    public static class Cartao extends JPanel {
        public Cartao(LayoutManager layout) {
            super(layout);
            setOpaque(false);
            setBorder(new EmptyBorder(24, 28, 26, 28));
        }

        @Override
        protected void paintComponent(Graphics g) {
            pintarCartao(g, getWidth(), getHeight());
        }
    }

    /** Cartão com linhas "rótulo + campo" já alinhadas. */
    public static class Formulario extends Cartao {

        private int linha = 0;

        public Formulario() {
            super(new GridBagLayout());
        }

        public void titulo(String texto) {
            JLabel lbl = new JLabel(texto);
            lbl.setFont(Tema.titulo(24));
            lbl.setForeground(AZUL);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = linha++;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(0, 0, 14, 0);
            add(lbl, c);
        }

        public void adicionar(String rotulo, JComponent campo) {
            GridBagConstraints c = new GridBagConstraints();
            c.gridy = linha++;
            c.insets = new Insets(0, 0, 12, 14);
            c.anchor = GridBagConstraints.NORTHWEST;

            c.gridx = 0;
            c.weightx = 0;
            c.fill = GridBagConstraints.NONE;
            c.insets = new Insets(8, 0, 12, 14);
            add(Tema.rotulo(rotulo), c);

            c.gridx = 1;
            c.weightx = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(0, 0, 12, 0);
            add(campo, c);
        }

        public void botoes(JComponent... botoes) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            p.setOpaque(false);
            for (JComponent b : botoes) {
                p.add(b);
            }
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = linha++;
            c.gridwidth = 2;
            c.weighty = 1;
            c.anchor = GridBagConstraints.NORTH;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(10, 0, 0, 0);
            add(p, c);
        }
    }

    // ------------------------------------------------------------------
    // CAMPOS, RÓTULOS E ETIQUETAS
    // ------------------------------------------------------------------
    /** Rótulo de formulário (para usar sobre o cartão creme). */
    public static JLabel rotulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(corpo(Font.BOLD, 13));
        lbl.setForeground(AZUL_ESCURO);
        return lbl;
    }

    private static Border bordaCampo(Color cor) {
        return new CompoundBorder(new LineBorder(cor, 2), new EmptyBorder(5, 8, 5, 8));
    }

    public static <T extends JTextComponent> T campo(T c) {
        c.setFont(mono(Font.BOLD, 14));
        c.setBackground(BRANCO);
        c.setForeground(TEXTO);
        c.setCaretColor(AZUL_ESCURO);
        c.setSelectionColor(AMARELO);
        c.setSelectedTextColor(AZUL_NOITE);
        c.setBorder(bordaCampo(AZUL));
        c.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                c.setBorder(bordaCampo(LARANJA));
            }

            @Override
            public void focusLost(FocusEvent e) {
                c.setBorder(bordaCampo(AZUL));
            }
        });
        return c;
    }

    public static JTextField campo() {
        return campo(new JTextField());
    }

    public static JPasswordField campoSenha() {
        return campo(new JPasswordField());
    }

    public static JTextArea areaTexto(int linhas) {
        JTextArea a = new JTextArea(linhas, 20);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setFont(mono(Font.BOLD, 14));
        a.setBackground(BRANCO);
        a.setForeground(TEXTO);
        a.setCaretColor(AZUL_ESCURO);
        a.setSelectionColor(AMARELO);
        a.setSelectedTextColor(AZUL_NOITE);
        a.setBorder(new EmptyBorder(5, 8, 5, 8));
        return a;
    }

    /** Moldura da área de texto (a borda fica no scroll, não no texto). */
    public static JScrollPane rolagem(JTextArea area) {
        JScrollPane sp = new JScrollPane(area);
        sp.setBorder(new LineBorder(AZUL, 2));
        area.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                sp.setBorder(new LineBorder(LARANJA, 2));
            }

            @Override
            public void focusLost(FocusEvent e) {
                sp.setBorder(new LineBorder(AZUL, 2));
            }
        });
        return sp;
    }

    public static <T> JComboBox<T> combo(JComboBox<T> c) {
        c.setFont(corpo(Font.BOLD, 14));
        c.setBackground(BRANCO);
        c.setForeground(TEXTO);
        c.setBorder(new LineBorder(AZUL, 2));
        return c;
    }

    public static JSpinner spinner(JSpinner s) {
        s.setFont(mono(Font.BOLD, 14));
        s.setBorder(new LineBorder(AMARELO, 2));
        JComponent editor = s.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(BRANCO);
            tf.setForeground(TEXTO);
            tf.setFont(mono(Font.BOLD, 14));
        }
        return s;
    }

    /** Rótulo para usar direto sobre fundo azul. */
    public static JLabel rotuloClaro(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(titulo(16));
        lbl.setForeground(AMARELO);
        return lbl;
    }

    /** Etiqueta arredondada (preço, aviso de estoque...). */
    public static JLabel etiqueta(String texto, Color fundo, Color letra) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(AZUL_NOITE);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lbl.setOpaque(false);
        lbl.setBackground(fundo);
        lbl.setForeground(letra);
        lbl.setFont(mono(Font.BOLD, 14));
        lbl.setBorder(new EmptyBorder(3, 12, 3, 12));
        return lbl;
    }

    /** Visor de caixa registradora (ex.: total da compra). */
    public static JLabel visor(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.RIGHT);
        lbl.setOpaque(true);
        lbl.setBackground(new Color(0x000A26));
        lbl.setForeground(AMARELO);
        lbl.setFont(mono(Font.BOLD, 20));
        lbl.setBorder(new CompoundBorder(
                new LineBorder(AMARELO, 2),
                new EmptyBorder(6, 14, 6, 14)));
        return lbl;
    }

    public static Border borda(String titulo) {
        return BorderFactory.createTitledBorder(
                new LineBorder(AMARELO, 2),
                " " + titulo + " ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                titulo(16),
                AMARELO);
    }

    // ------------------------------------------------------------------
    // TABELAS, ROLAGEM E DIVISOR
    // ------------------------------------------------------------------
    /** Aplica o estilo "ficha de locação" e devolve a tabela dentro de um JScrollPane. */
    public static JScrollPane tabela(JTable t) {
        t.setRowHeight(30);
        t.setFont(corpo(Font.PLAIN, 14));
        t.setForeground(TEXTO);
        t.setBackground(CREME);
        t.setGridColor(new Color(0xE8D78F));
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setSelectionBackground(AZUL);
        t.setSelectionForeground(AMARELO);
        t.setFillsViewportHeight(true);

        DefaultTableCellRenderer zebra = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tabela, Object valor,
                    boolean selecionado, boolean foco, int linha, int coluna) {
                super.getTableCellRendererComponent(tabela, valor, selecionado, false,
                        linha, coluna);
                if (!selecionado) {
                    setBackground(linha % 2 == 0 ? CREME : new Color(0xFFEBA3));
                    setForeground(TEXTO);
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return this;
            }
        };
        t.setDefaultRenderer(Object.class, zebra);
        t.setDefaultRenderer(Number.class, zebra);

        JTableHeader cab = t.getTableHeader();
        cab.setReorderingAllowed(false);
        cab.setPreferredSize(new Dimension(100, 36));
        cab.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tabela, Object valor,
                    boolean selecionado, boolean foco, int linha, int coluna) {
                super.getTableCellRendererComponent(tabela, valor, false, false,
                        linha, coluna);
                setBackground(AZUL_NOITE);
                setForeground(AMARELO);
                setFont(titulo(15));
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(new CompoundBorder(
                        new MatteBorder(0, 0, 3, 0, AMARELO),
                        new EmptyBorder(0, 10, 0, 10)));
                return this;
            }
        });

        JScrollPane sp = new JScrollPane(t);
        sp.getViewport().setBackground(CREME);
        sp.setBorder(new LineBorder(AMARELO, 3));
        return sp;
    }

    /** Área de rolagem para cards sobre fundo azul. */
    public static JScrollPane rolagemAzul(Component conteudo) {
        JScrollPane sp = new JScrollPane(conteudo);
        sp.setBorder(null);
        sp.getViewport().setBackground(AZUL);
        sp.getVerticalScrollBar().setUnitIncrement(18);
        return sp;
    }

    /** Divisor amarelo para JSplitPane. Chame antes de definir a posição. */
    public static void estilizarDivisor(JSplitPane sp) {
        sp.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                BasicSplitPaneDivider d = new BasicSplitPaneDivider(this);
                d.setBackground(AMARELO);
                d.setBorder(null);
                return d;
            }
        });
        sp.setBorder(null);
        sp.setDividerSize(8);
        sp.setBackground(AZUL);
    }

    // ------------------------------------------------------------------
    // BARRA DE MENU
    // ------------------------------------------------------------------
    public static void estilizarMenu(JMenuBar barra) {
        barra.setOpaque(true);
        barra.setBackground(AZUL_NOITE);
        barra.setBorder(new MatteBorder(0, 0, 3, 0, AMARELO));
        for (int i = 0; i < barra.getMenuCount(); i++) {
            JMenu menu = barra.getMenu(i);
            menu.setForeground(AMARELO);
            menu.setFont(corpo(Font.BOLD, 14));
            for (int j = 0; j < menu.getItemCount(); j++) {
                JMenuItem item = menu.getItem(j);
                if (item != null) {
                    item.setBackground(AZUL_ESCURO);
                    item.setForeground(CREME);
                    item.setFont(corpo(Font.BOLD, 14));
                }
            }
        }
    }
}
