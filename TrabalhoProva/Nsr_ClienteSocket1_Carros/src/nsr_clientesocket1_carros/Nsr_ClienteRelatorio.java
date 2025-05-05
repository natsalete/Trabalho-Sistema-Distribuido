package nsr_clientesocket1_carros;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Painel para exibir o relatório de veículos
 */
public class Nsr_ClienteRelatorio extends JPanel {
    private Nsr_SocketManager socketManager;
    private JTable tabelaVeiculos;
    private DefaultTableModel modeloTabela;
    private JButton atualizarButton;
    private JButton excluirButton;
    private JButton editarButton;
    
    /**
     * Construtor
     */
    public Nsr_ClienteRelatorio(Nsr_SocketManager socketManager) {
        this.socketManager = socketManager;
        initComponents();
    }
    
    /**
     * Inicializa os componentes da interface
     */
    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        
        // Criar modelo de tabela
        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Placa");
        modeloTabela.addColumn("Modelo");
        modeloTabela.addColumn("Marca");
        modeloTabela.addColumn("Ano");
        modeloTabela.addColumn("Combustível");
        modeloTabela.addColumn("Cor");
        modeloTabela.addColumn("Quilometragem");
        modeloTabela.addColumn("Categoria");
        modeloTabela.addColumn("Disponível");
        modeloTabela.addColumn("Valor Compra");
        modeloTabela.addColumn("Valor Venda");
        
        // Criar tabela
        tabelaVeiculos = new JTable(modeloTabela);
        tabelaVeiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabelaVeiculos);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        atualizarButton = new JButton("Atualizar Dados");
        atualizarButton.addActionListener(e -> carregarDados());
        
        editarButton = new JButton("Editar Selecionado");
        editarButton.addActionListener(e -> editarVeiculoSelecionado());
        
        excluirButton = new JButton("Excluir Selecionado");
        excluirButton.addActionListener(e -> excluirVeiculoSelecionado());
        
        botoesPanel.add(atualizarButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(excluirButton);
        
        // Adicionar componentes ao painel
        add(scrollPane, BorderLayout.CENTER);
        add(botoesPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Carrega os dados dos veículos na tabela
     */
    public void carregarDados() {
        // Limpar tabela
        while (modeloTabela.getRowCount() > 0) {
            modeloTabela.removeRow(0);
        }
        
        // Obter lista de veículos
        List<Nsr_Carro> veiculos = socketManager.getListaVeiculos();
        System.out.println("Veículos obtidos do SocketManager: " + veiculos.size());
        
        // Preencher tabela
        for (Nsr_Carro carro : veiculos) {
            Object[] rowData = {
                carro.getId(),
                carro.getPlaca(),
                carro.getModelo(),
                carro.getMarca(),
                carro.getAno(),
                carro.getCombustivel(),
                carro.getCor(),
                carro.getQuilometragem(),
                carro.getCategoria(),
                carro.isDisponivel() ? "Sim" : "Não",
                String.format("R$ %.2f", carro.getValorCompra()),
                String.format("R$ %.2f", carro.getValorVenda())
            };
            modeloTabela.addRow(rowData);
        }
    }
    
    /**
     * Edita o veículo selecionado na tabela
     */
    private void editarVeiculoSelecionado() {
        int selectedRow = tabelaVeiculos.getSelectedRow();
        
        if (selectedRow >= 0) {
            // Obter ID do veículo selecionado
            int id = (int) tabelaVeiculos.getValueAt(selectedRow, 0);
            
            // Obter os dados completos do veículo
            Nsr_Carro carro = socketManager.solicitarVeiculoPorId(id);
            
            if (carro != null) {
                // Aqui você poderia abrir um diálogo de edição ou chamar uma função no ClienteCadastro
                // Para este exemplo, vamos apenas exibir uma mensagem
                JOptionPane.showMessageDialog(this, 
                    "Função de edição para veículo ID " + id + " deve ser implementada.",
                    "Editar Veículo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Não foi possível obter os dados do veículo selecionado.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione um veículo na tabela para editar.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Exclui o veículo selecionado na tabela
     */
    private void excluirVeiculoSelecionado() {
        int selectedRow = tabelaVeiculos.getSelectedRow();
        
        if (selectedRow >= 0) {
            // Obter ID do veículo selecionado
            int id = (int) tabelaVeiculos.getValueAt(selectedRow, 0);
            
            // Confirmar exclusão
            int opcao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o veículo ID " + id + "?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            
            if (opcao == JOptionPane.YES_OPTION) {
                // Solicitar exclusão ao servidor
                boolean sucesso = socketManager.removerVeiculo(id);
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this,
                        "Veículo excluído com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Recarregar dados
                    carregarDados();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Não foi possível excluir o veículo.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione um veículo na tabela para excluir.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}