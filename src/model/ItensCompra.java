package model;

import java.math.BigDecimal;

public class ItensCompra {
      private int id;
      private int compraID;
      private int filmeID;
      private BigDecimal precoUnitario;
      private int quantidade;
      private BigDecimal subtotal;

      public ItensCompra() {

      }

      public int getId(){
        return this.id;
      }

      public void setId(int id){
        this.id = id;
      }

      public int getCopId(){
        return this.compraID;
      }

      public void setCopId(int copid){
        this.compraID = copid; 
      }

      public int getFilId(){
        return this.filmeID;
      }

      public void setFilId(int filid){
        this.filmeID = filid;
      }

      public BigDecimal getPreUn(){
        return this.precoUnitario;
      }

      public void setPreUn(BigDecimal preun){
        this.precoUnitario = preun;
      }

      public int getQuant(){
        return this.quantidade;
      }

      public void setQuant(int quant){
        this.quantidade = quant;
      }

      public BigDecimal getSub(){
        return this.subtotal;
      }

      public void setSub(BigDecimal sub){
        this.subtotal = sub;
      }
}
