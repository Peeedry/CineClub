
package view;

import dao.UsuariosDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Usuarios;
import util.Mensagem;
import util.Tema;

public class CRUDUsuario extends JFrame {

    private JTable tableUsuarios;
    private DefaultTableModel model;

    private JButton btnNovo, btnEditar, btnExcluir;

    private UsuariosDAO usuarioDAO = new UsuariosDAO();

    public CRUDUsuario() {

        setTitle("Gerenciamento de Usuários");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(Tema.cabecalho("Gerenciamento de usuários"), BorderLayout.NORTH);

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

        JScrollPane scrollPane = Tema.tabela(tableUsuarios);

        JPanel corpo = Tema.fundo(new BorderLayout());
        corpo.setBorder(BorderFactory.createEmptyBorder(18, 18, 6, 18));
        corpo.add(scrollPane, BorderLayout.CENTER);

        // PAINEL DE BOTÕES
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(14, 0, 8, 0));

        btnNovo = Tema.botao("Novo");
        btnEditar = Tema.botaoSecundario("Editar");
        btnExcluir = Tema.botaoPerigo("Excluir");

        painelBotoes.add(btnNovo);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        corpo.add(painelBotoes, BorderLayout.SOUTH);
        add(corpo, BorderLayout.CENTER);

        add(Tema.rodape(), BorderLayout.SOUTH);

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
                Mensagem.erro(this, "Usuário não encontrado!");
            }

        } else {

            Mensagem.erro(this, "Selecione um usuário para editar!");
        }
    }

    // EXCLUIR USUÁRIO
    private void excluirUsuario() {

        int linha = tableUsuarios.getSelectedRow();

        if (linha >= 0) {

            int id = (int) model.getValueAt(linha, 0);

            boolean confirmou = Mensagem.confirmar(
                this,
                "Confirmar exclusão",
                "Deseja realmente excluir este usuário?"
            );

            if (confirmou) {

                boolean sucesso = usuarioDAO.excluir(id);

                if (sucesso) {

                    carregarUsuarios();

                    Mensagem.info(this, "Usuário excluído com sucesso!");

                } else {

                    Mensagem.erro(this, "Erro ao excluir usuário!");
                }
            }

        } else {

            Mensagem.erro(this, "Selecione um usuário para excluir!");
        }
    }

    // ATUALIZAR TABELA
    public void atualizarTabela() {

        carregarUsuarios();
    }
}
