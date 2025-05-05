package com.poo2.nsr_servidorsocket1_carros;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Servidor de Sincronização de Cadastro de Carros
 * Com suporte para clientes e manipulação de dados
 */
public class Nsr_ServidorSocket1_Carros extends JFrame {
    
    // Componentes da interface
    private JPanel Nsr_statusPanel, Nsr_controlPanel, Nsr_syncPanel;
    private JLabel Nsr_statusLabel, Nsr_clientCountLabel;
    private JTextField Nsr_portaField, Nsr_arquivoField;
    private JTextField Nsr_ipSincronizacaoField, Nsr_portaSincronizacaoField;
    private JButton Nsr_iniciarButton, Nsr_pararButton, Nsr_sincronizarButton, Nsr_selecionarArquivoButton;
    private JComboBox<String> Nsr_tipoSincronizacaoCombo;
    private JTextArea Nsr_logArea;
    
    // Variáveis do servidor
    private ServerSocket Nsr_serverSocket;
    private int Nsr_serverPort = 2222;
    private boolean Nsr_running = false;
    private Thread Nsr_serverThread;
    private String Nsr_arquivoDados = "cadastro_carros.txt";
    private List<Nsr_ClienteHandler> Nsr_clientesConectados;
    
    // Constantes para sincronização
    private static final int Nsr_COMANDO_SINCRONIZAR = 1;
    private static final int Nsr_COMANDO_ENVIAR_ARQUIVO = 2;
    private static final int Nsr_COMANDO_RECEBER_ARQUIVO = 3;
    
    // Constantes para comandos de cliente
    private static final int Nsr_COMANDO_PING = 10;
    private static final int Nsr_COMANDO_HELLO = 11;
    private static final int Nsr_COMANDO_GET_ALL = 20;
    private static final int Nsr_COMANDO_GET_BY_ID = 21;
    private static final int Nsr_COMANDO_ADD = 30;
    private static final int Nsr_COMANDO_UPDATE = 31;
    private static final int Nsr_COMANDO_DELETE = 32;
    
    // Constantes para formato de arquivo
    private static final String Nsr_DELIMITADOR = "|";
    
    public Nsr_ServidorSocket1_Carros() {
        Nsr_clientesConectados = new CopyOnWriteArrayList<>();
        Nsr_initComponents();
        Nsr_verificarArquivoDados();
    }
    
