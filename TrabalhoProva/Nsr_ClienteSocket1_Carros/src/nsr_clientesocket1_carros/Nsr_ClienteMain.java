package nsr_clientesocket1_carros;

/**
 *
 * @author natsa
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

public class Nsr_ClienteMain extends JFrame {
    private String ipServidor;
    private int portaServidor;
    private Socket socket;
    private Nsr_SocketManager socketManager;
    private Nsr_ClienteRelatorio clienteRelatorio;
    private Nsr_ClienteCadastro clienteCadastro;
    private JTabbedPane tabbedPane;

    public Nsr_ClienteMain() {
        iniciarConexao();
        iniciarUI();
    }

    private void iniciarConexao() {
        JTextField ipField = new JTextField("127.0.0.1");
        JTextField portaField = new JTextField("2222");

        Object[] mensagem = {
            "IP do Servidor:", ipField,
            "Porta do Servidor:", portaField
        };

        int opcao = JOptionPane.showConfirmDialog(null, mensagem, "Conexão com o Servidor", 
                                                 JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            ipServidor = ipField.getText();
            try {
                portaServidor = Integer.parseInt(portaField.getText());
                conectarAoServidor();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Porta inválida. Utilize um número inteiro.");
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    private void conectarAoServidor() {
        try {
            System.out.println("Tentando conectar a " + ipServidor + ":" + portaServidor);
            socket = new Socket(ipServidor, portaServidor);
            System.out.println("Conexão estabelecida com sucesso");
            
            socketManager = new Nsr_SocketManager(socket, this);
            socketManager.start();
            
            // Adiciona um timer para verificar periodicamente por novos dados
            Timer timer = new Timer(10000, e -> {
                System.out.println("Solicitando atualização automática dos dados");
                if (socketManager != null) {
                    socketManager.solicitarTodosVeiculos();
                }
            });
            timer.start();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao servidor: " + e.getMessage(),
                                         "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void iniciarUI() {
        setTitle("Sistema de Cadastro de Veículos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        
        // Inicializando os componentes principais
        clienteCadastro = new Nsr_ClienteCadastro(socketManager);
        clienteRelatorio = new Nsr_ClienteRelatorio(socketManager);
        
        // Adicionando as abas
        tabbedPane.addTab("Cadastro de Veículo", clienteCadastro);
        tabbedPane.addTab("Relatório de Veículos", clienteRelatorio);
        
        // Painel com informações de conexão
        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informações de Conexão"));
        infoPanel.add(new JLabel("IP para coleta de dados: " + ipServidor));
        infoPanel.add(new JLabel("IP para envio de dados: " + ipServidor));
        
        // Layout principal
        setLayout(new BorderLayout());
        add(infoPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        
        // Adiciona listener para fechar a conexão quando a janela for fechada
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (socketManager != null) {
                    socketManager.encerrarConexao();
                }
            }
        });
        
        setVisible(true);
    }
    
    public void atualizarRelatorio() {
        if (clienteRelatorio != null) {
            clienteRelatorio.carregarDados();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Nsr_ClienteMain();
        });
    }
}