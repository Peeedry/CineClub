package view;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

import controller.FilmesController;
import util.Mensagem;
import util.Tema;

public class TelaCadastroFilme extends JFrame {

    private JTextField txtNome;
    private JTextField txtGenero;
    private JTextField txtDuracao;
    private JTextField txtFormato;
    private JTextField txtValor;
    private JTextField txtQuantidade;
    private JTextField txtPoster;
    private JTextArea txtSinopse;

    private JButton btnSalvar;

    private final FilmesController controller = new FilmesController();

    public TelaCadastroFilme() {

        setTitle("Cadastro de Filme");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(Tema.cabecalho("Novo filme"), BorderLayout.NORTH);

        txtNome = Tema.campo();
        txtGenero = Tema.campo();
        txtDuracao = Tema.campo();
        txtSinopse = Tema.areaTexto(3);
        txtFormato = Tema.campo();
        txtValor = Tema.campo();
        txtQuantidade = Tema.campo();
        txtPoster = Tema.campo();
        btnSalvar = Tema.botao("Salvar Filme");

        Tema.Formulario form = new Tema.Formulario();
        form.adicionar("Nome:", txtNome);
        form.adicionar("Gênero:", txtGenero);
        form.adicionar("Duração (minutos):", txtDuracao);
        form.adicionar("Sinopse:", Tema.rolagem(txtSinopse));
        form.adicionar("Formato:", txtFormato);
        form.adicionar("Valor:", txtValor);
        form.adicionar("Quantidade:", txtQuantidade);
        form.adicionar("Poster (caminho):", txtPoster);
        form.botoes(btnSalvar);

        JPanel centro = Tema.fundo(new BorderLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(20, 28, 24, 28));
        centro.add(form, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        add(Tema.rodape(), BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvarFilme());

        pack();
        setSize(Math.max(getWidth(), 600), getHeight());
        setLocationRelativeTo(null);

    }

    private void salvarFilme() {

        try {

            String nome = txtNome.getText().trim();
            String genero = txtGenero.getText().trim();
            int duracao = Integer.parseInt(txtDuracao.getText().trim());
            String sinopse = txtSinopse.getText().trim();
            String formato = txtFormato.getText().trim();

            BigDecimal valor = new BigDecimal(
                txtValor.getText().trim().replace(",", ".")
            );

            int quantidade = Integer.parseInt(
                txtQuantidade.getText().trim()
            );

            String poster = txtPoster.getText().trim();

            if (nome.isEmpty() || genero.isEmpty() ||
                sinopse.isEmpty() || formato.isEmpty() ||
                poster.isEmpty()) {

                Mensagem.erro("Preencha todos os campos!");
                return;
            }

            if (duracao <= 0 || quantidade < 0 ||
                valor.compareTo(BigDecimal.ZERO) <= 0) {

                Mensagem.erro("Informe duração, quantidade e valor válidos!");
                return;
            }

            boolean sucesso = controller.cadastrar(
                nome,
                genero,
                duracao,
                sinopse,
                formato,
                valor,
                quantidade,
                poster
            );

            if (sucesso) {

                Mensagem.info("Filme cadastrado com sucesso!");
                dispose();

            } else {

                Mensagem.erro("Erro ao cadastrar filme no banco de dados!");

            }

        } catch (NumberFormatException e) {

            Mensagem.erro("Verifique os campos de duração, valor e quantidade!");

        }

    }
}