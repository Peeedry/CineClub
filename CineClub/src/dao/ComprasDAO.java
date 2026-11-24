package dao;

import model.Compras;
import model.ItensCompra;
import java.math.BigDecimal;
import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import model.Compras;
import model.ItensCompra;

public class ComprasDAO {

    // CADASTRAR COMPRA
    public boolean inserir(Compras compra) {

        String sql = "INSERT INTO Compras "
                + "(usuarioID, dataCompra, valorTotal, status, formaPagamento) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getInstance().conectar();) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, compra.getUsID());
                stmt.setDate(2, Date.valueOf(compra.getData()));
                stmt.setBigDecimal(3, compra.getValor());
                stmt.setString(4, compra.getStatus());
                stmt.setString(5, compra.getFormaPagamento());

                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {

            System.out.println("Erro ao inserir compra: " + e.getMessage());
            return false;
        }
    }

    // LISTAR TODAS AS COMPRAS
    public List<Compras> listarTodos() {

        List<Compras> lista = new ArrayList<>();

        String sql = "SELECT * FROM Compras ORDER BY id DESC";

        try (Connection conn = Conexao.getInstance().conectar()) {

            if (conn == null) {
                return lista;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Compras compra = new Compras();

                    compra.setId(rs.getInt("id"));
                    compra.setUsID(rs.getInt("usuarioID"));
                    compra.setData(
                        rs.getDate("dataCompra").toLocalDate()
                    );
                    compra.setValor(rs.getBigDecimal("valorTotal"));
                    compra.setStatus(rs.getString("status"));
                    compra.setFormaPagamento(rs.getString("formaPagamento"));

                    lista.add(compra);
                }
            }

        } catch (SQLException e) {

            System.out.println("Erro ao listar compras: " + e.getMessage());
        }

