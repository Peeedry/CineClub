package view;

import model.Filmes;
import util.Tema;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class PainelCardsFilmes extends JPanel {

    private final NumberFormat moeda =
            NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public PainelCardsFilmes(
            List<Filmes> filmes,
            Consumer<Filmes> acaoEditar,
            Consumer<Filmes> acaoExcluir,
            Consumer<Filmes> acaoAdicionar
    ) {

        setLayout(new GridLayout(0, 3, 18, 18));
        setBackground(Tema.AZUL);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        for (Filmes filme : filmes) {
            add(criarCard(
                    filme,
                    acaoEditar,
                    acaoExcluir,
                    acaoAdicionar
            ));
        }
    }

    /** Caixa do filme: capa em cima, etiqueta de papel embaixo. */
    private static class CaixaVHS extends JPanel {

        CaixaVHS() {
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(16, 16, 16, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Tema.AZUL_ESCURO);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.setColor(Tema.AMARELO);
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 14, 14);
            g2.dispose();
        }
    }

    private JPanel criarCard(
            Filmes filme,
            Consumer<Filmes> acaoEditar,
            Consumer<Filmes> acaoExcluir,
            Consumer<Filmes> acaoAdicionar
    ) {

        JPanel card = new CaixaVHS();

        // POSTER
        JLabel lblPoster = new JLabel("", SwingConstants.CENTER);
        lblPoster.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPoster.setBorder(new LineBorder(Tema.CREME, 3));
        lblPoster.setForeground(Tema.AMARELO);
        lblPoster.setFont(Tema.corpo(Font.BOLD, 12));
        Dimension tamPoster = new Dimension(156, 216);
        lblPoster.setPreferredSize(tamPoster);
        lblPoster.setMinimumSize(tamPoster);
        lblPoster.setMaximumSize(tamPoster);

        carregarPoster(lblPoster, filme.getPoster());

        // ETIQUETA DE PAPEL
        JPanel etiqueta = new JPanel();
        etiqueta.setLayout(new BoxLayout(etiqueta, BoxLayout.Y_AXIS));
        etiqueta.setBackground(Tema.CREME);
        etiqueta.setBorder(new CompoundBorder(
                new LineBorder(Tema.AMARELO_ESCURO, 2),
                new EmptyBorder(8, 8, 8, 8)));
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        etiqueta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // NOME
        JLabel lblNome = new JLabel(
                "<html><body style='width:150px'><center>"
                + filme.getNome() + "</center></body></html>",
                SwingConstants.CENTER
        );
        lblNome.setFont(Tema.titulo(18));
        lblNome.setForeground(Tema.AZUL);
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        // GENERO
        JLabel lblGenero = new JLabel(
                filme.getGen() == null ? "" : filme.getGen()
        );
        lblGenero.setFont(Tema.mono(Font.BOLD, 12));
        lblGenero.setForeground(Tema.AZUL_ESCURO);
        lblGenero.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblFormato = new JLabel(
                "Formato: " + (filme.getForm() == null ? "Não informado" : filme.getForm()));
        lblFormato.setFont(Tema.mono(Font.BOLD, 12));
        lblFormato.setForeground(Tema.AZUL_ESCURO);
        lblFormato.setAlignmentX(Component.CENTER_ALIGNMENT);


        // PRECO
        JLabel lblPreco = Tema.etiqueta(
                moeda.format(filme.getVal()), Tema.AMARELO, Tema.AZUL_NOITE);
        lblPreco.setFont(Tema.mono(Font.BOLD, 16));
        lblPreco.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ESTOQUE
        JLabel lblEstoque = new JLabel(
                "Disponível: " + filme.getQuant()
        );
        lblEstoque.setFont(Tema.mono(Font.BOLD, 12));
        lblEstoque.setForeground(Tema.AZUL_ESCURO);
        lblEstoque.setAlignmentX(Component.CENTER_ALIGNMENT);

        etiqueta.add(lblNome);
        etiqueta.add(Box.createVerticalStrut(3));
        etiqueta.add(lblGenero);
        etiqueta.add(Box.createVerticalStrut(3));
        etiqueta.add(lblFormato);
        etiqueta.add(Box.createVerticalStrut(8));
        etiqueta.add(lblPreco);

        card.add(lblPoster);
        card.add(Box.createVerticalStrut(10));
        card.add(etiqueta);

        // BOTOES DE ADMINISTRACAO
        if (acaoEditar != null || acaoExcluir != null) {

            JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
            painelAcoes.setOpaque(false);

            if (acaoEditar != null) {
                JButton btnEditar = Tema.botao("Editar");
                btnEditar.setFont(Tema.titulo(13));
                btnEditar.addActionListener(e -> acaoEditar.accept(filme));
                painelAcoes.add(btnEditar);
            }

            if (acaoExcluir != null) {
                JButton btnExcluir = Tema.botaoPerigo("Excluir");
                btnExcluir.setFont(Tema.titulo(13));
                btnExcluir.addActionListener(e -> acaoExcluir.accept(filme));
                painelAcoes.add(btnExcluir);
            }

            card.add(Box.createVerticalStrut(12));
            card.add(painelAcoes);
        }

        // BOTAO DE COMPRA
        if (acaoAdicionar != null) {

            JButton btnAdicionar = Tema.botao("Adicionar ao carrinho");
            btnAdicionar.setFont(Tema.titulo(14));
            btnAdicionar.setAlignmentX(Component.CENTER_ALIGNMENT);

            btnAdicionar.addActionListener(
                    e -> acaoAdicionar.accept(filme)
            );

            card.add(Box.createVerticalStrut(12));
            card.add(btnAdicionar);
        }

        return card;
    }

    private void carregarPoster(JLabel label, String caminho) {

        try {

            if (caminho == null || caminho.trim().isEmpty()) {
                label.setText("Sem pôster");
                return;
            }

            ImageIcon icone = null;

            File arquivo = new File(caminho);

            if (arquivo.exists()) {
                icone = new ImageIcon(arquivo.getAbsolutePath());
            } else {
                URL recurso = getClass().getResource("/" + caminho);
                if (recurso != null) {
                    icone = new ImageIcon(recurso);
                }
            }

            if (icone == null || icone.getIconWidth() <= 0) {
                label.setText("Pôster não encontrado");
                return;
            }

            Image imagem = icone.getImage().getScaledInstance(
                    150, 210, Image.SCALE_SMOOTH
            );

            label.setIcon(new ImageIcon(imagem));

        } catch (Exception e) {
            label.setText("Erro ao carregar pôster");
        }
    }
}