    /**
     * Verifica se o arquivo de dados existe e cria um arquivo de exemplo se necessário
     */
    private void Nsr_verificarArquivoDados() {
        File Nsr_arquivo = new File(Nsr_arquivoDados);
        if (!Nsr_arquivo.exists()) {
            try {
                Nsr_arquivo.createNewFile();
                System.out.println("Arquivo de dados criado: " + Nsr_arquivoDados);
                
                // Adicionar dados de exemplo ao arquivo
                Nsr_criarArquivoExemplo(Nsr_arquivo);
                
            } catch (IOException e) {
                System.err.println("Erro ao criar arquivo de dados: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Erro ao criar arquivo de dados: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Cria um arquivo de exemplo com dados de carros
     */
    private void Nsr_criarArquivoExemplo(File arquivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivo))) {
            // Adicionar alguns carros de exemplo
            writer.println("1|ABC1234|Civic|Honda|2022|GASOLINA|Prata|15000.5|SEDAN|true|85000.00|98500.00");
            writer.println("2|DEF5678|Corolla|Toyota|2021|HIBRIDO|Branco|22350.8|SEDAN|true|92000.00|105000.00");
            writer.println("3|GHI9012|HB20|Hyundai|2020|FLEX|Vermelho|35600.2|HATCHBACK|true|55000.00|62500.00");
            writer.println("4|JKL3456|Tucson|Hyundai|2019|DIESEL|Preto|48900.0|SUV|false|72000.00|81000.00");
            writer.println("5|MNO7890|Hilux|Toyota|2023|DIESEL|Prata|8500.3|PICKUP|true|155000.00|175000.00");
            writer.println("6|PQR1357|Onix|Chevrolet|2021|FLEX|Azul|28700.0|HATCHBACK|true|58000.00|67500.00");
            writer.println("7|STU2468|City|Honda|2022|GASOLINA|Cinza|18900.5|SEDAN|true|79000.00|88000.00");
            writer.println("8|VWX3579|Compass|Jeep|2023|FLEX|Branco|12800.0|SUV|true|145000.00|162000.00");
            writer.println("9|YZA4680|Strada|Fiat|2020|ALCOOL|Vermelho|42500.0|PICKUP|false|62000.00|69500.00");
            writer.println("10|BCD5791|Model 3|Tesla|2023|ELETRICO|Azul|9800.0|SEDAN|true|280000.00|310000.00");
            
            System.out.println("Arquivo de exemplo criado com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo de exemplo: " + e.getMessage());
        }
    }
    
    /**
     * Inicializa os componentes da interface gráfica
     */
    private void Nsr_initComponents() {
        setTitle("Servidor de Sincronização de Cadastro de Carros");
        setSize(750, 500); // Aumentei um pouco para acomodar o log
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        // Painel de controle (parte superior)
        Nsr_controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        Nsr_controlPanel.setBorder(BorderFactory.createTitledBorder("Controles do Servidor"));
        
        // Campo para porta
        JLabel portaLabel = new JLabel("Porta:");
        Nsr_portaField = new JTextField(Integer.toString(Nsr_serverPort), 5);
        
        // Campo para arquivo de dados
        JLabel arquivoLabel = new JLabel("Arquivo de Dados:");
        Nsr_arquivoField = new JTextField(Nsr_arquivoDados, 30);
        
        // Botão para selecionar arquivo
        Nsr_selecionarArquivoButton = new JButton("...");
        Nsr_selecionarArquivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Selecionar Arquivo de Dados");
                if (fileChooser.showOpenDialog(Nsr_ServidorSocket1_Carros.this) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Nsr_arquivoField.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        
        // Botões
        Nsr_iniciarButton = new JButton("Iniciar Servidor");
        Nsr_iniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Nsr_serverPort = Integer.parseInt(Nsr_portaField.getText().trim());
                    Nsr_arquivoDados = Nsr_arquivoField.getText().trim();
                    Nsr_verificarArquivoDados();
                    Nsr_startServer();
                    Nsr_toggleControls(false);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Nsr_ServidorSocket1_Carros.this, 
                            "A porta deve ser um número válido", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        Nsr_pararButton = new JButton("Parar Servidor");
        Nsr_pararButton.setEnabled(false);
        Nsr_pararButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Nsr_stopServer();
                Nsr_toggleControls(true);
            }
        });
        
        Nsr_controlPanel.add(portaLabel);
        Nsr_controlPanel.add(Nsr_portaField);
        Nsr_controlPanel.add(arquivoLabel);
        Nsr_controlPanel.add(Nsr_arquivoField);
        Nsr_controlPanel.add(Nsr_selecionarArquivoButton);
        Nsr_controlPanel.add(Nsr_iniciarButton);
        Nsr_controlPanel.add(Nsr_pararButton);
        
        // Painel de sincronização (parte central)
        Nsr_syncPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        Nsr_syncPanel.setBorder(BorderFactory.createTitledBorder("Sincronização com outro Servidor"));
        
        JLabel ipLabel = new JLabel("IP do Servidor:");
        Nsr_ipSincronizacaoField = new JTextField("127.0.0.1", 15);
        
        JLabel portaDestLabel = new JLabel("Porta:");
        Nsr_portaSincronizacaoField = new JTextField("2223", 5);
        
        JLabel tipoSincLabel = new JLabel("Tipo de Sincronização:");
        Nsr_tipoSincronizacaoCombo = new JComboBox<>(new String[]{"Unilateral (Enviar)", "Unilateral (Receber)", "Bilateral"});
        
