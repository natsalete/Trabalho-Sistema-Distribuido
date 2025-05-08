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
import java.util.Timer;
import java.util.TimerTask;

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
    private JCheckBox Nsr_sincronizacaoAutomaticaCheckBox;
    private JSpinner Nsr_intervaloSincronizacaoSpinner;
    
    // Variáveis do servidor
    private ServerSocket Nsr_serverSocket;
    private int Nsr_serverPort = 2222;
    private boolean Nsr_running = false;
    private Thread Nsr_serverThread;
    private String Nsr_arquivoDados = "cadastro_carros.txt";
    private List<Nsr_ClienteHandler> Nsr_clientesConectados;
    
    // Variáveis para sincronização automática
    private boolean Nsr_sincronizacaoAutomaticaAtiva = false;
    private Timer Nsr_timerSincronizacao;
    private int Nsr_intervaloSincronizacao = 30; // segundos
    private String Nsr_ipServidorRemoto = "127.0.0.1";
    private int Nsr_portaServidorRemoto = 2223;
    private int Nsr_tipoSincronizacaoAtual = 0;
    
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
    
     /**
     * Variáveis necessárias para sincronização automática
     */
    private Timer Nsr_timerSincronizacao;
    private boolean Nsr_sincronizacaoAutomaticaAtiva;
    private int Nsr_intervaloSincronizacao;
    private String Nsr_ipServidorRemoto;
    private int Nsr_portaServidorRemoto;
    private int Nsr_tipoSincronizacaoAtual;

    // Componentes da interface de sincronização
    private JPanel Nsr_syncPanel;
    private JTextField Nsr_ipSincronizacaoField;
    private JTextField Nsr_portaSincronizacaoField;
    private JComboBox<String> Nsr_tipoSincronizacaoCombo;
    private JTextField Nsr_intervaloSincronizacaoField;
    private JCheckBox Nsr_habilitarSincAutomaticaCheck;

    // Constantes para formato de arquivo
    private static final String Nsr_DELIMITADOR = "|";
    
    public Nsr_ServidorSocket1_Carros() {
        Nsr_clientesConectados = new CopyOnWriteArrayList<>();
        Nsr_initComponents();
    }
    
    /**
     * Inicializa os componentes da interface gráfica
     */
    private void Nsr_initComponents() {
        setTitle("Servidor de Sincronização de Cadastro de Carros");
        setSize(800, 550); // Aumentei um pouco para acomodar o log e os novos controles
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        // Painel de controle (parte superior)
        Nsr_controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        Nsr_controlPanel.setBorder(BorderFactory.createTitledBorder("Controles do Servidor"));
        
        // Campo para porta
        JLabel Nsr_portaLabel = new JLabel("Porta:");
        Nsr_portaField = new JTextField(Integer.toString(Nsr_serverPort), 5);
        
        // Campo para arquivo de dados
        JLabel Nsr_arquivoLabel = new JLabel("Arquivo de Dados:");
        Nsr_arquivoField = new JTextField(Nsr_arquivoDados, 30);
        
        // Botão para selecionar arquivo
        Nsr_selecionarArquivoButton = new JButton("...");
        Nsr_selecionarArquivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser Nsr_fileChooser = new JFileChooser();
                Nsr_fileChooser.setDialogTitle("Selecionar Arquivo de Dados");
                if (Nsr_fileChooser.showOpenDialog(Nsr_ServidorSocket1_Carros.this) == JFileChooser.APPROVE_OPTION) {
                    File Nsr_selectedFile = Nsr_fileChooser.getSelectedFile();
                    Nsr_arquivoField.setText(Nsr_selectedFile.getAbsolutePath());
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
                    
                    // Verificar se o arquivo existe
                    File Nsr_arquivo = new File(Nsr_arquivoDados);
                    if (!Nsr_arquivo.exists()) {
                        JOptionPane.showMessageDialog(Nsr_ServidorSocket1_Carros.this, 
                            "Arquivo de dados não encontrado. Por favor, selecione um arquivo existente.", 
                            "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
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
        
        Nsr_controlPanel.add(Nsr_portaLabel);
        Nsr_controlPanel.add(Nsr_portaField);
        Nsr_controlPanel.add(Nsr_arquivoLabel);
        Nsr_controlPanel.add(Nsr_arquivoField);
        Nsr_controlPanel.add(Nsr_selecionarArquivoButton);
        Nsr_controlPanel.add(Nsr_iniciarButton);
        Nsr_controlPanel.add(Nsr_pararButton);
        // Painel de sincronização (parte central)
        Nsr_syncPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        Nsr_syncPanel.setBorder(BorderFactory.createTitledBorder("Sincronização com outro Servidor"));
        
        JPanel Nsr_syncServerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel Nsr_ipLabel = new JLabel("IP do Servidor:");
        Nsr_ipSincronizacaoField = new JTextField("127.0.0.1", 15);
        
        JLabel Nsr_portaDestLabel = new JLabel("Porta:");
        Nsr_portaSincronizacaoField = new JTextField("2223", 5);
        
        JLabel Nsr_tipoSincLabel = new JLabel("Tipo de Sincronização:");
        Nsr_tipoSincronizacaoCombo = new JComboBox<>(new String[]{"Unilateral (Enviar)", "Unilateral (Receber)", "Bilateral"});
        
        Nsr_sincronizarButton = new JButton("Conectar e Sincronizar");
        Nsr_sincronizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Nsr_sincronizarComOutroServidor();
            }
        });
        
        Nsr_syncServerPanel.add(Nsr_ipLabel);
        Nsr_syncServerPanel.add(Nsr_ipSincronizacaoField);
        Nsr_syncServerPanel.add(Nsr_portaDestLabel);
        Nsr_syncServerPanel.add(Nsr_portaSincronizacaoField);
        Nsr_syncServerPanel.add(Nsr_tipoSincLabel);
        Nsr_syncServerPanel.add(Nsr_tipoSincronizacaoCombo);
        Nsr_syncServerPanel.add(Nsr_sincronizarButton);
        
        // Painel para sincronização automática
        JPanel Nsr_autoSyncPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        Nsr_sincronizacaoAutomaticaCheckBox = new JCheckBox("Sincronização Automática");
        Nsr_sincronizacaoAutomaticaCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean Nsr_isSelected = Nsr_sincronizacaoAutomaticaCheckBox.isSelected();
                Nsr_intervaloSincronizacaoSpinner.setEnabled(Nsr_isSelected);
                
                // Salvar configurações atuais
                Nsr_ipServidorRemoto = Nsr_ipSincronizacaoField.getText().trim();
                Nsr_portaServidorRemoto = Integer.parseInt(Nsr_portaSincronizacaoField.getText().trim());
                Nsr_tipoSincronizacaoAtual = Nsr_tipoSincronizacaoCombo.getSelectedIndex();
                
                if (Nsr_isSelected && Nsr_running) {
                    Nsr_iniciarSincronizacaoAutomatica();
                } else {
                    Nsr_pararSincronizacaoAutomatica();
                }
            }
        });
        
        // Spinner para configurar o intervalo de sincronização
        JLabel Nsr_intervaloLabel = new JLabel("Intervalo (segundos):");
        SpinnerNumberModel Nsr_spinnerModel = new SpinnerNumberModel(30, 5, 3600, 5);
        Nsr_intervaloSincronizacaoSpinner = new JSpinner(Nsr_spinnerModel);
        Nsr_intervaloSincronizacaoSpinner.setEnabled(false);
        Nsr_intervaloSincronizacaoSpinner.addChangeListener(e -> {
            Nsr_intervaloSincronizacao = (Integer) Nsr_intervaloSincronizacaoSpinner.getValue();
            if (Nsr_sincronizacaoAutomaticaAtiva) {
                // Reiniciar o timer com o novo intervalo
                Nsr_pararSincronizacaoAutomatica();
                Nsr_iniciarSincronizacaoAutomatica();
            }
        });
        
        Nsr_autoSyncPanel.add(Nsr_sincronizacaoAutomaticaCheckBox);
        Nsr_autoSyncPanel.add(Nsr_intervaloLabel);
        Nsr_autoSyncPanel.add(Nsr_intervaloSincronizacaoSpinner);
        
        // Status da sincronização
        JPanel Nsr_syncStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel Nsr_lastSyncLabel = new JLabel("Última sincronização: Nunca");
        Nsr_syncStatusPanel.add(Nsr_lastSyncLabel);
        
        Nsr_syncPanel.add(Nsr_syncServerPanel);
        Nsr_syncPanel.add(Nsr_autoSyncPanel);
        Nsr_syncPanel.add(Nsr_syncStatusPanel);
        
        // Área de log
        Nsr_logArea = new JTextArea();
        Nsr_logArea.setEditable(false);
        JScrollPane Nsr_scrollPane = new JScrollPane(Nsr_logArea);
        Nsr_scrollPane.setBorder(BorderFactory.createTitledBorder("Log de Atividades"));
        Nsr_scrollPane.setPreferredSize(new Dimension(700, 150));
        
        // Painel de status (parte inferior)
        Nsr_statusPanel = new JPanel(new BorderLayout(5, 5));
        Nsr_statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel Nsr_leftStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Nsr_statusLabel = new JLabel("Status: Desconectado");
        Nsr_clientCountLabel = new JLabel("Clientes: 0");
        Nsr_leftStatusPanel.add(Nsr_statusLabel);
        Nsr_leftStatusPanel.add(Box.createHorizontalStrut(20));
        Nsr_leftStatusPanel.add(Nsr_clientCountLabel);

        Nsr_statusPanel.add(Nsr_leftStatusPanel, BorderLayout.WEST);

        // Adiciona componentes ao frame
        JPanel Nsr_mainPanel = new JPanel(new BorderLayout());
        Nsr_mainPanel.add(Nsr_controlPanel, BorderLayout.NORTH);
        Nsr_mainPanel.add(Nsr_syncPanel, BorderLayout.CENTER);
        Nsr_mainPanel.add(Nsr_scrollPane, BorderLayout.SOUTH);
        
        add(Nsr_mainPanel, BorderLayout.CENTER);
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
    private void Nsr_adicionarLog(String Nsr_mensagem) {
        SwingUtilities.invokeLater(() -> {
            Nsr_logArea.append(Nsr_mensagem + "\n");
            // Auto-scroll para a última linha
            Nsr_logArea.setCaretPosition(Nsr_logArea.getDocument().getLength());
        });
        System.out.println(Nsr_mensagem);
    }
    
    /**
     * Habilita/desabilita controles baseado no estado do servidor
     */
    private void Nsr_toggleControls(boolean Nsr_enabled) {
        Nsr_portaField.setEnabled(Nsr_enabled);
        Nsr_arquivoField.setEnabled(Nsr_enabled);
        Nsr_selecionarArquivoButton.setEnabled(Nsr_enabled);
        Nsr_iniciarButton.setEnabled(Nsr_enabled);
        Nsr_pararButton.setEnabled(!Nsr_enabled);
        
        // Se o servidor estiver rodando, iniciar sincronização automática se estiver selecionada
        if (!Nsr_enabled && Nsr_sincronizacaoAutomaticaCheckBox.isSelected()) {
            Nsr_iniciarSincronizacaoAutomatica();
        } else if (Nsr_enabled) {
            Nsr_pararSincronizacaoAutomatica();
        }
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
                            Socket Nsr_conexao = Nsr_serverSocket.accept();
                            Nsr_adicionarLog("Nova conexão aceita: " + Nsr_conexao.getInetAddress().getHostAddress());

                            // Inicia thread para lidar com o cliente
                            Nsr_ClienteHandler Nsr_clienteHandler = new Nsr_ClienteHandler(Nsr_conexao);
                            Nsr_clientesConectados.add(Nsr_clienteHandler);
                            Nsr_updateClientCount();
                            Nsr_clienteHandler.start();
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
        
        // Parar a sincronização automática
        Nsr_pararSincronizacaoAutomatica();
        
        try {
            // Fechar todas as conexões de clientes
            for (Nsr_ClienteHandler Nsr_cliente : Nsr_clientesConectados) {
                Nsr_cliente.Nsr_fecharConexao();
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
 * Inicia a sincronização automática com o servidor remoto
 */
private void Nsr_iniciarSincronizacaoAutomatica() {
    if (Nsr_timerSincronizacao != null) {
        Nsr_timerSincronizacao.cancel();
    }
    
    Nsr_timerSincronizacao = new Timer();
    Nsr_timerSincronizacao.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            try {
                // Sincronizar baseado nos parâmetros salvos
                Nsr_sincronizarComOutroServidorAutomatico();
            } catch (Exception e) {
                Nsr_adicionarLog("Erro na sincronização automática: " + e.getMessage());
            }
        }
    }, 0, Nsr_intervaloSincronizacao * 1000); // Converter segundos para milissegundos
    
    Nsr_sincronizacaoAutomaticaAtiva = true;
    Nsr_adicionarLog("Sincronização automática iniciada com intervalo de " + 
                   Nsr_intervaloSincronizacao + " segundos");
}