        return lista;
    }

    // BUSCAR COMPRA PELO ID
    public Compras buscarPorId(int id) {

        String sql = "SELECT * FROM Compras WHERE id = ?";

        try (Connection conn = Conexao.getInstance().conectar()) {

            if (conn == null) {
                return null;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {

                    if (rs.next()) {

                        Compras compra = new Compras();

                        compra.setId(rs.getInt("id"));
                        compra.setUsID(rs.getInt("usuarioID"));
                        compra.setData(
                            rs.getDate("dataCompra").toLocalDate()
                        );
                        compra.setValor(rs.getBigDecimal("valorTotal"));
                        compra.setStatus(rs.getString("status"));
                        compra.setFormaPagamento(
                                rs.getString("formaPagamento"));

                        return compra;
                    }
                }
            }

        } catch (SQLException e) {

            System.out.println("Erro ao buscar compra: " + e.getMessage());
        }

        return null;
    }

    // LISTAR COMPRAS DE UM USUARIO
    public List<Compras> listarPorUsuario(int usuarioID) {

        List<Compras> lista = new ArrayList<>();

        String sql = "SELECT * FROM Compras "
                   + "WHERE usuarioID = ? ORDER BY id DESC";

        try (Connection conn = Conexao.getInstance().conectar()) {

            if (conn == null) {
                return lista;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, usuarioID);

                try (ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {

                        Compras compra = new Compras();

                        compra.setId(rs.getInt("id"));
                        compra.setUsID(rs.getInt("usuarioID"));
                        compra.setData(
                            rs.getDate("dataCompra").toLocalDate()
                        );
                        compra.setValor(rs.getBigDecimal("valorTotal"));
                        compra.setStatus(rs.getString("status"));
                        compra.setFormaPagamento(
                                rs.getString("formaPagamento"));

                        lista.add(compra);
                    }
                }
            }

        } catch (SQLException e) {

            System.out.println("Erro ao listar compras do usuário: "
                    + e.getMessage());
        }

        return lista;
    }

    // ATUALIZAR STATUS
    public boolean atualizarStatus(int id, String status) {

        String sql = "UPDATE Compras SET status = ? WHERE id = ?";

        try (Connection conn = Conexao.getInstance().conectar()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, status);
                stmt.setInt(2, id);

                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {

            System.out.println("Erro ao atualizar compra: " + e.getMessage());
            return false;
        }
    }

    public boolean finalizarCompra(int usuarioID, List<ItensCompra> itens, String formaPagamento) {

    if (usuarioID <= 0 || itens == null || itens.isEmpty()) {
        return false;
    }

    try (Connection conn = Conexao.getInstance().conectar()) {

        if (conn == null) {
            return false;
        }

        conn.setAutoCommit(false);

        try {
            BigDecimal valorTotal = BigDecimal.ZERO;

            // 1. Verificar estoque e calcular valores
            for (ItensCompra item : itens) {

                if (item == null || item.getFilId() <= 0
                        || item.getQuant() <= 0) {
                    throw new SQLException("Dados do item inválidos.");
                }

                String sql = "SELECT valor, quantidade FROM Filmes "
                           + "WHERE id = ? FOR UPDATE";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setInt(1, item.getFilId());

                    try (ResultSet rs = stmt.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException("Filme não encontrado.");
                        }

                        BigDecimal preco = rs.getBigDecimal("valor");
                        int estoque = rs.getInt("quantidade");

                        if (estoque < item.getQuant()) {
                            throw new SQLException(
                                "Estoque insuficiente para o filme ID "
                                + item.getFilId()
                            );
                        }

                        BigDecimal subtotal = preco.multiply(
                            BigDecimal.valueOf(item.getQuant())
                        );

                        item.setPreUn(preco);
                        item.setSub(subtotal);

                        valorTotal = valorTotal.add(subtotal);
                    }
                }
            }

            // 2. Registrar a compra
            String sqlCompra = "INSERT INTO Compras "
        + "(usuarioID, dataCompra, valorTotal, status, formaPagamento) "
        + "VALUES (?, ?, ?, ?, ?)";

            int compraID;

            try (PreparedStatement stmt = conn.prepareStatement(
                    sqlCompra, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setInt(1, usuarioID);
                stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                stmt.setBigDecimal(3, valorTotal);
                stmt.setString(4, "Concluída");
                stmt.setString(5, formaPagamento);
                

                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {

                    if (!rs.next()) {
                        throw new SQLException(
                            "Não foi possível obter o ID da compra."
                        );
                    }

                    compraID = rs.getInt(1);
                }
            }

            // 3. Registrar itens e atualizar estoque
            String sqlItem = "INSERT INTO ItensCompra "
                    + "(compraID, filmeID, precoUnitario, quantidade, subtotal) "
                    + "VALUES (?, ?, ?, ?, ?)";

            String sqlEstoque = "UPDATE Filmes "
                    + "SET quantidade = quantidade - ? "
                    + "WHERE id = ? AND quantidade >= ?";

            for (ItensCompra item : itens) {

                try (PreparedStatement stmt = conn.prepareStatement(sqlItem)) {

                    stmt.setInt(1, compraID);
                    stmt.setInt(2, item.getFilId());
                    stmt.setBigDecimal(3, item.getPreUn());
                    stmt.setInt(4, item.getQuant());
                    stmt.setBigDecimal(5, item.getSub());

                    stmt.executeUpdate();
                }

                try (PreparedStatement stmt = conn.prepareStatement(sqlEstoque)) {

                    stmt.setInt(1, item.getQuant());
                    stmt.setInt(2, item.getFilId());
                    stmt.setInt(3, item.getQuant());

                    if (stmt.executeUpdate() == 0) {
                        throw new SQLException("Falha ao atualizar estoque.");
                    }
                }
            }

            // 4. Confirmar a operação
            conn.commit();

            return true;

        } catch (Exception e) {

            conn.rollback();

            System.out.println("Erro ao finalizar compra: " + e.getMessage());

            return false;
        }

    } catch (SQLException e) {

        System.out.println("Erro no banco: " + e.getMessage());

        return false;
    }

}

public boolean finalizarCompra(
        int usuarioID,
        List<ItensCompra> itens) {

    return finalizarCompra(usuarioID, itens, "DINHEIRO");

}

}

