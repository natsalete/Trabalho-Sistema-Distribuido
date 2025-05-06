package com.poo2.nsr_carros.servlets;
import jakarta.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Classe responsável por ler dados de carros do arquivo de texto.
 * @author natsa
 */
public class Nsr_BuscarArquivo {
    
    // Caminho fixo para o arquivo de dados
    private static final String ARQUIVO_CARROS = "C:\\Users\\natsa\\OneDrive\\Imagens\\cadastro_carros.txt";
    
    /**
     * Lê carros do arquivo de texto formatado com | como separador.
     * @param context O contexto do servlet para localizar o arquivo (não usado na versão modificada)
     * @return Lista de objetos Nsr_Carros lidos do arquivo
     * @throws IOException Se ocorrer erro na leitura do arquivo
     */
    public List<Nsr_Carros> lerArqCarros(ServletContext context) throws IOException {
        List<Nsr_Carros> lstCarros = new ArrayList<>();
        
        File f = new File(ARQUIVO_CARROS);
        
        if (!f.exists()) {
            System.out.println("Arquivo não encontrado: " + ARQUIVO_CARROS);
            // Se o arquivo não existir, retorna uma lista vazia
            return lstCarros;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    // Formato: id|placa|modelo|marca|ano|combustivel|cor|quilometragem|categoria|disponivel|valorCompra|valorVenda
                    String[] dados = linha.split("\\|");
                    if (dados.length >= 12) {
                        Nsr_Carros carro = new Nsr_Carros();
                        carro.setId(Integer.parseInt(dados[0].trim()));
                        carro.setPlaca(dados[1].trim());
                        carro.setModelo(dados[2].trim());
                        carro.setMarca(dados[3].trim());
                        carro.setAno(Integer.parseInt(dados[4].trim()));
                        carro.setCombustivel(dados[5].trim());
                        carro.setCor(dados[6].trim());
                        carro.setQuilometragem(Double.parseDouble(dados[7].trim()));
                        carro.setCategoria(dados[8].trim());
                        carro.setDisponivel(Boolean.parseBoolean(dados[9].trim()));
                        carro.setValorCompra(Double.parseDouble(dados[10].trim().replace(",", ".")));
                        carro.setValorVenda(Double.parseDouble(dados[11].trim().replace(",", ".")));
                        
                        lstCarros.add(carro);
                    }
                }
            }
        }
        
        return lstCarros;
    }
}