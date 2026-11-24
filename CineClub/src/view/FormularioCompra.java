
package view;

import controller.ComprasController;
import dao.FilmesDAO;
import model.Filmes;
import model.ItensCompra;
import util.Mensagem;
import util.Tema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FormularioCompra extends JFrame {

    private final int usuarioID;

    private final FilmesDAO filmesDAO = new FilmesDAO();

    private final ComprasController comprasController =
            new ComprasController();

    private final List<Filmes> filmes = new ArrayList<>();
    private final List<ItensCompra> carrinho = new ArrayList<>();

    private JPanel painelCards;
    private JScrollPane scrollCards;

    private JTable tabela;
    private DefaultTableModel modelo;

    private JSpinner spQuantidade;
    private JLabel lblTotal;
    private JComboBox<String> comboPagamento;

    private final NumberFormat moeda =
            NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public FormularioCompra(int usuarioID) {

        this.usuarioID = usuarioID;

        setTitle("CineClub - Nova Compra");
        setSize(1150, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        criarInterface();
        carregarFilmes();
    }

    private void criarInterface() {

        setLayout(new BorderLayout());

        add(Tema.cabecalho("Nova compra"), BorderLayout.NORTH);

        // PAINEL SUPERIOR
        JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        painelSuperior.setOpaque(false);
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        painelSuperior.add(Tema.rotuloClaro("Quantidade:"));

        spQuantidade = Tema.spinner(new JSpinner(
                new SpinnerNumberModel(1, 1, 999, 1)
        ));
        spQuantidade.setPreferredSize(new Dimension(90, 32));

        painelSuperior.add(spQuantidade);

        // PAINEL DOS CARDS
        painelCards = new JPanel();
        painelCards.setBackground(Tema.AZUL);

        scrollCards = Tema.rolagemAzul(painelCards);
        scrollCards.setBackground(Tema.AZUL);
        scrollCards.setBorder(Tema.borda("Catálogo de filmes"));

        // TABELA DO CARRINHO
        modelo = new DefaultTableModel(
                new Object[]{
                    "ID Filme",
                    "Filme",
                    "Preço Unitário",
                    "Quantidade",
                    "Subtotal"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);

        JScrollPane scrollTabela = Tema.tabela(tabela);
        scrollTabela.setBackground(Tema.AZUL);
        scrollTabela.setBorder(Tema.borda("Carrinho de compras"));

        // DIVISAO ENTRE CATALOGO E CARRINHO
        JSplitPane divisao = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                scrollCards,
                scrollTabela
        );

        Tema.estilizarDivisor(divisao);
        divisao.setResizeWeight(0.70);
        divisao.setDividerLocation(470);

        // CORPO (azul)
        JPanel corpo = Tema.fundo(new BorderLayout());
        corpo.setBorder(BorderFactory.createEmptyBorder(14, 16, 6, 16));
        corpo.add(painelSuperior, BorderLayout.NORTH);
        corpo.add(divisao, BorderLayout.CENTER);

        // PAINEL INFERIOR
        JPanel painelInferior = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 12, 0)
        );
        painelInferior.setOpaque(false);
        painelInferior.setBorder(BorderFactory.createEmptyBorder(14, 0, 8, 0));

        lblTotal = Tema.visor("Total: R$ 0,00");

        JButton btnRemover = Tema.botaoSecundario("Remover item");
        JButton btnFinalizar = Tema.botao("Finalizar compra");

        painelInferior.add(lblTotal);
        painelInferior.add(btnRemover);
        painelInferior.add(btnFinalizar);

        corpo.add(painelInferior, BorderLayout.SOUTH);
        add(corpo, BorderLayout.CENTER);

        add(Tema.rodape(), BorderLayout.SOUTH);

        comboPagamento = new JComboBox<>(new String[] {
                "DINHEIRO",
                "PIX",
                "CREDITO",
                "DEBITO"
        });

        painelInferior.add(Tema.rotuloClaro("Pagamento:"));
        painelInferior.add(comboPagamento);

        // EVENTOS
        btnRemover.addActionListener(e -> removerItem());

        btnFinalizar.addActionListener(e -> finalizarCompra());
    }

    // CARREGAR FILMES DO BANCO
    private void carregarFilmes() {

        filmes.clear();

        List<Filmes> lista = filmesDAO.listarTodos();

        for (Filmes filme : lista) {

            if (filme.getQuant() > 0) {
                filmes.add(filme);
            }
        }

        painelCards = new PainelCardsFilmes(
                filmes,
                null,
                null,
                filme -> adicionarItem(filme)
        );

        scrollCards.setViewportView(painelCards);

        painelCards.revalidate();
        painelCards.repaint();
    }

    // ADICIONAR FILME AO CARRINHO
    private void adicionarItem(Filmes filme) {

        int quantidade = (int) spQuantidade.getValue();

        // VERIFICAR SE O FILME JA ESTA NO CARRINHO
        ItensCompra itemExistente = null;

        for (ItensCompra item : carrinho) {

            if (item.getFilId() == filme.getID()) {
                itemExistente = item;
                break;
            }
        }

        int quantidadeAtual = 0;

        if (itemExistente != null) {
            quantidadeAtual = itemExistente.getQuant();
        }

        if (quantidadeAtual + quantidade > filme.getQuant()) {

            Mensagem.erro(this, "Estoque insuficiente para essa quantidade.");

            return;
        }

        BigDecimal preco = filme.getVal();

        if (itemExistente != null) {

            int novaQuantidade = quantidadeAtual + quantidade;

            BigDecimal subtotal = preco.multiply(
                    BigDecimal.valueOf(novaQuantidade)
            );

            itemExistente.setQuant(novaQuantidade);
            itemExistente.setPreUn(preco);
            itemExistente.setSub(subtotal);

        } else {

            ItensCompra item = new ItensCompra();

            item.setFilId(filme.getID());
            item.setPreUn(preco);
            item.setQuant(quantidade);

            BigDecimal subtotal = preco.multiply(
                    BigDecimal.valueOf(quantidade)
            );

            item.setSub(subtotal);

            carrinho.add(item);
        }

        atualizarTabela();

        Mensagem.info(this, filme.getNome() + " adicionado ao carrinho!");
    }

    // ATUALIZAR TABELA
    private void atualizarTabela() {

        modelo.setRowCount(0);

        BigDecimal total = BigDecimal.ZERO;

        for (ItensCompra item : carrinho) {

            Filmes filme = buscarFilme(item.getFilId());

            String nome = filme != null
                    ? filme.getNome()
                    : "Filme não encontrado";

            modelo.addRow(new Object[]{
                item.getFilId(),
                nome,
                moeda.format(item.getPreUn()),
                item.getQuant(),
                moeda.format(item.getSub())
                
            });

            total = total.add(item.getSub());
        }

        lblTotal.setText("Total: " + moeda.format(total));
    }

    // BUSCAR FILME
    private Filmes buscarFilme(int id) {

        for (Filmes filme : filmes) {

            if (filme.getID() == id) {
                return filme;
            }
        }

        return null;
    }

    // REMOVER ITEM
    private void removerItem() {

        int linha = tabela.getSelectedRow();

        if (linha < 0) {

            Mensagem.erro(this, "Selecione um item para remover.");

            return;
        }

        carrinho.remove(tabela.convertRowIndexToModel(linha));

        atualizarTabela();
    }

    // FINALIZAR COMPRA
    private void finalizarCompra() {

        if (carrinho.isEmpty()) {

            Mensagem.erro(this, "Adicione pelo menos um filme ao carrinho.");

            return;
        }

        boolean confirmou = Mensagem.confirmar(
                this,
                "Confirmar compra",
                "Deseja realmente finalizar esta compra?"
        );

        if (!confirmou) {
            return;
        }

        boolean sucesso = comprasController.finalizarCompra(
                usuarioID,
                carrinho,
                comboPagamento.getSelectedItem().toString());

        if (sucesso) {

            Mensagem.info(this, "Compra realizada com sucesso!");

            carrinho.clear();

            atualizarTabela();
            carregarFilmes();

        } else {

            Mensagem.erro(this,
                    "Não foi possível finalizar a compra.\n" + "Verifique o estoque e os dados informados.");
        }
    }

    
}



