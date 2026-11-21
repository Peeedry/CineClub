package view;

import controller.UsuarioController;
import util.Mensagem;
import util.ResultadoCadastro;
import javax.swing.*;
import java.awt.*;


public class TelaCadastro extends JFrame{
      
      private JTextField txtLogin;
      private JPasswordField txtSenha;
      private JButton btnSalvar;

      private final UsuarioController controller = new UsuarioController();

      public void TelaCadastroUsuario() {
           add(new JLabel("Senha"));
           txtSenha = new JPasswordField();
           add(txtSenha);

           btnSalvar.addActionListener(e -> {
            String login = txtLogin.getText();
            String senha = new String(txtSenha.getPassword());
            ResultadoCadastro resultado = controller.cadastrar(login, senha);
               switch (resultado) {
                case SUCESSO:
                     
                    break;
               
                default:
                    break;
               }

           });
      }

      
}