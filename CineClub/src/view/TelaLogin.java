package view;

import controller.UsuarioController;
import model.Usuarios;
import util.Mensagem;
import util.Tema;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar, btnCadastrar;

    private final UsuarioController controller = new UsuarioController();

    public TelaLogin() {
        setTitle("CineClub - Login");
        setSize(520, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(Tema.cabecalho("Bem-vindo"), BorderLayout.NORTH);

        // CARTÃO DE SÓCIO
        txtLogin = Tema.campo();
        txtSenha = Tema.campoSenha();

        btnEntrar = Tema.botao("Entrar");
        btnCadastrar = Tema.botaoSecundario("Cadastrar");

        Tema.Formulario cartao = new Tema.Formulario();
        cartao.titulo("CARTÃO DE SÓCIO");
        cartao.adicionar("Usuário:", txtLogin);
        cartao.adicionar("Senha:", txtSenha);
        cartao.botoes(btnCadastrar, btnEntrar);

        JPanel centro = Tema.fundo(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(20, 40, 20, 40);
        centro.add(cartao, c);
        add(centro, BorderLayout.CENTER);

        add(Tema.rodape(), BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnEntrar);

        btnEntrar.addActionListener(e -> {
            String login = txtLogin.getText().trim();
            String senha = new String(txtSenha.getPassword());

            Usuarios usuario = controller.login(login, senha);

            if (usuario != null) {
                Mensagem.info(this, "Login realizado!");

                dispose();
                new TelaMenu(usuario).setVisible(true);

            } else {
                Mensagem.erro(this, "Usuário ou senha incorretos.");
            }
        });

        btnCadastrar.addActionListener(e -> {
            new TelaCadastroUsuario().setVisible(true);
        });
    }
}
