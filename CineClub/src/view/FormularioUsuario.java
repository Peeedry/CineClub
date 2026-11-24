
package view;

import dao.UsuariosDAO;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.swing.*;
import model.Usuarios;
import util.Mensagem;
import util.ResultadoCadastro;
import util.Tema;

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

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        txtNome = Tema.campo();
        cbTipo = Tema.combo(new JComboBox<>(
            new String[]{"Usuario", "Administrador"}
        ));
        cbAtivo = Tema.combo(new JComboBox<>(
            new String[]{"Ativo", "Inativo"}
        ));
        btnSalvar = Tema.botao("Salvar");

        Tema.Formulario painel = new Tema.Formulario();
        painel.adicionar("Nome:", txtNome);

        // SENHA - SOMENTE NO CADASTRO
        if (usuario == null) {
            txtSenha = Tema.campoSenha();
            painel.adicionar("Senha:", txtSenha);
        }

        painel.adicionar("Tipo:", cbTipo);
        painel.adicionar("Ativo:", cbAtivo);
        painel.botoes(btnSalvar);

        add(Tema.cabecalho(usuario == null ? "Novo sócio" : "Editar sócio"),
                BorderLayout.NORTH);

        JPanel centro = Tema.fundo(new BorderLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(20, 28, 24, 28));
        centro.add(painel, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

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

        pack();
        setSize(Math.max(getWidth(), 560), getHeight());
        setLocationRelativeTo(parent);
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

                Mensagem.erro(this, "Nome obrigatorio!");

                return;
            }

            // CADASTRAR USUARIO
            if (usuario == null) {

                String senha =
                    new String(txtSenha.getPassword()).trim();

                if (senha.isEmpty()) {

                    Mensagem.erro(this, "Senha obrigatoria!");

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

                    Mensagem.info(this, "Usuario cadastrado com sucesso!");

                    parent.atualizarTabela();
                    dispose();

                } else if (resultado == ResultadoCadastro.JA_EXISTE) {

                    Mensagem.erro(this, "Este usuario ja existe!");

                } else {

                    Mensagem.erro(this, "Erro ao cadastrar usuario!");
                }

            } else {

                // EDITAR USUARIO
                usuario.setEmail(nome);
                usuario.setTipo(tipo);
                usuario.setAtivo(ativo);

                boolean sucesso = usuarioDAO.gravar(usuario);

                if (sucesso) {

                    Mensagem.info(this, "Usuario atualizado com sucesso!");

                    parent.atualizarTabela();
                    dispose();

                } else {

                    Mensagem.erro(this, "Erro ao atualizar usuario!");
                }
            }

        } catch (Exception ex) {

            ex.printStackTrace();

            Mensagem.erro(this, "Erro ao salvar usuario!");
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