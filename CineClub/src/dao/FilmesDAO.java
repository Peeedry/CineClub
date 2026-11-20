package dao;

import model.Filmes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilmesDAO {

    public boolean inserir(Filmes filme) {

        String sql = "INSERT INTO Filmes "
                + "(nome, genero, duracao, sinopse, formato, valor, quantidade, poster) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, filme.getNome());
            stmt.setString(2, filme.getGen());
            stmt.setInt(3, filme.getDurc());
            stmt.setBigDecimal(4, filme.getVal());
            stmt.setInt(5, filme.getQuant());
            stmt.setString(6, filme.getForm());
            stmt.setString(7, filme.getSinp());
            stmt.setString(8, filme.getPoster());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir filme: " + e.getMessage());
            return false;
        }
    }

    // LISTAR TODOS OS FILMES
    public List<Filmes> listarTodos() {

        List<Filmes> lista = new ArrayList<>();

        String sql = "SELECT * FROM Filmes ORDER BY id";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Filmes filme = new Filmes();

                filme.setID(rs.getInt("id"));
                filme.setNome(rs.getString("nome"));
                filme.setGen(rs.getString("genero"));
                filme.setDurc(rs.getInt("duracao"));
                filme.setVal(rs.getBigDecimal("valor"));
                filme.setQuant(rs.getInt("quantidade"));
                filme.setAno(rs.getInt("ano_lancamento"));
                filme.setPoster(rs.getString("poster"));

                lista.add(filme);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar filmes: " + e.getMessage());
        }

        return lista;
    }

    // BUSCAR FILME PELO ID
    public Filmes buscarPorId(int id) {

        String sql = "SELECT * FROM Filmes WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Filmes filme = new Filmes();

                    filme.setID(rs.getInt("id"));
                    filme.setNome(rs.getString("nome"));
                    filme.setGen(rs.getString("genero"));
                    filme.setDurc(rs.getInt("duracao"));
                    filme.setValDia(rs.getBigDecimal("valor_diaria"));
                    filme.setQuant(rs.getInt("quantidade"));
                    filme.setClasf(rs.getString("classificacao"));
                    filme.setAno(rs.getInt("ano_lancamento"));
                    filme.setPoster(rs.getString("poster"));

                    return filme;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar filme: " + e.getMessage());
        }

        return null;
    }

    // EXCLUIR FILME
    public boolean excluir(int id) {

        String sql = "DELETE FROM Filmes WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int resultado = stmt.executeUpdate();

            return resultado > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir filme: " + e.getMessage());
            return false;
        }
    }

    // ATUALIZAR FILME
    public boolean gravar(Filmes filme) {

        String sql = "UPDATE Filmes SET "
                + "nome = ?, genero = ?, duracao = ?, "
                + "valor_diaria = ?, quantidade = ?, "
                + "classificacao = ?, ano_lancamento = ?, poster = ? "
                + "WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, filme.getNome());
            stmt.setString(2, filme.getGen());
            stmt.setInt(3, filme.getDurc());
            stmt.setBigDecimal(4, filme.getValDia());
            stmt.setInt(5, filme.getQuant());
            stmt.setString(6, filme.getClasf());
            stmt.setInt(7, filme.getAno());
            stmt.setString(8, filme.getPoster());
            stmt.setInt(9, filme.getID());

            int resultado = stmt.executeUpdate();

            return resultado > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar filme: " + e.getMessage());
            return false;
        }
    }

}
