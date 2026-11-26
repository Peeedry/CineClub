
package dao;

import model.ItensCompra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItensCompraDAO {

    // CADASTRAR ITEM DA COMPRA
    public boolean inserir(ItensCompra item) {

        String sql = "INSERT INTO ItensCompra "
                + "(compraID, filmeID, precoUnitario, quantidade, subtotal) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getInstance().conectar()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, item.getCopId());
                stmt.setInt(2, item.getFilId());
                stmt.setBigDecimal(3, item.getPreUn());
                stmt.setInt(4, item.getQuant());
                stmt.setBigDecimal(5, item.getSub());

                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {

            System.out.println("Erro ao inserir item: " + e.getMessage());
            return false;
        }
    }

    // LISTAR ITENS DE UMA COMPRA
    public List<ItensCompra> listarPorCompra(int compraID) {

        List<ItensCompra> lista = new ArrayList<>();

        String sql = "SELECT * FROM ItensCompra WHERE compraID = ? ORDER BY id";

        try (Connection conn = Conexao.getInstance().conectar()) {

            if (conn == null) {
                return lista;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, compraID);

                try (ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {

                        ItensCompra item = new ItensCompra();

                        item.setId(rs.getInt("id"));
                        item.setCopId(rs.getInt("compraID"));
                        item.setFilId(rs.getInt("filmeID"));
                        item.setPreUn(rs.getBigDecimal("precoUnitario"));
                        item.setQuant(rs.getInt("quantidade"));
                        item.setSub(rs.getBigDecimal("subtotal"));

                        lista.add(item);
                    }
                }
            }

        } catch (SQLException e) {

            System.out.println("Erro ao listar itens: " + e.getMessage());
        }

        return lista;
    }

    // BUSCAR ITEM PELO ID
    public ItensCompra buscarPorId(int id) {

        String sql = "SELECT * FROM ItensCompra WHERE id = ?";

        try (Connection conn = Conexao.getInstance().conectar()) {

            if (conn == null) {
                return null;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {

                    if (rs.next()) {

                        ItensCompra item = new ItensCompra();

                        item.setId(rs.getInt("id"));
                        item.setCopId(rs.getInt("compraID"));
                        item.setFilId(rs.getInt("filmeID"));
                        item.setPreUn(rs.getBigDecimal("precoUnitario"));
                        item.setQuant(rs.getInt("quantidade"));
                        item.setSub(rs.getBigDecimal("subtotal"));

                        return item;
                    }
                }
            }

        } catch (SQLException e) {

            System.out.println("Erro ao buscar item: " + e.getMessage());
        }

        return null;
    }

    // EXCLUIR ITEM
    public boolean excluir(int id) {

        String sql = "DELETE FROM ItensCompra WHERE id = ?";

        try (Connection conn = Conexao.getInstance().conectar()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id);

                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {

            System.out.println("Erro ao excluir item: " + e.getMessage());
            return false;
        }
    }
}
