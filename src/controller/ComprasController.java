
package controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import dao.ComprasDAO;
import model.Compras;
import model.ItensCompra;

public class ComprasController {

    private final ComprasDAO cDAO = new ComprasDAO();

    // INTERFACE STRATEGY

    private interface PagamentoStrategy {
        String confirmar();

        String getFormaPagamento();
    }

    // PAGAMENTO EM DINHEIRO

    private class PagamentoDinheiro implements PagamentoStrategy {
          
        public String confirmar() {
            return "Pagamento via PIX selecionado.";
        }

        public String getFormaPagamento() {
            return "DINHEIRO";
        }
    }

    // PAGAMENTO PIX

    private class PagamentoPix implements PagamentoStrategy {

        public String confirmar() {
            return "Pagamento via PIX selecionado.";
        }

        public String getFormaPagamento() {
            return "PIX";
        }
    }

    // PAGAMENTO CARTÃO DE CRÉDITO

    private class PagamentoCredito implements PagamentoStrategy {

        public String confirmar() {
            return "Pagamento via PIX selecionado.";
        }

        public String getFormaPagamento() {
            return "CREDITO";
        }
    }

    // PAGAMENTO CARTÃO DE DÉBITO

    private class PagamentoDebito implements PagamentoStrategy {

        public String confirmar() {
            return "Pagamento via PIX selecionado.";
        }

        public String getFormaPagamento() {
            return "DEBITO";
        }
    }

    // SELECIONAR ESTRATÉGIA

    private PagamentoStrategy selecionarPagamento(String forma) {

        if (forma == null) {
            return null;
        }

        switch (forma.trim().toUpperCase()) {

            case "DINHEIRO":
                return new PagamentoDinheiro();

            case "PIX":
                return new PagamentoPix();

            case "CREDITO":
                return new PagamentoCredito();

            case "DEBITO":
                return new PagamentoDebito();

            default:
                return null;
        }
    }

    // CADASTRAR COMPRA

    public boolean cadastrar(
            int usuarioID,
            LocalDate dataCompra,
            BigDecimal valorTotal,
            String status) {

        Compras compra = new Compras();

        compra.setUsID(usuarioID);
        compra.setData(dataCompra);
        compra.setValor(valorTotal);
        compra.setStatus(status);
        compra.setFormaPagamento("DINHEIRO");

        return cDAO.inserir(compra);
    }

    // LISTAR TODAS AS COMPRAS

    public List<Compras> listarTodos() {
        return cDAO.listarTodos();
    }

    // BUSCAR COMPRA PELO ID

    public Compras buscarPorId(int id) {
        return cDAO.buscarPorId(id);
    }

    // LISTAR COMPRAS DE UM USUARIO

    public List<Compras> listarPorUsuario(int usuarioID) {
        return cDAO.listarPorUsuario(usuarioID);
    }

    // ATUALIZAR STATUS

    public boolean atualizarStatus(int id, String status) {
        return cDAO.atualizarStatus(id, status);
    }

    // FINALIZAR COMPRA COM STRATEGY

    public boolean finalizarCompra(
            int usuarioID,
            List<ItensCompra> itens,
            String formaPagamento) {

        PagamentoStrategy estrategia =
                selecionarPagamento(formaPagamento);

        if (estrategia == null) {
            System.out.println("Forma de pagamento inválida.");
            return false;
        }

        // O DAO calcula o valor real com base nos preços do banco.
        // Esta confirmação é uma simulação, não uma cobrança real.
        if (itens == null || itens.isEmpty()) {
            return false;
        }

        System.out.println(
                "Forma escolhida: " + estrategia.getFormaPagamento()
        );

        return cDAO.finalizarCompra(
                usuarioID,
                itens,
                estrategia.getFormaPagamento()
        );
    }

    // COMPATIBILIDADE COM CHAMADAS ANTIGAS

    public boolean finalizarCompra(
            int usuarioID,
            List<ItensCompra> itens) {

        return finalizarCompra(usuarioID, itens, "DINHEIRO");
    }
}

