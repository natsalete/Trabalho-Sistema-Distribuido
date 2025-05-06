package com.poo2.nsr_carros.servlets;

/**
 *
 * @author natsa
 */
public class Nsr_Carros {
   private int id;
    private String placa;
    private String modelo;
    private String marca;
    private int ano;
    private String combustivel;
    private String cor;
    private double quilometragem;
    private String categoria;
    private boolean disponivel;
    private double valorCompra;
    private double valorVenda;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getPlaca() {
        return placa;
    }
    
    public void setPlaca(String placa) {
        this.placa = placa;
    }
    
    public String getModelo() {
        return modelo;
    }
    
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
    
    public int getAno() {
        return ano;
    }
    
    public void setAno(int ano) {
        this.ano = ano;
    }
    
    public String getCombustivel() {
        return combustivel;
    }
    
    public void setCombustivel(String combustivel) {
        this.combustivel = combustivel;
    }
    
    public String getCor() {
        return cor;
    }
    
    public void setCor(String cor) {
        this.cor = cor;
    }
    
    public double getQuilometragem() {
        return quilometragem;
    }
    
    public void setQuilometragem(double quilometragem) {
        this.quilometragem = quilometragem;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public boolean isDisponivel() {
        return disponivel;
    }
    
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
    
    public double getValorCompra() {
        return valorCompra;
    }
    
    public void setValorCompra(double valorCompra) {
        this.valorCompra = valorCompra;
    }
    
    public double getValorVenda() {
        return valorVenda;
    }
    
    public void setValorVenda(double valorVenda) {
        this.valorVenda = valorVenda;
    }
}
