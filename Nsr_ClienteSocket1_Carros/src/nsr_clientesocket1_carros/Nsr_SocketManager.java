package nsr_clientesocket1_carros;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Gerencia a comunicação com o servidor
 */
public class Nsr_SocketManager extends Thread {
    private Socket Nsr_socket;
    private DataInputStream Nsr_entrada;
    private DataOutputStream Nsr_saida;
    private boolean Nsr_rodando = true;
    private List<Nsr_Carro> Nsr_listaVeiculos = new ArrayList<>();
    private Nsr_ClienteMain Nsr_clienteMain;
    
    // Constantes para comandos - DEVEM CORRESPONDER AOS MESMOS VALORES DO SERVIDOR
    private static final int NSR_COMANDO_PING = 10;
    private static final int NSR_COMANDO_HELLO = 11;
    private static final int NSR_COMANDO_GET_ALL = 20;
    private static final int NSR_COMANDO_GET_BY_ID = 21;
    private static final int NSR_COMANDO_ADD = 30;
    private static final int NSR_COMANDO_UPDATE = 31;
    private static final int NSR_COMANDO_DELETE = 32;
    
    /**
     * Construtor
     */
    public Nsr_SocketManager(Socket Nsr_socket, Nsr_ClienteMain Nsr_clienteMain) {
        this.Nsr_socket = Nsr_socket;
        this.Nsr_clienteMain = Nsr_clienteMain;
        try {
            Nsr_entrada = new DataInputStream(Nsr_socket.getInputStream());
            Nsr_saida = new DataOutputStream(Nsr_socket.getOutputStream());
            
            // Envia alguns comandos de teste para verificar a conexão
            System.out.println("Comando de teste enviado ao servidor");
            Nsr_enviarPing();
            Nsr_enviarHello();
            
            // Solicita todos os veículos logo ao conectar
            Nsr_solicitarTodosVeiculos();
            
        } catch (IOException Nsr_e) {
            Nsr_e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao inicializar conexão: " + Nsr_e.getMessage(),
                                         "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Envia um comando de ping para o servidor
     */
    public boolean Nsr_enviarPing() {
        try {
            Nsr_saida.writeInt(NSR_COMANDO_PING);
            System.out.println("Comando PING enviado ao servidor");
            return Nsr_entrada.readBoolean();
        } catch (IOException Nsr_e) {
            Nsr_e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Envia um comando de hello para o servidor
     */
    public String Nsr_enviarHello() {
        try {
            Nsr_saida.writeInt(NSR_COMANDO_HELLO);
            System.out.println("Comando HELLO enviado ao servidor");
            return Nsr_entrada.readUTF();
        } catch (IOException Nsr_e) {
            Nsr_e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Solicita todos os veículos do servidor
     */
    public void Nsr_solicitarTodosVeiculos() {
        try {
            Nsr_saida.writeInt(NSR_COMANDO_GET_ALL);
            System.out.println("Solicitação GET_ALL enviada ao servidor");
            int Nsr_quantidade = Nsr_entrada.readInt();
            
            // Limpa a lista atual
            Nsr_listaVeiculos.clear();
            
            // Lê todos os carros retornados
            for (int Nsr_i = 0; Nsr_i < Nsr_quantidade; Nsr_i++) {
                String Nsr_linhaCarro = Nsr_entrada.readUTF();
                Nsr_listaVeiculos.add(new Nsr_Carro(Nsr_linhaCarro));
            }
            
            // Atualiza a interface
            Nsr_clienteMain.Nsr_atualizarRelatorio();
            System.out.println("Atualizando relatório de veículos...");
            System.out.println("Veículos obtidos do SocketManager: " + Nsr_listaVeiculos.size());
            
            // Se a lista estiver vazia, tenta solicitar novamente (apenas uma vez)
            if (Nsr_listaVeiculos.isEmpty()) {
                System.out.println("Solicitando atualização dos dados porque a lista está vazia");
                Nsr_solicitarTodosVeiculos();
            }
            
        } catch (IOException Nsr_e) {
            Nsr_e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao solicitar veículos: " + Nsr_e.getMessage(),
                                         "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Solicita um veículo pelo ID
     */
    public Nsr_Carro Nsr_solicitarVeiculoPorId(int Nsr_id) {
        try {
            Nsr_saida.writeInt(NSR_COMANDO_GET_BY_ID);
            Nsr_saida.writeInt(Nsr_id);
            
            boolean Nsr_encontrado = Nsr_entrada.readBoolean();
            if (Nsr_encontrado) {
                String Nsr_linhaCarro = Nsr_entrada.readUTF();
                return new Nsr_Carro(Nsr_linhaCarro);
            }
            
        } catch (IOException Nsr_e) {
            Nsr_e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao solicitar veículo: " + Nsr_e.getMessage(),
                                         "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return null;
    }
    
    /**
     * Adiciona um novo veículo
     */
    public boolean Nsr_adicionarVeiculo(Nsr_Carro Nsr_carro) {
        try {
            Nsr_saida.writeInt(NSR_COMANDO_ADD);
            Nsr_saida.writeUTF(Nsr_carro.Nsr_paraString());
            
            boolean Nsr_sucesso = Nsr_entrada.readBoolean();
            if (Nsr_sucesso) {
                // Atualiza a lista local
                Nsr_solicitarTodosVeiculos();
            }
            
            return Nsr_sucesso;
            
        } catch (IOException Nsr_e) {
            Nsr_e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao adicionar veículo: " + Nsr_e.getMessage(),
                                         "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Atualiza um veículo existente
     */
    public boolean Nsr_atualizarVeiculo(Nsr_Carro Nsr_carro) {
        try {
            Nsr_saida.writeInt(NSR_COMANDO_UPDATE);
            Nsr_saida.writeUTF(Nsr_carro.Nsr_paraString());
            
            boolean Nsr_sucesso = Nsr_entrada.readBoolean();
            if (Nsr_sucesso) {
                // Atualiza a lista local
                Nsr_solicitarTodosVeiculos();
            }
            
            return Nsr_sucesso;
            
        } catch (IOException Nsr_e) {
            Nsr_e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar veículo: " + Nsr_e.getMessage(),
                                         "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Remove um veículo pelo ID
     */
    public boolean Nsr_removerVeiculo(int Nsr_id) {
        try {
            Nsr_saida.writeInt(NSR_COMANDO_DELETE);
            Nsr_saida.writeInt(Nsr_id);
            
            boolean Nsr_sucesso = Nsr_entrada.readBoolean();
            if (Nsr_sucesso) {
                // Atualiza a lista local
                Nsr_solicitarTodosVeiculos();
            }
            
            return Nsr_sucesso;
            
        } catch (IOException Nsr_e) {
            Nsr_e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao remover veículo: " + Nsr_e.getMessage(),
                                         "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Retorna a lista atual de veículos
     */
    public List<Nsr_Carro> Nsr_getListaVeiculos() {
        return Nsr_listaVeiculos;
    }
    
    /**
     * Encerra a conexão com o servidor
     */
    public void Nsr_encerrarConexao() {
        Nsr_rodando = false;
        try {
            if (Nsr_socket != null && !Nsr_socket.isClosed()) {
                Nsr_socket.close();
            }
        } catch (IOException Nsr_e) {
            Nsr_e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        // Este método ficaria ativo para receber notificações do servidor
        // se necessário, mas no modelo atual é o cliente que inicia todas
        // as comunicações
    }
}