        Nsr_sincronizarButton = new JButton("Conectar e Sincronizar");
        Nsr_sincronizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Nsr_sincronizarComOutroServidor();
            }
        });
        
        Nsr_syncPanel.add(ipLabel);
        Nsr_syncPanel.add(Nsr_ipSincronizacaoField);
        Nsr_syncPanel.add(portaDestLabel);
        Nsr_syncPanel.add(Nsr_portaSincronizacaoField);
        Nsr_syncPanel.add(tipoSincLabel);
        Nsr_syncPanel.add(Nsr_tipoSincronizacaoCombo);
        Nsr_syncPanel.add(Nsr_sincronizarButton);
        
        // Área de log
        Nsr_logArea = new JTextArea();
        Nsr_logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(Nsr_logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log de Atividades"));
        scrollPane.setPreferredSize(new Dimension(700, 150));
        
        // Painel de status (parte inferior)
        Nsr_statusPanel = new JPanel(new BorderLayout(5, 5));
        Nsr_statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel leftStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Nsr_statusLabel = new JLabel("Status: Desconectado");
        Nsr_clientCountLabel = new JLabel("Clientes: 0");
        leftStatusPanel.add(Nsr_statusLabel);
        leftStatusPanel.add(Box.createHorizontalStrut(20));
        leftStatusPanel.add(Nsr_clientCountLabel);

        Nsr_statusPanel.add(leftStatusPanel, BorderLayout.WEST);

        // Adiciona componentes ao frame
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(Nsr_controlPanel, BorderLayout.NORTH);
        mainPanel.add(Nsr_syncPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        add(Nsr_statusPanel, BorderLayout.SOUTH);

        // Lida com o fechamento da janela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Nsr_stopServer();
            }
        });
    }
    
    /**
     * Adiciona uma mensagem ao log
     */
    private void Nsr_adicionarLog(String mensagem) {
        SwingUtilities.invokeLater(() -> {
            Nsr_logArea.append(mensagem + "\n");
            // Auto-scroll para a última linha
            Nsr_logArea.setCaretPosition(Nsr_logArea.getDocument().getLength());
        });
        System.out.println(mensagem);
    }
    
    /**
     * Habilita/desabilita controles baseado no estado do servidor
     */
    private void Nsr_toggleControls(boolean enabled) {
        Nsr_portaField.setEnabled(enabled);
        Nsr_arquivoField.setEnabled(enabled);
        Nsr_selecionarArquivoButton.setEnabled(enabled);
        Nsr_iniciarButton.setEnabled(enabled);
        Nsr_pararButton.setEnabled(!enabled);
    }
    
    /**
     * Inicia o servidor na porta especificada
     */
    private void Nsr_startServer() {
        Nsr_serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Nsr_serverSocket = new ServerSocket(Nsr_serverPort);
                    Nsr_running = true;
                    Nsr_updateStatus("Servidor iniciado na porta " + Nsr_serverPort);
                    Nsr_adicionarLog("Servidor iniciado na porta " + Nsr_serverPort);
                    Nsr_adicionarLog("Utilizando arquivo: " + Nsr_arquivoDados);

                    while (Nsr_running) {
                        try {
                            Socket conexao = Nsr_serverSocket.accept();
                            Nsr_adicionarLog("Nova conexão aceita: " + conexao.getInetAddress().getHostAddress());

                            // Inicia thread para lidar com o cliente
                            Nsr_ClienteHandler clienteHandler = new Nsr_ClienteHandler(conexao);
                            Nsr_clientesConectados.add(clienteHandler);
                            Nsr_updateClientCount();
                            clienteHandler.start();
                        } catch (IOException e) {
                            if (Nsr_running) {
                                Nsr_adicionarLog("Erro ao aceitar conexão: " + e.getMessage());
                            }
                        }
                    }
                } catch (IOException e) {
                    Nsr_adicionarLog("Erro ao iniciar servidor: " + e.getMessage());
                    Nsr_updateStatus("Erro no servidor");
                    SwingUtilities.invokeLater(() -> Nsr_toggleControls(true));
                    JOptionPane.showMessageDialog(Nsr_ServidorSocket1_Carros.this, 
                        "Erro ao iniciar servidor: " + e.getMessage(), 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        Nsr_serverThread.start();
    }

    /**
     * Para o servidor e fecha todas as conexões
     */
    private void Nsr_stopServer() {
        Nsr_running = false;
        try {
            // Fechar todas as conexões de clientes
            for (Nsr_ClienteHandler cliente : Nsr_clientesConectados) {
                cliente.Nsr_fecharConexao();
            }
            Nsr_clientesConectados.clear();
            Nsr_updateClientCount();
            
            if (Nsr_serverSocket != null && !Nsr_serverSocket.isClosed()) {
                Nsr_serverSocket.close();
                Nsr_adicionarLog("Servidor encerrado");
                Nsr_updateStatus("Servidor desconectado");
            }
        } catch (IOException e) {
            Nsr_adicionarLog("Erro ao encerrar servidor: " + e.getMessage());
        }
    }
    
    /**
     * Sincroniza dados com outro servidor
     */
    private void Nsr_sincronizarComOutroServidor() {
        String ip = Nsr_ipSincronizacaoField.getText().trim();
        int Nsr_porta;
        
        try {
            Nsr_porta = Integer.parseInt(Nsr_portaSincronizacaoField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "A porta deve ser um número válido", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int Nsr_tipoSincronizacao = Nsr_tipoSincronizacaoCombo.getSelectedIndex();
        
        // Mostrar diálogo de progresso
        JDialog progressDialog = new JDialog(this, "Sincronização", true);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        JLabel Nsr_statusLabel = new JLabel("Conectando ao servidor remoto...");
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(Nsr_statusLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        
        progressDialog.add(panel);
        progressDialog.setSize(300, 100);
        progressDialog.setLocationRelativeTo(this);
        
        // Executar sincronização em thread separada
        new Thread(() -> {
            try {
                Nsr_adicionarLog("Conectando ao servidor " + ip + ":" + Nsr_porta);
                Socket socket = new Socket(ip, Nsr_porta);
                
                SwingUtilities.invokeLater(() -> Nsr_statusLabel.setText("Sincronizando dados..."));
                
                DataInputStream entrada = new DataInputStream(socket.getInputStream());
                DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
                
                // Enviar comando de sincronização
                saida.writeInt(Nsr_COMANDO_SINCRONIZAR);
                saida.writeInt(Nsr_tipoSincronizacao);
                
                switch (Nsr_tipoSincronizacao) {
                    case 0: // Unilateral (Enviar)
                        Nsr_adicionarLog("Enviando dados para servidor remoto...");
                        Nsr_enviarArquivo(saida, Nsr_arquivoDados);
                        break;
                    case 1: // Unilateral (Receber)
                        Nsr_adicionarLog("Recebendo dados do servidor remoto...");
                        Nsr_receberArquivo(entrada, Nsr_arquivoDados);
                        break;
                    case 2: // Bilateral
                        Nsr_adicionarLog("Iniciando sincronização bilateral...");
                        Nsr_enviarArquivo(saida, Nsr_arquivoDados);
                        Nsr_receberArquivo(entrada, Nsr_arquivoDados + ".temp");
                        
                        // Mesclar os arquivos
                        List<String> dadosOriginais = Nsr_lerArquivo(Nsr_arquivoDados);
                        List<String> dadosRecebidos = Nsr_lerArquivo(Nsr_arquivoDados + ".temp");
                        
                        // Mesclar e eliminar duplicações (baseado em ID)
                        List<String> dadosMesclados = Nsr_mesclarDados(dadosOriginais, dadosRecebidos);
                        
                        // Salvar arquivo mesclado
                        Nsr_salvarArquivo(dadosMesclados, Nsr_arquivoDados);
                        
                        // Excluir arquivo temporário
                        File tempFile = new File(Nsr_arquivoDados + ".temp");
                        if (tempFile.exists()) {
                            tempFile.delete();
                        }
                        break;
                }
                
                socket.close();
                Nsr_adicionarLog("Sincronização concluída com sucesso");
                
                // Fechar diálogo e mostrar mensagem de sucesso
                SwingUtilities.invokeLater(() -> {
                    progressDialog.dispose();
                    JOptionPane.showMessageDialog(Nsr_ServidorSocket1_Carros.this, 
                            "Sincronização concluída com sucesso!", 
                            "Sincronização", JOptionPane.INFORMATION_MESSAGE);
                });
                
            } catch (IOException e) {
                Nsr_adicionarLog("Falha na sincronização: " + e.getMessage());
                
                // Fechar diálogo e mostrar erro
                SwingUtilities.invokeLater(() -> {
                    progressDialog.dispose();
                    JOptionPane.showMessageDialog(Nsr_ServidorSocket1_Carros.this, 
                            "Erro ao sincronizar: " + e.getMessage(), 
                            "Erro de Sincronização", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
        
        // Exibir diálogo de progresso
        progressDialog.setVisible(true);
    }
    
    /**
     * Envia um arquivo para o servidor remoto
     */
    private void Nsr_enviarArquivo(DataOutputStream saida, String nomeArquivo) throws IOException {
        File Nsr_arquivo = new File(nomeArquivo);
        
        if (!Nsr_arquivo.exists()) {
            // Enviar tamanho zero
            saida.writeLong(0);
            return;
        }
        
        // Enviar tamanho do arquivo
        saida.writeLong(Nsr_arquivo.length());
        
        // Enviar conteúdo do arquivo
        FileInputStream fileIn = new FileInputStream(Nsr_arquivo);
        byte[] buffer = new byte[4096];
        int bytesRead;
        
        while ((bytesRead = fileIn.read(buffer)) != -1) {
            saida.write(buffer, 0, bytesRead);
        }
        
        saida.flush();
        fileIn.close();
    }
    
    /**
     * Recebe um arquivo do servidor remoto
     */
    private void Nsr_receberArquivo(DataInputStream entrada, String nomeArquivo) throws IOException {
        // Receber tamanho do arquivo
        long tamanhoArquivo = entrada.readLong();
        
        if (tamanhoArquivo == 0) {
            // Arquivo vazio ou inexistente no servidor remoto
            Nsr_adicionarLog("Arquivo vazio ou inexistente no servidor remoto");
            return;
        }
        
        // Receber conteúdo do arquivo
        FileOutputStream fileOut = new FileOutputStream(nomeArquivo);
        byte[] buffer = new byte[4096];
        long bytesRestantes = tamanhoArquivo;
        int bytesRead;
        
        while (bytesRestantes > 0 && (bytesRead = entrada.read(buffer, 0, (int)Math.min(buffer.length, bytesRestantes))) != -1) {
            fileOut.write(buffer, 0, bytesRead);
            bytesRestantes -= bytesRead;
        }
        
        fileOut.close();
    }
    
    /**
     * Lê o conteúdo de um arquivo e retorna como lista de strings
     */
    private List<String> Nsr_lerArquivo(String nomeArquivo) throws IOException {
        List<String> linhas = new ArrayList<>();
        File arquivo = new File(nomeArquivo);
        
        if (!arquivo.exists()) {
            return linhas;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linhas.add(linha);
            }
        }
        
        return linhas;
    }
    
    /**
     * Salva uma lista de strings em um arquivo
     */
    private void Nsr_salvarArquivo(List<String> linhas, String nomeArquivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            for (String linha : linhas) {
                writer.println(linha);
            }
        }
    }
    
    /**
     * Mescla dois conjuntos de dados, eliminando duplicações com base no ID
     */
    private List<String> Nsr_mesclarDados(List<String> dados1, List<String> dados2) {
        List<String> dadosMesclados = new ArrayList<>();
        
        // Manter track dos IDs já processados
        List<String> idsProcessados = new ArrayList<>();
        
        // Processar dados do primeiro conjunto
        for (String linha : dados1) {
            String[] campos = linha.split("\\|");
            if (campos.length > 0) {
                String id = campos[0].trim();
                if (!idsProcessados.contains(id)) {
                    dadosMesclados.add(linha);
                    idsProcessados.add(id);
                }
            }
        }
        
        // Processar dados do segundo conjunto
        for (String linha : dados2) {
            String[] campos = linha.split("\\|");
            if (campos.length > 0) {
                String id = campos[0].trim();
                if (!idsProcessados.contains(id)) {
                    dadosMesclados.add(linha);
                    idsProcessados.add(id);
                }
            }
        }
        
        return dadosMesclados;
    }

    /**
     * Atualiza o status do servidor na interface
     */
    private void Nsr_updateStatus(String status) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Nsr_statusLabel.setText("Status: " + status);
            }
        });
    }
    
    /**
     * Atualiza a contagem de clientes na interface
     */
    private void Nsr_updateClientCount() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Nsr_clientCountLabel.setText("Clientes: " + Nsr_clientesConectados.size());
            }
        });
    }
    
    /**
     * Classe para representar um carro
     */
    private class Carro {
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
        
        public Carro(String linha) {
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
    
    /**
     * Classe para manipular as operações com o arquivo de dados
     */
    private class Nsr_ArquivoManager {
        
        /**
         * Obtém todos os registros de carros do arquivo
         */
        public List<Carro> Nsr_obterTodosCarros() {
            List<Carro> carros = new ArrayList<>();
            try {
                List<String> linhas = Nsr_lerArquivo(Nsr_arquivoDados);
                for (String linha : linhas) {
                    carros.add(new Carro(linha));
                }
                Nsr_adicionarLog("Lidos " + carros.size() + " carros do arquivo");
            } catch (IOException e) {
                Nsr_adicionarLog("Erro ao ler arquivo de dados: " + e.getMessage());
            }
            return carros;
        }
        
        /**
         * Obtém um carro pelo ID
         */
        public Carro Nsr_obterCarroPorId(int id) {
            try {
                List<String> linhas = Nsr_lerArquivo(Nsr_arquivoDados);
                for (String linha : linhas) {
                    Carro carro = new Carro(linha);
                    if (carro.getId() == id) {
                        return carro;
                    }
                }
            } catch (IOException e) {
                Nsr_adicionarLog("Erro ao ler arquivo de dados: " + e.getMessage());
            }
            return null;
        }
        
        /**
         * Adiciona um novo carro ao arquivo
         */
        public boolean Nsr_adicionarCarro(Carro novoCarro) {
            try {
                // Verificar se já existe um carro com esse ID
                Carro existente = Nsr_obterCarroPorId(novoCarro.getId());
                if (existente != null) {
                    return false; // ID já existe
                }
                
                // Adicionar ao arquivo
                List<String> linhas = Nsr_lerArquivo(Nsr_arquivoDados);
                linhas.add(novoCarro.paraString());
                Nsr_salvarArquivo(linhas, Nsr_arquivoDados);
                Nsr_adicionarLog("Carro ID " + novoCarro.getId() + " adicionado");
                return true;
            } catch (IOException e) {
                Nsr_adicionarLog("Erro ao adicionar carro: " + e.getMessage());
                return false;
            }
        }
        
        /**
         * Atualiza um carro existente
         */
        public boolean Nsr_atualizarCarro(Carro carroAtualizado) {
            try {
                List<String> linhas = Nsr_lerArquivo(Nsr_arquivoDados);
                List<String> novasLinhas = new ArrayList<>();
                boolean encontrado = false;
                
                for (String linha : linhas) {
                    Carro carro = new Carro(linha);
                    if (carro.getId() == carroAtualizado.getId()) {
                        novasLinhas.add(carroAtualizado.paraString());
                        encontrado = true;
                    } else {
                        novasLinhas.add(linha);
                    }
                }
                
                if (encontrado) {
                    Nsr_salvarArquivo(novasLinhas, Nsr_arquivoDados);
                    Nsr_adicionarLog("Carro ID " + carroAtualizado.getId() + " atualizado");
                    return true;
                }
                
                return false;
            } catch (IOException e) {
                Nsr_adicionarLog("Erro ao atualizar carro: " + e.getMessage());
                return false;
            }
        }
        
        /**
         * Remove um carro pelo ID
         */
        public boolean Nsr_removerCarro(int id) {
            try {
                List<String> linhas = Nsr_lerArquivo(Nsr_arquivoDados);
                List<String> novasLinhas = new ArrayList<>();
                boolean removido = false;
                
                for (String linha : linhas) {
                    Carro carro = new Carro(linha);
                    if (carro.getId() != id) {
                        novasLinhas.add(linha);
                    } else {
                        removido = true;
                    }
                }
                
                if (removido) {
                    Nsr_salvarArquivo(novasLinhas, Nsr_arquivoDados);
                    Nsr_adicionarLog("Carro ID " + id + " removido");
                    return true;
                }
                
                return false;
            } catch (IOException e) {
                Nsr_adicionarLog("Erro ao remover carro: " + e.getMessage());
                return false;
            }
        }
    }
    
    /**
     * Classe para lidar com as conexões dos clientes
     */
    private class Nsr_ClienteHandler extends Thread {
        private Socket Nsr_socket;
        private DataInputStream Nsr_entrada;
        private DataOutputStream Nsr_saida;
        private boolean Nsr_conectado = false;
        private Nsr_ArquivoManager Nsr_arquivoManager;
        
        public Nsr_ClienteHandler(Socket socket) {
            this.Nsr_socket = socket;
            this.Nsr_arquivoManager = new Nsr_ArquivoManager();
            try {
                Nsr_entrada = new DataInputStream(socket.getInputStream());
                Nsr_saida = new DataOutputStream(socket.getOutputStream());
                Nsr_conectado = true;
            } catch (IOException e) {
                Nsr_adicionarLog("Erro ao inicializar streams: " + e.getMessage());
            }
        }
        
        @Override
        public void run() {
            try {
                while (Nsr_conectado && Nsr_running) {
                    // Ler comando do cliente
                    int comando = Nsr_entrada.readInt();
                    processarComando(comando);
                }
            } catch (IOException e) {
                // Pode ocorrer quando o cliente desconecta abruptamente
                Nsr_adicionarLog("Cliente desconectado: " + e.getMessage());
            } finally {
                Nsr_fecharConexao();
                Nsr_clientesConectados.remove(this);
                Nsr_updateClientCount();
            }
        }
        
        /**
         * Processa os comandos recebidos do cliente
         */
        private void processarComando(int comando) throws IOException {
            switch (comando) {
                case Nsr_COMANDO_PING:
                    // Responder ao ping
                    Nsr_saida.writeBoolean(true);
                    Nsr_adicionarLog("Comando PING recebido e respondido");
                    break;
                    
                case Nsr_COMANDO_HELLO:
                    // Responder com uma mensagem de saudação
                    String mensagem = "Bem-vindo ao Servidor de Cadastro de Carros!";
                    Nsr_saida.writeUTF(mensagem);
                    Nsr_adicionarLog("Comando HELLO recebido e respondido");
                    break;
                    
                case Nsr_COMANDO_GET_ALL:
                    // Enviar todos os carros
                    List<Carro> carros = Nsr_arquivoManager.Nsr_obterTodosCarros();
                    Nsr_saida.writeInt(carros.size());
                    for (Carro carro : carros) {
                        Nsr_saida.writeUTF(carro.paraString());
                    }
                    Nsr_adicionarLog("Comando GET_ALL: enviados " + carros.size() + " carros");
                    break;
                    
                case Nsr_COMANDO_GET_BY_ID:
                    // Obter carro pelo ID
                    int id = Nsr_entrada.readInt();
                    Carro carro = Nsr_arquivoManager.Nsr_obterCarroPorId(id);
                    if (carro != null) {
                        Nsr_saida.writeBoolean(true);
                        Nsr_saida.writeUTF(carro.paraString());
                    } else {
                        Nsr_saida.writeBoolean(false);
                    }
                    Nsr_adicionarLog("Comando GET_BY_ID: consultado ID " + id);
                    break;
                    
                case Nsr_COMANDO_ADD:
                    // Adicionar novo carro
                    String dadosNovoCarro = Nsr_entrada.readUTF();
                    Carro novoCarro = new Carro(dadosNovoCarro);
                    boolean adicionado = Nsr_arquivoManager.Nsr_adicionarCarro(novoCarro);
                    Nsr_saida.writeBoolean(adicionado);
                    Nsr_adicionarLog("Comando ADD: carro " + (adicionado ? "adicionado" : "não adicionado"));
                    break;
                    
                case Nsr_COMANDO_UPDATE:
                    // Atualizar carro existente
                    String dadosAtualizar = Nsr_entrada.readUTF();
                    Carro carroAtualizar = new Carro(dadosAtualizar);
                    boolean atualizado = Nsr_arquivoManager.Nsr_atualizarCarro(carroAtualizar);
                    Nsr_saida.writeBoolean(atualizado);
                    Nsr_adicionarLog("Comando UPDATE: carro ID " + carroAtualizar.getId() + 
                                   " " + (atualizado ? "atualizado" : "não encontrado"));
                    break;
                    
                case Nsr_COMANDO_DELETE:
                    // Remover carro pelo ID
                    int idRemover = Nsr_entrada.readInt();
                    boolean removido = Nsr_arquivoManager.Nsr_removerCarro(idRemover);
                    Nsr_saida.writeBoolean(removido);
                    Nsr_adicionarLog("Comando DELETE: carro ID " + idRemover + 
                                   " " + (removido ? "removido" : "não encontrado"));
                    break;
                    
                case Nsr_COMANDO_SINCRONIZAR:
                    // Lidar com solicitação de sincronização de outro servidor
                    int tipoSincronizacao = Nsr_entrada.readInt();
                    Nsr_adicionarLog("Solicitação de sincronização recebida, tipo: " + tipoSincronizacao);
                    
                    switch (tipoSincronizacao) {
                        case 0: // Unilateral (Receber)
                            Nsr_adicionarLog("Recebendo arquivo do servidor remoto...");
                            Nsr_receberArquivo(Nsr_entrada, Nsr_arquivoDados + ".temp");
                            
                            // Mesclar ou substituir
                            File tempFile = new File(Nsr_arquivoDados + ".temp");
                            File destFile = new File(Nsr_arquivoDados);
                            
                            if (tempFile.exists() && tempFile.length() > 0) {
                                if (destFile.exists()) {
                                    destFile.delete();
                                }
                                tempFile.renameTo(destFile);
                            }
                            break;
                            
                        case 1: // Unilateral (Enviar)
                            Nsr_adicionarLog("Enviando arquivo para o servidor remoto...");
                            Nsr_enviarArquivo(Nsr_saida, Nsr_arquivoDados);
                            break;
                            
                        case 2: // Bilateral
                            Nsr_adicionarLog("Realizando sincronização bilateral...");
                            Nsr_receberArquivo(Nsr_entrada, Nsr_arquivoDados + ".temp");
                            Nsr_enviarArquivo(Nsr_saida, Nsr_arquivoDados);
                            
                            // Mesclar os arquivos
                            List<String> dadosOriginais = Nsr_lerArquivo(Nsr_arquivoDados);
                            List<String> dadosRecebidos = Nsr_lerArquivo(Nsr_arquivoDados + ".temp");
                            
                            // Mesclar e eliminar duplicações
                            List<String> dadosMesclados = Nsr_mesclarDados(dadosOriginais, dadosRecebidos);
                            
                            // Salvar arquivo mesclado
                            Nsr_salvarArquivo(dadosMesclados, Nsr_arquivoDados);
                            
                            // Excluir arquivo temporário
                            tempFile = new File(Nsr_arquivoDados + ".temp");
                            if (tempFile.exists()) {
                                tempFile.delete();
                            }
                            break;
                    }
                    break;
                    
                default:
                    Nsr_adicionarLog("Comando desconhecido recebido: " + comando);
                    break;
            }
        }
        
        /**
         * Fecha a conexão com o cliente
         */
        public void Nsr_fecharConexao() {
            try {
                Nsr_conectado = false;
                if (Nsr_entrada != null) Nsr_entrada.close();
                if (Nsr_saida != null) Nsr_saida.close();
                if (Nsr_socket != null && !Nsr_socket.isClosed()) Nsr_socket.close();
            } catch (IOException e) {
                Nsr_adicionarLog("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
    
    /**
     * Método principal
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Nsr_ServidorSocket1_Carros().setVisible(true);
            }
        });
    }
}