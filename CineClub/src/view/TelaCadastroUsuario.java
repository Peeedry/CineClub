
package view;

import controller.UsuarioController;
import util.ResultadoCadastro;

import javax.swing.*;
import java.awt.*;

public class TelaCadastroUsuario extends JFrame {

    private JTextField txtNome;
    private JTextField txtTelefone;
    private JTextField txtLogin;
    private JPasswordField txtSenha;

    private JButton btnSalvar;

    private final UsuarioController controller =
            new UsuarioController();

    public TelaCadastroUsuario() {

        setTitle("CineClub - Cadastro de Usuário");
        setSize(380, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new GridLayout(5, 2, 5, 5));

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
                controller.cadastrar(nome, telefone, email, senha);

        if (resultado == ResultadoCadastro.SUCESSO) {

            JOptionPane.showMessageDialog(
                    this,
                    "Usuário cadastrado com sucesso!"
            );

            dispose();

        } else if (resultado == ResultadoCadastro.JA_EXISTE) {

            JOptionPane.showMessageDialog(
                    this,
                    "Este e-mail já está cadastrado."
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao cadastrar usuário."
            );
        }
    }
}

