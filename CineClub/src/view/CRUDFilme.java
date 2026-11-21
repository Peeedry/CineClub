
package view;

import dao.FilmesDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Filmes;

public class CRUDFilme extends JFrame {

    private JTable tableFilmes;
    private DefaultTableModel model;

    private JButton btnNovo, btnEditar, btnExcluir;

    private FilmesDAO filmeDAO = new FilmesDAO();

    public CRUDFilme() {

        setTitle("Gerenciamento de Filmes");
        setSize(1000, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // TABELA
        model = new DefaultTableModel(
            new String[] {
                "ID",
                "Nome",
                "Gênero",
                "Duração",
                "Formato",
                "Valor",
                "Quantidade"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableFilmes = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(tableFilmes);
        add(scrollPane, BorderLayout.CENTER);

        // PAINEL DE BOTÕES
        JPanel painelBotoes = new JPanel();

        btnNovo = new JButton("Novo");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");

        painelBotoes.add(btnNovo);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        add(painelBotoes, BorderLayout.SOUTH);

        // EVENTOS
        btnNovo.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editarFilme());
        btnExcluir.addActionListener(e -> excluirFilme());

        carregarFilmes();

        setVisible(true);
    }

    // CARREGAR FILMES NA TABELA
    private void carregarFilmes() {

        model.setRowCount(0);

        List<Filmes> filmes = filmeDAO.listarTodos();

        for (Filmes f : filmes) {

            model.addRow(new Object[] {
                f.getID(),
                f.getNome(),
                f.getGen(),
                f.getDurc(),
                f.getForm(),
                f.getVal(),
                f.getQuant()
            });
        }
    }

    // ABRIR FORMULÁRIO
    private void abrirFormulario(Filmes filme) {

        FormularioFilme form = new FormularioFilme(this, filme);

        form.setVisible(true);
    }

    // EDITAR FILME
    private void editarFilme() {

        int linha = tableFilmes.getSelectedRow();

        if (linha >= 0) {

            int id = (int) model.getValueAt(linha, 0);

            Filmes filme = filmeDAO.buscarPorId(id);

            if (filme != null) {

                abrirFormulario(filme);

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Filme não encontrado!"
                );
            }

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Selecione um filme para editar."
            );
        }
    }

    // EXCLUIR FILME
    private void excluirFilme() {

        int linha = tableFilmes.getSelectedRow();

        if (linha >= 0) {

            int id = (int) model.getValueAt(linha, 0);

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir este filme?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                boolean sucesso = filmeDAO.excluir(id);

                if (sucesso) {

                    carregarFilmes();

                    JOptionPane.showMessageDialog(
                        this,
                        "Filme excluído com sucesso!"
                    );

                } else {

                    JOptionPane.showMessageDialog(
                        this,
                        "Erro ao excluir filme."
                    );
                }
            }

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Selecione um filme para excluir."
            );
        }
    }

    // ATUALIZAR TABELA APÓS CADASTRO OU EDIÇÃO
    public void atualizarTabela() {

        carregarFilmes();
    }
}
