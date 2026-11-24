
package view;

import dao.FilmesDAO;
import model.Filmes;
import model.Usuarios;
import util.Mensagem;
import util.Tema;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CRUDFilme extends JFrame {

    private final FilmesDAO filmeDAO = new FilmesDAO();
    private final Usuarios usuario;

    private JPanel painelCards;
    private JScrollPane scrollPane;
    private JButton btnNovo;

    public CRUDFilme(Usuarios usuario) {

        this.usuario = usuario;

        if (usuario == null) {
            Mensagem.erro(this, "Usuário não identificado.");
            dispose();
            return;
        }

        boolean administrador =
                "A".equalsIgnoreCase(usuario.getTipo());

        setTitle("CineClub - Gerenciamento de Filmes");
        setSize(1100, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        btnNovo = Tema.botao("Novo Filme");

        // Somente administrador pode cadastrar filmes
        btnNovo.setVisible(administrador);

        add(
            Tema.cabecalho("Gerenciamento de filmes", btnNovo),
            BorderLayout.NORTH
        );

        painelCards = new JPanel();
        painelCards.setBackground(Tema.AZUL);

        scrollPane = Tema.rolagemAzul(painelCards);

        add(scrollPane, BorderLayout.CENTER);
        add(Tema.rodape(), BorderLayout.SOUTH);

        btnNovo.addActionListener(e -> abrirFormulario(null));

        carregarFilmes();
    }

    private boolean isAdministrador() {
        return "A".equalsIgnoreCase(usuario.getTipo());
    }

    private void carregarFilmes() {

        List<Filmes> filmes = filmeDAO.listarTodos();

        painelCards = new PainelCardsFilmes(
            filmes,

            // Editar
            isAdministrador()
                ? filme -> abrirFormulario(filme)
                : null,

            // Excluir
            isAdministrador()
                ? filme -> excluirFilme(filme)
                : null,

            // Adicionar ao carrinho não é usado nesta tela
            null
        );

        scrollPane.setViewportView(painelCards);

        painelCards.revalidate();
        painelCards.repaint();
    }

    private void abrirFormulario(Filmes filme) {

        if (!isAdministrador()) {
            Mensagem.erro(this,
                    "Você não tem permissão para editar filmes.");
            return;
        }

        FormularioFilme form =
                new FormularioFilme(this, filme);

        form.setVisible(true);
    }

    private void excluirFilme(Filmes filme) {

        if (!isAdministrador()) {
            Mensagem.erro(this,
                    "Você não tem permissão para excluir filmes.");
            return;
        }

        boolean confirmou = Mensagem.confirmar(
                this,
                "Confirmar exclusão",
                "Deseja realmente excluir o filme "
                        + filme.getNome() + "?"
        );

        if (confirmou) {

            boolean sucesso =
                    filmeDAO.excluir(filme.getID());

            if (sucesso) {
                Mensagem.info(this,
                        "Filme excluído com sucesso!");
                carregarFilmes();
            } else {
                Mensagem.erro(this,
                        "Erro ao excluir filme.");
            }
        }
    }

    public void atualizarTabela() {
        carregarFilmes();
    }
}
