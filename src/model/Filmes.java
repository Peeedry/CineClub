package model;

import java.math.BigDecimal;

public class Filmes{
      private int id;
      private String nome;
      private String genero;
      private int duracao;
      private String sinopse;
      private String formato;
      private BigDecimal valor;
      private int quantidade;
      private String poster;

      public Filmes() {

      }

      public int getID(){
        return this.id;
      }

      public void setID(int id){
        this.id = id;
      }

      public String getNome(){
        return this.nome;
      }

      public void setNome(String nome){
        this.nome = nome;
      }

      public String getGen(){
        return this.genero;
      }

      public void setGen(String gen) {
        this.genero = gen;
      }

      public int getDurc(){
        return this.duracao;
      }

      public void setDurc(int durc){
        this.duracao = durc;
      }

      public String getSinp(){
        return this.sinopse;
      }

      public void setSinp(String sinp){
        this.sinopse = sinp;
      }

      public String getForm(){
        return this.formato;
      }

      public void setForm(String form){
        this.formato = form;
      }

      public BigDecimal getVal(){
        return this.valor;
      }

      public void setVal(BigDecimal val){
        this.valor = val;
      }

      public int getQuant(){
        return this.quantidade;
      }

      public void setQuant(int quant){
        this.quantidade = quant;
      }

      public String getPoster() {
       return this.poster;
      }

      public void setPoster(String poster) {
       this.poster = poster;
      }


    // FACTORY METHOD

    public static abstract class CriadorFilme {
        public abstract Filmes criarFilme();
    }

    public static class CriadorDVD extends CriadorFilme {
        @Override
        public Filmes criarFilme() {
            return new DVD();
        }
    }

    public static class CriadorBluRay extends CriadorFilme {
        @Override
        public Filmes criarFilme() {
            return new BluRay();
        }
    }

    public static class CriadorVHS extends CriadorFilme {
        @Override
        public Filmes criarFilme() {
            return new VHS();
        }
    }

    public static class DVD extends Filmes {
        public DVD() {
            setForm("DVD");
        }
    }

    public static class BluRay extends Filmes {
        public BluRay() {
            setForm("BLU-RAY");
        }
    }

    public static class VHS extends Filmes {
        public VHS() {
            setForm("VHS");
        }
    }

    public static Filmes criarPorFormato(String formato) {

      if (formato == null) {
        throw new IllegalArgumentException("Formato não informado.");
      }

      CriadorFilme criador;

      switch (formato.trim().toUpperCase()) {

        case "DVD":
          criador = new CriadorDVD();
          break;

        case "BLU-RAY":
          criador = new CriadorBluRay();
          break;

        case "VHS":
          criador = new CriadorVHS();
          break;

        default:
          throw new IllegalArgumentException(
              "Formato não reconhecido: " + formato);
      }

      return criador.criarFilme();
    }
    
}