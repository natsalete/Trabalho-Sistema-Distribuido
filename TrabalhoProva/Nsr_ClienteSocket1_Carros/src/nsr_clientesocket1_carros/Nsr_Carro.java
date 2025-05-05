package nsr_clientesocket1_carros;

/**
 *
 * @author natsa
 */

/**
 * Classe que representa um Carro
 */
public class Nsr_Carro {
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
    
    /**
     * Construtor para criar um carro a partir de uma string delimitada
     */
    public Nsr_Carro(String linha) {
        String[] campos = linha.split("\\|");
        if (campos.length >= 12) {
            this.id = Integer.parseInt(campos[0].trim());
            this.placa = campos[1].trim();
            this.modelo = campos[2].trim();
            this.marca = campos[3].trim();
            this.ano = Integer.parseInt(campos[4].trim());
            this.combustivel = campos[5].trim();
            this.cor = campos[6].trim();
            this.quilometragem = Double.parseDouble(campos[7].trim());
            this.categoria = campos[8].trim();
            this.disponivel = Boolean.parseBoolean(campos[9].trim());
            this.valorCompra = Double.parseDouble(campos[10].trim());
            this.valorVenda = Double.parseDouble(campos[11].trim());
        }
    }
    
    /**
     * Construtor para criar um novo carro com todos os campos
     */
    public Nsr_Carro(int id, String placa, String modelo, String marca, int ano, 
                String combustivel, String cor, double quilometragem, 
                String categoria, boolean disponivel, double valorCompra, 
                double valorVenda) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.combustivel = combustivel;
        this.cor = cor;
        this.quilometragem = quilometragem;
        this.categoria = categoria;
        this.disponivel = disponivel;
        this.valorCompra = valorCompra;
        this.valorVenda = valorVenda;
    }
    
    /**
     * Converte o objeto para uma string delimitada
     */
    public String paraString() {
        return id + "|" + placa + "|" + modelo + "|" + marca + "|" + ano + "|" + 
               combustivel + "|" + cor + "|" + quilometragem + "|" + categoria + "|" +
               disponivel + "|" + valorCompra + "|" + valorVenda;
    }
    
    // Getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }
    public String getCombustivel() { return combustivel; }
    public void setCombustivel(String combustivel) { this.combustivel = combustivel; }
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
    public double getQuilometragem() { return quilometragem; }
    public void setQuilometragem(double quilometragem) { this.quilometragem = quilometragem; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    public double getValorCompra() { return valorCompra; }
    public void setValorCompra(double valorCompra) { this.valorCompra = valorCompra; }
    public double getValorVenda() { return valorVenda; }
    public void setValorVenda(double valorVenda) { this.valorVenda = valorVenda; }
}