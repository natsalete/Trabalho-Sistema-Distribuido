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
    private String Nsr_ipServidor;
    private int Nsr_portaServidor;
    private Socket Nsr_socket;
    private Nsr_SocketManager Nsr_socketManager;
    private Nsr_ClienteRelatorio Nsr_clienteRelatorio;
    private Nsr_ClienteCadastro Nsr_clienteCadastro;
    private JTabbedPane Nsr_tabbedPane;

    public Nsr_ClienteMain() {
        Nsr_iniciarConexao();
        Nsr_iniciarUI();
    }

    private void Nsr_iniciarConexao() {
        JTextField Nsr_ipField = new JTextField("127.0.0.1");
        JTextField Nsr_portaField = new JTextField("2222");

        Object[] Nsr_mensagem = {
            "IP do Servidor:", Nsr_ipField,
            "Porta do Servidor:", Nsr_portaField
        };

        int Nsr_opcao = JOptionPane.showConfirmDialog(null, Nsr_mensagem, "Conexão com o Servidor", 
                                                 JOptionPane.OK_CANCEL_OPTION);

        if (Nsr_opcao == JOptionPane.OK_OPTION) {
            Nsr_ipServidor = Nsr_ipField.getText();
            try {
                Nsr_portaServidor = Integer.parseInt(Nsr_portaField.getText());
                Nsr_conectarAoServidor();
            } catch (NumberFormatException Nsr_e) {
                JOptionPane.showMessageDialog(null, "Porta inválida. Utilize um número inteiro.");
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    private void Nsr_conectarAoServidor() {
        try {
            System.out.println("Tentando conectar a " + Nsr_ipServidor + ":" + Nsr_portaServidor);
            Nsr_socket = new Socket(Nsr_ipServidor, Nsr_portaServidor);
            System.out.println("Conexão estabelecida com sucesso");
            
            Nsr_socketManager = new Nsr_SocketManager(Nsr_socket, this);
            Nsr_socketManager.start();
            
            // Adiciona um timer para verificar periodicamente por novos dados
            Timer Nsr_timer = new Timer(10000, Nsr_e -> {
                System.out.println("Solicitando atualização automática dos dados");
                if (Nsr_socketManager != null) {
                    Nsr_socketManager.Nsr_solicitarTodosVeiculos();
                }
            });
            Nsr_timer.start();
            
        } catch (IOException Nsr_e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao servidor: " + Nsr_e.getMessage(),
                                         "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            Nsr_e.printStackTrace();
            System.exit(0);
        }
    }

    private void Nsr_iniciarUI() {
        setTitle("Sistema de Cadastro de Veículos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Nsr_tabbedPane = new JTabbedPane();
        
        // Inicializando os componentes principais
        Nsr_clienteCadastro = new Nsr_ClienteCadastro(Nsr_socketManager);
        Nsr_clienteRelatorio = new Nsr_ClienteRelatorio(Nsr_socketManager);
        
        // Adicionando as abas
        Nsr_tabbedPane.addTab("Cadastro de Veículo", Nsr_clienteCadastro);
        Nsr_tabbedPane.addTab("Relatório de Veículos", Nsr_clienteRelatorio);
        
        // Painel com informações de conexão
        JPanel Nsr_infoPanel = new JPanel(new GridLayout(1, 2));
        Nsr_infoPanel.setBorder(BorderFactory.createTitledBorder("Informações de Conexão"));
        Nsr_infoPanel.add(new JLabel("IP para coleta de dados: " + Nsr_ipServidor));
        Nsr_infoPanel.add(new JLabel("IP para envio de dados: " + Nsr_ipServidor));
        
        // Layout principal
        setLayout(new BorderLayout());
        add(Nsr_infoPanel, BorderLayout.NORTH);
        add(Nsr_tabbedPane, BorderLayout.CENTER);
        
        // Adiciona listener para fechar a conexão quando a janela for fechada
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent Nsr_e) {
                if (Nsr_socketManager != null) {
                    Nsr_socketManager.Nsr_encerrarConexao();
                }
            }
        });
        
        setVisible(true);
    }
    
    public void Nsr_atualizarRelatorio() {
        if (Nsr_clienteRelatorio != null) {
            Nsr_clienteRelatorio.Nsr_carregarDados();
        }
    }

    public static void main(String[] Nsr_args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception Nsr_e) {
                Nsr_e.printStackTrace();
            }
            new Nsr_ClienteMain();
        });
    }
}