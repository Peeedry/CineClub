package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Usuarios;
import util.ResultadoCadastro;

public class UsuariosDAO {

    public boolean autenticar(Usuarios usuario) {
        String sql = "SELECT login, senha FROM usuarios " +
                "WHERE login = ? AND senha = ?";

        try (Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getSenha());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Erro ao autenticar: " + e.getMessage());
            return false;
        }
    }

    public boolean existeUsuario(String nomeUsuario) {
        String sql = "SELECT 1 FROM usuarios WHERE login = ?";

        try (Connection minhaConexao = Conexao.conectar();
                PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {

            stmt.setString(1, nomeUsuario);

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
                "(login, senha, acesso, ativo) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection minhaConexao = Conexao.conectar();
                PreparedStatement stmt = minhaConexao.prepareStatement(sql)) {

            stmt.setString(1, dadosInformados.getEmail());
            stmt.setString(2, dadosInformados.getSenha());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(4, "U");
            stmt.setString(5, "T");
            stmt.setString(6, "");

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

        try (Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Usuarios u = new Usuarios();

                u.setId(rs.getInt("id"));
                u.setEmail(rs.getString("email"));
                u.setNome(rs.getString("nome"));
                u.setAtivo(rs.getString("ativo"));
                u.setSenha(rs.getString("senha"));
                u.setTipo(rs.getString("tipo"));

                lista.add(u);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean excluir(int id) {

        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int r = stmt.executeUpdate();
            return r > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuarios buscarPorId(int id) {
         String sql = "SELECT * FROM usuarios WHERE id = ?";

         try (Connection conn = Conexao.conectar();
              PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id);

                ResultSet rs = stmt.executeQuery();

                if(rs.next()) {
                    Usuarios u = new Usuarios();

                    u.setId(rs.getInt("id"));
                    u.setEmail(rs.getString("email"));
                    u.setNome(rs.getString("nome"));
                    u.setAtivo(rs.getString("ativo"));
                    u.setSenha(rs.getString("senha"));
                    u.setTipo(rs.getString("tipo"));

                    return u;
                }

              } catch (Exception e) {
                e.printStackTrace();
              }

              return null;
    }

    public  boolean gravar (Usuarios u) {
          String sql = "UPDATE usuarios SET login = ?, tipo = ?, ativo = ?, email = ? WHERE id = ?";

          try (Connection conn = Conexao.conectar();
               PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, u.getEmail());
                stmt.setString(2, u.getSenha());
                stmt.setString(3, u.getAtivo());
                stmt.setString(4, u.getNome());
                stmt.setInt(5, u.getId());

                stmt.executeUpdate();
                return true;
               
            } catch (SQLException e) {
                System.out.println("Erro ao atualizar: " + e.getMessage());
                return false;
            }

        }

        public void atualizar() {
            listarTodos();
        }
}