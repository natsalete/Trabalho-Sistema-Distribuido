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
    private Socket socket;
    private DataInputStream entrada;
    private DataOutputStream saida;
    private boolean rodando = true;
    private List<Nsr_Carro> listaVeiculos = new ArrayList<>();
    private Nsr_ClienteMain clienteMain;
    
    // Constantes para comandos - DEVEM CORRESPONDER AOS MESMOS VALORES DO SERVIDOR
    private static final int COMANDO_PING = 10;
    private static final int COMANDO_HELLO = 11;
    private static final int COMANDO_GET_ALL = 20;
    private static final int COMANDO_GET_BY_ID = 21;
    private static final int COMANDO_ADD = 30;
    private static final int COMANDO_UPDATE = 31;
    private static final int COMANDO_DELETE = 32;
    
    /**
     * Construtor
     */
    public Nsr_SocketManager(Socket socket, Nsr_ClienteMain clienteMain) {
        this.socket = socket;
        this.clienteMain = clienteMain;
        try {
            entrada = new DataInputStream(socket.getInputStream());
            saida = new DataOutputStream(socket.getOutputStream());
            
            // Envia alguns comandos de teste para verificar a conexão
            System.out.println("Comando de teste enviado ao servidor");
            enviarPing();
            enviarHello();
            
            // Solicita todos os veículos logo ao conectar
            solicitarTodosVeiculos();
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao inicializar conexão: " + e.getMessage(),
                                         "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Envia um comando de ping para o servidor
     */
    public boolean enviarPing() {
        try {
            saida.writeInt(COMANDO_PING);
            System.out.println("Comando PING enviado ao servidor");
            return entrada.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Envia um comando de hello para o servidor
     */
    public String enviarHello() {
        try {
            saida.writeInt(COMANDO_HELLO);
            System.out.println("Comando HELLO enviado ao servidor");
            return entrada.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Solicita todos os veículos do servidor
     */
    public void solicitarTodosVeiculos() {
        try {
            saida.writeInt(COMANDO_GET_ALL);
            System.out.println("Solicitação GET_ALL enviada ao servidor");
            int quantidade = entrada.readInt();
            
            // Limpa a lista atual
            listaVeiculos.clear();
            
            // Lê todos os carros retornados
            for (int i = 0; i < quantidade; i++) {
                String linhaCarro = entrada.readUTF();
                listaVeiculos.add(new Nsr_Carro(linhaCarro));
            }
            
            // Atualiza a interface
            clienteMain.atualizarRelatorio();
            System.out.println("Atualizando relatório de veículos...");
            System.out.println("Veículos obtidos do SocketManager: " + listaVeiculos.size());
            
            // Se a lista estiver vazia, tenta solicitar novamente (apenas uma vez)
            if (listaVeiculos.isEmpty()) {
                System.out.println("Solicitando atualização dos dados porque a lista está vazia");
                solicitarTodosVeiculos();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao solicitar veículos: " + e.getMessage(),
                                         "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Solicita um veículo pelo ID
     */
    public Nsr_Carro solicitarVeiculoPorId(int id) {
        try {
            saida.writeInt(COMANDO_GET_BY_ID);
            saida.writeInt(id);
            
            boolean encontrado = entrada.readBoolean();
            if (encontrado) {
                String linhaCarro = entrada.readUTF();
                return new Nsr_Carro(linhaCarro);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao solicitar veículo: " + e.getMessage(),
                                         "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return null;
    }
    
    /**
     * Adiciona um novo veículo
     */
    public boolean adicionarVeiculo(Nsr_Carro carro) {
        try {
            saida.writeInt(COMANDO_ADD);
            saida.writeUTF(carro.paraString());
            
            boolean sucesso = entrada.readBoolean();
            if (sucesso) {
                // Atualiza a lista local
                solicitarTodosVeiculos();
            }
            
            return sucesso;
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao adicionar veículo: " + e.getMessage(),
                                         "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Atualiza um veículo existente
     */
    public boolean atualizarVeiculo(Nsr_Carro carro) {
        try {
            saida.writeInt(COMANDO_UPDATE);
            saida.writeUTF(carro.paraString());
            
            boolean sucesso = entrada.readBoolean();
            if (sucesso) {
                // Atualiza a lista local
                solicitarTodosVeiculos();
            }
            
            return sucesso;
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar veículo: " + e.getMessage(),
                                         "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Remove um veículo pelo ID
     */
    public boolean removerVeiculo(int id) {
        try {
            saida.writeInt(COMANDO_DELETE);
            saida.writeInt(id);
            
            boolean sucesso = entrada.readBoolean();
            if (sucesso) {
                // Atualiza a lista local
                solicitarTodosVeiculos();
            }
            
            return sucesso;
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao remover veículo: " + e.getMessage(),
                                         "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Retorna a lista atual de veículos
     */
    public List<Nsr_Carro> getListaVeiculos() {
        return listaVeiculos;
    }
    
    /**
     * Encerra a conexão com o servidor
     */
    public void encerrarConexao() {
        rodando = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        // Este método ficaria ativo para receber notificações do servidor
        // se necessário, mas no modelo atual é o cliente que inicia todas
        // as comunicações
    }
}