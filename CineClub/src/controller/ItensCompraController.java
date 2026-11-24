
package controller;

import java.math.BigDecimal;
import java.util.List;

import dao.ItensCompraDAO;
import model.ItensCompra;

public class ItensCompraController {

    private final ItensCompraDAO itemDAO = new ItensCompraDAO();

    // CADASTRAR ITEM
    public boolean cadastrar(
            int compraID,
            int filmeID,
            BigDecimal precoUnitario,
            int quantidade) {

        if (compraID <= 0 || filmeID <= 0 ||
            precoUnitario == null ||
            precoUnitario.compareTo(BigDecimal.ZERO) <= 0 ||
            quantidade <= 0) {

            return false;
        }

        BigDecimal subtotal = precoUnitario.multiply(
            BigDecimal.valueOf(quantidade)
        );

        ItensCompra item = new ItensCompra();

        item.setCopId(compraID);
        item.setFilId(filmeID);
        item.setPreUn(precoUnitario);
        item.setQuant(quantidade);
        item.setSub(subtotal);

        return itemDAO.inserir(item);
    }

    // LISTAR ITENS DE UMA COMPRA
    public List<ItensCompra> listarPorCompra(int compraID) {

        return itemDAO.listarPorCompra(compraID);
    }

    // BUSCAR ITEM
    public ItensCompra buscarPorId(int id) {

        return itemDAO.buscarPorId(id);
    }

    // EXCLUIR ITEM
    public boolean excluir(int id) {

        return itemDAO.excluir(id);
    }
}
