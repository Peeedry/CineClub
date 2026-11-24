package controller;

import java.math.BigDecimal;

import dao.FilmesDAO;
import model.Filmes;

public class FilmesController {

    private final FilmesDAO fDAO = new FilmesDAO();

    public boolean cadastrar(
            String nome,
            String genero,
            int duracao,
            String sinopse,
            String formato,
            BigDecimal valor,
            int quantidade,
            String poster) {

        Filmes f = new Filmes();

        f.setNome(nome);
        f.setGen(genero);
        f.setDurc(duracao);
        f.setSinp(sinopse);
        f.setForm(formato);
        f.setVal(valor);
        f.setQuant(quantidade);
        f.setPoster(poster);

        return fDAO.inserir(f);
    }

    public boolean atualizar(Filmes filme) {
        return fDAO.gravar(filme);
    }
}