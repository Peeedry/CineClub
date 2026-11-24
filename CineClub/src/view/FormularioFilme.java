package view;

import controller.FilmesController;
import java.awt.*;
import java.math.BigDecimal;
import javax.swing.*;
import model.Filmes;
import util.Mensagem;
import util.Tema;

public class FormularioFilme extends JDialog {

    private JTextField txtNome;
    private JTextField txtGenero;
    private JTextField txtDuracao;
    private JTextArea txtSinopse;
    private JTextField txtFormato;
    private JTextField txtValor;
    private JTextField txtQuantidade;
    private JTextField txtPoster;

    private JButton btnSalvar;

    private final FilmesController controller = new FilmesController();

    private Filmes filme;
    private CRUDFilme parent;

    public FormularioFilme(CRUDFilme parent, Filmes filme) {

        super(parent, true);

        this.parent = parent;
        this.filme = filme;

        setTitle(filme == null ? "Novo Filme" : "Editar Filme");

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        txtNome = Tema.campo();
        txtGenero = Tema.campo();
        txtDuracao = Tema.campo();
        txtSinopse = Tema.areaTexto(3);
        txtFormato = Tema.campo();
        txtValor = Tema.campo();
        txtQuantidade = Tema.campo();
        txtPoster = Tema.campo();

        btnSalvar = Tema.botao("Salvar");

        Tema.Formulario painel = new Tema.Formulario();
        painel.adicionar("Nome:", txtNome);
        painel.adicionar("Gênero:", txtGenero);
        painel.adicionar("Duração (min):", txtDuracao);
        painel.adicionar("Sinopse:", Tema.rolagem(txtSinopse));
        painel.adicionar("Formato:", txtFormato);
        painel.adicionar("Valor:", txtValor);
        painel.adicionar("Quantidade:", txtQuantidade);
        painel.adicionar("Poster (caminho):", txtPoster);
        painel.botoes(btnSalvar);

        add(Tema.cabecalho(filme == null ? "Novo filme" : "Editar filme"),
                BorderLayout.NORTH);

        JPanel centro = Tema.fundo(new BorderLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(20, 28, 24, 28));
        centro.add(painel, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        // SE FOR EDIÇÃO, PREENCHE OS CAMPOS
        if (filme != null) {

            txtNome.setText(filme.getNome());
            txtGenero.setText(filme.getGen());
            txtDuracao.setText(String.valueOf(filme.getDurc()));
            txtSinopse.setText(filme.getSinp());
            txtFormato.setText(filme.getForm());
            txtValor.setText(filme.getVal().toString());
            txtQuantidade.setText(String.valueOf(filme.getQuant()));
            txtPoster.setText(filme.getPoster());
        }

        // EVENTO DO BOTÃO
        btnSalvar.addActionListener(e -> salvarFilme());

        pack();
        setSize(Math.max(getWidth(), 600), getHeight());
        setLocationRelativeTo(parent);
    }

    // SALVAR FILME
    private void salvarFilme() {

        try {

            String nome = txtNome.getText().trim();
            String genero = txtGenero.getText().trim();
            String duracaoTexto = txtDuracao.getText().trim();
            String sinopse = txtSinopse.getText().trim();
            String formato = txtFormato.getText().trim();

            String valorTexto = txtValor.getText()
                    .trim()
                    .replace(",", ".");

            String quantidadeTexto = txtQuantidade.getText().trim();
            String poster = txtPoster.getText().trim();

            // VALIDAR CAMPOS
            if (nome.isEmpty() || genero.isEmpty()
                    || duracaoTexto.isEmpty() || sinopse.isEmpty()
                    || formato.isEmpty() || valorTexto.isEmpty()
                    || quantidadeTexto.isEmpty() || poster.isEmpty()) {

                Mensagem.erro(this, "Todos os campos são obrigatórios!");

                return;
            }

            int duracao = Integer.parseInt(duracaoTexto);
            int quantidade = Integer.parseInt(quantidadeTexto);
            BigDecimal valor = new BigDecimal(valorTexto);

            if (duracao <= 0 || quantidade < 0) {

                Mensagem.erro(this, "A duração deve ser maior que zero e a quantidade não pode ser negativa!");

                return;
            }

            if (valor.compareTo(BigDecimal.ZERO) < 0) {

                Mensagem.erro(this, "O valor não pode ser negativo!");

                return;
            }

            // CADASTRAR FILME
            if (filme == null) {

                Filmes f = new Filmes();

                f.setNome(nome);
                f.setGen(genero);
                f.setDurc(duracao);
                f.setSinp(sinopse);
                f.setForm(formato);
                f.setVal(valor);
                f.setQuant(quantidade);
                f.setPoster(poster);

                boolean sucesso = controller.cadastrar(
                        nome,
                        genero,
                        duracao,
                        sinopse,
                        formato,
                        valor,
                        quantidade,
                        poster);

                if (sucesso) {

                    Mensagem.info(this, "Filme cadastrado com sucesso!");

                    parent.atualizarTabela();
                    dispose();

                } else {

                    Mensagem.erro(this, "Erro ao cadastrar filme!");
                }


            } else {

                // EDITAR FILME
                filme.setNome(nome);
                filme.setGen(genero);
                filme.setDurc(duracao);
                filme.setSinp(sinopse);
                filme.setForm(formato);
                filme.setVal(valor);
                filme.setQuant(quantidade);
                filme.setPoster(poster);

                boolean sucesso = controller.atualizar(filme);

                if (sucesso) {

                    Mensagem.info(this, "Filme atualizado com sucesso!");

                    parent.atualizarTabela();
                    dispose();

                } else {

                    Mensagem.erro(this, "Erro ao atualizar filme!");
                }
            }

        } catch (NumberFormatException e) {

            Mensagem.erro(this, "Digite valores numéricos válidos para duração, quantidade e valor!");

        } catch (Exception e) {

            e.printStackTrace();

            Mensagem.erro(this, "Erro ao salvar filme!");
        }
    }
}