/**
 * Para a sincronização automática
 */
private void Nsr_pararSincronizacaoAutomatica() {
    if (Nsr_timerSincronizacao != null) {
        Nsr_timerSincronizacao.cancel();
        Nsr_timerSincronizacao = null;
        Nsr_sincronizacaoAutomaticaAtiva = false;
        Nsr_adicionarLog("Sincronização automática interrompida");
    }
}

/**
 * Sincroniza dados com outro servidor (versão para chamada manual)
 */
private void Nsr_sincronizarComOutroServidor() {
    String Nsr_ip = Nsr_ipSincronizacaoField.getText().trim();
    int Nsr_porta;
    
    try {
        Nsr_porta = Integer.parseInt(Nsr_portaSincronizacaoField.getText().trim());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "A porta deve ser um número válido", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    int Nsr_tipoSincronizacao = Nsr_tipoSincronizacaoCombo.getSelectedIndex();
    
    // Salvar configurações para sincronização automática
    Nsr_ipServidorRemoto = Nsr_ip;
    Nsr_portaServidorRemoto = Nsr_porta;
    Nsr_tipoSincronizacaoAtual = Nsr_tipoSincronizacao;
    
    // Mostrar diálogo de progresso
    JDialog Nsr_progressDialog = new JDialog(this, "Sincronização", true);
    JProgressBar Nsr_progressBar = new JProgressBar();
    Nsr_progressBar.setIndeterminate(true);
    JLabel Nsr_statusLabel = new JLabel("Conectando ao servidor remoto...");
    
    JPanel Nsr_panel = new JPanel(new BorderLayout(10, 10));
    Nsr_panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    Nsr_panel.add(Nsr_statusLabel, BorderLayout.NORTH);
    Nsr_panel.add(Nsr_progressBar, BorderLayout.CENTER);
    
    // Adicionar opção para manter sincronização automática
    JCheckBox Nsr_manterSincronizacaoCheck = new JCheckBox("Manter sincronização automática");
    Nsr_manterSincronizacaoCheck.setSelected(Nsr_sincronizacaoAutomaticaAtiva);
    
    JPanel Nsr_optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    Nsr_optionsPanel.add(Nsr_manterSincronizacaoCheck);
    
    // Campo para intervalo de sincronização
    JPanel Nsr_intervaloPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel Nsr_intervaloLabel = new JLabel("Intervalo (segundos):");
    JTextField Nsr_intervaloField = new JTextField(5);
    Nsr_intervaloField.setText(String.valueOf(Nsr_intervaloSincronizacao));
    Nsr_intervaloPanel.add(Nsr_intervaloLabel);
    Nsr_intervaloPanel.add(Nsr_intervaloField);
    
    JPanel Nsr_southPanel = new JPanel(new BorderLayout());
    Nsr_southPanel.add(Nsr_optionsPanel, BorderLayout.NORTH);
    Nsr_southPanel.add(Nsr_intervaloPanel, BorderLayout.SOUTH);
    
    Nsr_panel.add(Nsr_southPanel, BorderLayout.SOUTH);
    
    Nsr_progressDialog.add(Nsr_panel);
    Nsr_progressDialog.setSize(350, 180);
    Nsr_progressDialog.setLocationRelativeTo(this);
    
    // Executar sincronização em thread separada
    new Thread(() -> {
        try {
            Nsr_realizarSincronizacao(Nsr_ip, Nsr_porta, Nsr_tipoSincronizacao, Nsr_statusLabel);
            
            // Salvar configurações para sincronização automática
            SwingUtilities.invokeLater(() -> {
                try {
                    Nsr_intervaloSincronizacao = Integer.parseInt(Nsr_intervaloField.getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                        Nsr_ServidorSocket1_Carros.this,
                        "Intervalo inválido, usando valor padrão de 60 segundos",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                    );
                    Nsr_intervaloSincronizacao = 60;
                }
                
                // Verificar se deve manter sincronização automática
                if (Nsr_manterSincronizacaoCheck.isSelected()) {
                    // Iniciar ou reiniciar a sincronização automática
                    Nsr_iniciarSincronizacaoAutomatica();
                } else {
                    // Parar sincronização automática se estiver ativa
                    if (Nsr_sincronizacaoAutomaticaAtiva) {
                        Nsr_pararSincronizacaoAutomatica();
                    }
                }
                
                // Fechar diálogo e mostrar mensagem de sucesso
                Nsr_progressDialog.dispose();
                
                String Nsr_mensagemSucesso = "Sincronização concluída com sucesso!";
                if (Nsr_sincronizacaoAutomaticaAtiva) {
                    Nsr_mensagemSucesso += "\nSincronização automática ativa a cada " + 
                                         Nsr_intervaloSincronizacao + " segundos.";
                }
                
                JOptionPane.showMessageDialog(
                    Nsr_ServidorSocket1_Carros.this, 
                    Nsr_mensagemSucesso,
                    "Sincronização", 
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                // Atualizar label de status na interface
                Nsr_atualizarStatusSincronizacao();
            });
            
        } catch (IOException e) {
            Nsr_adicionarLog("Falha na sincronização: " + e.getMessage());
            
            // Fechar diálogo e mostrar erro
            SwingUtilities.invokeLater(() -> {
                Nsr_progressDialog.dispose();
                JOptionPane.showMessageDialog(
                    Nsr_ServidorSocket1_Carros.this, 
                    "Erro ao sincronizar: " + e.getMessage(), 
                    "Erro de Sincronização", 
                    JOptionPane.ERROR_MESSAGE
                );
            });
        }
    }).start();
    
    // Exibir diálogo de progresso
    Nsr_progressDialog.setVisible(true);
}

/**
 * Sincroniza dados com outro servidor (versão para chamada automática)
 */
private void Nsr_sincronizarComOutroServidorAutomatico() {
    try {
        Nsr_adicionarLog("Iniciando sincronização automática com " + 
                       Nsr_ipServidorRemoto + ":" + Nsr_portaServidorRemoto);
        
        Nsr_realizarSincronizacao(Nsr_ipServidorRemoto, Nsr_portaServidorRemoto, 
                                Nsr_tipoSincronizacaoAtual, null);
        
        Nsr_adicionarLog("Sincronização automática concluída com sucesso");
        
        // Atualizar label de última sincronização
        SwingUtilities.invokeLater(() -> {
            Nsr_atualizarStatusSincronizacao();
        });
        
    } catch (IOException e) {
        Nsr_adicionarLog("Falha na sincronização automática: " + e.getMessage());
    }
}

/**
 * Atualiza o status de sincronização na interface
 */
private void Nsr_atualizarStatusSincronizacao() {
    for (Component comp : Nsr_syncPanel.getComponents()) {
        if (comp instanceof JPanel) {
            for (Component subComp : ((JPanel) comp).getComponents()) {
                if (subComp instanceof JLabel && ((JLabel) subComp).getText().startsWith("Última sincronização")) {
                    ((JLabel) subComp).setText("Última sincronização: " + 
                                            new java.util.Date().toString());
                } else if (subComp instanceof JLabel && ((JLabel) subComp).getText().startsWith("Status:")) {
                    if (Nsr_sincronizacaoAutomaticaAtiva) {
                        ((JLabel) subComp).setText("Status: Sincronização automática ativa (" + 
                                                Nsr_intervaloSincronizacao + " segundos)");
                        ((JLabel) subComp).setForeground(new Color(0, 128, 0)); // Verde
                    } else {
                        ((JLabel) subComp).setText("Status: Sincronização manual");
                        ((JLabel) subComp).setForeground(Color.BLACK);
                    }
                }
            }
        }
    }
}

/**
 * Realiza a sincronização com outro servidor (código comum para manual e automática)
 */
private void Nsr_realizarSincronizacao(String Nsr_ip, int Nsr_porta, int Nsr_tipoSincronizacao, 
                                    JLabel Nsr_statusLabel) throws IOException {
    
    Nsr_adicionarLog("Conectando ao servidor " + Nsr_ip + ":" + Nsr_porta);
    Socket Nsr_socket = new Socket(Nsr_ip, Nsr_porta);
    
    if (Nsr_statusLabel != null) {
        SwingUtilities.invokeLater(() -> Nsr_statusLabel.setText("Sincronizando dados..."));
    }
    
    DataInputStream Nsr_entrada = new DataInputStream(Nsr_socket.getInputStream());
    DataOutputStream Nsr_saida = new DataOutputStream(Nsr_socket.getOutputStream());
    
    // Enviar comando de sincronização
    Nsr_saida.writeInt(Nsr_COMANDO_SINCRONIZAR);
    Nsr_saida.writeInt(Nsr_tipoSincronizacao);
    
    switch (Nsr_tipoSincronizacao) {
        case 0: // Unilateral (Enviar)
            Nsr_adicionarLog("Enviando dados para servidor remoto...");
            Nsr_enviarArquivo(Nsr_saida, Nsr_arquivoDados);
            break;
        case 1: // Unilateral (Receber)
            Nsr_adicionarLog("Recebendo dados do servidor remoto...");
            Nsr_receberArquivo(Nsr_entrada, Nsr_arquivoDados);
            break;
        case 2: // Bilateral
            Nsr_adicionarLog("Iniciando sincronização bilateral...");
            Nsr_enviarArquivo(Nsr_saida, Nsr_arquivoDados);
            Nsr_receberArquivo(Nsr_entrada, Nsr_arquivoDados + ".temp");
            
            // Mesclar os arquivos
            List<String> Nsr_dadosOriginais = Nsr_lerArquivo(Nsr_arquivoDados);
            List<String> Nsr_dadosRecebidos = Nsr_lerArquivo(Nsr_arquivoDados + ".temp");
            
            // Mesclar e eliminar duplicações (baseado em ID)
            List<String> Nsr_dadosMesclados = Nsr_mesclarDados(Nsr_dadosOriginais, Nsr_dadosRecebidos);
            
            // Salvar arquivo mesclado
            Nsr_salvarArquivo(Nsr_dadosMesclados, Nsr_arquivoDados);
            
            // Excluir arquivo temporário
            File Nsr_tempFile = new File(Nsr_arquivoDados + ".temp");
            if (Nsr_tempFile.exists()) {
                Nsr_tempFile.delete();
            }
            break;
    }
    
    Nsr_socket.close();
    Nsr_adicionarLog("Sincronização concluída com sucesso");
}
/**
 * Inicializa a interface de sincronização
 */
private void Nsr_inicializarInterfaceSincronizacao() {
    // Variáveis de estado para sincronização
    Nsr_sincronizacaoAutomaticaAtiva = false;
    Nsr_intervaloSincronizacao = 60; // Padrão: 60 segundos
    Nsr_ipServidorRemoto = "";
    Nsr_portaServidorRemoto = 0;
    Nsr_tipoSincronizacaoAtual = 0;
    
    // Painel de sincronização
    Nsr_syncPanel = new JPanel();
    Nsr_syncPanel.setLayout(new BorderLayout(10, 10));
    Nsr_syncPanel.setBorder(BorderFactory.createTitledBorder("Sincronização com Outro Servidor"));
    
    // Painel de inputs para configuração de sincronização
    JPanel Nsr_configPanel = new JPanel(new GridLayout(0, 2, 5, 5));
    
    // IP do servidor remoto
    JLabel Nsr_ipLabel = new JLabel("IP do Servidor Remoto:");
    Nsr_ipSincronizacaoField = new JTextField(15);
    Nsr_configPanel.add(Nsr_ipLabel);
    Nsr_configPanel.add(Nsr_ipSincronizacaoField);
    
    // Porta do servidor remoto
    JLabel Nsr_portaLabel = new JLabel("Porta:");
    Nsr_portaSincronizacaoField = new JTextField(15);
    Nsr_configPanel.add(Nsr_portaLabel);
    Nsr_configPanel.add(Nsr_portaSincronizacaoField);
    
    // Tipo de sincronização
    JLabel Nsr_tipoSincLabel = new JLabel("Tipo de Sincronização:");
    Nsr_tipoSincronizacaoCombo = new JComboBox<>(new String[]{
        "Unilateral (Enviar dados)", 
        "Unilateral (Receber dados)", 
        "Bilateral (Enviar e receber)"
    });
    Nsr_configPanel.add(Nsr_tipoSincLabel);
    Nsr_configPanel.add(Nsr_tipoSincronizacaoCombo);
    
    // Intervalo de sincronização
    JLabel Nsr_intervaloLabel = new JLabel("Intervalo (segundos):");
    Nsr_intervaloSincronizacaoField = new JTextField(15);
    Nsr_intervaloSincronizacaoField.setText(String.valueOf(Nsr_intervaloSincronizacao));
    Nsr_configPanel.add(Nsr_intervaloLabel);
    Nsr_configPanel.add(Nsr_intervaloSincronizacaoField);
    
    // Botões de ação
    JPanel Nsr_buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    
    JButton Nsr_sincronizarButton = new JButton("Sincronizar Agora");
    Nsr_sincronizarButton.addActionListener(e -> Nsr_sincronizarComOutroServidor());
    Nsr_buttonPanel.add(Nsr_sincronizarButton);
    
    Nsr_habilitarSincAutomaticaCheck = new JCheckBox("Sincronização Automática");
    Nsr_habilitarSincAutomaticaCheck.addActionListener(e -> {
        if (Nsr_habilitarSincAutomaticaCheck.isSelected()) {
            try {
                // Validar campos
                String Nsr_ip = Nsr_ipSincronizacaoField.getText().trim();
                if (Nsr_ip.isEmpty()) {
                    throw new Exception("IP do servidor remoto não pode estar vazio");
                }
                
                int Nsr_porta = Integer.parseInt(Nsr_portaSincronizacaoField.getText().trim());
                if (Nsr_porta <= 0 || Nsr_porta > 65535) {
                    throw new Exception("Porta deve estar entre 1 e 65535");
                }
                
                int Nsr_intervalo = Integer.parseInt(Nsr_intervaloSincronizacaoField.getText().trim());
                if (Nsr_intervalo < 5) {
                    throw new Exception("Intervalo mínimo é de 5 segundos");
                }
                
                // Salvar configurações
                Nsr_ipServidorRemoto = Nsr_ip;
                Nsr_portaServidorRemoto = Nsr_porta;
                Nsr_tipoSincronizacaoAtual = Nsr_tipoSincronizacaoCombo.getSelectedIndex();
                Nsr_intervaloSincronizacao = Nsr_intervalo;
                
                // Iniciar sincronização automática
                Nsr_iniciarSincronizacaoAutomatica();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    Nsr_ServidorSocket1_Carros.this,
                    "Valores numéricos inválidos. Verifique porta e intervalo.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
                Nsr_habilitarSincAutomaticaCheck.setSelected(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    Nsr_ServidorSocket1_Carros.this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
                Nsr_habilitarSincAutomaticaCheck.setSelected(false);
            }
        } else {
            // Parar sincronização automática
            Nsr_pararSincronizacaoAutomatica();
        }
        
        // Atualizar status na interface
        Nsr_atualizarStatusSincronizacao();
    });
    Nsr_buttonPanel.add(Nsr_habilitarSincAutomaticaCheck);
    
    // Painel de status
    JPanel Nsr_statusSyncPanel = new JPanel(new GridLayout(0, 1, 5, 5));
    JLabel Nsr_statusSyncLabel = new JLabel("Status: Sincronização manual");
    JLabel Nsr_ultimaSincLabel = new JLabel("Última sincronização: Nunca");
    Nsr_statusSyncPanel.add(Nsr_statusSyncLabel);
    Nsr_statusSyncPanel.add(Nsr_ultimaSincLabel);
    
    // Adicionar componentes ao painel de sincronização
    Nsr_syncPanel.add(Nsr_configPanel, BorderLayout.NORTH);
    Nsr_syncPanel.add(Nsr_buttonPanel, BorderLayout.CENTER);
    Nsr_syncPanel.add(Nsr_statusSyncPanel, BorderLayout.SOUTH);
    
    // Adicionar à interface principal (supondo que haja um tabbedPane)
    Nsr_tabbedPane.addTab("Sincronização", Nsr_syncPanel);
}

/**
 * Libera recursos ao fechar o aplicativo
 */
private void Nsr_liberarRecursos() {
    // Parar sincronização automática se estiver ativa
    if (Nsr_sincronizacaoAutomaticaAtiva) {
        Nsr_pararSincronizacaoAutomatica();
    }
    
    // Parar servidor e fechar conexões de clientes
    Nsr_pararServidor();
    
    // Fechar todas as conexões de cliente
    for (Nsr_ClienteHandler cliente : Nsr_clientesConectados) {
        cliente.Nsr_fecharConexao();
    }
    Nsr_clientesConectados.clear();
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