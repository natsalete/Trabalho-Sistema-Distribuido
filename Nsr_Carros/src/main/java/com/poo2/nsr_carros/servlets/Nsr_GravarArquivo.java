package com.poo2.nsr_carros.servlets;

import jakarta.servlet.ServletContext;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Classe responsável por gravar dados de carros em arquivo de texto.
 * @author natsa
 */
public class Nsr_GravarArquivo {
    
    // Caminho fixo para o arquivo de dados
    private static final String Nsr_ARQUIVO_CARROS = "C:\\Users\\natsa\\OneDrive\\Imagens\\cadastro_carros.txt";
    
    /**
     * Escreve um objeto Nsr_Carros no arquivo de texto.
     * @param Nsr_carro O carro a ser gravado
     * @param Nsr_context O contexto do servlet (não usado na versão modificada)
     * @throws IOException Se ocorrer erro na escrita do arquivo
     */
    public void Nsr_escreverArqCarro(Nsr_Carros Nsr_carro, ServletContext Nsr_context) throws IOException {
        // Formatação para valores monetários
        DecimalFormat Nsr_df = new DecimalFormat("0.00");
        
        // Formata a linha conforme o padrão: id|placa|modelo|marca|ano|combustivel|cor|quilometragem|categoria|disponivel|valorCompra|valorVenda
        StringBuilder Nsr_linha = new StringBuilder();
        Nsr_linha.append(Nsr_carro.Nsr_getId()).append("|");
        Nsr_linha.append(Nsr_carro.Nsr_getPlaca()).append("|");
        Nsr_linha.append(Nsr_carro.Nsr_getModelo()).append("|");
        Nsr_linha.append(Nsr_carro.Nsr_getMarca()).append("|");
        Nsr_linha.append(Nsr_carro.Nsr_getAno()).append("|");
        Nsr_linha.append(Nsr_carro.Nsr_getCombustivel()).append("|");
        Nsr_linha.append(Nsr_carro.Nsr_getCor()).append("|");
        Nsr_linha.append(Nsr_carro.Nsr_getQuilometragem()).append("|");
        Nsr_linha.append(Nsr_carro.Nsr_getCategoria()).append("|");
        Nsr_linha.append(Nsr_carro.Nsr_isDisponivel()).append("|");
        Nsr_linha.append(Nsr_df.format(Nsr_carro.Nsr_getValorCompra()).replace(",", ".")).append("|");
        Nsr_linha.append(Nsr_df.format(Nsr_carro.Nsr_getValorVenda()).replace(",", "."));
        
        File Nsr_f = new File(Nsr_ARQUIVO_CARROS);
        
        // Verifica se o diretório pai existe e cria se necessário
        File Nsr_parentDir = Nsr_f.getParentFile();
        if (!Nsr_parentDir.exists()) {
            Nsr_parentDir.mkdirs();
        }
        
        // Append (true) para adicionar ao final do arquivo
        try (FileWriter Nsr_fw = new FileWriter(Nsr_f, true)) {
            Nsr_fw.write(Nsr_linha.toString() + "\n");
        }
    }
    
    /**
     * Reescreve todo o arquivo com a lista atualizada de carros.
     * @param Nsr_lstCarros A lista de carros a ser gravada
     * @param Nsr_context O contexto do servlet (não usado na versão modificada)
     * @throws IOException Se ocorrer erro na escrita do arquivo
     */
    public void Nsr_reescreverArquivo(List<Nsr_Carros> Nsr_lstCarros, ServletContext Nsr_context) throws IOException {
        // Formatação para valores monetários
        DecimalFormat Nsr_df = new DecimalFormat("0.00");
        
        File Nsr_f = new File(Nsr_ARQUIVO_CARROS);
        
        // Verifica se o diretório pai existe e cria se necessário
        File Nsr_parentDir = Nsr_f.getParentFile();
        if (!Nsr_parentDir.exists()) {
            Nsr_parentDir.mkdirs();
        }
        
        // Sobrescrever o arquivo (false = não append)
        try (FileWriter Nsr_fw = new FileWriter(Nsr_f, false)) {
            for (Nsr_Carros Nsr_carro : Nsr_lstCarros) {
                // Formata a linha conforme o padrão
                StringBuilder Nsr_linha = new StringBuilder();
                Nsr_linha.append(Nsr_carro.Nsr_getId()).append("|");
                Nsr_linha.append(Nsr_carro.Nsr_getPlaca()).append("|");
                Nsr_linha.append(Nsr_carro.Nsr_getModelo()).append("|");
                Nsr_linha.append(Nsr_carro.Nsr_getMarca()).append("|");
                Nsr_linha.append(Nsr_carro.Nsr_getAno()).append("|");
                Nsr_linha.append(Nsr_carro.Nsr_getCombustivel()).append("|");
                Nsr_linha.append(Nsr_carro.Nsr_getCor()).append("|");
                Nsr_linha.append(Nsr_carro.Nsr_getQuilometragem()).append("|");
                Nsr_linha.append(Nsr_carro.Nsr_getCategoria()).append("|");
                Nsr_linha.append(Nsr_carro.Nsr_isDisponivel()).append("|");
                Nsr_linha.append(Nsr_df.format(Nsr_carro.Nsr_getValorCompra()).replace(",", ".")).append("|");
                Nsr_linha.append(Nsr_df.format(Nsr_carro.Nsr_getValorVenda()).replace(",", "."));
                
                Nsr_fw.write(Nsr_linha.toString() + "\n");
            }
        }
    }
}