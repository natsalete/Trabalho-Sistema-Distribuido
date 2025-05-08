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
    private static final String Nsr_ARQUIVO_CARROS = "C:\\Users\\natsa\\OneDrive\\Imagens\\cadastro_carros.txt";
    
    /**
     * Lê carros do arquivo de texto formatado com | como separador.
     * @param Nsr_context O contexto do servlet para localizar o arquivo (não usado na versão modificada)
     * @return Lista de objetos Nsr_Carros lidos do arquivo
     * @throws IOException Se ocorrer erro na leitura do arquivo
     */
    public List<Nsr_Carros> Nsr_lerArqCarros(ServletContext Nsr_context) throws IOException {
        List<Nsr_Carros> Nsr_lstCarros = new ArrayList<>();
        
        File Nsr_f = new File(Nsr_ARQUIVO_CARROS);
        
        if (!Nsr_f.exists()) {
            System.out.println("Arquivo não encontrado: " + Nsr_ARQUIVO_CARROS);
            // Se o arquivo não existir, retorna uma lista vazia
            return Nsr_lstCarros;
        }
        
        try (BufferedReader Nsr_br = new BufferedReader(new FileReader(Nsr_f))) {
            String Nsr_linha;
            while ((Nsr_linha = Nsr_br.readLine()) != null) {
                if (!Nsr_linha.trim().isEmpty()) {
                    // Formato: id|placa|modelo|marca|ano|combustivel|cor|quilometragem|categoria|disponivel|valorCompra|valorVenda
                    String[] Nsr_dados = Nsr_linha.split("\\|");
                    if (Nsr_dados.length >= 12) {
                        Nsr_Carros Nsr_carro = new Nsr_Carros();
                        Nsr_carro.Nsr_setId(Integer.parseInt(Nsr_dados[0].trim()));
                        Nsr_carro.Nsr_setPlaca(Nsr_dados[1].trim());
                        Nsr_carro.Nsr_setModelo(Nsr_dados[2].trim());
                        Nsr_carro.Nsr_setMarca(Nsr_dados[3].trim());
                        Nsr_carro.Nsr_setAno(Integer.parseInt(Nsr_dados[4].trim()));
                        Nsr_carro.Nsr_setCombustivel(Nsr_dados[5].trim());
                        Nsr_carro.Nsr_setCor(Nsr_dados[6].trim());
                        Nsr_carro.Nsr_setQuilometragem(Double.parseDouble(Nsr_dados[7].trim()));
                        Nsr_carro.Nsr_setCategoria(Nsr_dados[8].trim());
                        Nsr_carro.Nsr_setDisponivel(Boolean.parseBoolean(Nsr_dados[9].trim()));
                        Nsr_carro.Nsr_setValorCompra(Double.parseDouble(Nsr_dados[10].trim().replace(",", ".")));
                        Nsr_carro.Nsr_setValorVenda(Double.parseDouble(Nsr_dados[11].trim().replace(",", ".")));
                        
                        Nsr_lstCarros.add(Nsr_carro);
                    }
                }
            }
        }
        
        return Nsr_lstCarros;
    }
}