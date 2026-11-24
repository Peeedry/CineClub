package view;

import controller.ComprasController;
import model.Compras;
import util.Tema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

public class CRUDCompras extends JFrame {

    private final int usuarioID;

    private final ComprasController controller =
            new ComprasController();

    private JTable tabela;
    private DefaultTableModel modelo;

    public CRUDCompras(int usuarioID) {

        this.usuarioID = usuarioID;

        setTitle("CineClub - Minhas Compras");
        setSize(820, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        criarInterface();
        carregarCompras();
    }

    private void criarInterface() {

        setLayout(new BorderLayout());

        add(Tema.cabecalho("Minhas compras"), BorderLayout.NORTH);

        // TABELA
        modelo = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Data",
                    "Valor Total",
                    "Status"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);

        JPanel corpo = Tema.fundo(new BorderLayout());
        corpo.setBorder(BorderFactory.createEmptyBorder(18, 18, 6, 18));
        corpo.add(Tema.tabela(tabela), BorderLayout.CENTER);

        // BOTÕES
        JPanel painelBotoes = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 12, 0)
        );
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(14, 0, 8, 0));

        JButton btnNovaCompra = Tema.botao("Nova compra");
        JButton btnAtualizar = Tema.botaoSecundario("Atualizar");

        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnNovaCompra);

        corpo.add(painelBotoes, BorderLayout.SOUTH);
        add(corpo, BorderLayout.CENTER);

        add(Tema.rodape(), BorderLayout.SOUTH);

        btnAtualizar.addActionListener(e -> carregarCompras());

        btnNovaCompra.addActionListener(e -> abrirFormulario());
    }

    // CARREGAR HISTÓRICO
    private void carregarCompras() {

        modelo.setRowCount(0);

        List<Compras> compras =
                controller.listarPorUsuario(usuarioID);

        for (Compras compra : compras) {

            modelo.addRow(new Object[]{
                compra.getId(),
                compra.getData(),
                compra.getValor(),
                compra.getStatus()
            });
        }
    }

    // ABRIR FORMULÁRIO
    private void abrirFormulario() {

        FormularioCompra formulario =
                new FormularioCompra(usuarioID);

        formulario.setVisible(true);
    }
}
