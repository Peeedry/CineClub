package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Compras {
      private int id;
      private int usuarioID;
      private LocalDate dataCompra;
      private BigDecimal valorTotal;
      private String status;
    
      public Compras(){

      }

      public int getId(){
        return this.id;
      }

      public void setId(int id){
        this.id = id;
      }

      public int getUsID(){
        return this.usuarioID;
      }

      public void setUsID(int usid){
        this.usuarioID = usid;
      }

      public LocalDate getData(){
        return this.dataCompra;
      }

      public void setData(LocalDate data){
        this.dataCompra = data;
      }

      public BigDecimal getValor(){
        return this.valorTotal;
      }

      public void setValor(BigDecimal valor){
        this.valorTotal = valor;
      }

      public String getStatus(){
        return this.status;
      }

      public void setStatus(String status){
        this.status = status;
      }

      
}
