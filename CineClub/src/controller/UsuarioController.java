
package controller;

import dao.UsuariosDAO;
import model.Usuarios;
import util.Funcoes;
import util.ResultadoCadastro;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class UsuarioController {

    private final UsuariosDAO usuarioDAO = new UsuariosDAO();

    // CADASTRAR USUARIO
    
    public ResultadoCadastro cadastrar(
            String nome,
            String telefone,
            String email,
            String senha) {

        if (nome == null || nome.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || senha == null || senha.isEmpty()) {

            return ResultadoCadastro.ERRO_BANCO;
        }

        Usuarios usuario = new Usuarios();

        usuario.setNome(nome.trim());
        usuario.setTelefone(telefone.trim());
        usuario.setEmail(email.trim());
        usuario.setSenha(Funcoes.gerarHashSHA256(senha));
        usuario.setTipo("U");
        usuario.setAtivo("S");

        return usuarioDAO.inserir(usuario);
    }


    // LOGIN
    public Usuarios login(String login, String senha) {

        if (login == null || login.trim().isEmpty()
                || senha == null || senha.isEmpty()) {
            return null;
        }

        String senhaHash = Funcoes.gerarHashSHA256(senha);

        return usuarioDAO.autenticar(login, senhaHash);
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
