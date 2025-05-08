package nsr_clientesocket1_carros;

/**
 *
 * @author natsa
 */

/**
 * Classe que representa um Carro
 */
public class Nsr_Carro {
    private int Nsr_id;
    private String Nsr_placa;
    private String Nsr_modelo;
    private String Nsr_marca;
    private int Nsr_ano;
    private String Nsr_combustivel;
    private String Nsr_cor;
    private double Nsr_quilometragem;
    private String Nsr_categoria;
    private boolean Nsr_disponivel;
    private double Nsr_valorCompra;
    private double Nsr_valorVenda;
    
    /**
     * Construtor para criar um carro a partir de uma string delimitada
     */
    public Nsr_Carro(String Nsr_linha) {
        String[] Nsr_campos = Nsr_linha.split("\\|");
        if (Nsr_campos.length >= 12) {
            this.Nsr_id = Integer.parseInt(Nsr_campos[0].trim());
            this.Nsr_placa = Nsr_campos[1].trim();
            this.Nsr_modelo = Nsr_campos[2].trim();
            this.Nsr_marca = Nsr_campos[3].trim();
            this.Nsr_ano = Integer.parseInt(Nsr_campos[4].trim());
            this.Nsr_combustivel = Nsr_campos[5].trim();
            this.Nsr_cor = Nsr_campos[6].trim();
            this.Nsr_quilometragem = Double.parseDouble(Nsr_campos[7].trim());
            this.Nsr_categoria = Nsr_campos[8].trim();
            this.Nsr_disponivel = Boolean.parseBoolean(Nsr_campos[9].trim());
            this.Nsr_valorCompra = Double.parseDouble(Nsr_campos[10].trim());
            this.Nsr_valorVenda = Double.parseDouble(Nsr_campos[11].trim());
        }
    }
    
    /**
     * Construtor para criar um novo carro com todos os campos
     */
    public Nsr_Carro(int Nsr_id, String Nsr_placa, String Nsr_modelo, String Nsr_marca, int Nsr_ano, 
                String Nsr_combustivel, String Nsr_cor, double Nsr_quilometragem, 
                String Nsr_categoria, boolean Nsr_disponivel, double Nsr_valorCompra, 
                double Nsr_valorVenda) {
        this.Nsr_id = Nsr_id;
        this.Nsr_placa = Nsr_placa;
        this.Nsr_modelo = Nsr_modelo;
        this.Nsr_marca = Nsr_marca;
        this.Nsr_ano = Nsr_ano;
        this.Nsr_combustivel = Nsr_combustivel;
        this.Nsr_cor = Nsr_cor;
        this.Nsr_quilometragem = Nsr_quilometragem;
        this.Nsr_categoria = Nsr_categoria;
        this.Nsr_disponivel = Nsr_disponivel;
        this.Nsr_valorCompra = Nsr_valorCompra;
        this.Nsr_valorVenda = Nsr_valorVenda;
    }
    
    /**
     * Converte o objeto para uma string delimitada
     */
    public String Nsr_paraString() {
        return Nsr_id + "|" + Nsr_placa + "|" + Nsr_modelo + "|" + Nsr_marca + "|" + Nsr_ano + "|" + 
               Nsr_combustivel + "|" + Nsr_cor + "|" + Nsr_quilometragem + "|" + Nsr_categoria + "|" +
               Nsr_disponivel + "|" + Nsr_valorCompra + "|" + Nsr_valorVenda;
    }
    
    // Getters e setters
    public int Nsr_getId() { return Nsr_id; }
    public void Nsr_setId(int Nsr_id) { this.Nsr_id = Nsr_id; }
    
    public String Nsr_getPlaca() { return Nsr_placa; }
    public void Nsr_setPlaca(String Nsr_placa) { this.Nsr_placa = Nsr_placa; }
    
    public String Nsr_getModelo() { return Nsr_modelo; }
    public void Nsr_setModelo(String Nsr_modelo) { this.Nsr_modelo = Nsr_modelo; }
    
    public String Nsr_getMarca() { return Nsr_marca; }
    public void Nsr_setMarca(String Nsr_marca) { this.Nsr_marca = Nsr_marca; }
    
    public int Nsr_getAno() { return Nsr_ano; }
    public void Nsr_setAno(int Nsr_ano) { this.Nsr_ano = Nsr_ano; }
    
    public String Nsr_getCombustivel() { return Nsr_combustivel; }
    public void Nsr_setCombustivel(String Nsr_combustivel) { this.Nsr_combustivel = Nsr_combustivel; }
    
    public String Nsr_getCor() { return Nsr_cor; }
    public void Nsr_setCor(String Nsr_cor) { this.Nsr_cor = Nsr_cor; }
    
    public double Nsr_getQuilometragem() { return Nsr_quilometragem; }
    public void Nsr_setQuilometragem(double Nsr_quilometragem) { this.Nsr_quilometragem = Nsr_quilometragem; }
    
    public String Nsr_getCategoria() { return Nsr_categoria; }
    public void Nsr_setCategoria(String Nsr_categoria) { this.Nsr_categoria = Nsr_categoria; }
    
    public boolean Nsr_isDisponivel() { return Nsr_disponivel; }
    public void Nsr_setDisponivel(boolean Nsr_disponivel) { this.Nsr_disponivel = Nsr_disponivel; }
    
    public double Nsr_getValorCompra() { return Nsr_valorCompra; }
    public void Nsr_setValorCompra(double Nsr_valorCompra) { this.Nsr_valorCompra = Nsr_valorCompra; }
    
    public double Nsr_getValorVenda() { return Nsr_valorVenda; }
    public void Nsr_setValorVenda(double Nsr_valorVenda) { this.Nsr_valorVenda = Nsr_valorVenda; }
}