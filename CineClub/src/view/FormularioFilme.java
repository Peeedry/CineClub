package view;

import dao.FilmesDAO;
import java.awt.*;
import java.math.BigDecimal;
import javax.swing.*;
import model.Filmes;

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

    private final FilmesDAO filmeDAO = new FilmesDAO();

    private Filmes filme;
    private CRUDFilme parent;

    public FormularioFilme(CRUDFilme parent, Filmes filme) {

        super(parent, true);

        this.parent = parent;
        this.filme = filme;

        setTitle(filme == null ? "Novo Filme" : "Editar Filme");

        setSize(450, 550);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new GridLayout(8, 2, 10, 10));

        // NOME
        painel.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painel.add(txtNome);

        // GENERO
        painel.add(new JLabel("Gênero:"));
        txtGenero = new JTextField();
        painel.add(txtGenero);

        // DURACAO
        painel.add(new JLabel("Duração (min):"));
        txtDuracao = new JTextField();
        painel.add(txtDuracao);

        // SINOPSE
        painel.add(new JLabel("Sinopse:"));
        txtSinopse = new JTextArea(3, 20);
        txtSinopse.setLineWrap(true);
        txtSinopse.setWrapStyleWord(true);
        painel.add(new JScrollPane(txtSinopse));

        // FORMATO
        painel.add(new JLabel("Formato:"));
        txtFormato = new JTextField();
        painel.add(txtFormato);

        // VALOR
        painel.add(new JLabel("Valor:"));
        txtValor = new JTextField();
        painel.add(txtValor);

        // QUANTIDADE
        painel.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField();
        painel.add(txtQuantidade);

        // POSTER
        painel.add(new JLabel("Poster (caminho):"));
        txtPoster = new JTextField();
        painel.add(txtPoster);

        add(painel, BorderLayout.CENTER);

        // BOTÃO SALVAR
        btnSalvar = new JButton("Salvar");

        JPanel painelBotao = new JPanel();
        painelBotao.add(btnSalvar);

        add(painelBotao, BorderLayout.SOUTH);

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

                JOptionPane.showMessageDialog(
                    this,
                    "Todos os campos são obrigatórios!"
                );

                return;
            }

            int duracao = Integer.parseInt(duracaoTexto);
            int quantidade = Integer.parseInt(quantidadeTexto);
            BigDecimal valor = new BigDecimal(valorTexto);

            if (duracao <= 0 || quantidade < 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "A duração deve ser maior que zero e a quantidade não pode ser negativa!"
                );

                return;
            }

            if (valor.compareTo(BigDecimal.ZERO) < 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "O valor não pode ser negativo!"
                );

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

                boolean sucesso = filmeDAO.inserir(f);

                if (sucesso) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Filme cadastrado com sucesso!");

                    parent.atualizarTabela();
                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Erro ao cadastrar filme!");
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

                boolean sucesso = filmeDAO.gravar(filme);

                if (sucesso) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Filme atualizado com sucesso!"
                    );

                    parent.atualizarTabela();
                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                        this,
                        "Erro ao atualizar filme!"
                    );
                }
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Digite valores numéricos válidos para duração, quantidade e valor!"
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                this,
                "Erro ao salvar filme!"
            );
        }
    }
}
