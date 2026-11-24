package view;

import model.Usuarios;
import util.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaMenu extends JFrame {

    private final Usuarios usuario;

    public TelaMenu(Usuarios usuario) {
        this.usuario = usuario;

        boolean administrador =
                "A".equalsIgnoreCase(usuario.getTipo());

        setTitle("CineClub - Menu Principal - Usuario: "
                + usuario.getNome());
        setSize(760, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();

        JMenu menuCadastro = new JMenu("Cadastro");
        JMenuItem menuUsuario = new JMenuItem("Usuarios");
        JMenuItem menuCliente = new JMenuItem("Cliente");
        JMenuItem menuProduto = new JMenuItem("Filmes");

        menuCadastro.add(menuUsuario);
        menuCadastro.add(menuCliente);
        menuCadastro.add(menuProduto);

        // Cadastro aparece somente para administradores
        menuCadastro.setVisible(administrador);

        JMenu menuSair = new JMenu("Sair");
        JMenuItem sair = new JMenuItem("Logout");
        menuSair.add(sair);

        menuBar.add(menuCadastro);
        menuBar.add(menuSair);

        Tema.estilizarMenu(menuBar);
        setJMenuBar(menuBar);

        add(Tema.cabecalho("Menu principal"), BorderLayout.NORTH);

        JLabel lblSaudacao = new JLabel(
                "Bem-Vindo, " + usuario.getNome() + "!",
                SwingConstants.CENTER
        );

        lblSaudacao.setFont(Tema.titulo(40));
        lblSaudacao.setForeground(Tema.AMARELO);
        lblSaudacao.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCompras = Tema.botao("Compras");
        btnCompras.setFont(Tema.titulo(24));
        btnCompras.setBorder(
                new EmptyBorder(14, 42, 15, 42)
        );
        btnCompras.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnCompras.addActionListener(e -> {
            CRUDCompras tela =
                    new CRUDCompras(usuario.getId());
            tela.setVisible(true);
        });

        JPanel centro = Tema.fundo(new GridBagLayout());

        JPanel bloco = new JPanel();
        bloco.setOpaque(false);
        bloco.setLayout(
                new BoxLayout(bloco, BoxLayout.Y_AXIS)
        );

        bloco.add(lblSaudacao);
        bloco.add(Box.createVerticalStrut(30));
        bloco.add(btnCompras);

        centro.add(bloco);
        add(centro, BorderLayout.CENTER);

        add(Tema.rodape(), BorderLayout.SOUTH);

        // Eventos do administrador
        menuUsuario.addActionListener(e ->
                new CRUDUsuario().setVisible(true)
        );

        menuCliente.addActionListener(e ->
                new TelaCadastroUsuario().setVisible(true)
        );

        menuProduto.addActionListener(e ->
                new CRUDFilme(usuario).setVisible(true)
        );

        sair.addActionListener(e -> {
            dispose();
            new TelaLogin().setVisible(true);
        });
    }
}

