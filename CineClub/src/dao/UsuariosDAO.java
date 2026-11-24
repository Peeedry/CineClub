
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Usuarios;
import util.ResultadoCadastro;

public class UsuariosDAO {

    public Usuarios autenticar(String email, String senhaHash) {

        String sql = "SELECT * FROM Usuarios "
                + "WHERE email = ? AND senha = ? AND ativo = 'S'";

        try (Connection conn = Conexao.getInstance().conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senhaHash);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Usuarios usuario = new Usuarios();

                    usuario.setId(rs.getInt("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setTelefone(rs.getString("telefone"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setTipo(rs.getString("tipo"));
                    usuario.setAtivo(rs.getString("ativo"));

                    return usuario;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao autenticar usuário: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean existeUsuario(String email) {

        String sql = "SELECT 1 FROM usuarios WHERE email = ?";

        try (Connection conn = Conexao.getInstance().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Erro ao verificar existência de usuário: " + e.getMessage());
            return false;
        }
    }

    public ResultadoCadastro inserir(Usuarios dadosInformados) {

        if (existeUsuario(dadosInformados.getEmail())) {
            return ResultadoCadastro.JA_EXISTE;
        }

        String sql = "INSERT INTO usuarios " +
                     "(nome, telefone, email, senha, tipo, ativo) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getInstance().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dadosInformados.getNome());
            stmt.setString(2, dadosInformados.getTelefone());
            stmt.setString(3, dadosInformados.getEmail());
            stmt.setString(4, dadosInformados.getSenha());
            stmt.setString(5, dadosInformados.getTipo() == null ? "U" : dadosInformados.getTipo());
            stmt.setString(6, dadosInformados.getAtivo() == null ? "S" : dadosInformados.getAtivo());

            stmt.executeUpdate();

            return ResultadoCadastro.SUCESSO;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir usuario: " + e.getMessage());
            return ResultadoCadastro.ERRO_BANCO;
        }
    }

    public List<Usuarios> listarTodos() {

        List<Usuarios> lista = new ArrayList<>();

        String sql = "SELECT * FROM usuarios ORDER BY id";

        try (Connection conn = Conexao.getInstance().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Usuarios u = new Usuarios();

                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setTelefone(rs.getString("telefone"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                u.setTipo(rs.getString("tipo"));
                u.setAtivo(rs.getString("ativo"));

                lista.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean excluir(int id) {

        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = Conexao.getInstance().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuarios buscarPorId(int id) {

        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection conn = Conexao.getInstance().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Usuarios u = new Usuarios();

                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setTelefone(rs.getString("telefone"));
                    u.setEmail(rs.getString("email"));
                    u.setSenha(rs.getString("senha"));
                    u.setTipo(rs.getString("tipo"));
                    u.setAtivo(rs.getString("ativo"));

                    return u;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean gravar(Usuarios u) {

        String sql = "UPDATE usuarios SET nome = ?, telefone = ?, " +
                     "email = ?, senha = ?, tipo = ?, ativo = ? WHERE id = ?";

        try (Connection conn = Conexao.getInstance().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getTelefone());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getSenha());
            stmt.setString(5, u.getTipo());
            stmt.setString(6, u.getAtivo());
            stmt.setInt(7, u.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar: " + e.getMessage());
            return false;
        }
    }

    public void atualizar() {
        listarTodos();
    }
}
