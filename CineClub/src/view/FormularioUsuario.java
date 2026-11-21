
package view;

import dao.UsuariosDAO;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.swing.*;
import model.Usuarios;
import util.ResultadoCadastro;

public class FormularioUsuario extends JDialog {

    private JTextField txtNome;
    private JPasswordField txtSenha;
    private JComboBox<String> cbTipo, cbAtivo;
    private JButton btnSalvar;

    private final UsuariosDAO usuarioDAO = new UsuariosDAO();

    private Usuarios usuario;
    private CRUDUsuario parent;

    public FormularioUsuario(CRUDUsuario parent, Usuarios usuario) {

        super(parent, true);

        this.parent = parent;
        this.usuario = usuario;

        setTitle(usuario == null ? "Novo Usuario" : "Editar Usuario");

        setSize(350, usuario == null ? 280 : 230);

        setLayout(new GridLayout(usuario == null ? 5 : 4, 2, 10, 10));

        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // NOME
        add(new JLabel("Nome:"));

        txtNome = new JTextField();
        add(txtNome);

        // SENHA - SOMENTE NO CADASTRO
        if (usuario == null) {

            add(new JLabel("Senha:"));

            txtSenha = new JPasswordField();
            add(txtSenha);
        }

        // TIPO
        add(new JLabel("Tipo:"));

        cbTipo = new JComboBox<>(
            new String[]{"Usuario", "Administrador"}
        );

        add(cbTipo);

        // ATIVO
        add(new JLabel("Ativo:"));

        cbAtivo = new JComboBox<>(
            new String[]{"Ativo", "Inativo"}
        );

        add(cbAtivo);

        // BOTÃO SALVAR
        btnSalvar = new JButton("Salvar");

        add(new JLabel());
        add(btnSalvar);

        // PREENCHER CAMPOS NA EDIÇÃO
        if (usuario != null) {

            txtNome.setText(usuario.getEmail());

            cbTipo.setSelectedItem(
                "A".equals(usuario.getTipo())
                    ? "Administrador"
                    : "Usuario"
            );

            cbAtivo.setSelectedItem(
                "T".equals(usuario.getAtivo())
                    ? "Ativo"
                    : "Inativo"
            );
        }

        btnSalvar.addActionListener(e -> salvarUsuario());
    }

    // SALVAR USUARIO
    private void salvarUsuario() {

        try {

            String nome = txtNome.getText().trim();

            String tipoSelecionado =
                (String) cbTipo.getSelectedItem();

            String ativoSelecionado =
                (String) cbAtivo.getSelectedItem();

            String tipo = "Administrador".equals(tipoSelecionado)
                ? "A" : "U";

            String ativo = "Ativo".equals(ativoSelecionado)
                ? "T" : "F";

            // VALIDAR NOME
            if (nome.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Nome obrigatorio!"
                );

                return;
            }

            // CADASTRAR USUARIO
            if (usuario == null) {

                String senha =
                    new String(txtSenha.getPassword()).trim();

                if (senha.isEmpty()) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Senha obrigatoria!"
                    );

                    return;
                }

                String senhaHash = gerarHashSHA256(senha);

                Usuarios u = new Usuarios();

                u.setEmail(nome);
                u.setSenha(senhaHash);
                u.setTipo(tipo);
                u.setAtivo(ativo);

                ResultadoCadastro resultado = usuarioDAO.inserir(u);

                if (resultado == ResultadoCadastro.SUCESSO) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Usuario cadastrado com sucesso!"
                    );

                    parent.atualizarTabela();
                    dispose();

                } else if (resultado == ResultadoCadastro.JA_EXISTE) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Este usuario ja existe!"
                    );

                } else {

                    JOptionPane.showMessageDialog(
                        this,
                        "Erro ao cadastrar usuario!"
                    );
                }

            } else {

                // EDITAR USUARIO
                usuario.setEmail(nome);
                usuario.setTipo(tipo);
                usuario.setAtivo(ativo);

                boolean sucesso = usuarioDAO.gravar(usuario);

                if (sucesso) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Usuario atualizado com sucesso!"
                    );

                    parent.atualizarTabela();
                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                        this,
                        "Erro ao atualizar usuario!"
                    );
                }
            }

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                this,
                "Erro ao salvar usuario!"
            );
        }
    }

    // GERAR HASH SHA-256
    private String gerarHashSHA256(String senha) throws Exception {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] hash = digest.digest(
            senha.getBytes(StandardCharsets.UTF_8)
        );

        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {

            String hex = Integer.toHexString(0xff & b);

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }
}