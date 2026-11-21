
package view;

import dao.UsuariosDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Usuarios;

public class CRUDUsuario extends JFrame {

    private JTable tableUsuarios;
    private DefaultTableModel model;

    private JButton btnNovo, btnEditar, btnExcluir;

    private UsuariosDAO usuarioDAO = new UsuariosDAO();

    public CRUDUsuario() {

        setTitle("Gerenciamento de Usuários");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // MODELO DA TABELA
        model = new DefaultTableModel(
            new String[] {
                "ID",
                "Nome",
                "Telefone",
                "E-mail",
                "Tipo",
                "Situação"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableUsuarios = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(tableUsuarios);
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
        btnEditar.addActionListener(e -> editarUsuario());
        btnExcluir.addActionListener(e -> excluirUsuario());

        carregarUsuarios();

        setVisible(true);
    }

    // CARREGAR USUÁRIOS
    private void carregarUsuarios() {

        model.setRowCount(0);

        List<Usuarios> usuarios = usuarioDAO.listarTodos();

        for (Usuarios u : usuarios) {

            String tipoLegivel = u.getTipo().equals("A")
                    ? "Administrador"
                    : "Usuário";

            String ativoLegivel = u.getAtivo().equals("S")
                    ? "Ativo"
                    : "Inativo";

            model.addRow(new Object[] {
                u.getId(),
                u.getNome(),
                u.getTelefone(),
                u.getEmail(),
                tipoLegivel,
                ativoLegivel
            });
        }
    }

    // ABRIR FORMULÁRIO
    private void abrirFormulario(Usuarios usuario) {

        FormularioUsuario form = new FormularioUsuario(this, usuario);

        form.setVisible(true);
    }

    // EDITAR USUÁRIO
    private void editarUsuario() {

        int linha = tableUsuarios.getSelectedRow();

        if (linha >= 0) {

            int id = (int) model.getValueAt(linha, 0);

            Usuarios usuario = usuarioDAO.buscarPorId(id);

            if (usuario != null) {
                abrirFormulario(usuario);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Usuário não encontrado!"
                );
            }

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Selecione um usuário para editar!"
            );
        }
    }

    // EXCLUIR USUÁRIO
    private void excluirUsuario() {

        int linha = tableUsuarios.getSelectedRow();

        if (linha >= 0) {

            int id = (int) model.getValueAt(linha, 0);

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir este usuário?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                boolean sucesso = usuarioDAO.excluir(id);

                if (sucesso) {

                    carregarUsuarios();

                    JOptionPane.showMessageDialog(
                        this,
                        "Usuário excluído com sucesso!"
                    );

                } else {

                    JOptionPane.showMessageDialog(
                        this,
                        "Erro ao excluir usuário!"
                    );
                }
            }

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Selecione um usuário para excluir!"
            );
        }
    }

    // ATUALIZAR TABELA
    public void atualizarTabela() {

        carregarUsuarios();
    }
}
