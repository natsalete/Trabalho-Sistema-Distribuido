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
    private static final String ARQUIVO_CARROS = "C:\\Users\\natsa\\OneDrive\\Imagens\\cadastro_carros.txt";
    
    /**
     * Escreve um objeto Nsr_Carros no arquivo de texto.
     * @param carro O carro a ser gravado
     * @param context O contexto do servlet (não usado na versão modificada)
     * @throws IOException Se ocorrer erro na escrita do arquivo
     */
    public void escreverArqCarro(Nsr_Carros carro, ServletContext context) throws IOException {
        // Formatação para valores monetários
        DecimalFormat df = new DecimalFormat("0.00");
        
        // Formata a linha conforme o padrão: id|placa|modelo|marca|ano|combustivel|cor|quilometragem|categoria|disponivel|valorCompra|valorVenda
        StringBuilder linha = new StringBuilder();
        linha.append(carro.getId()).append("|");
        linha.append(carro.getPlaca()).append("|");
        linha.append(carro.getModelo()).append("|");
        linha.append(carro.getMarca()).append("|");
        linha.append(carro.getAno()).append("|");
        linha.append(carro.getCombustivel()).append("|");
        linha.append(carro.getCor()).append("|");
        linha.append(carro.getQuilometragem()).append("|");
        linha.append(carro.getCategoria()).append("|");
        linha.append(carro.isDisponivel()).append("|");
        linha.append(df.format(carro.getValorCompra()).replace(",", ".")).append("|");
        linha.append(df.format(carro.getValorVenda()).replace(",", "."));
        
        File f = new File(ARQUIVO_CARROS);
        
        // Verifica se o diretório pai existe e cria se necessário
        File parentDir = f.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        // Append (true) para adicionar ao final do arquivo
        try (FileWriter fw = new FileWriter(f, true)) {
            fw.write(linha.toString() + "\n");
        }
    }
    
    /**
     * Reescreve todo o arquivo com a lista atualizada de carros.
     * @param lstCarros A lista de carros a ser gravada
     * @param context O contexto do servlet (não usado na versão modificada)
     * @throws IOException Se ocorrer erro na escrita do arquivo
     */
    public void reescreverArquivo(List<Nsr_Carros> lstCarros, ServletContext context) throws IOException {
        // Formatação para valores monetários
        DecimalFormat df = new DecimalFormat("0.00");
        
        File f = new File(ARQUIVO_CARROS);
        
        // Verifica se o diretório pai existe e cria se necessário
        File parentDir = f.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        // Sobrescrever o arquivo (false = não append)
        try (FileWriter fw = new FileWriter(f, false)) {
            for (Nsr_Carros carro : lstCarros) {
                // Formata a linha conforme o padrão
                StringBuilder linha = new StringBuilder();
                linha.append(carro.getId()).append("|");
                linha.append(carro.getPlaca()).append("|");
                linha.append(carro.getModelo()).append("|");
                linha.append(carro.getMarca()).append("|");
                linha.append(carro.getAno()).append("|");
                linha.append(carro.getCombustivel()).append("|");
                linha.append(carro.getCor()).append("|");
                linha.append(carro.getQuilometragem()).append("|");
                linha.append(carro.getCategoria()).append("|");
                linha.append(carro.isDisponivel()).append("|");
                linha.append(df.format(carro.getValorCompra()).replace(",", ".")).append("|");
                linha.append(df.format(carro.getValorVenda()).replace(",", "."));
                
                fw.write(linha.toString() + "\n");
            }
        }
    }
}