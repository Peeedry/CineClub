package controller;

import dao.UsuariosDAO;
import model.Usuarios;
import util.ResultadoCadastro;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class UsuarioController {

    private final UsuariosDAO usuarioDAO = new UsuariosDAO();

    // CADASTRAR USUARIO
    public ResultadoCadastro cadastrar(String login, String senha) {

        try {

            if (login == null || login.trim().isEmpty()
                    || senha == null || senha.trim().isEmpty()) {
                return ResultadoCadastro.ERRO_BANCO;
            }

            Usuarios usuario = new Usuarios();

            usuario.setEmail(login.trim());
            usuario.setSenha(gerarHashSHA256(senha));
            usuario.setTipo("U");
            usuario.setAtivo("S");

            return usuarioDAO.inserir(usuario);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultadoCadastro.ERRO_BANCO;
        }
    }

    // LOGIN
    public boolean login(String login, String senha) {

        try {

            if (login == null || login.trim().isEmpty()
                    || senha == null || senha.isEmpty()) {
                return false;
            }

            Usuarios usuario = new Usuarios();

            usuario.setEmail(login.trim());
            usuario.setSenha(gerarHashSHA256(senha));

            return usuarioDAO.autenticar(usuario);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
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