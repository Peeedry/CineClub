
package view;

import controller.UsuarioController;
import util.Mensagem;
import util.ResultadoCadastro;

import javax.swing.*;
import java.awt.*;

public class TelaCadastro extends JFrame {

    private JTextField txtNome;
    private JTextField txtTelefone;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnSalvar;

    private final UsuarioController controller =
            new UsuarioController();

    public TelaCadastro() {

        setTitle("CineClub - Cadastro");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        txtNome = new JTextField();
        txtTelefone = new JTextField();
        txtLogin = new JTextField();
        txtSenha = new JPasswordField();

        btnSalvar = new JButton("Cadastrar");

        add(new JLabel("Nome:"));
        add(txtNome);

        add(new JLabel("Telefone:"));
        add(txtTelefone);

        add(new JLabel("E-mail:"));
        add(txtLogin);

        add(new JLabel("Senha:"));
        add(txtSenha);

        add(new JLabel(""));
        add(btnSalvar);

        btnSalvar.addActionListener(e -> cadastrar());
    }

    private void cadastrar() {

        String nome = txtNome.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());

        ResultadoCadastro resultado =
                controller.cadastrar(
                        nome,
                        telefone,
                        email,
                        senha
                );

        switch (resultado) {

            case SUCESSO:
                Mensagem.info(this,
                        "Usuário cadastrado com sucesso!");
                dispose();
                break;

            case JA_EXISTE:
                Mensagem.erro(this,
                        "Este e-mail já está cadastrado.");
                break;

            default:
                Mensagem.erro(this,
                        "Não foi possível cadastrar o usuário.");
                break;
        }
    }
}
